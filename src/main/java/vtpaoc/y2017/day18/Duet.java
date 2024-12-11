package vtpaoc.y2017.day18;

import lombok.Data;

import java.util.*;

import static vtpaoc.helper.InputLoader.*;

public class Duet {

    private static final String INPUT_FILE_NAME = "year_2017/day18_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    private static List<Instruction> instructions = new ArrayList<>();
    private static Set<String> tempRegisters = new HashSet<>();
    private static Map<String, Long> registers = new TreeMap<>();
    private static Long lastPlayedSoundFrequency;
    private static boolean recovered = false;
    private static boolean p0Terminated = false;
    private static boolean p1Terminated = false;

    public static void main(String[] args) {
        System.out.println("----   ADVENT Of code   2017    ----");
        long start = new Date().getTime();
        System.out.println("\n:::START = " + start);
        System.out.println("\n                ---=== Day 18 ===---     ");
        System.out.println("                   - Duet -     ");
        System.out.println("\n              ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");
        partOne();

        System.out.println("\n  Part 1 solution:    \"the value of the recovered frequency\"= [" + lastPlayedSoundFrequency + "]");


        long p2Start = new Date().getTime();
        System.out.println("\n  P1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");

        System.out.println("\n              ---=== Part 2 ===---     ");

        partTwo();
        System.out.println("\n    Part 2 solution:      \"how many times did program 1 send a value\"= [" + p1SentValues + "]");

        closeInput();


        long end = new Date().getTime();
        System.out.println("\n  P2 Duration: " + (end - p2Start) + "ms (" + (end - p2Start) / 1000 + "s)");
        System.out.println("\nTotal Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");
        System.out.println("\n:::END = " + end);
    }

    /**
     * You discover a tablet containing some strange assembly code labeled simply "Duet".
     * Rather than bother the sound card with it, you decide to run the code yourself.
     * Unfortunately, you don't see any documentation, so you're left to figure out what the instructions mean on your own.
     * <p>
     * It seems like the assembly is meant to operate on a set of registers that are each named with
     * a single letter and that can each hold a single integer. You suppose each register should start with a value of 0.
     * <p>
     * There aren't that many instructions, so it shouldn't be hard to figure out what they do. Here's what you determine:
     * <p>
     * snd X plays a sound with a frequency equal to the value of X.
     * set X Y sets register X to the value of Y.
     * add X Y increases register X by the value of Y.
     * mul X Y sets register X to the result of multiplying the value contained in register X by the value of Y.
     * mod X Y sets register X to the remainder of dividing the value contained in register X by the value of Y (that is, it sets X to the result of X modulo Y).
     * rcv X recovers the frequency of the last sound played, but only when the value of X is not zero. (If it is zero, the command does nothing.)
     * jgz X Y jumps with an offset of the value of Y, but only if the value of X is greater than zero. (An offset of 2 skips the next instructionType, an offset of -1 jumps to the previous instructionType, and so on.)
     * <p>
     * Many of the instructions can take either a register (a single letter) or a number.
     * The value of a register is the integer it contains; the value of a number is that number.
     * <p>
     * After each jump instructionType, the program continues with the instructionType to which the jump jumped.
     * After any other instructionType, the program continues with the next instructionType.
     * Continuing (or jumping) off either end of the program terminates it.
     * <p>
     * For example:
     * <p>
     * set a 1
     * add a 2
     * mul a a
     * mod a 5
     * snd a
     * set a 0
     * rcv a
     * jgz a -1
     * set a 1
     * jgz a -2
     * The first four instructions set a to 1, add 2 to it, square it, and then set it to itself modulo 5, resulting in a value of 4.
     * Then, a sound with frequency 4 (the value of a) is played.
     * After that, a is set to 0, causing the subsequent rcv and jgz instructions to both be skipped (rcv because a is 0, and jgz because a is not greater than 0).
     * Finally, a is set to 1, causing the next jgz instructionType to activate, jumping back two instructions to another jump, which jumps again to the rcv, which ultimately triggers the recover operation.
     * At the time the recover operation is executed, the frequency of the last sound played is 4.
     * <p>
     * What is the value of the recovered frequency (the value of the most recently played sound) the first time a rcv instructionType is executed with a non-zero value?
     */
    private static void partOne() {

        while (getMainIn().hasNextLine()) {

            instructions.add(new Instruction(getMainIn().nextLine()));
        }


        for (Instruction curr : instructions) {
            if (curr.getAffectedRegisterOrNumber() instanceof String) {
                tempRegisters.add((String) curr.getAffectedRegisterOrNumber());
            }
        }

        // initialize registers Map:
        for (String curr : tempRegisters) {
            registers.put(curr, 0L);
        }

        execute();


    }

