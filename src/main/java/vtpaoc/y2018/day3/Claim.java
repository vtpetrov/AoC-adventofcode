package vtpaoc.y2018.day3;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Claim {

    private int id;
    private int leftOffset;
    private int topOffset;
    private int width;
    private int height;

    Claim(String input){// #1 @ 1,3: 4x4

        this.id = Integer.parseInt(input.substring(input.indexOf("#") + 1, input.indexOf(" ")));
        this.leftOffset  = Integer.parseInt(input.substring(input.indexOf("@") + 2, input.indexOf(",")));
        this.topOffset  = Integer.parseInt(input.substring(input.indexOf(",") + 1, input.indexOf(":")));
        this.width  = Integer.parseInt(input.substring(input.indexOf(":") + 2, input.indexOf("x")));
        this.height  = Integer.parseInt(input.substring(input.indexOf("x") + 1));
    }

}
