package ir.sharif.math.ap2023.hw5;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Worker extends Thread {
    private final int id;
    private long leftIndex, rightIndex;
    private final Object lockLeftIndex = new Object(), lockRightIndex = new Object();
    private final SourceProvider sourceProvider;
    private RandomAccessFile randomAccessFile;
    private final String dest;
    private boolean isNeeded = true;

    public Worker(int id, long leftIndex, long rightIndex, SourceProvider sourceProvider, String dest){
        this.id = id;
        this.leftIndex = leftIndex;
        this.rightIndex = rightIndex;
        this.sourceProvider = sourceProvider;
        this.dest = dest;
        setRandomAccessFile();
    }

    private void setRandomAccessFile(){
        try {
            randomAccessFile = new RandomAccessFile(dest, "rw");
            randomAccessFile.seek(leftIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (isNeeded) {
            System.out.println("hi");
            SourceReader sourceReader = sourceProvider.connect(leftIndex);
            try {
                // if (id == 2) Thread.sleep(2000);
                while (leftIndex < rightIndex) {
                    //randomAccessFile.writeByte(id);
                    randomAccessFile.write(sourceReader.read() + id - 20);
                    synchronized (lockLeftIndex) {
                        leftIndex++;
                    }
                }
                randomAccessFile.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public long getRemainingWork(){
        return rightIndex - leftIndex;
    }

    public void reduceRightIndex(long amount){
        synchronized (lockRightIndex) {
            rightIndex -= amount;
        }
    }

    public long getLeftIndex() {
        return leftIndex;
    }

    public void setLeftIndex(long leftIndex) {
        this.leftIndex = leftIndex;
    }

    public long getRightIndex() {
        return rightIndex;
    }

    public void setRightIndex(long rightIndex) {
        this.rightIndex = rightIndex;
    }

    public void setNeeded(boolean needed) {
        isNeeded = needed;
    }
}
