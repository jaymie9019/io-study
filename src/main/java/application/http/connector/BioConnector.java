package application.http.connector;

import application.http.core.Request;
import application.http.core.Response;
import application.http.processor.StaticProcessor;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Jaymie on 2021/1/21
 */
public class BioConnector implements Runnable {
    public static final int PORT = 8888;

    private ServerSocket serverSocket;

    private int port;

    public BioConnector(int port) {
        this.port = port;
    }

    public BioConnector() {
        this(PORT);
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        System.out.println("启动服务器，监听端口: " + port);
        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket socket = serverSocket.accept()) {

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            Request request = new Request(inputStream);
            request.parse();
            Response response = new Response(outputStream);
            response.setRequest(request);

            // 只处理静态资源
            StaticProcessor staticProcessor = new StaticProcessor();
            staticProcessor.process(request, response);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
