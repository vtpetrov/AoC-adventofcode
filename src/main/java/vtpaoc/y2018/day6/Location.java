package vtpaoc.y2018.day6;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static java.lang.Integer.parseInt;

@Getter
@ToString
@Setter
public class Location {

    private int x;
    private int y;
    private int index;
    private int area;
    boolean isFinite = true;

    Location(int ind, int eks, int why) {
        this.index = ind;
        this.x = eks;
        this.y = why;
        this.area = 1;
    }

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Location(String xy, String delimiter) {
        this.x = parseInt(xy.split(delimiter)[0].trim());
        this.y = parseInt(xy.split(delimiter)[1].trim());
    }

    public String toStringXY() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