    private static boolean p0IsWaiting = false;
    private static boolean p1IsWaiting = false;

    private static Queue<Long> p0Queue = new LinkedList<>();
    private static Queue<Long> p1Queue = new LinkedList<>();

    private static Map<String, Long> p0Registers = new TreeMap<>();
    private static Map<String, Long> p1Registers = new TreeMap<>();

    private static int p0Index = 0;
    private static int p1Index = 0;

    private static int p1SentValues = 0;


    /**
     * As you congratulate yourself for a job well done, you notice that the documentation has been on the back of the tablet this entire time. While you actually got most of the instructions correct, there are a few key differences. This assembly code isn't about sound at all - it's meant to be run twice at the same time.
     * <p>
     * Each running copy of the program has its own set of registers and follows the code independently - in fact, the programs don't even necessarily run at the same speed.
     * To coordinate, they use the send (snd) and receive (rcv) instructions:
     * <p>
     * snd X sends the value of X to the other program. These values wait in a queue until that program is ready to receive them.
     * Each program has its own message queue, so a program can never receive a message it sent.
     * <p>
     * rcv X receives the next value and stores it in register X. If no values are in the queue, the program waits for a value to be sent to it. Programs do not continue to the next instruction until they have received a value. Values are received in the order they are sent.
     * Each program also has its own program ID (one 0 and the other 1); the register p should begin with this value.
     * <p>
     * For example:
     * <p>
     * snd 1
     * snd 2
     * snd p
     * rcv a
     * rcv b
     * rcv c
     * rcv d
     * <p>
     * Both programs begin by sending three values to the other. Program 0 sends 1, 2, 0; program 1 sends 1, 2, 1. Then, each program receives a value (both 1) and stores it in a, receives another value (both 2) and stores it in b, and then each receives the program ID of the other program (program 0 receives 1; program 1 receives 0) and stores it in c. Each program now sees a different value in its own copy of register c.
     * <p>
     * Finally, both programs try to rcv a fourth time, but no data is waiting for either of them, and they reach a deadlock. When this happens, both programs terminate.
     * <p>
     * It should be noted that it would be equally valid for the programs to run at different speeds; for example, program 0 might have sent all three values and then stopped at the first rcv before program 1 executed even its first instruction.
     * <p>
     * Once both of your programs have terminated (regardless of what caused them to do so), how many times did program 1 send a value?
     */
    private static void partTwo() {

        // initialize registers:
        for (Map.Entry<String, Long> e : registers.entrySet()) {
            p0Registers.put(e.getKey(), 0L);
            p1Registers.put(e.getKey(), 0L);
        }

        // set initial value of register 'p' per condition (program 0 -> 0, program 1 -> 1):
        p1Registers.put("p", 1L);

        while ((!p0Terminated && !p0IsWaiting) || (!p1Terminated && !p1IsWaiting)) {
            if (!p0Terminated) p0Execute();
            if (!p1Terminated) p1Execute();

        }

        // both programs terminated, announce final result in main();
    }

