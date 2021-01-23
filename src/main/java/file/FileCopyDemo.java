package file;

import com.github.houbb.junitperf.core.annotation.JunitPerfConfig;
import com.github.houbb.junitperf.core.report.impl.ConsoleReporter;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Jaymie on 2021/1/19
 */
public class FileCopyDemo {
    private static final  int ROUNDS = 5;

    private static void benchmark(FileCopyRunner fileCopyRunner, File src, File dst) {
        long elapsed = 0L;
        for (int i = 0; i < ROUNDS; i++) {
            long startTime = System.currentTimeMillis();
            fileCopyRunner.copyFile(src, dst);
            elapsed += System.currentTimeMillis() - startTime;
            dst.delete();
        }
        System.out.println("平均耗时: " + elapsed / ROUNDS);
    }

    @JunitPerfConfig(threads = 2, warmUp = 1000, duration = 2000, reporter = ConsoleReporter.class)
    public void perfTest(FileCopyRunner fileCopyRunner, File src, File dst) {
        fileCopyRunner.copyFile(src, dst);
        dst.delete();
    }



    public static void main(String[] args) {
        // 按字节拷贝
        FileCopyRunner noBufferStreamCopy = (src, dist) -> {
            try (InputStream in = new FileInputStream(src);
                 OutputStream out = new FileOutputStream(dist)) {
                int res;
                while ((res = in.read()) != -1) {
                    out.write(res);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        // 使用缓冲区
        FileCopyRunner bufferedStreamCopy = (src, dist) -> {
            try (InputStream in = new BufferedInputStream(new FileInputStream(src));
                 OutputStream out = new BufferedOutputStream(new FileOutputStream(dist))) {
//                byte[] buffer = new byte[1024];
                byte[] buffer = new byte[2048];
                // 读取到的字节数量
                int res;
                while ((res = in.read(buffer, 0, buffer.length)) != -1) {
                    // 从缓冲区开始读
                    out.write(buffer, 0, res);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };


        FileCopyRunner nioBufferCopy = (src, dist) -> {
            try (FileChannel in = new FileInputStream(src).getChannel();
                 FileChannel out = new FileOutputStream(dist).getChannel()) {
                // 使用缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                while (in.read(buffer) != -1) {
                    // 往buffer 里面写完了
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        out.write(buffer);
                    }
                    // 读模式转换成写模式
                    buffer.clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        FileCopyRunner nioBufferCopyV2 = (src, dst) -> {
            try (FileChannel in = new FileInputStream(src).getChannel();
                 FileChannel out = new FileOutputStream(dst).getChannel()){
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                while (true) {
                    byteBuffer.clear();
                    int read = in.read(byteBuffer);
                    if (read == -1) {
                        break;
                    }
                    byteBuffer.flip();
                    out.write(byteBuffer);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        // 使用两个 channel 直接传输数据
        FileCopyRunner nioTransferCopy = (src, dist) -> {
            try (FileChannel in = new FileInputStream(src).getChannel();
                 FileChannel out = new FileOutputStream(dist).getChannel()) {
                long transferred = 0L;
                while (transferred != in.size()) {
                    // transferred 表示每次转移的字节数量
                    transferred += in.transferTo(0, in.size(), out);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        FileCopyRunner apacheFileUtilCopy = (src, dist) -> {
            try {
                FileUtils.copyFile(src, dist);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };



        // 开始测试
        File src = new File("src/main/resources/img.jpeg");
        File dst = new File("src/main/resources/img_copy.jpeg");

//
//
//        // 248k的图片
//        System.out.print("noBufferStreamCopy");
//        benchmark(noBufferStreamCopy, src, dst);
//
        System.out.print("bufferedStreamCopy");
//        benchmark(bufferedStreamCopy, src, dst);
//
//        System.out.print("nioBufferCopy");
//        benchmark(nioBufferCopy, src, dst);
//
//        System.out.print("nioTransferCopy");
//        benchmark(nioTransferCopy, src, dst);

        // 7.5M
        // 344.4MB
//        File bigSrc = new File("src/main/resources/big.jpg");
        File bigSrc = new File("src/main/resources/book.pdf");
//        File bigDst = new File("src/main/resources/big_copy.jpg");
        File bigDst = new File("src/main/resources/book_copy.jpg");

//        nioTransferCopy.copyFile(bigSrc, bigDst);
//
//        System.out.print("bufferedStreamCopy");
//        benchmark(bufferedStreamCopy, bigSrc, bigDst);
//
//        System.out.print("nioBufferCopy");
//        benchmark(nioBufferCopy, bigSrc, bigDst);
//
//        System.out.print("nioTransferCopy");
//        benchmark(nioTransferCopy, bigSrc, bigDst);
//
//        System.out.print("apacheFileUtilCopy");
//        benchmark(apacheFileUtilCopy, bigSrc, bigDst);


    }


}

interface FileCopyRunner {
    /**
     * 拷贝文件
     * @param source
     * @param dst
     */
    void copyFile(File source, File dst);
}
