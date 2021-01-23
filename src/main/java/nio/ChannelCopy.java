package nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * 通道之间进行拷贝
 * @author Jaymie on 2021/1/21
 */
public class ChannelCopy {
    public static void main(String[] args) throws IOException {
        try (FileChannel src = new FileInputStream("src/main/resources/big.jpg").getChannel();
             FileChannel dest = new FileOutputStream("src/main/resources/big_copy.jpg").getChannel()){
            channelCopy1(src, dest);
        }
    }

    private static void channelCopy1(ReadableByteChannel src, WritableByteChannel dest) throws IOException {
        // 定义一个字节缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(16 * 1024);
        // 将src中的内容读到buffer中，即往buffer中写数据，知道读取src的出现EOF，即读完了的信号 -1
        while (src.read(buffer) != -1) {
            // flip 表示buffer读完了，从读模式切换到写模式
            buffer.flip();
            // 只要buffer还有剩余，就一直写到 dest，即dest从buffer中读数据
            while (buffer.hasRemaining()) {
                dest.write(buffer);
            }
            // 清理buffer，下次需要继续写入
            buffer.clear();
        }
    }

}
