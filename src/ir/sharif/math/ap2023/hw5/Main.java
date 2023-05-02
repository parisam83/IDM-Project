package ir.sharif.math.ap2023.hw5;

public class Main {

    public static void main(String[] args) {
        for (int i = 0; i < 26; i++) {
            // System.out.println((char) (i + 97));
        }
        new MultiThreadCopier(new Sos(), "./a.result", 26).start();
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
                    Thread.sleep(10);
                else
                    Thread.sleep(100);
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
