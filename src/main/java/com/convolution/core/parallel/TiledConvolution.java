package com.convolution.core.parallel;

import boofcv.alg.sfm.DepthSparse3D;
import boofcv.struct.image.GrayU8;
import com.convolution.core.BaseConvolution;
import com.convolution.core.BorderMode;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class TiledConvolution extends BaseConvolution {

    private final Integer tileWidth;
    private final Integer tileHeight;

    public TiledConvolution(BorderMode mode, int tileWidth, int tileHeight) {
        super(mode);
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    @Override
    public GrayU8 applyKernel(GrayU8 input, float[][] kernel) {
        int width = input.width, height = input.height;
        int kH = kernel.length, kW = kernel[0].length;
        int oX = kW / 2, oY = kH / 2;

        int cols = (width + tileWidth - 1) / tileWidth;
        int rows = (height + tileHeight - 1) / tileHeight;
        int numTiles = cols * rows;

        GrayU8 output = new GrayU8(width, height);

        IntStream.range(0, numTiles).parallel().forEach(tileIndex -> {
            int tileY = tileIndex / cols;
            int tileX = tileIndex % cols;

            int x0 = tileX * tileWidth;
            int y0 = tileY * tileHeight;
            int x1 = Math.min(x0 + tileWidth, width);
            int y1 = Math.min(y0 + tileHeight, height);

            for (int y = y0; y < y1; y++) {
                for (int x = x0; x < x1; x++) {
                    float sum = 0f;
                    for (int ky = 0; ky < kH; ky++)
                        for (int kx = 0; kx < kW; kx++) {
                            int px = x + kx - oX;
                            int py = y + ky - oY;
                            int val = getPixelSafe(input, px, py);
                            sum += val * kernel[ky][kx];
                        }
                    output.set(x, y, clamp(Math.round(sum)));
                }
            }
        });

        return output;
    }
}
