package com.convolution.batch;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;
import com.convolution.core.BaseConvolution;
import com.convolution.utils.GrayScaleConverter;

public class SimpleBatchConvolutionRunner {

    public static void runRecursive(Path inputRoot, BaseConvolution convolution, float[][] kernel, Path outputRoot) throws IOException {
        List<Path> imagePaths = Files.walk(inputRoot)
                .filter(Files::isRegularFile)
                .filter(path -> { String n = path.getFileName().toString().toLowerCase();
                    return n.endsWith(".jpg")||n.endsWith(".png")||n.endsWith(".bmp");
                })
                .toList();

        List<Long> times = new ArrayList<>();
        for (Path imagePath : imagePaths) {

            BufferedImage inputImage = ImageIO.read(imagePath.toFile());
            if (inputImage == null) {
                System.out.println("Failed to load: " + imagePath);
                continue;
            }

            GrayU8 gray = GrayScaleConverter.toGrayScale(inputImage);

            long start = System.nanoTime();
            GrayU8 result = convolution.applyKernel(gray, kernel);
            long end = System.nanoTime();
            long elapsed = (end - start) / 1_000_000;
            times.add(elapsed);

            Path relative = inputRoot.relativize(imagePath);
            Path outputPath = outputRoot.resolve(relative);
            Files.createDirectories(outputPath.getParent());
            BufferedImage outputImage = ConvertBufferedImage.convertTo(result, null);
            ImageIO.write(outputImage, "jpg", outputPath.toFile());
        }
    }
}