package com.convolution.batch.pipeline;

import boofcv.struct.image.GrayU8;

import java.nio.file.Path;

public class ResultTask {
    public final Path outputPath;
    public final GrayU8 result;
    public final long tReadStart, tReadEnd, tProcessStart, tProcessEnd;
    public static final ResultTask POISON = new ResultTask(null, null, -1, -1, -1, -1);

    public ResultTask(Path outputPath, GrayU8 result, long tReadStart, long tReadEnd, long tProcessStart, long tProcessEnd) {
        this.outputPath = outputPath;
        this.result = result;
        this.tReadStart = tReadStart;
        this.tReadEnd = tReadEnd;
        this.tProcessStart = tProcessStart;
        this.tProcessEnd = tProcessEnd;
    }
}
