package vtpaoc.y2024.day14;

import lombok.AllArgsConstructor;
import lombok.Getter;
import vtpaoc.y2018.day6.Location;

@AllArgsConstructor
@Getter
public class ClawMachine {
    Location moveButA;
    Location moveButB;
    Location prizeWinPos;

    @Override
    public String toString() {
        return "CM{" +
                "A=" + moveButA.toStringXY() +
                ",B=" + moveButB.toStringXY() +
                ", WinPos=" + prizeWinPos.toStringXY() +
                '}';
    }

    public int calcLowestPriceToWinPrizeP1() {
        

        return 0;
    }
}
