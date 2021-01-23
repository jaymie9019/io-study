package server;

import java.nio.charset.StandardCharsets;

/**
 * @author Jaymie on 2021/1/18
 */
public class HttpProtocolUtil {
    /**
     * 提供200的响应头信息
     * @return 200的响应头
     */
    public static String getHttpHeader200(long contentLength) {
        // 如果是图片类型 Content-Type需要修改为 image/png
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html;charset=utf-8\r\n" +
                "Content-Length: " + contentLength + "\r\n" +
                "\r\n";
    }

    /**
     * 提供404的响应头信息
     * @return 404的响应头信息
     */
    public static String getHttpHeader404() {
        String str404 = "<h1>404 not found</h1>";
        return "HTTP/1.1 404 NOT Found \n" +
                "Content-Type: text/html;charset=utf-8 \n" +
                "Content-Length: " + str404.getBytes(StandardCharsets.UTF_8).length + " \n" +
                "\r\n";
    }
}
