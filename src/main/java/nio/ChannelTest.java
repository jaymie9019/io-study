package nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;

/**
 * @author Jaymie on 2021/1/17
 */
public class ChannelTest {
    public static void main(String[] args) throws IOException {
        try (ReadableByteChannel source = Channels.newChannel(System.in);
             WritableByteChannel dest = Channels.newChannel(System.out)) {

            ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
            while (source.read(buffer) != -1) {
                // 每次读取 16 * 1023
                buffer.flip();
                dest.write(buffer);
                buffer.compact();
            }
            buffer.flip();

            while (buffer.hasRemaining()) {
                dest.write(buffer);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void openChannel() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8888));

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket socket = serverSocketChannel.socket();
        socket.bind(new InetSocketAddress(8888));


        DatagramChannel datagramChannel = DatagramChannel.open();

        RandomAccessFile randomAccessFile = new RandomAccessFile("somefile", "r");
        FileChannel fileChannel = randomAccessFile.getChannel();
    }


}
