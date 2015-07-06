package com.colton;

import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Created by colton on 7/4/15.
 */
public class SimulatorTest {
    boolean debug = false;
    final String FIELD = "field.txt";
    final String SCRIPT = "script.txt";
    final String OUTPUT = "output.txt";

    @Test
    public void testExample1() throws Exception {
        String inputDir = "Ex1/";
        executeAndValidate(inputDir);
    }

    @Test
    public void testExample2() throws Exception {
        String inputDir = "Ex2/";
        executeAndValidate(inputDir);
    }

    @Test
    public void testExample3() throws Exception {
        String inputDir = "Ex3/";
        executeAndValidate(inputDir);
    }

    @Test
    public void testExample4() throws Exception {
        String inputDir = "Ex4/";
        executeAndValidate(inputDir);
    }

    @Test
    public void testExample5() throws Exception {
        String inputDir = "Ex5/";
        executeAndValidate(inputDir);
    }

    @Test
    public void testCaseChange() throws Exception {
        //same as Ex2 with mine depth change
        String inputDir = "CaseChange/";
        executeAndValidate(inputDir);
    }

    @Test
    public void testInefficientField() throws Exception {
        String inputDir = "InefficientField/";
        executeAndValidate(inputDir);
    }

    @Test
    public void testLotsOfMines() throws Exception {
        String inputDir = "LotsOfMines/";
        executeAndValidate(inputDir);
    }

    @Test
    public void testWhitespacing() throws Exception {
        //same output as Ex2
        String inputDir = "Whitespacing/";
        executeAndValidate(inputDir);
    }

    /**
     * Load and run the example simulations, expects the resource dir to contain field, script, and expected output files
     * with titles "field.txt", "script.txt", and "output.txt" respectively.
     *
     * @param exampleDir the name of the directory inside the marked test-resource directory
     * @throws Exception
     */
    private void executeAndValidate(String exampleDir) throws Exception {
        Simulator simulator = new Simulator();
        ClassLoader classLoader = getClass().getClassLoader();
        File field = new File( classLoader.getResource( exampleDir + FIELD ).getFile() );
        File script = new File( classLoader.getResource( exampleDir + SCRIPT ).getFile() );
        List simOutput = simulator.runSimulation(field.getAbsolutePath(), script.getAbsolutePath());

        List<String> expectedOutput = Files.readAllLines( Paths.get( classLoader.getResource( exampleDir + OUTPUT ).getPath() ) );
        for(int i=0; i<simOutput.size(); i++) {
            //TODO switch this to use logging (log4j)
            if(debug) {
                System.out.println("Actual:   '" + simOutput.get(i) + "'");
                System.out.println("Expected: '" + expectedOutput.get(i) + "'");
            }
            assertEquals(simOutput.get(i), expectedOutput.get(i));
        }
    }
}