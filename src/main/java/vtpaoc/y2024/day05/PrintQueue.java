package vtpaoc.y2024.day05;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import vtpaoc.base.BaseDay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static vtpaoc.helper.InputLoader.closeInput;
import static vtpaoc.helper.Misc.prettyPrintNumber;

@Slf4j
public class PrintQueue extends BaseDay {

    static String year = "2024";
    static String day = "05";
    static String puzzleTitle = "Print Queue";

    static {
        inputFileName = "year_" + year + "/day" + day + "_input.txt";
        // inputFileName = "debug.txt";
    }

    static List<Rule> rules = new ArrayList<>();
    static List<List<Page>> updates = new ArrayList<>();
    static List<List<Page>> correctUpdates = new ArrayList<>();
    static int sumOfMiddlePageNumbersCorrectUpdates = 0;
    static List<List<Page>> incorrectUpdates = new ArrayList<>();
    static List<List<Page>> fixedUpdates = new ArrayList<>();
    static int sumOfMiddlePageNumbersFixedUpdates = 0;

    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        loadDayInput();

        parseRulesAndUpdatesData();
        determineCorrectAndIncorrectUpdates();

        solvePartOne();
        logStartP2();
        solvePartTwo();
        closeInput();
        logEndP2();
    }

    private static void solvePartOne() {
        for (List<Page> update : correctUpdates) {
            sumOfMiddlePageNumbersCorrectUpdates += update.get(update.size() / 2).number;
        }
        solutionP1 = sumOfMiddlePageNumbersCorrectUpdates;
        log.info("""
                Part 1 solution:
                 Determine which updates are already in the correct order.
                 What do you get if you add up the middle page number from those correctly-ordered updates?
                 = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1, '\''));
    }

    private static void solvePartTwo() {
        fixIncorrectUpdates();

        for (List<Page> update : fixedUpdates) {
            sumOfMiddlePageNumbersFixedUpdates += update.get(update.size() / 2).number;
        }
        solutionP2 = sumOfMiddlePageNumbersFixedUpdates;

        log.info("""
                Part 2 solution:
                 Find the updates which are not in the correct order.
                 What do you get if you add up the middle page numbers after correctly ordering just those updates?
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2, '\''));

    }

    private static void fixIncorrectUpdates() {
        for (List<Page> originalIncorrectUpdate : incorrectUpdates) {
            List<Page> swappedUpdate = new ArrayList<>(originalIncorrectUpdate);
            do {
                swappedUpdate = swapFailingPages(swappedUpdate);
            }
            while (!isCorrect(swappedUpdate));

            fixedUpdates.add(swappedUpdate);
        }
    }

    private static List<Page> swapFailingPages(List<Page> originalUpdate) {
        log.debug("original incorrect update: {}", originalUpdate);
        ArrayList<Page> newUpdate = new ArrayList<>(originalUpdate);
        //swap the 2 pages having non-empty moreInfo field:
        List<Page> failingPages = newUpdate.stream().filter(page -> !page.moreInfo.isBlank()).toList();
        Collections.swap(newUpdate,
                newUpdate.indexOf(failingPages.get(0)),
                newUpdate.indexOf(failingPages.get(1))
        );

        // clear moreInfo field using Stream:
        newUpdate.forEach(page -> page.setMoreInfo(""));

        log.debug("swapped update: {}", newUpdate);
        return newUpdate;
    }

    private static void determineCorrectAndIncorrectUpdates() {
        for (List<Page> update : updates) {
            if (isCorrect(update)) {
                correctUpdates.add(update);
            } else {
                incorrectUpdates.add(update);
                log.info("incorrect update: {}", update);
            }
        }
    }

    private static boolean isCorrect(List<Page> update) {
        // for each Page check if there is a Rule where the current page is parent and next page is child:
        for (int i = 0; i < update.size() - 1; i++) {
            Page currentPage = update.get(i);
            Page nextPage = update.get(i + 1);

            boolean matchingRuleFound = false;
            for (Rule rule : rules) {
                if (rule.parentPage == currentPage.number && rule.childPage == nextPage.number) {
                    matchingRuleFound = true;
                    break;
                }
            }

            // if matching rule was not found for a pair of pages, then the update is not correct
            if (!matchingRuleFound) {
                currentPage.moreInfo = "fp";
                nextPage.moreInfo = "fc";
                return false;
            }
        }
        return true;
    }

    private static void parseRulesAndUpdatesData() {
        log.info("input is {}", inputLines);
        boolean readingRules = true;
        for (String line : inputLines) {
            if (line.isBlank()) {
                readingRules = false;
                continue;
            }

            if (readingRules) { //reading rules
                String[] rulePair = line.split("\\|");
                rules.add(new Rule(parseInt(rulePair[0]), parseInt(rulePair[1])));
            } else { // reading updates
                List<Page> updatePages = new ArrayList<>();
                Stream.of(line.split(",")).forEach(page -> updatePages.add(new Page(parseInt(page), calcPagePriority(parseInt(page)))));
                updates.add(updatePages);
            }
        }
        log.info("rules: {}", rules);
        log.info("updates: {}", updates);
    }

    private static int calcPagePriority(int pageNumber) {
        int priority = 0;
        for (Rule rule : rules) {
            //assign 5 points for each occurrence of the page as 'parent' in a Rule
            if (rule.parentPage == pageNumber) {
                priority += 5;
            }
            //assign 1 point for each occurrence of the page as 'child' in a Rule
            if (rule.childPage == pageNumber) {
                priority += 1;
            }
        }

        return priority;
    }

    @Getter
    @Setter
    private static class Page {
        int number;
        int priority;
        String moreInfo = "";

        public Page(int number, int priority) {
            this.number = number;
            this.priority = priority;
        }

        @Override
        public String toString() {
            return String.format("%d %s", number, moreInfo);
        }

        public static java.util.Comparator<Page> priorityComparator() {
            return java.util.Comparator.comparingInt(Page::getPriority);
        }

        public static java.util.Comparator<Page> numberComparator() {
            return java.util.Comparator.comparingInt(Page::getNumber);
        }
    }

    @AllArgsConstructor
    @Getter
    private static class Rule {
        int parentPage;
        int childPage;

        @Override
        public String toString() {
            return String.format("%d>%d", parentPage, childPage);
        }
    }
}
