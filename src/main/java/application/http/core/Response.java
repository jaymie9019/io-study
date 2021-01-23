package application.http.core;

import application.http.utils.HttpProtocolUtil;
import application.http.utils.StaticResourceUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * @author Jaymie on 2021/1/21
 */
public class Response implements ServletResponse {
    private static final int BUFFER_SIZE = 1024;

    private Request request;

    private final OutputStream output;

    public Response(OutputStream outputStream) {
        this.output = outputStream;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public OutputStream getOutput() {
        return output;
    }

    public void sendStaticResource() throws IOException {
        try {
            String fileAbsolutePath = StaticResourceUtil.getAbsolutePath("static", request.getRequestURI());
            File file = new File(fileAbsolutePath);
            // 文件存在
            if (file.exists()) {
                StaticResourceUtil.outPutStaticResource(new FileInputStream(file), output);
            } else {
                // 文件不存在
                output.write(HttpProtocolUtil.getHttpHeader404().getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(output, true);
    }

    @Override
    public void setCharacterEncoding(String s) {

    }

    @Override
    public void setContentLength(int i) {

    }

    @Override
    public void setContentType(String s) {

    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
