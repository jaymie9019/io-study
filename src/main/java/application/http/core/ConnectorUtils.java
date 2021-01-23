package application.http.core;

import java.io.File;

/**
 * @author Jaymie on 2021/1/21
 */
public class ConnectorUtils {
    public static final String WEB_ROOT =
            System.getProperty("user.dir") + File.separator + "src/main/resources/static/";

    public static final String PROTOCOL = "HTTP /1.1";
    public static final String CARRIAGE = "\r";
    public static final String NEWLINE = "\n";
    public static final String SPACE = " ";

    public static String  rendersStatus(HttpStatus status) {
        StringBuilder sb = new StringBuilder(PROTOCOL)
                .append(SPACE)
                .append(status.getStatusCode())
                .append(SPACE)
                .append(status.getReason())
                .append(CARRIAGE).append(NEWLINE)
                .append(CARRIAGE).append(NEWLINE);

        return sb.toString();
    }
}