    private static void p1Execute() {
        switch (instructions.get(p1Index).type) {

            case SET:
                //                    set X Y      sets register X to the value of Y.
                p1Registers.put((String) instructions.get(p1Index).getAffectedRegisterOrNumber(), instructions.get(p1Index).getValue(1));
                p1Index++;
                break;
            case ADD:
                //                    add X Y      increases register X by the value of Y.
                p1Registers.put((String) instructions.get(p1Index).getAffectedRegisterOrNumber(), p1Registers.get(instructions.get(p1Index).getAffectedRegisterOrNumber()) + instructions.get(p1Index).getValue(1));
                p1Index++;
                break;
            case MUL:
                //                    mul X Y      sets register X to the result of multiplying the value contained in register X by the value of Y.
                p1Registers.put((String) instructions.get(p1Index).getAffectedRegisterOrNumber(), p1Registers.get(instructions.get(p1Index).getAffectedRegisterOrNumber()) * instructions.get(p1Index).getValue(1));
                p1Index++;
                break;
            case MOD:
                //                    mod X Y      sets register X to the remainder of dividing the value contained in register X by the value of Y (that is, it sets X to the result of X modulo Y).
                p1Registers.put((String) instructions.get(p1Index).getAffectedRegisterOrNumber(), p1Registers.get(instructions.get(p1Index).getAffectedRegisterOrNumber()) % instructions.get(p1Index).getValue(1));
                p1Index++;
                break;
            case SND:
                //      snd X  sends the value of X to the other program. These values wait in a queue until that program is ready to receive them.
                //     Each program has its own message queue, so a program can never receive a message it sent.
                p0Queue.add(instructions.get(p1Index).affectedRegisterOrNumber instanceof Long ? (Long) instructions.get(p1Index).affectedRegisterOrNumber :
                        p1Registers.get(String.valueOf(instructions.get(p1Index).affectedRegisterOrNumber)));
                p1Index++;
                p1SentValues++;
                break;
            case RCV:
                // rcv X receives the next value and stores it in register X. If no values are in the queue, the program waits for a value to be sent to it.
                // Programs do not continue to the next instruction until they have received a value. Values are received in the order they are sent.
                if (!p1Queue.isEmpty()) {
                    p1IsWaiting = false;
                    // get the value and store it in the corresponding register:
                    p1Registers.put((String) instructions.get(p1Index).getAffectedRegisterOrNumber(), p1Queue.remove());
                    p1Index++;
                } else {
                    p1IsWaiting = true;
                    // do not increment index, as we are still waiting
                }
                break;
            case JGZ:
                // jgz X Y      jumps with an offset of the value of Y, but only if the value of X is greater than zero.
                // (An offset of 2 skips the next instructionType, an offset of -1 jumps to the previous instructionType, and so on.)
                if (instructions.get(p1Index).affectedRegisterOrNumber instanceof Long ? (Long) instructions.get(p1Index).affectedRegisterOrNumber > 0
                        : p1Registers.get(String.valueOf(instructions.get(p1Index).affectedRegisterOrNumber)) > 0) {
                    p1Index += instructions.get(p1Index).getValue(1);
                } else {
                    p1Index++;
                }
                break;
        }

        if (p1Index >= instructions.size() || p1Index < 0) {
            p1Terminated = true;
        }

    }

