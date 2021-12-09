package y2021.day5;

import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Data
class Line {

    private int x1, y1, x2, y2;

    // Predicates:
    //0,9 -> 5,9 - horizontal (y1 = y2)
    public static Predicate<Line> horizontalLine = line -> (line.y1 == line.y2);
    //7,0 -> 7,4 - vertical (x1 = x2):
    public static Predicate<Line> verticalLine = line -> (line.x1 == line.x2);
    // neither horizontal nor vertical
    public static Predicate<Line> diagonalLine = line -> (horizontalLine.and(verticalLine).negate().test(line));


    /**
     * Parses input in the form of:
     * x1,y1 -> x2,y2
     * <br/> e.g. 9,4 -> 3,4
     * <br/> And produces a Line object out of it:
     * <br/> Line(x1=9, y1=4, x2=3, y2=4)
     *
     * @param lineFromInput the input to parse
     */
    public Line(final String lineFromInput) {
        final List<String> lineEnds = Arrays.asList(lineFromInput.split(" -> "));
        final List<String> coordsEnd1 = Arrays.asList(lineEnds.get(0).split(","));
        final List<String> coordsEnd2 = Arrays.asList(lineEnds.get(1).split(","));

        x1 = Integer.parseInt(coordsEnd1.get(0));
        y1 = Integer.parseInt(coordsEnd1.get(1));
        x2 = Integer.parseInt(coordsEnd2.get(0));
        y2 = Integer.parseInt(coordsEnd2.get(1));

        System.out.println(this);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(x1).append(", ").append(y1).append("]")
                .append("[").append(x2).append(", ").append(y2).append("]");
        sb.append(
                horizontalLine.test(this) ? " horizontal" :
                        (verticalLine.test(this) ? " vertical" :
                        (diagonalLine.test(this) ? " diagonal" : " unknown"))
        );
        return sb.toString();
    }

}
