package com.fido.tro.cli;

public class CliBenchmark {
    private final long beginTime;

    private CliBenchmark() {
        beginTime = System.currentTimeMillis();
    }

    public CliBenchmark(boolean showTime) {
        this();

        if (showTime) {
            System.out.println("Benchmark start at " + beginTime + "ms");
        }
    }

    public long timeTaken() {
        return timeTaken(false);
    }

    private long timeTaken(boolean showTime) {
        final long endTime = System.currentTimeMillis();
        final long timeTaken = endTime - beginTime;

        if (showTime) {
            System.out.println("Total execution time: " + timeTaken + "ms");
        }

        return timeTaken;
    }
}
