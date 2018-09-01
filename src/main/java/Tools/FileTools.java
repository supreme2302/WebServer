package Tools;

import java.io.*;

public class FileTools {
    public static String getFileExtension(File file) {
//        System.out.println("getFileExtension");
        String name = file.getName();
        int index = name.lastIndexOf('.');
        return index > 0 ? name.substring(index + 1) : null;
    }
    public static String getFileExtension(String name) {
//        System.out.println("getFileExtension");
        int index = name.lastIndexOf('.');
        return index > 0 ? name.substring(index + 1) : null;
    }

    public static File getFile(String path) {
//        System.out.println("getFile");
        File file = new File(path);
        System.out.println(path);
        return file.exists() ? file : null;
    }

    public static void sendFile(File file, BufferedOutputStream bos) throws IOException {
//        System.out.println("sendFile");
        try(final var fileInputStream = new FileInputStream(file)) {
//            final byte[] buffer = new byte[(int)file.length()];
            final byte[] buffer = new byte[fileInputStream.available()];
            int amountOfBytesRead = 0;
            while ((amountOfBytesRead = fileInputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, amountOfBytesRead);
                bos.flush();
            }
        } catch (FileNotFoundException ignored) {}
    }
}
