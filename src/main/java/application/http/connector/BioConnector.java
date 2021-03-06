package application.http.connector;

import application.http.constants.HttpServerConstants;
import application.http.core.Request;
import application.http.core.Response;
import application.http.processor.SocketHandler;
import application.http.processor.StaticProcessor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Jaymie on 2021/1/21
 */
public class BioConnector {

    private ServerSocket serverSocket;

    public BioConnector() {
    }


    public void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(HttpServerConstants.PORT);
        System.out.println("启动服务器，监听端口: " + HttpServerConstants.PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            SocketHandler.handle(socket);
        }

    }
}
