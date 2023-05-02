package ir.sharif.math.ap2023.hw5;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Main2 {
    public static void main(String[] args) throws IOException {
//        randomAccessFile.write(new byte[100]);
//        randomAccessFile.close();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile("a.txt", "rw");
                    randomAccessFile.seek(50);
                    for (int i = 50; i < 100; i++) {
                        randomAccessFile.writeByte(98);
                    }
                    randomAccessFile.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        RandomAccessFile randomAccessFile = new RandomAccessFile("a.txt", "rw");
        for (int i = 0; i < 50; i++) {
            randomAccessFile.writeByte(97);
        }
        randomAccessFile.close();
    }
}
