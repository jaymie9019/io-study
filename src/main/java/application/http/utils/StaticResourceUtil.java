package application.http.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 静态资源的工具类
 * @author Jaymie on 2020/12/22
 */
public class StaticResourceUtil {
    /**
     * 获取请求的静态资源的绝对路径
     * @param staticRoot 静态资源的根路径
     * @param path 请求的path
     * @return 绝对路径
     */
    public static String getAbsolutePath(String staticRoot, String path) {
        String absolutePath = StaticResourceUtil.class.getResource("/").getPath();
        return absolutePath.replace("\\\\", "/") + staticRoot + "/" + path;
    }

    /**
     * 读取静态文件中的内容，写入到输出流中
     */
    public static void outPutStaticResource(InputStream inputStream, OutputStream outputStream) {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            // outputSteam中先写入http响应头
            outputStream.write(HttpProtocolUtil.getHttpHeader200(inputStream.available()).getBytes(StandardCharsets.UTF_8));
            byte[] buffer = new byte[1024];
            int readBytes = 0;
            while (readBytes != -1) {
                readBytes = bis.read(buffer);
                outputStream.write(buffer);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
