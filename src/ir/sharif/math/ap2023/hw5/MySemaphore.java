package ir.sharif.math.ap2023.hw5;

public class MySemaphore {
    private int signals = 0;
    private final Object object = new Object();

    public void doNotify(){
        synchronized (object){
            signals++;
            object.notify();
        }
    }

    public void doWait(){
        synchronized (object){
            if (signals == 0) {
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            signals--;
        }
    }
}
