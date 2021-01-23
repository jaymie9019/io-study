package server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Jaymie on 2021/1/18
 */
public class HttpServer {

    private static ByteBuffer echoBuffer = ByteBuffer.allocate(1024);


    public static void main(String[] args) throws IOException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors() + 2,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3000));


        // 创建选择器
        Selector selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket socket = serverSocketChannel.socket();
        socket.bind(new InetSocketAddress(9010));
        // 注册到选择器中
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 接受连接
        while (true) {
            int num = selector.select();
            if (num <= 0) {
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            if (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                    // 接受新的连接
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = ssc.accept();
                    socketChannel.configureBlocking(false);
                    // 将新的连接注册到选择器
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    iterator.remove();
                } else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();

                    // Echo data
                    int bytesEchoed = 0;

                    // 读取数据

                    while (true) {
                        echoBuffer.clear();
                        int r = socketChannel.read(echoBuffer);
                        if (r <= 0) {
                            break;
                        }

                        echoBuffer.flip();
                        socketChannel.write(echoBuffer);
                        bytesEchoed += r;
                    }

                    System.out.println("Echoed " + bytesEchoed + " from " + socketChannel);
                    iterator.remove();
                }
            }
        }

    }
}
