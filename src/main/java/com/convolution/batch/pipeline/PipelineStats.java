package com.convolution.batch.pipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PipelineStats {
    public final AtomicInteger filesRead = new AtomicInteger(0);
    public final AtomicInteger filesProcessed = new AtomicInteger(0);
    public final AtomicInteger filesWritten = new AtomicInteger(0);
    public final int totalFiles;

    public final List<Long> readTimes = Collections.synchronizedList(new ArrayList<>());
    public final List<Long> processTimes = Collections.synchronizedList(new ArrayList<>());
    public final List<Long> writeTimes = Collections.synchronizedList(new ArrayList<>());
    public volatile boolean finished = false;

    public PipelineStats(int totalFiles) {
        this.totalFiles = totalFiles;
    }
}
