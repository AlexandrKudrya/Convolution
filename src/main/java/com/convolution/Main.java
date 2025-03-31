package com.convolution;

import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;
import com.convolution.core.Convolution;
import com.convolution.utils.GrayScaleConverter;
import com.convolution.utils.ImageLoader;

import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {
        String inputPath = "src/main/resources/input.jpg";
        String outputPath = "build/output.jpg";

        // Загрузка изображения
        BufferedImage colorImage = ImageLoader.loadImage(inputPath);
        GrayU8 grayImage = GrayScaleConverter.toGrayScale(colorImage);

        // Ядро свёртки (sharpen)
        float[][] kernelData = {
                {0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f},
                {0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f},
                {0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f},
                {0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f},
                {0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f},
                {0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f},
                {0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f},
                {0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f},
                {0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f, 0.0123f},

        };

        // Применение свёртки
        Convolution convolution = new Convolution(kernelData);
        long startTime = System.currentTimeMillis();
        GrayU8 result = convolution.apply(grayImage);
        long endTime = System.currentTimeMillis();

        // Конвертация обратно в BufferedImage и сохранение
        BufferedImage outputImage = ConvertBufferedImage.convertTo(result, null);
        ImageLoader.saveImage(outputImage, outputPath);

        System.out.println("Convolution time: " + (endTime - startTime) + " ms");
    }
}