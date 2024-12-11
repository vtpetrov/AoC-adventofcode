package vtpaoc.y2018.day7;

import java.util.*;

import static vtpaoc.helper.InputLoader.*;

public class TheSumOfItsParts {

        private static final String INPUT_FILE_NAME = "year_2018/day7_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";
    private static Map<String, Step> allSteps = new TreeMap<>();
    private static Map<String, Step> allStepsP2 = new TreeMap<>();
    private static PriorityQueue<Step> availabeSteps = new PriorityQueue<>();
    private static StringBuilder orderOfSteps = new StringBuilder();
    public static StringBuilder orderOfStepsP2 = new StringBuilder();
    private static final int NUMBER_OF_WORKERS = 5;//5
    private static List<Worker> myWorkers = new ArrayList<>(NUMBER_OF_WORKERS);
    public static final int PENALTY_TIME = 60;//60


    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2018    ----");
        long start = new Date().getTime();
        System.out.println("\n:::START = " + start);
        System.out.println("\n                ---=== Day 7 ===---     ");
        System.out.println("              - The Sum of Its Parts -     ");

        System.out.println("\n    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");

        // Loop through instructions and mark steps as being AVAILABLE + add them to allSteps list:
        parseInput(allSteps);
        closeInput();
//
//        for (Step s : allSteps.values()) {
//            System.out.println("s = " + s);
//        }


        partOne();


        long p2Start = new Date().getTime();
        System.out.println("\nP1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");

        System.out.println("=========================================================================================");
        System.out.println("\n    ---=== Part 2 ===---     \n");


        loadInput(INPUT_FILE_NAME, "");

        // Loop through instructions and mark steps as being AVAILABLE + add them to allSteps list:
        parseInput(allStepsP2);
        closeInput();

        partTwo();


        long end = new Date().getTime();
        System.out.println("\nP2 Duration: " + (end - p2Start) + "ms (" + (end - p2Start) / 1000 + "s)");
        System.out.println("==========");
        System.out.println("\nTotal Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");

