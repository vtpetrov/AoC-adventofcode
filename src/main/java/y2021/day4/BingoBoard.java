package y2021.day4;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class BingoBoard {

    private List<List<BingoNumber>> numbers = new ArrayList<>();
    private int lastMarkedNumber;

    public void addLine(@NotNull String line) {
        line = line.trim();
        List<BingoNumber> lineOfBingoNumbers = new ArrayList<>();
        final String[] lineSplit = line.split("[^0-9]{1,3}");
        Arrays.stream(lineSplit).forEach(n -> {
            lineOfBingoNumbers.add(new BingoNumber(Integer.parseInt(n)));
        });

        this.numbers.add(lineOfBingoNumbers);
    }

    public void markNumberOnThisBoard(final int numberToMark){
        // TODO: add search and mark logic here
        this.lastMarkedNumber = numberToMark;
    }


    @Data
    private class BingoNumber {
        private int value;
        private boolean marked;

        public BingoNumber(int number) {
            this.value = number;
            this.marked = false;
        }

        public void mark(){
            this.marked = true;
        }

        public void unMark(){
            this.marked = false;
        }
    }
}
