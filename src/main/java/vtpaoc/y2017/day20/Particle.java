package vtpaoc.y2017.day20;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//@ToString(includeFieldNames = false)
class Particle {

    Point position;
    Point velocity;
    Point acceleration;

    static Particle parse(String inputLine) {


        String pStr = inputLine.substring(3, inputLine.indexOf(">, "));
        inputLine = inputLine.substring(inputLine.indexOf(pStr) + pStr.length() + 3, inputLine.length());

        String vStr = inputLine.substring(3, inputLine.indexOf(">, "));
        inputLine = inputLine.substring(inputLine.indexOf(vStr) + vStr.length() + 3, inputLine.length());

        String aStr = inputLine.substring(3, inputLine.indexOf(">"));

        Point p = new Point(pStr);
        Point v = new Point(vStr);
        Point a = new Point(aStr);

        return new Particle(p, v, a);
    }

    /**
     * Increase the X velocity by the X acceleration.
     * Increase the Y velocity by the Y acceleration.
     * Increase the Z velocity by the Z acceleration.
     * <p>
     * Increase the X position by the X velocity.
     * Increase the Y position by the Y velocity.
     * Increase the Z position by the Z velocity.
     */
    void updateCoordinates() {
        velocity.x += acceleration.x;
        velocity.y += acceleration.y;
        velocity.z += acceleration.z;

        position.x += velocity.x;
        position.y += velocity.y;
        position.z += velocity.z;
    }

    String getPositionSimpleValue() {
        return Long.toString(position.getX()) + Long.toString(position.getY()) + Long.toString(position.getZ());
    }

//    p=<-6,0,0>, v=<3,0,0>, a=<0,0,0>
    @Override
    public String toString() {
        return "{ p=<" + position +">, v=<" + velocity +">, a=<" + acceleration +"> }\n";
    }
}
