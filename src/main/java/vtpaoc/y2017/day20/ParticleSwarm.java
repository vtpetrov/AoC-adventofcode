package vtpaoc.y2017.day20;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static vtpaoc.helper.InputLoader.*;

public class ParticleSwarm {

        private static final String INPUT_FILE_NAME = "year_2017/day20_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    private static List<Particle> particlesInitial = new ArrayList<>();
    private static List<Particle> remainingParticles;

    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2017    ----");
        long start = new Date().getTime();
        System.out.println("\n:::START = " + start);
        System.out.println("\n                ---=== Day 20 ===---     ");
        System.out.println("                   - Particle Swarm -     ");
        System.out.println("\n    ---=== Part 1 ===---     ");

        String line;
        loadInput(INPUT_FILE_NAME, "");

        while (getMainIn().hasNextLine()) {
            line = getMainIn().nextLine();
            particlesInitial.add(Particle.parse(line));
        }

        long minAcc, minVel, minPos;

        List<Particle> minAccList = new ArrayList<>();
        List<Particle> minVelList = new ArrayList<>();
        List<Particle> minPosList = new ArrayList<>();


        // - acceleration:
        minAcc = Collections.min(particlesInitial.stream().map(Particle::getAcceleration).map(Point::getDistanceToCenter).collect
                (Collectors
                        .toSet
                                ()));
        particlesInitial.stream().forEach(entry -> {
            if (entry.getAcceleration().getDistanceToCenter() == minAcc) minAccList.add(entry);
        });

        // - velocity:
        minVel = Collections.min(minAccList.stream().map(Particle::getVelocity).map(Point::getDistanceToCenter).collect
                (Collectors
                        .toSet
                                ()));
        minAccList.stream().forEach(entry -> {
            if (entry.getVelocity().getDistanceToCenter() == minVel) minVelList.add(entry);
        });

        // - position:
        minPos = Collections.min(minVelList.stream().map(Particle::getPosition).map(Point::getDistanceToCenter).collect
                (Collectors
                        .toSet
                                ()));
        minVelList.stream().forEach(entry -> {
            if (entry.getPosition().getDistanceToCenter() == minPos) minPosList.add(entry);
        });


        System.out.println("minAcc = " + minAcc);
        System.out.println("minVel = " + minVel);
        System.out.println("minPos = " + minPos);

        System.out.println("minPosList = " + minPosList);
        System.out.println("minPosList.size() = " + minPosList.size());

        if (minPosList.size() != 1) {
            System.out.println("WARNING more than 1 point satisfies condition!!!");
        } else {
            System.out.println("\n    Part 1 solution:   closest particle index = [" + particlesInitial.indexOf(minPosList
                    .get(0)
            ) + "]");
        }

        long p2Start = new Date().getTime();
        System.out.println("\nP1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");

        System.out.println("=========================================================================================");

        remainingParticles = new ArrayList<>(particlesInitial);
        int c = 1;
        while (c < 45 && remainingParticles.size() > 1) {
            removeCollisions();
            tick(remainingParticles);
            c++;
//            System.out.println("         remaining particles= [" + remainingParticles.size() + "] " + remainingParticles);
        }

//        System.out.println("remainingParticles = " + remainingParticles);
        System.out.println("\n    Part 2 solution:   remaining particles count= [" + remainingParticles.size() + "] ");

        /*System.out.println("\n IMPl2");
        remainingParticles = new ArrayList<>(particlesInitial);
        c = 1;
        while (c < 2 && remainingParticles.size() > 1) {
            impl2();
            tick(remainingParticles);
            c++;
            System.out.println("       IMPl2  remaining particles= [" + remainingParticles.size() + "] " + remainingParticles);
        }*/

        closeInput();


        long end = new Date().getTime();
        System.out.println("\nP2 Duration: " + (end - p2Start) + "ms (" + (end - p2Start) / 1000 + "s)");
        System.out.println("\nTotal Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");

        System.out.println("\n:::END = " + end);

    }


    /**
     * To simplify the problem further, the GPU would like to remove any particles that collide.
     * Particles collide if their positions ever exactly match.
     * Because particles are updated simultaneously, more than two particles can collide at the same time and place.
     * Once particles collide, they are removed and cannot collide with anything else after that tick.
     */
    private static void removeCollisions() {
        int i = 0;
        while (i < remainingParticles.size()) {
            boolean match = false;
            int checkDigit = 0;
            List<Particle> particlesToRemove = new ArrayList<>();
            for (int j = i + 1; j < remainingParticles.size(); j++) {
                if (remainingParticles.get(i).getPosition().equals(remainingParticles.get(j).getPosition())) {
                    checkDigit++;
//                    System.out.println(String.format(" !!    MATCH    %s   !! i= %s , j=%s", checkDigit, i, j));
                    particlesToRemove.add(remainingParticles.get(j));
                    match = true;
                }
            }

            if (match) {
                checkDigit++;

                particlesToRemove.add(remainingParticles.get(i));

                remainingParticles.removeAll(particlesToRemove);
//                System.out.println("    - removed collisions:       [" + particlesToRemove.size() + "]");
//                System.out.println(String.format("checkDigit= [%s]", checkDigit));
                if (checkDigit != particlesToRemove.size()) {

                    throw new Error("VTP, digits doesn't match !!!");
                }
            } else {// if there was no match, increase the index (start checking the next element.
                i++;
            }

        }
    }

    private static void tick(List<Particle> remainingParticlesParam) {
        for (Particle current : remainingParticlesParam) {
            current.updateCoordinates();
        }

    }

}
