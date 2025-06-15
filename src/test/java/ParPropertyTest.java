import boofcv.struct.image.GrayU8;
import com.convolution.core.parallel.ParallelColumnConvolution;
import com.convolution.core.parallel.ParallelPixelConvolution;
import com.convolution.core.parallel.ParallelRowConvolution;
import com.convolution.core.parallel.TiledConvolution;
import com.convolution.core.sequential.SequentialConvolution;
import net.jqwik.api.*;

import static utils.ImageAssert.assertEqualImage;

import com.convolution.core.*;

class ParPropertyTest {

    @Property
    void parallelPixelEqualsSequential(
            @ForAll("grayU8s") GrayU8 img,
            @ForAll("kernels") float[][] kernel
    ) {
        compareToSequential(new ParallelPixelConvolution(BorderMode.CLAMP), img, kernel);
    }

    @Property
    void parallelRowEqualsSequential(
            @ForAll("grayU8s") GrayU8 img,
            @ForAll("kernels") float[][] kernel
    ) {
        compareToSequential(new ParallelRowConvolution(BorderMode.CLAMP), img, kernel);
    }

    @Property
    void parallelCollumnEqualsSequential(
            @ForAll("grayU8s") GrayU8 img,
            @ForAll("kernels") float[][] kernel
    ) {
        compareToSequential(new ParallelColumnConvolution(BorderMode.CLAMP), img, kernel);
    }

    @Property
    void tiledEqualsSequential(
            @ForAll("grayU8s") GrayU8 img,
            @ForAll("kernels") float[][] kernel
    ) {
        compareToSequential(new TiledConvolution(BorderMode.CLAMP, 16, 16), img, kernel);
    }

    private void compareToSequential(BaseConvolution testImpl, GrayU8 img, float[][] kernel) {
        BaseConvolution seq = new SequentialConvolution(BorderMode.CLAMP);
        GrayU8 ref = seq.applyKernel(img, kernel);
        GrayU8 test = testImpl.applyKernel(img, kernel);
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