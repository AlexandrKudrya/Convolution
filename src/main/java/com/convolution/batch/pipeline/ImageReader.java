package com.convolution.batch.pipeline;

import com.convolution.utils.ImageLoader;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ImageReader implements Runnable {
    private final BlockingQueue<ImageTask> outQueue;
    private final List<Path> files;
    private final Path inputRoot, outputRoot;
    private final PipelineStats stats;

    public ImageReader(List<Path> files, Path inputRoot, Path outputRoot, BlockingQueue<ImageTask> outQueue, PipelineStats stats) {
        this.files = files;
        this.inputRoot = inputRoot;
        this.outputRoot = outputRoot;
        this.outQueue = outQueue;
        this.stats = stats;
    }

    @Override
    public void run() {
        for (Path path : files) {
            long t0 = System.nanoTime();
            try {
                BufferedImage image = ImageLoader.loadImage(path.toString());
                Path relative = inputRoot.relativize(path);
                Path outPath = outputRoot.resolve(relative);
                outQueue.put(new ImageTask(path, outPath, image, t0, System.nanoTime()));
                stats.filesRead.incrementAndGet();
            } catch (Exception e) {
                System.err.println("[Reader] Failed: " + path + " " + e.getMessage());
            }
        }
    }
}
