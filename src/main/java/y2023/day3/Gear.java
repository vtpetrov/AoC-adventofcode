package y2023.day3;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
class Gear {
    List<Integer> parts = new ArrayList<>();

    long getRatio() {
        int  ratio = parts
                .stream()
                .mapToInt(Integer::intValue)
                .reduce(1, (a, b) -> a * b);
        return ratio;
    }

    boolean isValidGear() {
        return parts.size() == 2;
    }
}
