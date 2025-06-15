import boofcv.struct.image.GrayU8;
import com.convolution.core.parallel.ParallelColumnConvolution;
import com.convolution.core.parallel.ParallelPixelConvolution;
import com.convolution.core.parallel.ParallelRowConvolution;
import com.convolution.core.parallel.TiledConvolution;
import com.convolution.core.sequential.SequentialConvolution;
import net.jqwik.api.*;

import static utils.ImageAssert.assertEqualImage;

import com.convolution.core.*;

class SequentialConvolutionPropertyTest {

    @Property
    void identityKernelIsNoop(@ForAll("grayU8s") GrayU8 img) {
        float[][] id = new float[][]{
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };
        BaseConvolution seq = new SequentialConvolution(BorderMode.CLAMP);
        GrayU8 out = seq.applyKernel(img, id);
        assertEqualImage(img, out);
    }

    @Property
    void shiftLeftAndRightIsIdentity(@ForAll("grayU8s") GrayU8 img) {
        float[][] shiftLeft = {
                {0, 0, 0},
                {1, 0, 0},
                {0, 0, 0}
        };
        float[][] shiftRight = {
                {0, 0, 0},
                {0, 0, 1},
                {0, 0, 0}
        };

        var conv = new SequentialConvolution(BorderMode.REFLECT);

        // Сдвинул влево, потом вправо
        GrayU8 left = conv.applyKernel(img, shiftLeft);
        GrayU8 result = conv.applyKernel(left, shiftRight);

        assertEqualImage(img, result);
    }

    @Property
    void kernelPaddingZerosDoesNotChangeResult(
            @ForAll("grayU8s") GrayU8 img,
            @ForAll("kernels") float[][] kernel
    ) {
        // Сделаем padding ядра до +2 по размеру, по центру — исходное
        int k = kernel.length;
        int pad = 2;
        int newSize = k + 2 * pad;
        float[][] padded = new float[newSize][newSize];
        for (int y = 0; y < k; ++y)
            for (int x = 0; x < k; ++x)
                padded[y + pad][x + pad] = kernel[y][x];

        var conv = new SequentialConvolution(BorderMode.CLAMP);
        GrayU8 ref = conv.applyKernel(img, kernel);
        GrayU8 test = conv.applyKernel(img, padded);

        assertEqualImage(ref, test);
    }

    @Provide
    Arbitrary<GrayU8> grayU8s() {
        return utils.ConvolutionArbitraries.grayU8s();
    }

    @Provide
    Arbitrary<float[][]> kernels() {
        return utils.ConvolutionArbitraries.kernels();
    }
}