package vtpaoc.y2017.day20;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
//@ToString(includeFieldNames = false, exclude = "distanceToCenter")
@EqualsAndHashCode(exclude = "distanceToCenter")
class Point {

    long x;
    long y;
    long z;
    long distanceToCenter;

    Point(String input) {
        String[] inputArr = input.split(",");
        this.x = Integer.parseInt(inputArr[0]);
        this.y = Integer.parseInt(inputArr[1]);
        this.z = Integer.parseInt(inputArr[2]);

        this.distanceToCenter = Math.abs(x) + Math.abs(y) + Math.abs(z);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("");
        sb.append(x);
        sb.append(",").append(y);
        sb.append(",").append(z);
        return sb.toString();
    }
}
