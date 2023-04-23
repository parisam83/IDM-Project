package ir.sharif.math.ap2023.hw5;

public interface SourceProvider {
    SourceReader connect(long offset);

    long size();
}
