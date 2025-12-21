package vtpaoc.y2018.day6;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Grid {

    protected static final int DISTANCE_LIMIT = 10000;
//    protected static final int DISTANCE_LIMIT = 32;
private final int width;
    private final int height;
    private final String initialSymbol;
    private int regionArea = 0;
    private final String[][] data;
    private final String[][] copyData;
    private Map<Integer, Location> originalLocations = new HashMap<>();


    public Grid() {
        this(360, 360, ".");
    }

    public Grid(int width, int height, String initialSymbol) {
        this.width = width;
        this.height = height;
        this.initialSymbol = initialSymbol;
        this.data = new String[height][width];
        this.copyData = new String[height][width];

        for (String[] row : data) {
            Arrays.fill(row, initialSymbol);
        }

        for (String[] row : copyData) {
            Arrays.fill(row, initialSymbol);
        }
    }

    /**
     * Adds a new location to the grid and registers it as an original location.
     * Parses the x and y coordinates from the input array, creates a Location object,
     * and marks the position in both data and copyData arrays with the location's index.
     *
     * @param index The unique index to assign to this location.
     * @param input A String array where index 0 is the x-coordinate and index 1 is the y-coordinate.
     */
    public void addLocationToGrid(int index, String[] input) {
        Location loc = new Location(index, Integer.valueOf(input[0].trim()), Integer.valueOf(input[1].trim()));
        originalLocations.put(index, loc);
        data[loc.getY()][loc.getX()] = String.valueOf(index);
        copyData[loc.getY()][loc.getX()] = String.valueOf(index);
    }

    /**
     * Traverses all coordinates in the grid and marks each cell with the index of its closest original location
     * based on Manhattan distance.
     *
     * Logic:
     * - Only cells currently marked with the {@code initialSymbol} (i.e., not original locations) are processed.
     * - For each cell, it calculates the distance to all registered {@code originalLocations}.
     * - The cell is marked with the index of the single closest location.
     * - If there is a tie for the closest distance between two or more locations, the cell is marked with {@code initialSymbol}.
     * - For each successfully marked cell, the area count of the corresponding location is incremented.
     * - If a location's area reaches the grid boundary, that location is marked as infinite ({@code isFinite = false}).
     */
    public void markClosestLocation() {
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                String val = data[j][i];
                int minDist = Integer.MAX_VALUE;
                int currDist = -1;

                if (val.equals(initialSymbol)) { // if not original location:

                    // call distance to each vtpaoc.base location:
                    for (Location location : originalLocations.values()) {
                        currDist = calcDistance(i, j, location.getX(), location.getY());

                        if (currDist < minDist) {
                            minDist = currDist;
                            val = String.valueOf(location.getIndex());
                        } else if (currDist == minDist) { // distance to this location is same as to some other location and it is, mark with initialSymbol
                            val = initialSymbol;
                        }
                    }

                    data[j][i] = val;

                    // increase given location area:
                    if (!val.equals(initialSymbol)) {
                        int area = originalLocations.get(Integer.valueOf(val)).getArea();
                        originalLocations.get(Integer.valueOf(val)).setArea(area + 1);

                        // mark infinite if touches border:
                        if (i == 0 || j == 0 || i == width - 1 || j == height - 1) {
                            originalLocations.get(Integer.valueOf(val)).isFinite = false;
                        }

                    }


                }

            }
        }
    }

    private int calcDistance(int fromX, int fromY, int toX, int toY) {
        return Math.abs(fromX - toX) + Math.abs(fromY - toY);
    }

    public Location getLargestFiniteLocation() {

        int maxIndex = -1;
        int maxArea = Integer.MIN_VALUE;

        for (Location loc : originalLocations.values()) {
            if (loc.isFinite()) {
                int currInd = loc.getIndex();
                int currArea = loc.getArea();

                if (currArea > maxArea) {
                    maxIndex = currInd;
                    maxArea = currArea;
                }

            }

        }

        return originalLocations.get(maxIndex);
    }

    public void calculateRegionArea() {

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {

                int totalDistance = 0;
                int distance = -1;
                boolean wasZero = false;

                // calc distance to each vtpaoc.base location AND sum it:
                for (Location location : originalLocations.values()) {
                    distance = calcDistance(i, j, location.getX(), location.getY());
                    if (distance == 0) wasZero = true;
                    totalDistance += distance;

                    if (totalDistance > DISTANCE_LIMIT) break;
                }

                if (totalDistance < DISTANCE_LIMIT) { // coord is within region

                    regionArea++;

                    if (!wasZero) {// if not an original Location mark with '#'
                        copyData[j][i] = "#";
                    }

                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("\n data: \n");

        for (String[] row : data) {
            for (String val : row) {
                sb.append(val);
                for (int a = val.length(); a < 3; a++) {
                    sb.append(' ');
                }
            }
            sb.append("|\n");
        }

        sb.append(" copyData: \n");
        for (String[] row : copyData) {
            for (String val : row) {
                sb.append(val);
                for (int a = val.length(); a < 3; a++) {
                    sb.append(' ');
                }
            }
            sb.append("|\n");
        }


        sb.append("\n").append(originalLocations);

        return sb.toString();
    }
}
