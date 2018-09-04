package Tools;

import java.io.*;
import java.util.Objects;

public class ParseConf {
    private static String path = "httpd.conf";
    public static String[] readConf() throws IOException {
        File file = FileTools.getFile(path);
        if (file != null) {
            try (var bufReader = new BufferedReader(new FileReader(file))) {
                String[] lines = new String[3];
                for (int i = 0; i < 3; ++i) {
                    lines[i] = bufReader.readLine();
                }
                return lines;
            }
            catch (FileNotFoundException ignored) {}
        }
        return null;
    }
    public static Integer getPort(String parsePort) {
        String[] splitLine = parsePort.split(" ");
        if (splitLine[0].equals("listen")) {
            return Integer.parseInt(splitLine[1]);
        }
        return null;
    }
    public static Integer getAmountOfThreads(String parseThreads) {
        String[] splitLine = parseThreads.split(" ");
        if (splitLine[0].equals("max_threads")) {
            return Integer.parseInt(splitLine[1]);
        }
        return null;
    }
    public static String getStaticDir(String parseDir) {
        String[] splitLine = parseDir.split(" ");
        if (splitLine[0].equals("document_root")) {
            return splitLine[1];
        }
        return null;
    }
}
