import application.http.core.ConnectorUtils;
import application.http.core.Request;
import application.http.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Jaymie on 2021/1/21
 */
public class ResponseTest {
    private static final String validRequest ="GET /index.html HTTP/1.1 \\n";
    private static final String invalidRequest ="GET /notfound.html HTTP/1.1 \\n";

    private static final String status200 = "HTTP /1.1 200 OK\r\n\r\n";
    private static final String status404 = "HTTP /1.1 404 File Not Found\r\n\r\n";

    @Test
    public void validRequestTest() throws IOException {
        Request request = TestUtils.createRequest(validRequest);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Response response = new Response(outputStream);
        response.setRequest(request);
        response.sendStaticResource();

        String resource = TestUtils.readFileToString(ConnectorUtils.WEB_ROOT + request.getRequestURI());
        Assertions.assertEquals(status200 + resource, outputStream.toString());

    }

    @Test
    public void invalidRequestTest() throws IOException {
        Request request = TestUtils.createRequest(invalidRequest);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Response response = new Response(outputStream);
        response.setRequest(request);
        response.sendStaticResource();

        String resource = TestUtils.readFileToString(ConnectorUtils.WEB_ROOT + "/404.html");
        Assertions.assertEquals(status404 + resource, outputStream.toString());
    }

}
