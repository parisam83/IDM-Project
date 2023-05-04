package ir.sharif.math.ap2023.hw5;

public class Main {

    public static void main(String[] args) {
        new MultiThreadCopier(new Sos(), "./a.result", 6).start();
    }

    static class Ss implements SourceReader {
        long offset;

        Ss(long offset) {
            this.offset = offset;
        }

        @Override
        public byte read() {
            try {
                if (offset < 50)
                    Thread.sleep(1);
                else
                    Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            offset++;
            return 97;
        }
    }

    static class Sos implements SourceProvider {

        @Override
        public SourceReader connect(long offset) {
            return new Ss(offset);
        }

        @Override
        public long size() {
            return 100;
        }
    }
}
