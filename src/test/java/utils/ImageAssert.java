package utils;

import boofcv.struct.image.GrayU8;

public class ImageAssert {
    public static void assertEqual(GrayU8 expected, GrayU8 actual, int epsilon) {
        if (expected.width != actual.width || expected.height != actual.height) {
            throw new AssertionError("Image dimensions differ: expected " +
                    expected.width + "x" + expected.height + ", actual " +
                    actual.width + "x" + actual.height);
        }

        for (int y = 0; y < expected.height; y++) {
            for (int x = 0; x < expected.width; x++) {
                int a = expected.get(x, y) & 0xFF;
                int b = actual.get(x, y) & 0xFF;
                if (Math.abs(a - b) > epsilon) {
                    throw new AssertionError(String.format(
                            "Pixel mismatch at (%d,%d): expected %d but was %d (|diff| = %d > epsilon = %d)",
                            x, y, a, b, Math.abs(a - b), epsilon
                    ));
                }
            }
        }
    }

    public static void assertEqualImage(GrayU8 expected, GrayU8 actual) {
        assertEqual(expected, actual, 1);
    }
}
