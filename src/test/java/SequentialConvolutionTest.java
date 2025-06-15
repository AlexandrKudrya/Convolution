import boofcv.struct.image.GrayU8;
import com.convolution.core.BorderMode;
import com.convolution.core.sequential.SequentialConvolution;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static utils.ImageAssert.assertEqualImage;

class SequentialConvolutionTest {

    private final float[][] IDENTITY = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
    };

    private final float[][] ZERO = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
    };

    private final float[][] BLUR = {
            {1/9f, 1/9f, 1/9f},
            {1/9f, 1/9f, 1/9f},
            {1/9f, 1/9f, 1/9f}
    };

    @Test
    void identityKernelReturnsSameImage() {
        GrayU8 img = testImage(8, 8);
        SequentialConvolution conv = new SequentialConvolution(BorderMode.CLAMP);
        GrayU8 result = conv.applyKernel(img, IDENTITY);

        assertEqualImage(img, result);
    }

    @Test
    void zeroKernelReturnsZeroImage() {
        GrayU8 img = testImage(8, 8);
        SequentialConvolution conv = new SequentialConvolution(BorderMode.CLAMP);
        GrayU8 result = conv.applyKernel(img, ZERO);

        for (int y = 0; y < result.height; ++y)
            for (int x = 0; x < result.width; ++x)
                assertEquals(0, result.get(x, y));
    }

    @Test
    void blurKernelBlurs() {
        GrayU8 img = testImage(5, 5);
        SequentialConvolution conv = new SequentialConvolution(BorderMode.REFLECT);
        GrayU8 result = conv.applyKernel(img, BLUR);

        int expected = average9(img, 1, 1);
        assertEquals(expected, result.get(1, 1), 1);
    }

    private GrayU8 testImage(int w, int h) {
        GrayU8 img = new GrayU8(w, h);
        int value = 0;
        for (int y = 0; y < h; ++y)
            for (int x = 0; x < w; ++x)
                img.set(x, y, (value++) % 256);
        return img;
    }

    private int average9(GrayU8 img, int cx, int cy) {
        int sum = 0, count = 0;
        for (int dy = -1; dy <= 1; ++dy)
            for (int dx = -1; dx <= 1; ++dx) {
                int x = Math.max(0, Math.min(img.width - 1, cx + dx));
                int y = Math.max(0, Math.min(img.height - 1, cy + dy));
                sum += img.get(x, y);
                count++;
            }
        return sum / count;
    }
}
