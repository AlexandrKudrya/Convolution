package com.convolution.core;

import boofcv.struct.image.GrayU8;

public class Convolution {
    private final float[][] kernel;
    private final int kernelWidth;
    private final int kernelHeight;

    public Convolution(float[][] kernel) {
        this.kernel = kernel;
        this.kernelHeight = kernel.length;
        this.kernelWidth = kernel[0].length;
    }

    public GrayU8 apply(GrayU8 input) {
        GrayU8 output = new GrayU8(input.width, input.height);
        int offsetX = kernelWidth / 2;
        int offsetY = kernelHeight / 2;

        // Проходим по каждому пикселю изображения (кроме краёв)
        for (int y = offsetY; y < input.height - offsetY; y++) {
            for (int x = offsetX; x < input.width - offsetX; x++) {
                float sum = 0.0f;

                // Применяем ядро свёртки
                for (int ky = 0; ky < kernelHeight; ky++) {
                    for (int kx = 0; kx < kernelWidth; kx++) {
                        int imgX = x + (kx - offsetX);
                        int imgY = y + (ky - offsetY);
                        int pixelValue = input.get(imgX, imgY); // Значение пикселя (0-255)
                        float kernelValue = kernel[ky][kx];
                        sum += pixelValue * kernelValue;
                    }
                }

                // Ограничиваем результат в пределах 0-255
                int result = Math.round(sum);
                if (result < 0) result = 0;
                if (result > 255) result = 255;

                output.set(x, y, result);
            }
        }

        return output;
    }
}