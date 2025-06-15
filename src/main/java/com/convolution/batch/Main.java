package com.convolution.batch;

import com.convolution.batch.pipeline.PipelineController;
import com.convolution.core.BaseConvolution;
import com.convolution.core.BorderMode;
import com.convolution.core.parallel.ParallelColumnConvolution;
import com.convolution.core.parallel.ParallelPixelConvolution;
import com.convolution.core.parallel.ParallelRowConvolution;
import com.convolution.core.parallel.TiledConvolution;
import com.convolution.core.sequential.SequentialConvolution;
import com.convolution.utils.BasicKernels;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Path inputDir = Paths.get("src/main/resources/archive/data");
        List<BaseConvolution> implementations = List.of(
                new SequentialConvolution(BorderMode.REFLECT),
                new ParallelPixelConvolution(BorderMode.REFLECT),
                new ParallelColumnConvolution(BorderMode.REFLECT),
                new ParallelRowConvolution(BorderMode.REFLECT),
                new TiledConvolution(BorderMode.REFLECT, 16, 16),
                new TiledConvolution(BorderMode.REFLECT, 32, 32),
                new TiledConvolution(BorderMode.REFLECT, 128, 128)
        );

        float[][] kernel = BasicKernels.topEdgeDetector(3);

        int count = 0;
        for (BaseConvolution conv : implementations) {
            String name = conv.getClass().getSimpleName() + "_" + count;
            Path output = Paths.get("build/batch_output_" + name.toLowerCase());
            measure("Pipeline: " + name,
                    () ->
                    {
                        try {
                            PipelineController.runPipeline(
                                    inputDir, output, conv, kernel, 16, 16, 16, 128
                            );
                        } catch (InterruptedException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    () ->
                    {
                        try {
                            SimpleBatchConvolutionRunner.runRecursive(
                                    inputDir, conv, kernel, output
                            );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
            count++;
        }
    }

    public static void measure(String label, Runnable action1, Runnable action2) {
        long t1 = System.nanoTime();
        action1.run();
        long t2 = System.nanoTime();

        long t3 = System.nanoTime();
        action2.run();
        long t4 = System.nanoTime();
        System.out.printf("%s: %.2f s %.2f s%n", label, (t2 - t1) / 1e9, (t4 - t3) / 1e9);
    }
}