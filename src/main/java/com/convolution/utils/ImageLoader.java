package com.convolution.utils;

import boofcv.io.image.UtilImageIO;

import java.awt.image.BufferedImage;

public class ImageLoader {
    public static BufferedImage loadImage(String path) {
        return UtilImageIO.loadImage(path);
    }

    public static void saveImage(BufferedImage image, String path) {
        UtilImageIO.saveImage(image, path);
    }
}