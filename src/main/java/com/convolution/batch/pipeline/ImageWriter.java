package com.convolution.batch.pipeline;

import boofcv.io.image.ConvertBufferedImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.util.concurrent.BlockingQueue;

public class ImageWriter implements Runnable {
    private final BlockingQueue<ResultTask> inQueue;
    private final PipelineStats stats;

    public ImageWriter(BlockingQueue<ResultTask> inQueue, PipelineStats stats) {
        this.inQueue = inQueue;
        this.stats = stats;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ResultTask result = inQueue.take();
                if (result == ResultTask.POISON) break;
                long tWriteStart = System.nanoTime();

                Files.createDirectories(result.outputPath.getParent());
                BufferedImage output = ConvertBufferedImage.convertTo(result.result, null);
                ImageIO.write(output, "jpg", result.outputPath.toFile());

                long tWriteEnd = System.nanoTime();

                // Сохраняем времена:
                stats.readTimes.add((result.tReadEnd - result.tReadStart) / 1_000_000);
                stats.processTimes.add((result.tProcessEnd - result.tProcessStart) / 1_000_000);
                stats.writeTimes.add((tWriteEnd - tWriteStart) / 1_000_000);

                stats.filesWritten.incrementAndGet();
            }
        } catch (Exception e) {
            throw new RuntimeException("[Writer] Error: " + e.getMessage(), e);
        }
    }

}
