package convolution;

import com.convolution.core.BorderMode;
import com.convolution.core.parallel.ParallelColumnConvolution;
import com.convolution.core.parallel.ParallelPixelConvolution;
import com.convolution.core.parallel.ParallelRowConvolution;
import com.convolution.core.parallel.TiledConvolution;
import com.convolution.core.sequential.SequentialConvolution;
import org.openjdk.jmh.annotations.*;
import boofcv.struct.image.GrayU8;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(3)
@Warmup(iterations = 3)
@Measurement(iterations = 3)
public class ConvolutionBenchmark {
    @Param({"64", "1024"})
    int imageSize;

    @Param({"3", "9"})
    int kernelSize;

    GrayU8 image;
    float[][] kernel;

    SequentialConvolution seq;
    ParallelRowConvolution parRow;
    ParallelPixelConvolution parPix;
    ParallelColumnConvolution parCol;
    TiledConvolution tiled16;
    TiledConvolution tiled32;
    TiledConvolution tiled128;

    @Setup
    public void setup() {
        image = randomImage(imageSize, imageSize);
        kernel = randomKernel(kernelSize);
        seq = new SequentialConvolution(BorderMode.CLAMP);
        parRow = new ParallelRowConvolution(BorderMode.CLAMP);
        parPix = new ParallelPixelConvolution(BorderMode.CLAMP);
        parCol = new ParallelColumnConvolution(BorderMode.CLAMP);
        tiled16 = new TiledConvolution(BorderMode.CLAMP, 16, 16);
        tiled32 = new TiledConvolution(BorderMode.CLAMP, 32, 32);
        tiled128 = new TiledConvolution(BorderMode.CLAMP, 128, 128);
    }

    @Benchmark
    public GrayU8 seqConvolution() {
        return seq.applyKernel(image, kernel);
    }

    @Benchmark
    public GrayU8 parRowConvolution() {
        return parRow.applyKernel(image, kernel);
    }

    @Benchmark
    public GrayU8 parPixConvolution() {
        return parPix.applyKernel(image, kernel);
    }

    @Benchmark
    public GrayU8 parColConvolution() {
        return parCol.applyKernel(image, kernel);
    }

    @Benchmark
    public GrayU8 tiled16Convolution() {
        return tiled16.applyKernel(image, kernel);
    }

    @Benchmark
    public GrayU8 tiled32Convolution() {
        return tiled32.applyKernel(image, kernel);
    }

    @Benchmark
    public GrayU8 tiled128Convolution() {
        return tiled128.applyKernel(image, kernel);
    }

    private GrayU8 randomImage(int w, int h) {
        GrayU8 img = new GrayU8(w, h);
        Random rnd = new Random(42);
        for (int y = 0; y < h; ++y)
            for (int x = 0; x < w; ++x)
                img.set(x, y, rnd.nextInt(256));
        return img;
    }

    private float[][] randomKernel(int k) {
        float[][] kernel = new float[k][k];
        Random rnd = new Random(142);
        for (int i = 0; i < k; ++i)
            for (int j = 0; j < k; ++j)
                kernel[i][j] = rnd.nextFloat() * 2f - 1f;
        return kernel;
    }
}