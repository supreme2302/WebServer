package Responses;

import java.io.File;
import java.util.Date;

public class Responses {
    public static String writeResponse(File file, String content_type) {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Server: Supreme\r\n" +
                "Content-Type: " + content_type + "\r\n" +
                "Date: " + new Date().toString() + "\r\n" +
                "Content-Length: " + file.length() + "\r\n" +
                "Connection: close\r\n\r\n";
        return response;
    }
    public static String writeRejectResponse(String errorCode) {
        String response = "HTTP/1.1 " + errorCode + "\r\n" +
                "Server: Supreme\r\n" +
                "Date: " + new Date().toString() + "\r\n" +
                "Connection: close\r\n\r\n";
        return response;
    }
}
