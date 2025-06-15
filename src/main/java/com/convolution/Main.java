package com.convolution;

import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;
import com.convolution.core.BaseConvolution;
import com.convolution.core.BorderMode;
import com.convolution.core.parallel.ParallelColumnConvolution;
import com.convolution.core.parallel.ParallelPixelConvolution;
import com.convolution.core.parallel.ParallelRowConvolution;
import com.convolution.core.parallel.TiledConvolution;
import com.convolution.core.sequential.SequentialConvolution;
import com.convolution.utils.BasicKernels;
import com.convolution.utils.GrayScaleConverter;
import com.convolution.utils.ImageLoader;

import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {
        String inputPath = "src/main/resources/input.jpg";

        // Загрузка цветного изображения
        BufferedImage colorImage = ImageLoader.loadImage(inputPath);
        GrayU8 gray = GrayScaleConverter.toGrayScale(colorImage);

        float[][] kernel = BasicKernels.topEdgeDetector(3);

        SequentialConvolution sequentialConvolution = new SequentialConvolution(BorderMode.REFLECT);
        ParallelPixelConvolution parallelPixelConvolution = new ParallelPixelConvolution(BorderMode.REFLECT);
        ParallelRowConvolution parallelRowConvolution = new ParallelRowConvolution(BorderMode.REFLECT);
        ParallelColumnConvolution parallelColumnConvolution = new ParallelColumnConvolution(BorderMode.REFLECT);
        TiledConvolution tiledConvolution = new TiledConvolution(BorderMode.REFLECT, 128, 128);

        // measureTime(sequentialConvolution, gray, kernel, "build/outputSeq.jpg");
        measureTime(parallelPixelConvolution, gray, kernel, "build/outputParPix.jpg");
        measureTime(parallelRowConvolution, gray, kernel,"build/outputParRow.jpg");
        measureTime(parallelColumnConvolution, gray, kernel,"build/outputParCol.jpg");
        measureTime(tiledConvolution, gray, kernel,"build/outputParTile.jpg");
    }

    public static void measureTime(BaseConvolution convolution, GrayU8 input, float[][] kernel, String outputPath) {
        long t1 = System.nanoTime();
        GrayU8 result = convolution.applyKernel(input, kernel);
        long t2 = System.nanoTime();
        System.out.println("Time: " + (t2 - t1) / 1_000_000 + " ms");

        BufferedImage outputImageParRow = ConvertBufferedImage.convertTo(result, null);
        ImageLoader.saveImage(outputImageParRow, outputPath);
    }
}