package ir.sharif.math.ap2023.hw5;

import java.util.ArrayList;

public class MainThread extends Thread {
    private MySemaphore mainSemaphore;
    private ArrayList<Worker> workers;

    public MainThread(MySemaphore mainSemaphore, ArrayList<Worker> workers){
        this.mainSemaphore = mainSemaphore;
        this.workers = workers;
    }

    private Worker findBikarWorker(){
        for (Worker worker : workers)
            if (!worker.isFinished() && worker.getLeftIndex() >= worker.getRightIndex())
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

    private boolean isDownloadingDone(){
        for (Worker worker : workers)
            if (!worker.isFinished())
                return false;
        return true;
    }

    private void stopAllWorkers(){
        for (Worker worker : workers)
            worker.getMySemaphore().doNotify();
    }

    @Override
    public void run() {
        while (!isDownloadingDone()){
            mainSemaphore.doWait();
            Worker bikarWorker = findBikarWorker();
            Worker porkarWorker = findPorkarWorker();
            if (porkarWorker == null) {
                bikarWorker.setFinished(true);
                bikarWorker.getMySemaphore().doNotify();
                continue;
            }

            long remainingWork = porkarWorker.getRemainingWork();
            bikarWorker.setRightIndex(porkarWorker.getRightIndex());
            porkarWorker.reduceRightIndex(remainingWork/2);
            bikarWorker.setLeftIndex(porkarWorker.getRightIndex());
            bikarWorker.setFinished(false);
            bikarWorker.getMySemaphore().doNotify();
        }
    }
}
