package ir.sharif.math.ap2023.hw5;

import java.util.ArrayList;

public class MainThread extends Thread {
    private ArrayList<Worker> workers;

    public MainThread(ArrayList<Worker> workers){
        this.workers = workers;
    }

    private Worker findBikarWorker(){
        for (Worker worker : workers)
            if (worker.getLeftIndex() >= worker.getRightIndex())
                return worker;
        return null;
    }

    private Worker findPorkarWorker(){
        long maxRemainingWork = 0;
        Worker porkarWorker = null;
        for (Worker worker : workers)
            if (worker.getRemainingWork() > maxRemainingWork){
                maxRemainingWork = worker.getRemainingWork();
                porkarWorker = worker;
            }
        if (maxRemainingWork < 6) return null;
        return porkarWorker;
    }

    @Override
    public void run() {
        while (true){
            Worker bikarWorker = findBikarWorker();
            if (bikarWorker == null) continue;

            Worker porkarWorker = findPorkarWorker();
            if (porkarWorker == null) continue;

            long remainingWork = porkarWorker.getRemainingWork();
            bikarWorker.setRightIndex(porkarWorker.getRightIndex());
            porkarWorker.reduceRightIndex(remainingWork/2);
            bikarWorker.setLeftIndex(porkarWorker.getLeftIndex());
        }
    }
}
