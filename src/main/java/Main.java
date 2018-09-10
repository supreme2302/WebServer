import ResonseHandler.ResponseHandler;
import ThreadPool.ThreadPool;
import Tools.ParseConf;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Objects;


public class Main {
    public static void main(String[] args) throws IOException {
        List<String> parseConf = ParseConf.readConf();
        Integer defaultPort = 8080;
        Integer port;
        try {
            port = ParseConf.getPort(parseConf.get(0));
            port = port == null ? defaultPort : port;
        } catch (NullPointerException e) {
            port = defaultPort;
        }


        Integer defaultAmountOfThreads = 16;
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
        try (var serverSocket = new ServerSocket(port)) {
            var threadPool = new ThreadPool(amountOfThreads);
            while (true) {
                var incoming = serverSocket.accept();
                var r = new ResponseHandler(incoming, document_root);
                threadPool.execute(r);
            }
        }
    }
}