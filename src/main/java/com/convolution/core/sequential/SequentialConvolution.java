package com.convolution.core.sequential;

import boofcv.struct.image.GrayU8;
import com.convolution.core.BaseConvolution;
import com.convolution.core.BorderMode;

public class SequentialConvolution extends BaseConvolution {
    public SequentialConvolution(BorderMode borderMode) {
        super(borderMode);
    }

    @Override
    public GrayU8 applyKernel(GrayU8 input, float[][] kernel) {
        int width = input.width;
        int height = input.height;
        GrayU8 output = new GrayU8(width, height);

        int kernelHeight = kernel.length;
        int kernelWidth = kernel[0].length;
        int offsetX = kernelWidth / 2;
        int offsetY = kernelHeight / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float sum = 0.0f;

                for (int ky = 0; ky < kernelHeight; ky++) {
                    for (int kx = 0; kx < kernelWidth; kx++) {
                        int imgX = x + (kx - offsetX);
                        int imgY = y + (ky - offsetY);

                        int pixelValue = getPixelSafe(input, imgX, imgY);
                        float kernelValue = kernel[ky][kx];
                        sum += pixelValue * kernelValue;
                    }
                }

                int result = Math.round(sum);
                output.set(x, y, Math.max(0, Math.min(255, result)));
            }
        }

        return output;
    }
}