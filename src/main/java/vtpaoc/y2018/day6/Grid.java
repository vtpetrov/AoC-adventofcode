package vtpaoc.y2018.day6;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Grid {

        protected static final int GRID_SIZE = 360;
//    protected static final int GRID_SIZE = 10;
        protected static final int DISTANCE_LIMIT = 10000;
//    protected static final int DISTANCE_LIMIT = 32;
    private int regionArea = 0;
    private String[][] data = new String[GRID_SIZE][GRID_SIZE];
    private String[][] copyData = new String[GRID_SIZE][GRID_SIZE];
    private Map<Integer, Location> originalLocations = new HashMap<>();


    Grid() {
        for (String[] row : data) {
            Arrays.fill(row, ".");
        }

        for (String[] row : copyData) {
            Arrays.fill(row, ".");
        }
    }

    public void addLocationToGrid(int index, String[] input) {
        Location loc = new Location(index, Integer.valueOf(input[0].trim()), Integer.valueOf(input[1].trim()));
        originalLocations.put(index, loc);
        data[loc.getY()][loc.getX()] = String.valueOf(index);
        copyData[loc.getY()][loc.getX()] = String.valueOf(index);
    }

    /**
     * Traverse all coordinates in the grid and mark their respective closest location
     */
    public void markClosestLocation() {
        for (int j = 0; j < GRID_SIZE; j++) {
            for (int i = 0; i < GRID_SIZE; i++) {
                String val = data[j][i];
                int minDist = Integer.MAX_VALUE;
                int currDist = -1;

                if (val.equals(".")) { // if not original location:

                    // call distance to each vtpaoc.base location:
                    for (Location location : originalLocations.values()) {
                        currDist = calcDistance(i, j, location.x, location.y);

                        if (currDist < minDist) {
                            minDist = currDist;
                            val = String.valueOf(location.index);
                        } else if (currDist == minDist) { // distance to this location is same as to some other location and it is, mark with "."
                            val = ".";
                        }
                    }

                    data[j][i] = val;

                    // increase given location area:
                    if (!val.equals(".")) {
                        originalLocations.get(Integer.valueOf(val)).area++;

                        // mark infinite if touches border:
                        if (i == 0 || j == 0 || i == Grid.GRID_SIZE - 1 || j == Grid.GRID_SIZE - 1) {
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

        for (int j = 0; j < GRID_SIZE; j++) {
            for (int i = 0; i < GRID_SIZE; i++) {

                int totalDistance = 0;
                int distance = -1;
                boolean wasZero = false;

                // calc distance to each vtpaoc.base location AND sum it:
                for (Location location : originalLocations.values()) {
                    distance = calcDistance(i, j, location.x, location.y);
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

    @Getter
    @ToString
    class Location {

        private int index;
        private int y;
        private int x;
        private int area;
        boolean isFinite = true;


        Location(int ind, int eks, int why) {
            this.index = ind;
            this.y = why;
            this.x = eks;
            this.area = 1;
        }

        void calcFinite() {

        }

    }
}
