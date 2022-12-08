package y2022.day7;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static helper.InputLoader.closeInput;
import static helper.InputLoader.getMainIn;
import static helper.InputLoader.loadInput;

public class NoSpaceLeft {

    private static final Logger logger = LoggerFactory.getLogger(NoSpaceLeft.class.getSimpleName());
    //    private static final String INPUT_FILE_NAME = "year_202x/day07_input.txt";
    private static final String INPUT_FILE_NAME = "debug.txt";

    static final List<String> inputLines = new ArrayList<>();
    static final List<String> shortInput = new ArrayList<>();
    private static String solution;
    public static final Integer DIR_BOUNDARY_SIZE = 100_000;
    private static List<MyDirectory> dirList = new ArrayList<>();
    private static int currDirIdxKeeper = -1; //start, before any elems added to dirList
    static AtomicInteger cntr = new AtomicInteger(-1);


    /**
     * - / (dir)
     * - a (dir)
     * - e (dir)
     * - i (file, size=584)
     * - f (file, size=29116)
     * - g (file, size=2557)
     * - h.lst (file, size=62596)
     * - b.txt (file, size=14848514)
     * - c.dat (file, size=8504156)
     * - d (dir)
     * - j (file, size=4060174)
     * - d.log (file, size=8033020)
     * - d.ext (file, size=5626152)
     * - k (file, size=7214296)
     *
     * @param args
     */
    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2022    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 7 ===---     ");
        logger.info("            - No Space Left On Device -     ");

        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");
        while (getMainIn().hasNextLine()) {
            String line = getMainIn().nextLine();
            inputLines.add(line);
        }

        parseInput();

        solvePartOne();

        long p2Start = new Date().getTime();
        logger.info("P1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");

        logger.info("=========================================================================================");
        logger.info("    ---=== Part 2 ===---     ");

        solvePartTwo();

        closeInput();


        long end = new Date().getTime();
        logger.info("P2 Duration: " + (end - p2Start) + "ms (" + (end - p2Start) / 1000 + "s)");
        logger.info("==========");
        logger.info("Total Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");

