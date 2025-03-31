package com.convolution.utils;

import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;

import java.awt.image.BufferedImage;

public class GrayScaleConverter {
    public static GrayU8 toGrayScale(BufferedImage colorImage) {
        // Создаём выходное изображение в градациях серого
        GrayU8 grayImage = new GrayU8(colorImage.getWidth(), colorImage.getHeight());

        // Преобразуем BufferedImage в GrayU8
        ConvertBufferedImage.convertFrom(colorImage, grayImage, true);

        return grayImage;
    }
}