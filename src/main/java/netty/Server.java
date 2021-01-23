package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jaymie on 2021/1/23
 */
@Slf4j
public class Server {
    public static void main(String[] args) throws InterruptedException {
        // 1. 创建两个线程组，一个用于进行网络连接接受，另一个用于网络通讯的读写
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        // 2. 通过辅助类去构造 server / client
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        // 3. 进行 nio server的基础配置
        // 3.1
        serverBootstrap.group(workGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_RCVBUF, 1024 * 32)
                .childOption(ChannelOption.SO_SNDBUF, 1024 * 32)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ServerHandler());
                    }
                });

        // 4. 服务器端绑定端口并启动服务，使用 channel 级别监听close端口 阻塞的方式
        ChannelFuture cf = serverBootstrap.bind(9999).sync();
        cf.channel().closeFuture().sync();

        // 5. 释放资源
        boosGroup.shutdownGracefully();
        workGroup.shutdownGracefully();

    }
}
