package com.colton;

import java.io.File;
import java.io.IOException;

public class Main {
    private static final Simulator simulation = new Simulator();

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

        try {
            // read in field file, where it is also validated
            // see Simulation
            simulation.loadField(new File(args[0]));

            // read in command file, where it is also validated
            // use same Simulation from above
            simulation.loadInstructions(new File(args[1]));

            // game loop
            while(!simulation.isComplete) {
                simulation.displayAndExecuteTurn(false).stream().forEach(System.out::println);
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
        simulation.determineResults();

        // exit
        System.exit(0);
    }

}
