

import ResonseHandler.ResponseHandler;
import ThreadPool.ThreadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class Main {
    public static void main(String[] args) throws IOException {
        try (var serverSocket = new ServerSocket(8080)) {
            var threadPool = new ThreadPool(16);
            while (true) {
                var incoming = serverSocket.accept();
                Runnable r = new ResponseHandler(incoming);
                threadPool.execute(r);
            }
        }
    }
}