package com.convolution.core;

import boofcv.struct.image.GrayU8;

public abstract class BaseConvolution {
    protected final BorderMode borderMode;

    public BaseConvolution(BorderMode borderMode) {
        this.borderMode = borderMode;
    }

    public int getPixelSafe(GrayU8 img, int x, int y) {
        int w = img.width, h = img.height;
        switch (borderMode) {
            case ZERO:
                if (x < 0 || x >= w || y < 0 || y >= h) return 0;
                return img.get(x, y);
            case CLAMP:
                x = Math.max(0, Math.min(w - 1, x));
                y = Math.max(0, Math.min(h - 1, y));
                return img.get(x, y);
            case REFLECT:
                return img.get(reflect(x, w), reflect(y, h));
            default:
                throw new IllegalArgumentException("Unknown border mode");
        }
    }

    private int reflect(int coord, int limit) {
        if (limit <= 1) return 0;
        if (coord < 0) return limit + coord;
        if (coord >= limit) return coord - limit;
        return coord;
    }

    public int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }

    public abstract GrayU8 applyKernel(GrayU8 input, float[][] kernel);
}