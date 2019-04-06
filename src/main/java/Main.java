import ResonseHandler.ResponseHandler;
import ThreadPool.ThreadPool;
import Tools.ParseConf;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    public static void main(String[] args) throws IOException {

        List<String> parseConf = ParseConf.readConf();
        Integer defaultPort = 80;
        Integer port;
        try {
            port = ParseConf.getPort(parseConf.get(0));
            port = port == null ? defaultPort : port;
        } catch (NullPointerException e) {
            port = defaultPort;
        }


        Integer defaultAmountOfThreads = 5;
        Integer amountOfThreads;
        try {
            amountOfThreads = ParseConf.getAmountOfThreads(parseConf.get(1));
            amountOfThreads = amountOfThreads == null ?
                    defaultAmountOfThreads : amountOfThreads;
        } catch (NullPointerException e) {
            amountOfThreads = defaultAmountOfThreads;
        }

        String document_root;
        try {
            document_root =  ParseConf.getStaticDir(parseConf.get(2));
        } catch (NullPointerException e) {
            document_root = null;
        }

        System.out.println("Main: " + Thread.currentThread().getName());
        System.out.println(amountOfThreads);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            ThreadPool threadPool = new ThreadPool(amountOfThreads);
            while (true) {
                Socket incoming = serverSocket.accept();
                ResponseHandler r = new ResponseHandler(incoming, document_root);
                threadPool.execute(r);
            }
        }
    }
}
