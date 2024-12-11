package vtpaoc.y2017.common;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Program {


    private static final int TO_STRING_DAY = 12;

    private String name;
    private Integer weight;
    private List<String> holdingTowersAsStringNames;
    private Long stackWeight = 0L;
    private Long totalWeight = 0L; // weight + stackWeight
    private int id;
    private List<Program> holdingPrograms;
    private ArrayList<Integer> communicatesWithIDs = new ArrayList<>();

    public Program() {

    }

    /**
     * Input might have the following format:
     * <pre>
     * Program NOT holding another programs:     ktlj (57)
     * Program holding another programs:         fwft (72) -> ktlj, cntj, xhth
     * </pre>
     *
     * @param inputFromFile
     */
    public Program(String inputFromFile) {

        // name OR id of tower is starting at the beginning until the first space
        String firstToken = inputFromFile.substring(0, inputFromFile.indexOf(" "));
        try {
            this.id = NumberFormat.getInstance().parse(firstToken).intValue();
        } catch (ParseException e) {
            this.name = String.valueOf(firstToken);
        }

        // set weight if available
        if (inputFromFile.contains("(")) {
            this.weight = Integer.valueOf(inputFromFile.substring(inputFromFile.indexOf("(") + 1, inputFromFile.indexOf(")")));
        }

        // set programs it communicates to (vtpaoc.y2017.day12: <->) OR holding programs (vtpaoc.y2017.day7: ->)
        final String substring = inputFromFile.substring(inputFromFile.indexOf(">") + 2);
        if (inputFromFile.contains("<->")) {

            for (String elem : substring.split(", ")) {
                this.communicatesWithIDs.add(Integer.valueOf(elem));
            }
        } else if (inputFromFile.contains("->")) {
            // holding programs:
            this.holdingTowersAsStringNames = new ArrayList<>(Arrays.asList(substring.split(", ")));

        }

    }

    public static Program fromStringName(String name) {
        Program toReturn = new Program();
        toReturn.setName(name);
        return toReturn;
    }

    public static int getToStringDay() {
        return TO_STRING_DAY;
    }

    public ArrayList<Integer> getCommunicatesWithIDs() {
        return communicatesWithIDs;
    }

    public void setCommunicatesWithIDs(ArrayList<Integer> communicatesWithIDs) {
        this.communicatesWithIDs = communicatesWithIDs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public List<String> getHoldingProgramsAsStringNames() {
        return holdingTowersAsStringNames;
    }

    public Long getStackWeight() {
        return stackWeight;
    }

    public void setStackWeight(Long stackWeight) {
        this.stackWeight = stackWeight;
    }

    public List<Program> getHoldingPrograms() {
        return holdingPrograms;
    }

    public Long getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Long totalWeight) {
        this.totalWeight = totalWeight;
    }

    public void addHoldingProgram(Program programToAdd) {
        if (this.holdingPrograms == null) {
            this.holdingPrograms = new ArrayList<>();
            holdingPrograms.add(programToAdd);
        } else {
            holdingPrograms.add(programToAdd);
        }
    }

    public String toString() {
        switch (TO_STRING_DAY) {
            case 12:
                return "Program{" +
                        "id=" + id +
                        ", communicatesWithIDs=" + communicatesWithIDs +
                        "}\n";
            default:
                return "Program{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", weight=" + weight +
                        ", holdingTowersAsStringNames=" + holdingTowersAsStringNames +
                        ", holdingPrograms=" + holdingPrograms +
                        ", stackWeight=" + stackWeight +
                        ", totalWeight=" + totalWeight +
                        ", communicatesWithIDs=" + communicatesWithIDs +
                        "}\n";
        }
    }

}
