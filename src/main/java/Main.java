

import ResonseHandler.ResponseHandler;
import ThreadPool.ThreadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class Main {
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            ThreadPool threadPool = new ThreadPool(16);
            while (true) {
                Socket incoming = serverSocket.accept();
                Runnable r = new ResponseHandler(incoming);
                threadPool.execute(r);
            }
        }
    }
}