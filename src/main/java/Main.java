import ResonseHandler.ResponseHandler;
import ThreadPool.ThreadPool;
import Tools.ParseConf;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Objects;


public class Main {
    public static void main(String[] args) throws IOException {
        String[] parseConf = ParseConf.readConf();
        int defaultPort = 8080;
        var port = ParseConf.getPort(Objects.requireNonNull(parseConf)[0]);
        port = port == null ? defaultPort : port;

        int defaultAmountOfThreads = 16;
        var amountOfThreads = ParseConf.getAmountOfThreads(parseConf[1]);
        amountOfThreads = amountOfThreads == null ? defaultAmountOfThreads : amountOfThreads;

        var document_root =  ParseConf.getStaticDir(parseConf[2]);

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