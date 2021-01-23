package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 单线程版本
 * @author Jaymie on 2021/1/18
 */
public class HttpServerV1 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9090);
        while (true) {
            try (Socket accept = serverSocket.accept();
                 OutputStream outputStream = accept.getOutputStream())
            {
                service(outputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void service(OutputStream outputStream) throws IOException {
        String content = "Hello World";
        outputStream.write(HttpProtocolUtil.getHttpHeader200(content.getBytes(StandardCharsets.UTF_8).length).getBytes(StandardCharsets.UTF_8));
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }
}
