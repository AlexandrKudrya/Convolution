package com.convolution.batch.pipeline;

import boofcv.struct.image.GrayU8;
import com.convolution.core.BaseConvolution;
import com.convolution.utils.GrayScaleConverter;

import java.util.concurrent.BlockingQueue;

public class ConvolutionWorker implements Runnable {
    private final BlockingQueue<ImageTask> inQueue;
    private final BlockingQueue<ResultTask> outQueue;
    private final BaseConvolution convolution;
    private final PipelineStats stats;
    private final float[][] kernel;

    public ConvolutionWorker(BlockingQueue<ImageTask> inQueue, BlockingQueue<ResultTask> outQueue, BaseConvolution convolution, float[][] kernel, PipelineStats stats) {
        this.inQueue = inQueue;
        this.outQueue = outQueue;
        this.convolution = convolution;
        this.kernel = kernel;
        this.stats = stats;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ImageTask task = inQueue.take();
                if (task == ImageTask.POISON) {
                    break;
                }
                long tProcessStart = System.nanoTime();
                GrayU8 gray = GrayScaleConverter.toGrayScale(task.image);
                GrayU8 result = convolution.applyKernel(gray, kernel);
                long tProcessEnd = System.nanoTime();

                outQueue.put(new ResultTask(
                        task.outputPath, result,
                        task.tReadStart, task.tReadEnd,
                        tProcessStart, tProcessEnd
                ));
                stats.filesProcessed.incrementAndGet();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
