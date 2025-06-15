package utils;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Provide;
import net.jqwik.api.arbitraries.IntegerArbitrary;
import boofcv.struct.image.GrayU8;

public class ConvolutionArbitraries {
    @Provide
    public static Arbitrary<GrayU8> grayU8s() {
        IntegerArbitrary w = net.jqwik.api.Arbitraries.integers().between(10, 64);
        IntegerArbitrary h = net.jqwik.api.Arbitraries.integers().between(10, 64);
        return w.flatMap(width ->
                h.flatMap(height -> net.jqwik.api.Arbitraries.integers().between(0, 255)
                        .array(int[].class).ofSize(width * height)
                        .map(flatPixels -> {
                            GrayU8 img = new GrayU8(width, height);
                            for (int y = 0; y < height; y++)
                                for (int x = 0; x < width; x++)
                                    img.set(x, y, flatPixels[y * width + x]);
                            return img;
                        })));
    }

    @Provide
    public static Arbitrary<float[][]> kernels() {
        return Arbitraries.integers()
                .between(3, 9)
                .filter(size -> size % 2 == 1) // Только нечётные
                .flatMap(size ->
                        Arbitraries.floats().between(-2f, 2f)
                                .array(float[].class).ofSize(size * size)
                                .map(flat -> {
                                    float[][] kernel = new float[size][size];
                                    for (int i = 0; i < size; i++)
                                        for (int j = 0; j < size; j++)
                                            kernel[i][j] = flat[i * size + j];
                                    return kernel;
                                })
                );
    }
}
