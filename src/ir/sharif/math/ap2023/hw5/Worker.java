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
    private boolean isFinished;
    private MySemaphore mainSemaphore, mySemaphore;


    public Worker(int id, long leftIndex, long rightIndex, SourceProvider sourceProvider, String dest, MySemaphore mainSemaphore){
        this.id = id;
        this.leftIndex = leftIndex;
        this.rightIndex = rightIndex;
        this.sourceProvider = sourceProvider;
        this.dest = dest;
        this.mainSemaphore = mainSemaphore;
        this.mySemaphore = new MySemaphore();
        this.isFinished = false;
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
        while (!isFinished) {
            SourceReader sourceReader = sourceProvider.connect(leftIndex);
            setRandomAccessFile();
            try {
                while (leftIndex < rightIndex) {
                    randomAccessFile.write(sourceReader.read());
                    synchronized (lockLeftIndex) {
                        leftIndex++;
                    }
                }
                randomAccessFile.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mainSemaphore.doNotify();
            mySemaphore.doWait();
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

    public MySemaphore getMySemaphore() {
        return mySemaphore;
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

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public long getId() {
        return id;
    }
}
