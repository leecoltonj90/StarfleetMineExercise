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
            String fieldFilename = args[0];
            String scriptFilename = args[1];
            simulation.runSimulation(fieldFilename, scriptFilename).stream().forEach(System.out::println);
        } catch (Exception e) {
            // this is where any exception is caught due to a malformed input
            System.err.println("Exception caught. Exiting... " + e.getMessage());
            printUsageAndExit(args);
        }
        // exit
        System.exit(0);
    }

}
