package server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.*;

/**
 * @author Jaymie on 2021/1/20
 */
public class MultiPortEcho {
    private void go() throws IOException {
        //1. 创建selector
        Selector selector = Selector.open();

        //2. 开启channel，设置为非阻塞
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        //3. 绑定端口
        ServerSocket ss = serverSocketChannel.socket();
        ss.bind(new InetSocketAddress(8888));

        // 将新打开的 ServerSocketChannels 注册到 Selector上, 注册感兴趣的事件为建立连接
        SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // readOps() 方法告诉我们该事件是新的连接。
        if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
            // 获取channel，进行强转
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = channel.accept();
            socketChannel.configureBlocking( false );
            SelectionKey readyKey = socketChannel.register(selector, SelectionKey.OP_READ);

        }



    }
}