        logger.info(":::END = " + end);
        logger.info(":::END = " + LocalDateTime.ofEpochSecond(end / 1000, 0, ZoneOffset.ofHours(2)));
    }

    private static void parseInput() {
//        Collections.copy(shortInput, inputLines);
        
        traverseInput(currDirIdxKeeper);
        populateChildren();
        calcAndSetDirSizes();

    }

    public static final Pattern PTRN_CD_TO_ROOT_DIR_RGX = Pattern.compile("\\$ cd \\/");
    public static final Pattern PTRN_CD_TO_DIR_WITH_NAME_RGX = Pattern.compile("\\$ cd ([a-z]+)");
    public static final Pattern PTRN_LIST_DIR_CONTENT_RGX = Pattern.compile("\\$ ls");
    public static final Pattern PTRN_DIR_WITH_NAME_RGX = Pattern.compile("dir \\S+");
    public static final Pattern PTRN_FILE_WITH_SIZE_AND_NAME_RGX = Pattern.compile("(\\d{1,10}) (.*)");
    public static final Pattern PTRN_GO_TO_PARENT_DIR_RGX = Pattern.compile("\\$ cd \\.\\.");

    /**
     * // start from top
     * // upon $ cd x - set current dir to x/move into this dir
     * // upon $ ls - read content of a dir (may have files and other dirs)
     * // upon $ cd .. - go one dir up/out
     * <p>
     * $ cd / - first line of the input, read only once. indicates the content of the 'root' folder
     * <p>
     * $ ls - list current dir content
     * dir a - this is directory in the CURRENT dir, just skip it for now there will be 'cd a' for it below.
     * 14848514 b.txt - file in the current dir, add it to the field list
     * 8504156 c.dat  - file in the current dir, add it to the field list
     * dir d  - this is directory in the CURRENT dir, just skip it for now there will be 'cd d' for it below.
     * <p>
     * $ cd e - meka this the current dir. set the OLD current as a 'parent' to this one
     * $ ls
     * 584 i
     * $ cd .. - go one level up -> set current dir's parent as the current one, etc...
     * $ cd .. - ...etc, as above
     * $ cd d
     * $ ls
     * 4060174 j
     * 8033020 d.log
     * 5626152 d.ext
     * 7214296 k
     */
    private static void traverseInput(int parentIdx) {
        //        // add node to list:
//        MyNode parentToAdd;
//        if (nodes.size() == 0) {
//            parentToAdd = null; // this is the root
//        } else {
//            parentToAdd = nodes.get(parentIndex);
//        }
//
//        nodes.add(new MyNode(parentToAdd, quantityOfChildNodes, quantityOfMetadataEntries));
//        int indexOfLastAdded = nodes.size() - 1;
//        nodes.get(indexOfLastAdded).setId(indexOfLastAdded);
//
//        // read child nodes
//        for (int n = 0; n < quantityOfChildNodes; n++) {
//            parseDataP2(indexOfLastAdded);
//
//        }
        Matcher myRegexMatcher;
        String errMsg;
        int currLineIdx = cntr.incrementAndGet();
        while (currLineIdx < inputLines.size() && cntr.get() < inputLines.size()) {
            String line = inputLines.get(currLineIdx);

            // set root dir, add it to list of dirs:
            if (line.matches(PTRN_CD_TO_ROOT_DIR_RGX.toString())) {
                logger.info("encountered go to root dir command...");
                addDirToList("root", null);
                // parent idx is the idx of this(target):
                currDirIdxKeeper = dirList.size() - 1;
                traverseInput(currDirIdxKeeper);
            }

            // list dir content:
            if (line.matches(PTRN_LIST_DIR_CONTENT_RGX.toString())) { // basically do nothing
                logger.info("encountered command to list current dir content... = > {}", line);
                // leave parent index as is:
                traverseInput(currDirIdxKeeper);
            }

            // dir appears in a list of current folder:
            if (line.matches(PTRN_DIR_WITH_NAME_RGX.toString())) { // basically do nothing
                logger.info("encountered DIR name listed in current folder => {} in {}", line, dirList.get(currDirIdxKeeper).getName());
                // leave parent index as is:
                traverseInput(currDirIdxKeeper);
            }

            // // FILE appears in a list of current folder::
            if (line.matches(PTRN_FILE_WITH_SIZE_AND_NAME_RGX.toString())) {
                logger.info("encountered FILE info => {}", line);
                myRegexMatcher = PTRN_FILE_WITH_SIZE_AND_NAME_RGX.matcher(line);
                if (!myRegexMatcher.find()) {
                    errMsg = String.format("Problem occurred trying to obtain file info from [%s] via [%s]"
                            , line, PTRN_FILE_WITH_SIZE_AND_NAME_RGX);
                    throw new Error(errMsg);
                }

                // create new file obj, set properties:
                MyFile tmpFile = new MyFile(myRegexMatcher.group(2), Integer.parseInt(myRegexMatcher.group(1)));
                dirList.get(parentIdx).getFiles().add(tmpFile);
                // leave parent index as is:
                traverseInput(currDirIdxKeeper);
            }

            // go to DIR
            if (line.matches(PTRN_CD_TO_DIR_WITH_NAME_RGX.toString())) { // $ cd ([a-z]+)";
                logger.info("encountered command to go to directory => {}", line);
                myRegexMatcher = PTRN_CD_TO_DIR_WITH_NAME_RGX.matcher(line);
                if (!myRegexMatcher.find()) {
                    errMsg = String.format("Problem occurred trying to obtain dir name from [%s] via [%s]"
                            , line, PTRN_CD_TO_DIR_WITH_NAME_RGX);
                    throw new Error(errMsg);
                }

                //add dir
                String dirName = myRegexMatcher.group(1);// set the name coming from the command
                // the parent of this(target) dir is the current(source) dir
                addDirToList(dirName, dirList.get(parentIdx));
                // parent idx is the idx of this(target):
                currDirIdxKeeper = dirList.size() - 1;
                traverseInput(currDirIdxKeeper);
            }

            // go back 1 level (to parent dir):
            if (line.matches(PTRN_GO_TO_PARENT_DIR_RGX.toString())) {
                logger.info("encountered command to go 1 level UP: from current dir [{}] to parent dir [{}] ?!?",
                        dirList.get(currDirIdxKeeper).getName(), dirList.get(currDirIdxKeeper - 1).getName());
                // set CURR dir to the parent of this one:
                // decrement currDirIdxKeeper by 1 and call recursion:
                traverseInput(--currDirIdxKeeper);
            }

//            logger.info("END traversing command lines;");
            // call recursion 1 last time to increment counter correctly and end the while loop:
//            traverseInput(currDirIdxKeeper);
        }
    }

    private static void addDirToList(String dirName, MyDirectory parent) {
        dirList.add(new MyDirectory(dirName, parent));
    }

    static void populateChildren() {

        for (MyDirectory thisDir : dirList) {
            for (MyDirectory currentDir : dirList) {
                if (currentDir.getParent() != null) {
                    if (Objects.equals(currentDir.getParent().getName(), thisDir.getName())) {
                        // "this" is parent of "current"
                        thisDir.getChildren().add(currentDir);
                    }
                }
            }
        }

//        for (MemoryManeuver.MyNode currentNode : nodes) {
//            if (currentNode.getParent() != null) {
//                if (currentNode.getParent().getId() == this.getId()) {
//                    // "this" is parent of "current"
//                    this.getChildNodes().add(currentNode);
//                }
//            }
//
//        }
    }

    private static void calcAndSetDirSizes() {

    }

    private static void solvePartOne() {


        logger.info("    Part 1 solution:\n XXXXXX= [{}]", "<solution_goes_here>");

    }

    private static void solvePartTwo() {


        logger.info("    Part 2 solution:\n YYYYYYYYYYYY= [{}]", "<solution_goes_here>");
    }

    @Data
    private static class MyDirectory {
        private String name;
        private MyDirectory parent;
        private List<MyDirectory> children = new ArrayList<>();
        private List<MyFile> files = new ArrayList<>();
        private int size; // the sum of sizes of its files and subdirectories' files

        public MyDirectory(String name, MyDirectory parent) {
            this.name = name;
            this.parent = parent;
        }

        @Override
        public String toString() {
            return "dir{" +
                    "name= " + name +
                    ", parent= " + (parent != null ? parent.getName() : null) +
                    ", children=" + (children.stream().map(MyDirectory::getName).collect(Collectors.toList())) +
                    ", files= " + files +
                    ", size= '" + size + "'" +
                    "};\n";
        }

    }

    @Data
    @AllArgsConstructor
    private static class MyFile {
        private String name;
        private int size; // the size of this file

        @Override
        public String toString() {
            return "file{" +
                    "'" + name + '\'' +
                    ", " + size +
                    '}';
        }
    }
}
