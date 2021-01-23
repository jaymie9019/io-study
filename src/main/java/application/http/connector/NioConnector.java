package application.http.connector;

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
import java.nio.channels.*;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jaymie on 2021/1/23
 */
@Slf4j
public class NioConnector implements Runnable {
    public static final int PORT = 9999;

    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    private AtomicInteger count =new AtomicInteger(0);

    private int port;

    public NioConnector(int port) {
        this.port = port;
    }

    public NioConnector() {
        this(PORT);
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));

            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            log.info("启动服务器，监听端口: {}", port);

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
            log.info("count: {}", count.incrementAndGet());
        } else if (selectionKey.isReadable()){
            SocketChannel client = (SocketChannel) selectionKey.channel();
            // 不希望 SocketChannel 再被 select 轮训，取消注册
            selectionKey.cancel();
            // 然后重新把 SocketChannel 改成阻塞状态
            client.configureBlocking(true);
            Socket socket = client.socket();
            SocketHandler.handle(socket, client);
        }

    }
}
