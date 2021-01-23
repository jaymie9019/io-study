import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author Jaymie on 2021/1/21
 */
public class TestClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8888);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("GET /index.html HTTP/1.1 \\n".getBytes(StandardCharsets.UTF_8));
        socket.shutdownOutput();

        InputStream inputStream = socket.getInputStream();
        byte[] buffer = new byte[2048];
        int length = inputStream.read(buffer);
        System.out.println(length);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) buffer[i]);
        }
        System.out.println(sb.toString());

        socket.shutdownInput();
        socket.close();

    }
}
