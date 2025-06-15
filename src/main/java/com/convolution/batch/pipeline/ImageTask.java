package com.convolution.batch.pipeline;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

public class ImageTask {
    public final Path inputPath, outputPath;
    public final BufferedImage image;
    public final long tReadStart, tReadEnd; // можно добавить для мониторинга
    public static final ImageTask POISON = new ImageTask(null, null, null, 0, 0);

    public ImageTask(Path in, Path out, BufferedImage img, long tStart, long tEnd) {
        inputPath = in;
        outputPath = out;
        image = img;
        tReadStart = tStart;
        tReadEnd = tEnd;
    }
}
