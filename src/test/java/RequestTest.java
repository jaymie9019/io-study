import application.http.core.Request;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @author Jaymie on 2021/1/21
 */
public class RequestTest {
    private static final String validRequest ="GET /index.html HTTP/1.1 \\n";
    @Test
    public void testRequest() {
        Request request = TestUtils.createRequest(validRequest);
        System.out.println(request.getRequestURI());
        assertEquals("/index.html", request.getRequestURI());
    }
}
