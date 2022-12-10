package y2022.day7;

import helper.Misc;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
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
//    private static final String INPUT_FILE_NAME = "year_2022/day07_input.txt";
    private static final String INPUT_FILE_NAME = "debug.txt";

    static final List<String> inputLines = new ArrayList<>();
    static final List<String> shortInput = new ArrayList<>();
    private static String solution;
    public static final Integer DIR_BOUNDARY_SIZE_P1 = 100_000;
    private static final List<MyDirectory> dirList = new ArrayList<>();
    private static List<MyDirectory> smallDirs = new ArrayList<>();
//    private static int currDirIdxKeeper = -1; //start, before any elems added to dirList
    static AtomicInteger cntr = new AtomicInteger(-1);

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


    private static void solvePartOne() {

        traverseInput(-1);
        populateChildren();
        calcAndSetDirSizes();

        NumberFormat numFormatter = NumberFormat.getInstance();


        logger.info("List of ALL DIRs :\n{}", Misc.prettyPrintList(dirList));
        logger.info("---------------------------------------------===================================----------------------------------");
        logger.info("---------------------------------------------===================================----------------------------------");
        logger.info("List of DIRs with size < 100'000 :\n{}", Misc.prettyPrintList(smallDirs));
        
        String average = numFormatter.format(smallDirs.stream().mapToInt(MyDirectory::getSize)
                .average().orElse(0));
        solution = numFormatter.format(smallDirs.stream().mapToInt(MyDirectory::getSize).sum());

        logger.info("avg= {}", average);
        logger.info("    Part 1 solution:\n The sum of the TOTAL sizes of directories with size at most {} is => [{}]"
                , DIR_BOUNDARY_SIZE_P1, solution);

    }


    private static void solvePartTwo() {


        logger.info("    Part 2 solution:\n YYYYYYYYYYYY= [{}]", "<solution_goes_here>");
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
    private static void traverseInput(int iMHereParentIdx) {
        Matcher myRegexMatcher;
        String errMsg;
        int currLineIdx = cntr.incrementAndGet();
//        while (currLineIdx < inputLines.size() && cntr.get() < inputLines.size()) {
        while (cntr.get() < inputLines.size()) {
            String line = inputLines.get(currLineIdx);
            int goThereNewParentIdx;
            MyDirectory justAddedDir;

            // set root dir, add it to list of dirs:
            if (line.matches(PTRN_CD_TO_ROOT_DIR_RGX.toString())) {
                logger.info("{} encountered go to root dir command...", cntr.get());
                justAddedDir = addDirToList("root", null);
                // parent idx is the idx of this(target):
//                currDirIdxKeeper = dirList.size() - 1;
                goThereNewParentIdx = dirList.indexOf(justAddedDir);
//                traverseInput(currDirIdxKeeper);
                traverseInput(goThereNewParentIdx);
            } else {
                // list dir content:
                if (line.matches(PTRN_LIST_DIR_CONTENT_RGX.toString())) { // basically do nothing
                    logger.info("{} encountered command to list current dir content... = > {}", cntr.get(), line);
                    // leave parent index as is:
                    traverseInput(iMHereParentIdx);
                } else {           // dir appears in a list of current folder:
                    if (line.matches(PTRN_DIR_WITH_NAME_RGX.toString())) { // basically do nothing
                        logger.info("{} encountered DIR name listed in current folder => {} in {}", cntr.get(), line, dirList.get(iMHereParentIdx).getName());
                        // leave parent index as is:
                        traverseInput(iMHereParentIdx);
                    } else {

                        // // FILE appears in a list of current folder::
                        if (line.matches(PTRN_FILE_WITH_SIZE_AND_NAME_RGX.toString())) {
                            logger.info("{} encountered FILE info => {}", cntr.get(), line);
                            myRegexMatcher = PTRN_FILE_WITH_SIZE_AND_NAME_RGX.matcher(line);
                            if (!myRegexMatcher.find()) {
                                errMsg = String.format("Problem occurred trying to obtain file info from [%s] via [%s]"
                                        , line, PTRN_FILE_WITH_SIZE_AND_NAME_RGX);
                                throw new Error(errMsg);
                            }

                            // create new file obj, set properties:
                            MyFile tmpFile = new MyFile(myRegexMatcher.group(2), Integer.parseInt(myRegexMatcher.group(1)));
                            dirList.get(iMHereParentIdx).getFiles().add(tmpFile);
                            // leave parent index as is:
                            traverseInput(iMHereParentIdx);
                        } else {
                            // go to DIR
                            if (line.matches(PTRN_CD_TO_DIR_WITH_NAME_RGX.toString())) { // $ cd ([a-z]+)";
                                logger.info("{} encountered command to go to directory => {}", cntr.get(), line);
                                myRegexMatcher = PTRN_CD_TO_DIR_WITH_NAME_RGX.matcher(line);
                                if (!myRegexMatcher.find()) {
                                    errMsg = String.format("Problem occurred trying to obtain dir name from [%s] via [%s]"
                                            , line, PTRN_CD_TO_DIR_WITH_NAME_RGX);
                                    throw new Error(errMsg);
                                }

                                //add dir
                                String dirName = myRegexMatcher.group(1);// set the name coming from the command
                                // the parent of this(target) dir is the current(source) dir
//                addDirToList(dirName, dirList.get(iMHereParentIdx));
                                justAddedDir = addDirToList(dirName, dirList.get(iMHereParentIdx));
                                // parent idx is the idx of this(target):
//                currDirIdxKeeper = dirList.size() - 1;
                                goThereNewParentIdx = dirList.indexOf(justAddedDir);
//                traverseInput(currDirIdxKeeper);
                                traverseInput(goThereNewParentIdx);
                            } else {

                                // go back 1 level (to parent dir):
                                if (line.matches(PTRN_GO_TO_PARENT_DIR_RGX.toString())) {
                                    goThereNewParentIdx = iMHereParentIdx - 1;
                                    logger.info("{} encountered command to go 1 level UP: from current dir [{}] to parent dir [{}] ?!?"
                                            , cntr.get(), dirList.get(iMHereParentIdx).getName(), dirList.get(goThereNewParentIdx).getName());
                                    // set CURR dir to the parent of this one:
                                    // decrement currDirIdxKeeper by 1 and call recursion:
                                    traverseInput(goThereNewParentIdx);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * append index to the dir name to make it unique
     *
     * @param dirName the dir name
     * @param parent  the parent
     * @return
     */
    private static MyDirectory addDirToList(String dirName, MyDirectory parent) {
        int append = dirList.size();
        dirList.add(new MyDirectory(dirName + append, parent));
        return dirList.get(dirList.size() - 1);
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
    }

    private static void calcAndSetDirSizes() {
        dirList.forEach(MyDirectory::calculateSize);
    }

    @Data
    private static class MyDirectory {
        private String name;
        private MyDirectory parent;
        private List<MyDirectory> children = new ArrayList<>();
        private List<MyFile> files = new ArrayList<>();
        private int size = -1; // the sum of sizes of its files and subdirectories' files

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

        public int calculateSize() {
            if (this.size == -1) { // size not yet calculated, calculated it and return it
                int sizeValue = 0;
                // first sum the size of FILES in this. dir:
                for (MyFile currFile : this.getFiles()) {
                    sizeValue += currFile.getSize();
                }

                // then travers the contained folders the same way, then come back of the recursion:
                for (MyDirectory childDir : this.getChildren()) {
                    sizeValue += childDir.calculateSize();
                }

                this.size = sizeValue;
                logger.info("Setting size of dir '{}' => to [{}] and return it", this.getName(), this.getSize());
                if(this.size <= DIR_BOUNDARY_SIZE_P1) {
                    smallDirs.add(this);
                }
                return sizeValue;
            } else { // size was already calculated, just return it
                logger.info("Just returning the size of dir '{}'= [{}]", this.getName(), this.getSize());
                return this.size;
            }
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
