package vtpaoc.helper;

import java.math.BigInteger;

public class DistanceAndArea {

    private DistanceAndArea() {
        throw new IllegalStateException("Utility class 'DistanceAndArea'");
    }

    /**
     * Calculates the Manhattan distance between two points defined by their coordinates (x1, y1) and (x2, y2).
     * The Manhattan distance is the sum of the absolute differences of their coordinates.
     *
     * @param x1 the x-coordinate of the first point
     * @param y1 the y-coordinate of the first point
     * @param x2 the x-coordinate of the second point
     * @param y2 the y-coordinate of the second point
     * @return the Manhattan distance between the two points
     */
    public static int calculateManhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    /**
     * Calculates the area of a rectangle defined by two opposite corners (x1, y1) and (x2, y2).
     * If the points form a flat figure (line) and includeFlatFigures is true, it returns the distance between them.
     *
     * @param x1                 the x-coordinate of the first point
     * @param y1                 the y-coordinate of the first point
     * @param x2                 the x-coordinate of the second point
     * @param y2                 the y-coordinate of the second point
     * @param includeFlatFigures if true, flat figures return their length; if false, they return 0
     * @param includeEndPoints   if true, adds 1 to the distances (e.g., length 1 becomes 2)
     * @return the area of the rectangle or the length of the line if includeFlatFigures is true
     */
    public static BigInteger calculateRectangleArea(int x1, int y1, int x2, int y2, boolean includeFlatFigures, boolean includeEndPoints) {
        long width = Math.abs((long) x1 - x2);
        long height = Math.abs((long) y1 - y2);

        if (includeEndPoints) {
            width++;
            height++;
        }

        if ((width == 0 || height == 0) && includeFlatFigures) {
            return BigInteger.valueOf(width + height);
        } else {
            return BigInteger.valueOf(width).multiply(BigInteger.valueOf(height));
        }
    }

    /**
     * Calculates the Euclidean distance between two points defined by their coordinates (x1, y1) and (x2, y2).
     * The Euclidean distance is the straight-line distance between two points: sqrt((x1 - x2)^2 + (y1 - y2)^2).
     *
     * @param x1 the x-coordinate of the first point
     * @param y1 the y-coordinate of the first point
     * @param x2 the x-coordinate of the second point
     * @param y2 the y-coordinate of the second point
     * @return the Euclidean distance between the two points
     */
    public static double calculateEuclideanDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
