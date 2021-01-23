package application.http;

import application.http.connector.BioConnector;
import application.http.connector.NioConnector;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jaymie on 2021/1/21
 */
@Slf4j
public class BootStrap {

    public static void main(String[] args) throws InterruptedException {
        BioConnector connector = new BioConnector();
//        NioConnector connector = new NioConnector();
        connector.start();
    }
}
