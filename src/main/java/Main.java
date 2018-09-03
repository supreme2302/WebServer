import ResonseHandler.ResponseHandler;
import ThreadPool.ThreadPool;
import Tools.ParseConf;

import java.io.IOException;
import java.net.ServerSocket;


public class Main {
    public static void main(String[] args) throws IOException {
        String[] parseConf = ParseConf.readConf();
        Integer defaultPort = 8080;
        Integer port = ParseConf.getPort(parseConf[0]);
        port = port == 0 ? defaultPort : port;

        Integer defaultAmountOfThreads = 16;
        Integer amountOfThreads = ParseConf.getAmountOfThreads(parseConf[1]);
        amountOfThreads = amountOfThreads == 0 ? defaultAmountOfThreads : amountOfThreads;

        String document_root =  ParseConf.getStaticDir(parseConf[2]);

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