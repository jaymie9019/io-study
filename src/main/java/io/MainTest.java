package io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.CharBuffer;

/**
 * @author Jaymie on 2021/1/18
 */
public class MainTest {
    public static void main(String[] args) throws IOException {
        // 放入hello
        CharBuffer charBuffer = CharBuffer.allocate(10);
        charBuffer.put('H');
        charBuffer.put('E');
        charBuffer.put('L');
        charBuffer.put('L');
        charBuffer.put('O');
        printBuffer(charBuffer);
        // 可以看出clear只是把 position 的位置设置为了0
        charBuffer.clear();
        charBuffer.put("S");
        printBuffer(charBuffer);

    }

    private static void printBuffer(CharBuffer charBuffer) {
        System.out.println();
        for (char c : charBuffer.array()) {
            System.out.print(c);
        }
    }
}
