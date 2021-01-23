package application.http.connector;

import application.http.constants.HttpServerConstants;
import application.http.core.Request;
import application.http.core.Response;
import application.http.processor.StaticProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Jaymie on 2021/1/21
 */
public class BioConnectorWithoutThreadPool {

    private ServerSocket serverSocket;

    public BioConnectorWithoutThreadPool() {
    }


    public void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(HttpServerConstants.PORT);
        System.out.println("启动服务器，监听端口: " + HttpServerConstants.PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            try (InputStream inputStream = socket.getInputStream();
                 OutputStream outputStream = socket.getOutputStream()) {
                Request request = new Request(inputStream);
                request.parse();
                Response response = new Response(outputStream);
                response.setRequest(request);
                StaticProcessor processor = new StaticProcessor();
                processor.process(request, response);
                socket.close();
            }
        }

    }
}
