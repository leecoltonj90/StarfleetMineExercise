package com.colton;

import java.util.List;

/**
 * Created by colton on 7/3/15.
 */
public class Grader {
    // never allow this to be initialized
    private Grader() {}

    /**
     * Determine if there are any mines that haven't been destroyed
     * @param mines the list of mines to process
     * @return true if all the mines have been destroyed, false otherwise
     */
    public static boolean areAllMinesDestroyed(List<Mine> mines) {
        for (Mine mine : mines) {
            if (mine.didMineExplode()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determine if there are any commands that have yet to be run
     * @param commands the list of commands loaded from the script file
     * @param commandIndex the current command that is in focus
     * @return the number of commands that remain
     */
    public static int commandsRemaining(List<String> commands, int commandIndex) {
        return commands.size() - commandIndex;
    }

    /**
     * Grade and score the results of the simulation after processing the scripted instructions
     * @param instructionList the list of instructions read in from the script file
     * @param mineList the list of mines that were loaded in from the field file
     * @param moveCount the number of move commands the ship accepted
     * @param volleyCount the number of firing patterns that were executed
     * @param stepCount the index of the final instruction that was executed
     * @return the computed score of the script file based on the simulation results
     *      computed based on a function of total number of mines, moves made, and volleys fired;
     *      1 if there were excess instructions included
     */
    public static int computeGrade(List<String> instructionList, List<Mine> mineList, int moveCount, int volleyCount, int stepCount) {
        int grade;
        int numCommandsRemaining = commandsRemaining(instructionList, stepCount);
        if (numCommandsRemaining == 0) {
            // we played a perfect game!
            final int numMines = mineList.size();
            final int volleysFired = volleyCount;
            final int numMovesMade = moveCount;

            // initial score is 10 * numMines
            int finalScore = 10 * numMines;

            // take away points for every shot fired (not to exceed 5 shots)
            int workingMaxShotsFired = Math.min(numMines, volleysFired);
            finalScore -= (workingMaxShotsFired * 5);

            // take away 2 pts for every move
            int workingMaxMovedMade = Math.min(3 * numMines, 2 * numMovesMade);
            finalScore -= workingMaxMovedMade;

            grade = finalScore;
        } else {
            grade = 1;
        }
        return grade;
    }
}