        System.out.println("\n:::END = " + end);
    }

    private static void parseInput(Map<String, Step> storeTo) {
        while (getMainIn().hasNextLine()) {
            String line = getMainIn().nextLine();
            Step inputPrereq, inputDep;

            inputPrereq = new Step(line.substring(5, 6));
            inputDep = new Step(line.substring(36, 37));

//            inputPrereq.addDependent(inputDep);
            inputDep.addPrerequisite(inputPrereq);

            // add step 1 to list
            if (storeTo.containsKey(inputPrereq.name)) {
                Step existingPrereq = storeTo.get(inputPrereq.name);
//                existingPrereq.addDependent(inputDep);

                storeTo.replace(inputPrereq.name, existingPrereq);

            } else {
                storeTo.put(inputPrereq.name, inputPrereq);
            }

            // add step 2 to list
            if (storeTo.containsKey(inputDep.name)) {
                Step existingDependent = storeTo.get(inputDep.name);
                existingDependent.addPrerequisite(inputPrereq);

                storeTo.replace(inputDep.name, existingDependent);

            } else {
                storeTo.put(inputDep.name, inputDep);
            }

        }
    }

    private static void partOne() {

        int loop = 1;
        do {
//            System.out.println("loop = " + loop);
            recalculateAvailableSteps(allSteps);

//            System.out.println("    in p1 ...");
//            for (Object s : availabeSteps.toArray()) {
//                System.out.println("        avail = " + (Step) s);
//            }
//            for (Object s : allSteps.values()) {
//                System.out.println("        All = " + (Step) s);
//            }

//            System.out.println(" => orderOfSteps = " + orderOfSteps);

            execute(1, allSteps);
            loop++;


        } while (orderOfSteps.toString().length() < allSteps.size());

//        System.out.println("END availabeSteps = " + availabeSteps);
        System.out.println("\n    Part 1 solution:   Steps are completed in the following order= " + orderOfSteps);

    }

    /**
     * - removes 1st step from availabeSteps queue
     * - adds it to the orderOfSteps string
     * - remove executed from prereqs
     *
     * @param p
     */
    private static void execute(int p, Map<String, Step> source) {

        Step executed = (Step) availabeSteps.poll();

        if (executed != null) {
            orderOfSteps.append(executed.getName());

            // update prereqs - remove executed step from prereqs of other steps
            for (Step s : source.values()) {
                s.getPrereqs().remove(executed);
            }

//        // remove executed step from the list:
//        allSteps.remove(executed.getName());
        }
    }

    /**
     * mark available steps:
     */
    private static void recalculateAvailableSteps(Map<String, Step> source) {
//        System.out.println("source to Recalculate= " + source);
        // add to availabeSteps queue
        for (Step curr : source.values()) {
            if (curr.getPrereqs().size() == 0) {// if no prereqs are present, this step is available for execution
                //add the step to the ordered queue if it is not there AND if not already executed:
                if (!availabeSteps.contains(curr) && !orderOfSteps.toString().contains(curr.getName()))
                    availabeSteps.add(curr);
            }
        }
    }

    /**
     * mark available steps:
     */
    private static void recalculateAvailableStepsP2(Map<String, Step> source) {
//        System.out.println("source to Recalculate= " + source);
        // add to availabeSteps queue
        for (Step curr : source.values()) {
            if (curr.getPrereqs().size() == 0 && !availabeSteps.contains(curr)
                    && !curr.isExecuted()
                    && !curr.isQueued()) {// if no prereqs are present, this step is available for execution
                // add to the queue
                curr.queue();
                availabeSteps.add(curr);
            }
        }
    }


    /**
     * ASCII:
     * A - 65
     * Z - 90
     */
    private static int timeSpent(Step s) {
        return s.getName().getBytes()[0] - 64 + PENALTY_TIME;
    }

    private static void partTwo() {

        orderOfSteps = new StringBuilder();
        int timer = 0;

//        for (Step s : allStepsP2.values()) {
//            System.out.println("p2 = " + s);
//        }

        for (int i = 0; i < NUMBER_OF_WORKERS; i++) {
            myWorkers.add(new Worker());
        }
//        System.out.println("myWorkers = " + myWorkers);

        recalculateAvailableStepsP2(allStepsP2);

//        System.out.println("P2 availabeSteps = " + availabeSteps);

        int myDebugLoop = 5;
        while (!jobsDone()) { // we have jobs/steps to do
//        while (myDebugLoop >= 0) { // we have jobs/steps to do

//            System.out.println("\n => Timer = " + timer);

//            System.out.println("-- BEFORE assignment: ");
//            System.out.println("        avail steps = " + availabeSteps);
//            System.out.println("        myWorkers = " + myWorkers);


            for (Worker currentWorker : myWorkers) {

//                System.out.println("\n  Worker ----> " + currentWorker.getId() + '\n');

                if (currentWorker.isFree()) { // if worker is free, assign new available task to him (if any):

                    if (!availabeSteps.isEmpty()) {
                        Step pickedTask = (Step) availabeSteps.poll();
                        currentWorker.startJob(pickedTask);
                        currentWorker.getTask().queue();

//                        System.out.println("       Worker [" + currentWorker + "] \n    " +
//                                "   picked up job [" + currentWorker.getTask() + "]!");

                    } else {
//                        System.out.println("    no job for this worker... continue...");
                    }
                } else {
//                    System.out.println(".. this worker is busy... !");
                }
//
//                System.out.println("-- AFTER assignment and before working 1 cycle : ");
//                System.out.println("        avail steps = " + availabeSteps);
//                System.out.println("       myWorkers = " + myWorkers);

                // work on the assigned task (if any) for 1 cycle:
                Step taskBeingExecuted = currentWorker.processJob();
//                System.out.println("    taskBeingExecuted = " + taskBeingExecuted);

//                System.out.println("-- AFTER working 1 cycle");
//                System.out.println("       myWorkers = " + myWorkers);


                // if job execution is finished (time spent in queue is enough), mark it as such and remove as prereq from others:
                if (taskBeingExecuted != null && taskBeingExecuted.isExecuted()) {
//                    System.out.println("  ... completing job... => " + taskBeingExecuted);
                    taskBeingExecuted.completeThisJob(allStepsP2);
                }

            }

//            System.out.println(" orderOfStepsP2 = [" + orderOfStepsP2 + "]");

            // advance the timer
            timer++;
//            System.out.println("... timer advance + 1...");

//
            recalculateAvailableStepsP2(allStepsP2);

            myDebugLoop--;
        }

//        System.out.println(" orderOfStepsP2 = [" + orderOfStepsP2 + "]");

        System.out.println("\n    Part 2 solution:   Total Time spent to complete all of the steps= " + timer);
    }


    private static boolean jobsDone() {

        for (Step job : allStepsP2.values()) {
            if (!job.isExecuted()) return false; // if at least 1 job is not done, return FALSE.
        }

        // otherwise return TRUE
        return true;
    }

}
