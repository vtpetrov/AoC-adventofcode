package vtpaoc.y2017.common;

import java.util.*;
import java.util.stream.Collectors;


public class Programs {

    public static final Boolean PRINT = true;
    public static Program imbalancedHolderDay7;
    public static List<Program> imbalancedProgramsDay7;
    public static Program wrongProgramDay7;
    public static Program refProgramDay7;
    private List<Program> programs = new ArrayList<>();

    static int count = 0;


    public Programs() {
    }

    public Programs(List<Program> input) {
        this.programs = input;
    }

    public static void calculateStackWeight(Program programInput) {
        count++;
        System.out.println("\n--- count= " + count + "\n");
        if (programInput.getStackWeight() == 0L) {

            Long stackWeight = 0L;

            if (null == programInput.getHoldingPrograms()) { // if program doesn't hold other programs it's stack weight is 0:
                programInput.setStackWeight(0L);
            } else {
                for (Program t : programInput.getHoldingPrograms()) {
                    calculateStackWeight(t);
                    stackWeight += t.getWeight() + t.getStackWeight();
                }
                programInput.setStackWeight(stackWeight);

            }

            System.out.println("[" + programInput.getName() + "] stackWeight = " + stackWeight);
            programInput.setTotalWeight(programInput.getWeight() + programInput.getStackWeight());

        }
    }

    public static int calcBalancingValue() {

        System.out.println("\n - Balancing programs.... ");
        System.out.println("    [" + imbalancedProgramsDay7.stream().map(Program::getName).collect(Collectors.toList()) + "]");
        System.out.println("    [" + imbalancedProgramsDay7.stream().map(Program::getWeight).collect(Collectors.toList()) + "]");
        System.out.println("    [" + imbalancedProgramsDay7.stream().map(Program::getStackWeight).collect(Collectors.toList()) + "]");

        Map<Long, Integer> weightCounts = new HashMap<>();

        for (int i = 0; i < imbalancedProgramsDay7.size(); i++) {
            Program curr = imbalancedProgramsDay7.get(i);
            long currTotal = curr.getWeight() + curr.getStackWeight();
            System.out.println(curr.getName() + ": " + "weight= '" + curr.getWeight() + "', stackWeight= '" + curr
                    .getStackWeight() + "' ; TOTAL: " + currTotal);
            if (weightCounts.containsKey(currTotal)) { // increase occurrences by 1:
                weightCounts.put(currTotal, weightCounts.get(currTotal) + 1);
            } else {
                weightCounts.put(currTotal, 1);
            }
        }

        for (Map.Entry<Long, Integer> test : weightCounts.entrySet()) {
            if (test.getValue().equals(1)) {
                System.out.println(" [!] imbalanced weight : " + test.getKey());
                for (int j = 0; j < imbalancedProgramsDay7.size(); j++) {
                    Program obj = imbalancedProgramsDay7.get(j);
                    if (obj.getTotalWeight().equals(test.getKey())) {
                        wrongProgramDay7 = obj;
                        refProgramDay7 = imbalancedProgramsDay7.get(j + 1 % imbalancedProgramsDay7.size());

                        System.out.println(String.format("Wrong program: name=%s, weight=%s, total=%s", wrongProgramDay7.getName(),
                                wrongProgramDay7.getWeight(), wrongProgramDay7.getTotalWeight()));
                        System.out.println(String.format("REF program: name=%s, weight=%s, total=%s", refProgramDay7.getName(),
                                refProgramDay7.getWeight(), refProgramDay7.getTotalWeight()));

                    }
                }
                break;
            }
        }

        long diff = wrongProgramDay7.getTotalWeight() - refProgramDay7.getTotalWeight();
        System.out.println("diff = " + diff);

        // balance the wrong program:
        System.out.println("OLD program weight = " + wrongProgramDay7.getWeight());
        long correctWeight = wrongProgramDay7.getWeight() - diff;
        System.out.println("NEW program weight = [" + correctWeight + "]");


        return 0;
    }

    public List<Program> getPrograms() {
        return programs;
    }

    public void addProgram(Program t) {
        programs.add(t);
    }

    public Program getProgram(String name) {

        for (Program t : programs) {
            if (t.getName().equals(name)) {
                return programs.get(programs.indexOf(t));
            }
        }
        return null;
    }

    public Program getProgram(Integer id) {
//        System.out.println("DEBUG getProgram(Integer id): " + id);
        for (Program p : programs) {
            if (p.getId() == id) {
                return programs.get(programs.indexOf(p));
            }
        }
        return null;
    }

