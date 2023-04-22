package ir.sharif.math.ap2023.hw4;

public interface SourceProvider {
    SourceReader connect(long offset);

    long size();
}
