package com.convolution.core.parallel;

import boofcv.struct.image.GrayU8;
import com.convolution.core.BaseConvolution;
import com.convolution.core.BorderMode;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ParallelColumnConvolution extends BaseConvolution {

    public ParallelColumnConvolution(BorderMode mode) {
        super(mode);
    }

    @Override
    public GrayU8 applyKernel(GrayU8 input, float[][] kernel) {
        int w = input.width;
        int h = input.height;
        int kH = kernel.length;
        int kW = kernel[0].length;
        int oX = kW / 2;
        int oY = kH / 2;

        GrayU8 output = new GrayU8(w, h);

        IntStream.range(0, w).parallel().forEach(x -> {
            for (int y = 0; y < h; y++) {
                float sum = 0f;
                for (int ky = 0; ky < kH; ky++) {
                    for (int kx = 0; kx < kW; kx++) {
                        int px = x + kx - oX;
                        int py = y + ky - oY;
                        int val = getPixelSafe(input, px, py);
                        sum += val * kernel[ky][kx];
                    }
                }
                output.set(x, y, clamp(Math.round(sum)));
            }
        });

        return output;
    }
}
