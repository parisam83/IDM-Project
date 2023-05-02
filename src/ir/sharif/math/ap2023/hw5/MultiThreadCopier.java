package ir.sharif.math.ap2023.hw5;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class MultiThreadCopier {
    public static final long SAFE_MARGIN = 6;
    private SourceProvider sourceProvider;
    private String dest;
    private int workerCount;
    private long size, length;
    private MainThread mainThread;
    private ArrayList<Worker> workers = new ArrayList<>();

    public MultiThreadCopier(SourceProvider sourceProvider, String dest, int workerCount) {
        this.sourceProvider = sourceProvider;
        this.dest = dest;
        this.workerCount = workerCount;
        size = sourceProvider.size();
        length = size/workerCount;
    }
    public void start() {
        createFile();
        divideFileBetweenWorkers();
        mainThread = new MainThread(workers);
    }

    private void createFile(){
        try {
            RandomAccessFile file = new RandomAccessFile(dest, "rw");
            file.write(new byte[(int) size]);
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void divideFileBetweenWorkers(){
        for (long i = 0; i < workerCount - 1; i++)
            createNewWorker(i * length, i * length + length);
        createNewWorker((workerCount - 1) * length, size);
    }

    private void createNewWorker(long leftIndex, long rightIndex){
        Worker worker = new Worker(workers.size(), leftIndex, rightIndex, sourceProvider, dest);
        worker.start();
        workers.add(worker);
    }
}
