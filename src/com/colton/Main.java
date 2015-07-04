package com.colton;

import java.io.File;
import java.io.IOException;

public class Main {
    private static final Simulator simulation = new Simulator();
    private static final boolean DEBUG_SHOW_SHIP_IN_TERMINAL = false;

    public static void printUsageAndExit(String [] args) {
        System.err.println("Inappropriate arg list: " + args);
        System.err.println("USAGE: [fieldFile] [commandFile]");
        System.exit(1);
    }

    public static void main(String [] args) {
        //arg1 = field, arg2 = instruction
        if (2 != args.length) {
            System.err.println("You must provide 2 arguments.");
            printUsageAndExit(args);
        }

        int stepCount = 0;

        try {
            // read in field file, where it is also validated
            // see Simulation
            simulation.loadField(new File(args[0]));

            // read in command file, where it is also validated
            // use same Simulation from above
            simulation.loadInstructions(new File(args[1]));

            // game loop
            while(true) {
                // display step count
                System.out.println("Step " + (stepCount + 1));
                System.out.println();

                // display map
                simulation.drawTerminal(DEBUG_SHOW_SHIP_IN_TERMINAL);

                // verify map is playable - exit early if not playable
                if (! simulation.isMapValid()) {
                    break;
                }

                // execute command
                simulation.executeTurn(stepCount);

                // decrement any remaining mines' TTL
                simulation.decremementTimes();

                // display new map
                simulation.drawTerminal(DEBUG_SHOW_SHIP_IN_TERMINAL);

                // increment step counter and check for exit conditions
                stepCount++;
                //terminal cases: no remaining mines, pass or explode a mine, no more commands to execute
                if (!simulation.checkForLiveMines() || simulation.checkForMineExplosion() || simulation.getCommandsRemaining(stepCount) == 0) {
                    break;
                }
            }
        } catch (IOException ioe) {
            System.err.println("IOException caught. Exiting... " + ioe.getMessage());
            System.exit(2);
        } catch (Exception e) {
            // this is where any exception is caught due to a malformed input
            System.err.println("Exception caught. Exiting... " + e.getMessage());
            printUsageAndExit(args);
        }

        // give results of Grader
        simulation.determineResults(stepCount);

        // exit
        System.exit(0);
    }

}
