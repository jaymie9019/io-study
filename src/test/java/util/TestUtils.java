package util;

import application.http.core.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Jaymie on 2021/1/21
 */
public class TestUtils {
    public static Request createRequest(String requestString) {
        InputStream inputStream = new ByteArrayInputStream(requestString.getBytes(StandardCharsets.UTF_8));
        Request request = new Request(inputStream);
        request.parse();
        return request;
    }

    public static String readFileToString(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
}
