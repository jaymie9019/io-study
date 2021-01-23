package application.http;

import application.http.connector.NioConnector;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jaymie on 2021/1/21
 */
@Slf4j
public class BootStrap {

    public static void main(String[] args) throws InterruptedException {
//        Connector connector = new Connector();
        log.info("主线程启动");
        NioConnector connector = new NioConnector();
        connector.start();
    }
}
