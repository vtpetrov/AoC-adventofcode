package vtpaoc.y2018.day3;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
public class Fabric {

    private static final int FABRIC_SIZE = 1000;
    private String[][] data = new String[FABRIC_SIZE][FABRIC_SIZE];
    private int overlaps = 0;
    private Set<Integer> overlapingClaims = new HashSet<>();
    private Set<Integer> nonOverlapingClaims = new HashSet<>();

    Fabric() {
        for (String[] row : data) {
            Arrays.fill(row, ".");
        }
    }


    void drawClaim(Claim claim) {
//        System.out.println("drawing Claim => " + claim);

        boolean isOverlaping = false;

        for (int i = claim.getTopOffset(); i < (claim.getTopOffset() + claim.getHeight()); i++) {
            for (int j = claim.getLeftOffset(); j < (claim.getLeftOffset() + claim.getWidth()); j++) {
                if (data[i][j].equals(".")) {
                    data[i][j] = String.valueOf(claim.getId());
                } else if (!data[i][j].equals("#")) {
                    nonOverlapingClaims.remove(Integer.valueOf(data[i][j]));//remove from nonOverlapping
                    overlapingClaims.add(Integer.valueOf(data[i][j])); // add it to the overlaping ones

                    data[i][j] = "#";
                    overlaps++;

                    isOverlaping = true;
                } else {
                    isOverlaping = true;
                }
            }
        }


        if (isOverlaping) {
            overlapingClaims.add(claim.getId());
        } else {
            nonOverlapingClaims.add(claim.getId());
        }

    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        for (String[] row : data) {
            for (String val : row) {
                sb.append(val);
                for(int a = val.length(); a < 5; a++){
                    sb.append(' ');
                }
            }
            sb.append("|\n");
        }

        return sb.toString();
    }
}