    private static void p0Execute() {

        switch (instructions.get(p0Index).type) {

            case SET:
                //                    set X Y      sets register X to the value of Y.
                p0Registers.put((String) instructions.get(p0Index).getAffectedRegisterOrNumber(), instructions.get(p0Index).getValue(0));
                p0Index++;
                break;
            case ADD:
                //                    add X Y      increases register X by the value of Y.
                p0Registers.put((String) instructions.get(p0Index).getAffectedRegisterOrNumber(), p0Registers.get(instructions.get(p0Index).getAffectedRegisterOrNumber()) + instructions.get(p0Index).getValue(0));
                p0Index++;
                break;
            case MUL:
                //                    mul X Y      sets register X to the result of multiplying the value contained in register X by the value of Y.
                p0Registers.put((String) instructions.get(p0Index).getAffectedRegisterOrNumber(), p0Registers.get(instructions.get(p0Index).getAffectedRegisterOrNumber()) * instructions.get(p0Index).getValue(0));
                p0Index++;
                break;
            case MOD:
                //                    mod X Y      sets register X to the remainder of dividing the value contained in register X by the value of Y (that is, it sets X to the result of X modulo Y).
                p0Registers.put((String) instructions.get(p0Index).getAffectedRegisterOrNumber(), p0Registers.get(instructions.get(p0Index).getAffectedRegisterOrNumber()) % instructions.get(p0Index).getValue(0));
                p0Index++;
                break;
            case SND:
                //      snd X  sends the value of X to the other program. These values wait in a queue until that program is ready to receive them.
                //     Each program has its own message queue, so a program can never receive a message it sent.
                p1Queue.add(instructions.get(p0Index).affectedRegisterOrNumber instanceof Long ? (Long) instructions.get(p0Index).affectedRegisterOrNumber :
                        p0Registers.get(String.valueOf(instructions.get(p0Index).affectedRegisterOrNumber)));
                p0Index++;
                break;
            case RCV:
                // rcv X receives the next value and stores it in register X. If no values are in the queue, the program waits for a value to be sent to it.
                // Programs do not continue to the next instruction until they have received a value. Values are received in the order they are sent.
                if (!p0Queue.isEmpty()) {
                    p0IsWaiting = false;
                    // get the value and store it in the corresponding register:
                    p0Registers.put((String) instructions.get(p0Index).getAffectedRegisterOrNumber(), p0Queue.remove());
                    p0Index++;
                } else {
                    p0IsWaiting = true;
                    // do not increment index, as we are still waiting
                }

                break;
            case JGZ:
                // jgz X Y      jumps with an offset of the value of Y, but only if the value of X is greater than zero.
                // (An offset of 2 skips the next instructionType, an offset of -1 jumps to the previous instructionType, and so on.)
                if (instructions.get(p0Index).affectedRegisterOrNumber instanceof Long ? (Long) instructions.get(p0Index).affectedRegisterOrNumber > 0
                        : p0Registers.get(String.valueOf(instructions.get(p0Index).affectedRegisterOrNumber)) > 0) {
                    p0Index += instructions.get(p0Index).getValue(0);
                } else {
                    p0Index++;
                }
                break;
        }

        if (p0Index >= instructions.size() || p0Index < 0) {
            p0Terminated = true;
        }

    }


    /**
     * set a 1
     * add a 2
     * mul a a
     * <p>
     * set X Y      sets register X to the value of Y.
     * add X Y      increases register X by the value of Y.
     * mul X Y      sets register X to the result of multiplying the value contained in register X by the value of Y.
     * mod X Y      sets register X to the remainder of dividing the value contained in register X by the value of Y (that is, it sets X to the result of X modulo Y).
     * <p>
     * snd X        plays a sound with a frequency equal to the value of X.
     * rcv X        recovers the frequency of the last sound played, but only when the value of X is not zero. (If it is zero, the command does nothing.)
     * jgz X Y      jumps with an offset of the value of Y, but only if the value of X is greater than zero. (An offset of 2 skips the next instructionType, an offset of -1 jumps to the previous instructionType, and so on.)
     * <p>
     * <p>
     * Many of the instructions can take either a register (a single letter) or a number.
     * The value of a register is the integer it contains; the value of a number is that number.
     */
    @Data
    public static class Instruction {

        InstructionType type;
        Object affectedRegisterOrNumber;
        Object value; // offset

        Long getValue() {
            return value instanceof Long ? Long.valueOf(String.valueOf(value)) : registers.get(String.valueOf(value));
        }

        Long getValue(int program) {
            if (program == 0)
                return value instanceof Long ? Long.valueOf(String.valueOf(value)) : p0Registers.get(String.valueOf(value));
            else
                return value instanceof Long ? Long.valueOf(String.valueOf(value)) : p1Registers.get(String.valueOf(value));
        }

