package application.http.connector;

import application.http.constants.HttpServerConstants;
import application.http.core.Request;
import application.http.core.Response;
import application.http.processor.SocketHandler;
import application.http.processor.StaticProcessor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * @author Jaymie on 2021/1/23
 */
@Slf4j
public class NioConnectorWithoutThreadPool {
    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    public NioConnectorWithoutThreadPool() {
    }


    public void run() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(HttpServerConstants.PORT));

            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            log.info("启动服务器，监听端口: {}", HttpServerConstants.PORT);

            while (true) {
                // 阻塞在select（第一阶段阻塞）
                selector.select();
                // 存在就绪的channel
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey : selectionKeys) {
                    handles(selectionKey);
                }
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handles(SelectionKey selectionKey) throws IOException {
        // ACCEPT 事件
        if (selectionKey.isAcceptable()) {
            log.info("服务端接收到连接请求事件");
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel client = serverSocketChannel.accept();
            client.configureBlocking(false);
            // 把SocketChannel 也注册上去，注册读取事件
            client.register(selector, SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()){
            SocketChannel client = (SocketChannel) selectionKey.channel();
            // 不希望 SocketChannel 再被 select 轮训，取消注册
            selectionKey.cancel();
            // 然后重新把 SocketChannel 改成阻塞状态
            client.configureBlocking(true);
            Socket socket = client.socket();
            try (InputStream inputStream = socket.getInputStream();
                 OutputStream outputStream = socket.getOutputStream()){
                Request request = new Request(inputStream);
                request.parse();
                Response response = new Response(outputStream);
                response.setRequest(request);
                StaticProcessor processor = new StaticProcessor();
                processor.process(request, response);
                socket.close();
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
