package application.http.processor;

import application.http.core.Request;
import application.http.core.Response;

import java.io.IOException;

/**
 * @author Jaymie on 2021/1/21
 */
public class StaticProcessor {
    public void process(Request request, Response response) {
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