    public List<String> getNames() {

        return programs.stream().map(Program::getName).collect(Collectors.toList());
    }

    public List<Integer> getWeights() {

        return programs.stream().map(Program::getWeight).collect(Collectors.toList());
    }

    public List<Program> getTowersHolding(boolean holding) {

        List<Program> programList = new ArrayList<>();

        for (Program entry : programs) {
            boolean isHolding = entry.getHoldingProgramsAsStringNames() != null;

            if (isHolding == holding) {
                programList.add(entry);
            }
        }

        return programList;
    }

    public List<Program> getTowersBeingHold(boolean beingHold) {

        List<Program> listToReturn = new ArrayList<>();

        for (Program checkSelf : this.programs) {

            boolean isBenigHold = false;

            for (Program checkHolding : this.programs) {
                if (checkHolding.getHoldingProgramsAsStringNames() != null) {
                    if (checkHolding.getHoldingProgramsAsStringNames().contains(checkSelf.getName())) {

                        isBenigHold = true;

                    }
                }
            }

            if (beingHold == isBenigHold) { // result match what we want - holded OR not holded

                listToReturn.add(checkSelf);
            }


        }

//        System.out.println("FIN:  listToReturn = " + listToReturn.stream().map(Program::getName).collect(Collectors.toList()));

        return listToReturn;
    }

    public void populateHoldedTowers() {
        for (Program current : this.programs) {
            if (current.getHoldingProgramsAsStringNames() != null) {
                for (String holdedTowerName : current.getHoldingProgramsAsStringNames()) {
                    current.addHoldingProgram(this.getProgram(holdedTowerName));
                }
            }
        }
    }

    public void findInbalancedDisks() {

        totalka:
        for (Program current : this.getPrograms()) {
            System.out.println("\n totalka: " + current.getName());

            long[] weights;
            if (current.getHoldingPrograms() != null) {

                weights = new long[current.getHoldingPrograms().size()];
                int i = 0;
                for (Program h : current.getHoldingPrograms()) {
                    weights[i] = h.getWeight() + h.getStackWeight();
                    i++;
                }

                long curr;
                for (int j = 0; j < weights.length - 1; j++) {
                    curr = weights[j];

                    if (weights[j + 1] != curr) {
                        imbalancedHolderDay7 = current;
                        imbalancedProgramsDay7 = current.getHoldingPrograms();
                        break totalka;
                    }
                }

            }

        }

    }

    private int recursionCount;
    private Map<Integer, Set<Integer>> programGroups = new HashMap<>();

    public void findConnectedToZero() {
        findConnectedTo_(0, 0);
    }

    private void findConnectedTo_(int initiator, int caller) {
        if (initiator == caller) {
            recursionCount = 1;
        } else {
            recursionCount++;
        }

        programGroups.putIfAbsent(initiator, new TreeSet<>());

/*
        System.out.println("    recursionCount      = " + recursionCount);
        System.out.println("            initiator        = " + initiator);
        System.out.println("            caller           = " + caller);
*/

        programGroups.get(initiator).add(caller);

        Set<Integer> subIdsConnectedToInitiator = new TreeSet<>();
        subIdsConnectedToInitiator.addAll(programs.get(caller).getCommunicatesWithIDs());

        subIdsConnectedToInitiator.removeAll(programGroups.get(initiator));
        programGroups.get(initiator).addAll(subIdsConnectedToInitiator);

        for (int sub : subIdsConnectedToInitiator) {
            findConnectedTo_(initiator, sub);
        }
    }

//    public Set<Integer> getProgramIdsConnectedTo_() {
//        return programIdsConnectedTo_;
//    }


    public void determineProgramGroups() {
        Set<Integer> inGroupAlready = new TreeSet<>();
        Set<Integer> remaining = programs.stream().map(Program::getId).collect(Collectors.toSet());

        do {
            int i = new ArrayList<>(remaining).get(0);
            findConnectedTo_(i, i);

            inGroupAlready.addAll(programGroups.get(i));
            remaining.removeAll(inGroupAlready);

/*
            System.out.println("programGroups = " + programGroups);
            System.out.println("--------------=====================--------");
            System.out.println("inGroupAlready = " + inGroupAlready);
            System.out.println("remaining = " + remaining);
*/

        } while (remaining.size() > 0);
    }

    public Map<Integer, Set<Integer>> getProgramGroups() {
        return programGroups;
    }

    @Override
    public String toString() {
        return "Programs{" +
                "programs=" + programs +
                '}';
    }

}
