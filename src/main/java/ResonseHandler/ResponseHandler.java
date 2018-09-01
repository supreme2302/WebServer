package ResonseHandler;

import Responses.Responses;
import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static Tools.FileTools.getFile;
import static Tools.FileTools.getFileExtension;
import static Tools.FileTools.sendFile;


public class ResponseHandler implements Runnable {
    private Socket incoming;
    private boolean isOpen = true;

    private InputStream is;
    private OutputStream os;
    private BufferedOutputStream bos;
    //todo: Hardcode
    private final String document_root = "/home/supreme/Projects/IdeaProjects/WebServer";
//    final String document_root = "/var/www/html";


   public ResponseHandler(Socket incoming) {
        this.incoming = incoming;
        try {
            this.is = incoming.getInputStream();
            this.os = incoming.getOutputStream();
            this.bos = new BufferedOutputStream(incoming.getOutputStream());
        } catch (IOException ignored) {}

    }
    private String fullPath(String path) {
        String fPath  = path.endsWith("/") ? path + "index.html" : path;
        return document_root + fPath;
    }

    private String content_type(String ext) {
        var types = new HashMap<String, String>();
        types.put("txt", "text/txt");
        types.put("html", "text/html");
        types.put("css", "text/css");
        types.put("jpeg", "image/jpeg");
        types.put("jpg", "image/jpeg");
        types.put("png", "image/png");
        types.put("gif", "image/gif");
        types.put("js", "application/javascript");
        types.put("swf", "application/x-shockwave-flash");
        return types.get(ext);
    }



    private void getResponse(File file)  {
//        System.out.println("getResponse");
        var ext = getFileExtension(file);
        var content_type  = content_type(ext);
        try {
            os.write(Responses.writeResponse(file, content_type).getBytes());
            os.flush();
            sendFile(file, bos);
        } catch (IOException ignored) {}
    }
    private void headResponse(File file) {
        var ext = getFileExtension(file);
        var content_type  = content_type(ext);
        try {
            os.write(Responses.writeResponse(file, content_type).getBytes());
            os.flush();
        } catch (IOException ignored) {}
    }
    private void rejectResponseForbidden() {
//        System.out.println("rejectResponseForbidden");
        try {
            os.write(Responses.writeRejectResponse("403 Forbidden").getBytes());
        } catch (IOException ignored) {}
    }
    private void rejectResponseNotFound() {
        try {
            os.write(Responses.writeRejectResponse("404 Not Found").getBytes());
        } catch (IOException ignored) {}
    }
    private void rejectResponseOk() {
        try {
            os.write(Responses.writeRejectResponse("200 OK").getBytes());
        } catch (IOException ignored) {}
    }
    private void rejectResponseMethodNotAllowed() {
        try {
            os.write(Responses.writeRejectResponse("405 Method Not Allowed").getBytes());
        } catch (IOException ignored) {}
    }

    private String[] readInputHeaders() throws IOException {
//        System.out.println("readInputHeaders");
        var br = new BufferedReader(new InputStreamReader(is));
        var sum = new StringBuilder();
        while(true) {
            String s = br.readLine();
            if(s == null || s.trim().length() == 0) {
                break;
            }
            sum.append(s);
        }
        System.out.println(sum.toString());
        return sum.toString().split(" ");
    }

    private void makeResponse(String[] req)  {
        String method = req[0];
        if (!method.equals("GET") && !method.equals("HEAD")) {
            rejectResponseMethodNotAllowed();
            return;
        }
        String rowPath = req[1];
        if (rowPath.equals("/httptest/")) {
            rejectResponseOk();
            return;
        }
        if (rowPath.contains("../")) {
            rejectResponseForbidden();
            return;
        }
        var path = URLDecoder.decode(rowPath, StandardCharsets.UTF_8).split("\\?")[0];

        var indexOfSlash = path.lastIndexOf('/');
        if (indexOfSlash == path.length() - 1) {
            var pathWithoutSlash = path.substring(0, indexOfSlash);
            var tempExt = getFileExtension(pathWithoutSlash);
            if (tempExt != null) {
                rejectResponseNotFound();
                return;
            }
        }
        var fullPath = fullPath(path);
        var file = getFile(fullPath);
        if (file != null) {
            if (method.equals("HEAD")) {
                headResponse(file);
            } else {
                getResponse(file);
            }

        } else {
            if (!fullPath.contains("index.html")) {
                rejectResponseNotFound();
            } else {
                rejectResponseForbidden();
            }

        }
    }
    @Override
    public void run() {
        try {
            String[] a = readInputHeaders();
            makeResponse(a);

        }
        catch (IOException ignored) {}

        finally {
            try {
                incoming.close();
                is.close();
                os.close();
                bos.close();
            } catch (IOException ignored) {}
        }
    }

//    public boolean isOpen() {
//        return isOpen;
//    }
}


