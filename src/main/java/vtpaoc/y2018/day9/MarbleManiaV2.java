package vtpaoc.y2018.day9;

import java.util.Arrays;
import java.util.Date;

public class MarbleManiaV2 {

    private long[] playersScore;
    private static long numberOfPlayers;

    public static void main(String[] args) {
        System.out.println("----   ADVENT Of code   2018    ----");
        System.out.println("\n                ---=== Day 9 ===---     ");
        System.out.println("                 - Marble Mania -     ");

        long start = new Date().getTime();
        System.out.println("\n:::START = " + start);

        MarbleManiaV2 game = new MarbleManiaV2();

        game.play(9, 25);
        if (Arrays.stream(game.playersScore).max().isPresent()) {
            System.out.println("\n    EXAMPLE solution:   the winning Elf's score= [" + Arrays.stream(game.playersScore).max().getAsLong() + "]");
        }

        game.play(413, 71082);
        if (Arrays.stream(game.playersScore).max().isPresent()) {
            System.out.println("\n    Part 1 solution:   the winning Elf's score= [" + Arrays.stream(game.playersScore).max().getAsLong() + "]");
        }

        game.play(413, 7108200);
        if (Arrays.stream(game.playersScore).max().isPresent()) {
            System.out.println("\n    Part 2 solution:   the winning Elf's score= [" + Arrays.stream(game.playersScore).max().getAsLong() + "]");
        }

        long end = new Date().getTime();
        System.out.println("\n:::END = " + end);
        System.out.println("\nTotal Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");
    }

    private void play(int players, int marbles) {
        numberOfPlayers = players;
        long lastMarbleWorths = marbles;

        playersScore = new long[(int) numberOfPlayers];
        Node currentMarble = new Node(0);

        for (int marble = 1; marble <= lastMarbleWorths; marble++) {
//            System.out.println("marble = " + marble);
            if (marble % 23 != 0) {
                currentMarble = setCurrentMarble(currentMarble, marble);
            } else {
                currentMarble = takeMarbleAndResetCurrent(currentMarble, marble);
            }

        }

    }

    class Node {
        long value;
        Node next;
        Node previous;

        Node(long d) {
            value = d;
            next = this;
            previous = this;
        }

        void print() {
            System.out.print("Printing [" + this.value + "]: ");
            Node start = this;
            do {
                System.out.print(start.value + " -> ");
                start = start.next;
            } while (start.value != this.value);

            System.out.println("\n");
        }

    }

    private Node setCurrentMarble(Node oldCurrent, long marbleValue) {
//        System.out.println("    Setting current marble => " + marbleValue);
        Node newCurrent = new Node(marbleValue);

        Node one, two;
        one = oldCurrent.next;
        two = one.next;

        // set current between 1 and 2
        one.next = newCurrent;
        newCurrent.previous = one;
        newCurrent.next = two;
        two.previous = newCurrent;

//        System.out.println("        New current= " + newCurrent.value);
        return newCurrent;
    }

    private Node takeMarbleAndResetCurrent(Node oldCurrent, long marbleValue) {
//        System.out.println("    Gain score and reset current => " + marbleValue);
        playersScore[Math.toIntExact(marbleValue % numberOfPlayers)] += marbleValue;

        Node newCurrent, prev;
        prev = oldCurrent.previous;

        // move 7 to left and add this marble to player's score too; also remove it from the linked list:
        int move = 6;
        while (move > 0) {
            prev = prev.previous;
            move--;
        }

//        System.out.println("        7th to the left= " + prev.value);
        playersScore[Math.toIntExact(marbleValue % numberOfPlayers)] += prev.value;
        prev.previous.next = prev.next; // remove 7th from list
        newCurrent = prev.next;

//        System.out.println("        New current= " + newCurrent.value);
        return newCurrent;
    }

}

