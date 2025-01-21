package application.http;

import application.http.connector.BioConnector;
import application.http.connector.BioConnectorWithoutThreadPool;
import application.http.connector.NioConnectorWithoutThreadPool;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author Jaymie on 2021/1/21
 */
@Slf4j
public class BootStrap {

    public static void main(String[] args) throws InterruptedException, IOException {
        BioConnector connector = new BioConnector();

        // BioConnectorWithoutThreadPool connector = new
        // BioConnectorWithoutThreadPool();
        // NioConnector connector = new NioConnector();
        // NioConnectorWithoutThreadPool connector = new
        // NioConnectorWithoutThreadPool();
        connector.run();
    }
}