        Instruction(String s) {
            String[] tokens = s.split(" ");

            this.type = InstructionType.fromString(tokens[0]);

            try {
                this.affectedRegisterOrNumber = Long.valueOf(tokens[1]);
            } catch (NumberFormatException e) {
                this.affectedRegisterOrNumber = tokens[1];
            }

            if (tokens.length > 2) {
                try {
                    this.value = Long.valueOf(tokens[2]);
                } catch (NumberFormatException e) {
                    this.value = tokens[2];
                }

            }


        }

        @Override
        public String toString() {
            return "{" + "type=" + type +
                    ", " + affectedRegisterOrNumber +
                    ", value=" + value +
                    '}';
        }
    }

    private static void execute() {

        int i = 0;
        while (!recovered && i < instructions.size() && i >= 0) {

            switch (instructions.get(i).type) {

                case SET:
                    //                    set X Y      sets register X to the value of Y.
                    registers.put((String) instructions.get(i).getAffectedRegisterOrNumber(), instructions.get(i).getValue());
                    i++;
                    break;
                case ADD:
                    //                    add X Y      increases register X by the value of Y.
                    registers.put((String) instructions.get(i).getAffectedRegisterOrNumber(), registers.get(instructions.get(i).getAffectedRegisterOrNumber()) + instructions.get(i).getValue());
                    i++;
                    break;
                case MUL:
                    //                    mul X Y      sets register X to the result of multiplying the value contained in register X by the value of Y.
                    registers.put((String) instructions.get(i).getAffectedRegisterOrNumber(), registers.get(instructions.get(i).getAffectedRegisterOrNumber()) * instructions.get(i).getValue());
                    i++;
                    break;
                case MOD:
                    //                    mod X Y      sets register X to the remainder of dividing the value contained in register X by the value of Y (that is, it sets X to the result of X modulo Y).
                    registers.put((String) instructions.get(i).getAffectedRegisterOrNumber(), registers.get(instructions.get(i).getAffectedRegisterOrNumber()) % instructions.get(i).getValue());
                    i++;
                    break;
                case SND:
                    //                    snd X        plays a sound with a frequency equal to the value of X.
                    lastPlayedSoundFrequency = instructions.get(i).affectedRegisterOrNumber instanceof Long ? (Long) instructions.get(i).affectedRegisterOrNumber :
                            registers.get(String.valueOf(instructions.get(i).affectedRegisterOrNumber));
                    i++;
                    break;
                case RCV:
                    //                    rcv X        recovers the frequency of the last sound played, but only when the value of X is not zero. (If it is zero, the command does nothing.)
                    recovered = instructions.get(i).affectedRegisterOrNumber instanceof Long ? (Long) instructions.get(i).affectedRegisterOrNumber != 0 : registers.get(String.valueOf(instructions.get(i).affectedRegisterOrNumber)) != 0;
                    //                        What is the value of the recovered frequency (the value of the most recently played sound) the first time a rcv instruction is executed with a non-zero value?
//                    System.out.println("        recovered = " + recovered);
                    i++;
                    break;
                case JGZ:
                    //                    jgz X Y      jumps with an offset of the value of Y, but only if the value of X is greater than zero.
                    //                      (An offset of 2 skips the next instructionType, an offset of -1 jumps to the previous instructionType, and so on.)
                    if (instructions.get(i).affectedRegisterOrNumber instanceof Long ? (Long) instructions.get(i).affectedRegisterOrNumber > 0 : registers.get(String.valueOf(instructions.get(i).affectedRegisterOrNumber)) > 0) {
                        i += instructions.get(i).getValue();
                    } else {
                        i++;
                    }
                    break;
            }
        }

    }

    enum InstructionType {
        SND,
        SET,
        ADD,
        MUL,
        MOD,
        RCV,
        JGZ;

        static InstructionType fromString(String input) {
            switch (input) {
                case "snd":
                    return SND;
                case "set":
                    return SET;
                case "add":
                    return ADD;
                case "mul":
                    return MUL;
                case "mod":
                    return MOD;
                case "rcv":
                    return RCV;
                case "jgz":
                    return JGZ;
                default:
                    throw new Error("ERROR VTP: invalid InstructionType!");
            }
        }
    }

}