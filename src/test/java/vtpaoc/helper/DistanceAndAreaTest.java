package vtpaoc.helper;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DistanceAndAreaTest {

    @Test
    public void testCalculateRectangleArea_NormalRectangle() {
        // 5x4 rectangle
        assertEquals(BigInteger.valueOf(20), DistanceAndArea.calculateRectangleArea(0, 0, 5, 4, false, false));
        assertEquals(BigInteger.valueOf(20), DistanceAndArea.calculateRectangleArea(0, 0, 5, 4, true, false));
        // With endpoints: (5+1)x(4+1) = 6x5 = 30
        assertEquals(BigInteger.valueOf(30), DistanceAndArea.calculateRectangleArea(0, 0, 5, 4, false, true));
        assertEquals(BigInteger.valueOf(30), DistanceAndArea.calculateRectangleArea(0, 0, 5, 4, true, true));
    }

    @Test
    public void testCalculateRectangleArea_FlatLineX() {
        // Line on x-axis from 2 to 7 (length 5)
        assertEquals(BigInteger.ZERO, DistanceAndArea.calculateRectangleArea(2, 3, 7, 3, false, false));
        assertEquals(BigInteger.valueOf(5), DistanceAndArea.calculateRectangleArea(2, 3, 7, 3, true, false));
        // With endpoints: width = 5+1 = 6, height = 0+1 = 1. Line area: 6 (if flat included)
        assertEquals(BigInteger.valueOf(6), DistanceAndArea.calculateRectangleArea(2, 3, 7, 3, true, true));
    }

    @Test
    public void testCalculateRectangleArea_FlatLineY() {
        // Line on y-axis from 3 to 10 (length 7)
        assertEquals(BigInteger.ZERO, DistanceAndArea.calculateRectangleArea(5, 3, 5, 10, false, false));
        assertEquals(BigInteger.valueOf(7), DistanceAndArea.calculateRectangleArea(5, 3, 5, 10, true, false));
        // With endpoints: width = 0+1 = 1, height = 7+1 = 8. Line area: 8 (if flat included)
        assertEquals(BigInteger.valueOf(8), DistanceAndArea.calculateRectangleArea(5, 3, 5, 10, true, true));
    }

    @Test
    public void testCalculateRectangleArea_Point() {
        // Same point
        assertEquals(BigInteger.ZERO, DistanceAndArea.calculateRectangleArea(5, 5, 5, 5, false, false));
        assertEquals(BigInteger.ZERO, DistanceAndArea.calculateRectangleArea(5, 5, 5, 5, true, false));
        // With endpoints: width = 0+1 = 1, height = 0+1 = 1. Area = 1x1 = 1
        assertEquals(BigInteger.ONE, DistanceAndArea.calculateRectangleArea(5, 5, 5, 5, false, true));
        // For point, width and height are both 1 now. Since they are equal, it's not "flat" in the sense of width OR height being 0 anymore.
        // Wait, width and height are 1. So (width == 0 || height == 0) is false.
        // It returns width * height = 1 * 1 = 1.
        assertEquals(BigInteger.ONE, DistanceAndArea.calculateRectangleArea(5, 5, 5, 5, true, true));
    }

    @Test
    public void testCalculateManhattanDistance() {
        assertEquals(9, DistanceAndArea.calculateManhattanDistance(0, 0, 5, 4));
    }

    @Test
    public void testCalculateEuclideanDistance() {
        assertEquals(5.0, DistanceAndArea.calculateEuclideanDistance(0, 0, 3, 4), 0.0001);
    }
}
