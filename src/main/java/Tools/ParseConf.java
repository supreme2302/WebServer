package Tools;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParseConf {
    private static String path = "httpd.conf";
    public static List<String> readConf() throws IOException {
        File file = FileTools.getFile(path);
        if (file != null) {
            try (var bufReader = new BufferedReader(new FileReader(file))) {
                Stream<String> stringStream = Arrays.stream(new String[3]);
                return stringStream.map(line -> {
                    try {
                        line = bufReader.readLine();
                        return line;
                    } catch (IOException ignored) {}
                    return null;

                }).collect(Collectors.toList());
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
