package application.http.processor;

import application.http.core.Request;
import application.http.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.NamedThreadFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Jaymie on 2021/1/23
 */
@Slf4j
public class SocketHandler {
    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5
            , 100, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(2),
            new NamedThreadFactory("worker"));

    /**
     * 负责从socket对象中读取和写入数据
     * @param socket socket
     */
    public static void handle(Socket socket, SocketChannel client) {
        threadPoolExecutor.execute(() -> {
            log.warn("开始处理数据");
            try (InputStream inputStream = socket.getInputStream();
                 OutputStream outputStream = socket.getOutputStream()){
                Request request = new Request(inputStream);
                request.parse();
                Response response = new Response(outputStream);
                response.setRequest(request);
                StaticProcessor processor = new StaticProcessor();
                processor.process(request, response);
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}