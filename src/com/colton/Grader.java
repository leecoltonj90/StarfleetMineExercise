package com.colton;

import java.util.List;

/**
 * Created by colton on 7/3/15.
 */
public class Grader {
    // never allow this to be initialized
    private Grader() {}

    // mines still remain
    public static boolean areAllMinesDestroyed(List<Mine> mines) {
        for (Mine mine : mines) {
            if (mine.didMineExplode()) {
                return false;
            }
        }
        return true;
    }

    // commands still remain
    public static int commandsRemaining(List<String> commands, int commandIndex) {
        return commands.size() - commandIndex;
    }

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
