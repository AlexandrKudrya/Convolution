package com.convolution.utils;

public class BasicKernels {

    private static void validateSize(int size) {
        if (size < 1 || size % 2 == 0) {
            throw new IllegalArgumentException("Kernel size must be an odd number >= 1");
        }
    }

    /** Центр — 1, всё остальное — 0 */
    public static float[][] centerSpot(int size) {
        validateSize(size);
        float[][] kernel = new float[size][size];
        int c = size / 2;
        kernel[c][c] = 1f;
        return kernel;
    }

    /** Единичный box-blur (без нормализации) */
    public static float[][] boxBlur(int size) {
        validateSize(size);
        float[][] kernel = new float[size][size];
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                kernel[y][x] = 1f / size / size;
        return kernel;
    }

    /** Центр 8, остальное -1 (усиление резкости) */
    public static float[][] edgeEnhancer(int size) {
        validateSize(size);
        float[][] kernel = new float[size][size];
        int c = size / 2;
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                kernel[y][x] = -1f;
        kernel[c][c] = (float) (size * size - 1); // центр = сумма остальных по модулю
        return kernel;
    }

    /** Верх положительный, низ отрицательный */
    public static float[][] topEdgeDetector(int size) {
        validateSize(size);
        float[][] kernel = new float[size][size];
        int c = size / 2;
        for (int y = 0; y < size; y++) {
            float value = y < c ? 1f : (y > c ? -1f : 0f);
            for (int x = 0; x < size; x++)
                kernel[y][x] = value;
        }
        return kernel;
    }

    /** Крест: центр и + по вертикали и горизонтали усилены */
    public static float[][] crossMask(int size) {
        validateSize(size);
        float[][] kernel = new float[size][size];
        int c = size / 2;
        for (int i = 0; i < size; i++) {
            kernel[c][i] = 1f;
            kernel[i][c] = 1f;
        }
        kernel[c][c] = 4f;
        return kernel;
    }

    /** Контрастирующий фильтр — центр +, окружение - */
    public static float[][] crazyContrast(int size) {
        validateSize(size);
        float[][] kernel = new float[size][size];
        int c = size / 2;
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                kernel[y][x] = -1f;
        kernel[c][c] = (float) (size * size); // центр мощный положительный
        return kernel;
    }

    /** Сдвиг вниз-вправо: центр 1, угол -1 */
    public static float[][] shadowBottomRight(int size) {
        validateSize(size);
        float[][] kernel = new float[size][size];
        int c = size / 2;
        kernel[c][c] = 1f;
        kernel[size - 1][size - 1] = -1f;
        return kernel;
    }
}
