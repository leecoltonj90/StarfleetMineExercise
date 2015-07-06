package com.colton;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by colton on 7/3/15.
 */
public class Simulator {
    List<String> m_instructionList;
    List<Mine> m_mineList = new LinkedList<>();
    Point m_ship;
    int moveCount = 0;
    int volleyCount = 0;
    int instructionCount = 0;
    boolean isComplete = false;

    public Simulator() {
    }

    /**
     * Score the results of the simulation after processing the loaded script file.
     */
    public String determineResults() {
        String passOrFail;
        int grade = 0;
        if (!checkForLiveMines()) {
            // all mines are destroyed. we passed.
            passOrFail = "pass";

            // now let's determine our score
            grade = Grader.computeGrade(m_instructionList, m_mineList, moveCount, volleyCount, instructionCount);
        } else {
            passOrFail = "fail";
        }

        return passOrFail + " (" + grade + ")";
    }

    /**
     * Execute a line from the script file, can contain a move and/or a firing instruction.
     */
    public List<String> executeTurn(int commandNumber) throws Exception {
        List<String> commandOutput = new LinkedList<>();
        String command = m_instructionList.get(commandNumber);
        commandOutput.add(command);
        commandOutput.add("");
        if(command != null && !command.trim().isEmpty()) {
            parseCommand(command, false);
        }
        return commandOutput;
    }

    /**
     * Changes the ships coordinate value
     * @param direction the direction in which to move the ship
     */
    private void moveShip(String direction) {
        if (Constants.DIRECTION_LIST.contains(direction)) {
            switch(direction) {
                case Constants.DIRECTION_EAST:
                    m_ship.setLocation(m_ship.getX() + 1, m_ship.getY());
                    break;
                case Constants.DIRECTION_SOUTH:
                    m_ship.setLocation(m_ship.getX(), m_ship.getY() + 1);
                    break;
                case Constants.DIRECTION_WEST:
                    m_ship.setLocation(m_ship.getX() - 1, m_ship.getY());
                    break;
                case Constants.DIRECTION_NORTH:
                    m_ship.setLocation(m_ship.getX(), m_ship.getY() - 1);
                    break;
                default: // possibly a new direction that hasn't been implemented
                    throw new IllegalArgumentException("Improper Direction detected: ' " + direction + "' ");
            }
        } else {
            throw new IllegalArgumentException("Improper Direction detected: ' " + direction + "' ");
        }
        moveCount++;
    }

    /**
     * Fire a missle volley in one of the established patterns
     * @param pattern the string representation of the missle volley pattern
     */
    private void fireVolley(String pattern) {
//        PATTERN.ALPHA: //(-1,-1),(-1,1),(1,-1),(1,1)
//        PATTERN.BETA: //(-1,0),(0,-1),(0,1),(1,0)
//        PATTERN.GAMMA: //(-1,0),(0,0),(1,0)
//        PATTERN.DELTA: //(0,-1),(0,0),(0,1)
        if (Constants.ACTION_LIST.contains(pattern)) {
            final int shipX = (int) m_ship.getX();
            final int shipY = (int) m_ship.getY();
            final List<Point> fireLocations = new LinkedList<>();
            switch(pattern) {
                case Constants.ACTION_ALPHA:
                    fireLocations.add(new Point(shipX - 1, shipY - 1));
                    fireLocations.add(new Point(shipX - 1, shipY + 1));
                    fireLocations.add(new Point(shipX + 1, shipY - 1));
                    fireLocations.add(new Point(shipX + 1, shipY + 1));
                    break;
                case Constants.ACTION_BETA:
                    fireLocations.add(new Point(shipX - 1, shipY));
                    fireLocations.add(new Point(shipX, shipY - 1));
                    fireLocations.add(new Point(shipX, shipY + 1));
                    fireLocations.add(new Point(shipX + 1, shipY));
                    break;
                case Constants.ACTION_GAMMA:
                    fireLocations.add(new Point(shipX - 1, shipY));
                    fireLocations.add(new Point(shipX, shipY));
                    fireLocations.add(new Point(shipX + 1, shipY));
                    break;
                case Constants.ACTION_DELTA:
                    fireLocations.add(new Point(shipX, shipY - 1));
                    fireLocations.add(new Point(shipX, shipY));
                    fireLocations.add(new Point(shipX, shipY + 1));
                    break;
                default: // possibly a new direction that hasn't been implemented
                    throw new IllegalArgumentException("Improper Attack detected: ' " + pattern + "' ");
            }
            fireMines(fireLocations);
        } else {
            throw new IllegalArgumentException("Improper Attack detected: ' " + pattern + "' ");
        }
        volleyCount++;
    }

    /**
     * fires into the given coordinates and destroys mines if alive
     * @param fireLocations non-null list of locations to fire a missle to
     */
    protected void fireMines(List<Point> fireLocations) {
        for (Point fireLocation : fireLocations) {
            for (Mine possibleRecipient : m_mineList) {
                if (possibleRecipient.getLocation().equals(fireLocation) && possibleRecipient.isAlive()) {
                    // we hit a live mine!
                    possibleRecipient.destroy();
                }
            }
        }
    }

    /**
     * Load the field into the simulator
     * @param fieldFile the file object that contains the intended field
     * @throws IOException
     */
    public void loadField(File fieldFile) throws IOException {
        List<String> fromFile = Files.readAllLines(fieldFile.toPath());
        int gridDimension = 0;

        if(fromFile.size()%2 == 0) {
            throw new IOException("Incorrect grid dimensions found.");
        }

        //parse the grid for all the mines
        for(int rowIndex = 0; rowIndex < fromFile.size(); rowIndex++) {
            String row = fromFile.get(rowIndex);
            // do validation on row
            if(row.length() == 0) {
                throw new IOException("detected a graph x dimension of 0");
            } else if(gridDimension == 0) { //set x dimension on first pass through
                gridDimension = row.length();
                if(gridDimension%2 == 0) {
                    throw new IOException("invalid dimension found");
                }
            } else if (gridDimension != row.length()) {
                throw new IOException("Graph was incorrectly formatted, each line was not the same size");
            }

            // build a Mine (if present) from each character in the row
            //alt: use java.util.regex
            for(int i=0; i<row.length(); i++) {
                if(Character.isAlphabetic(row.charAt(i))) {
                    m_mineList.add(new Mine(i, rowIndex, row.charAt(i)));
                } else if(row.charAt(i) != '.') {
                    throw new IOException("Field file not correctly formatted, found invalid char:\'" + row.charAt(i) + "\'");
                }
            }
        }

        m_ship = new Point(gridDimension/2, fromFile.size()/2);
    }

    /**
     * Load the script file into the simulation with all the instructions
     * @param instructionFile the file object that contains all the instructions for the simulation
     * @throws IOException
     */
    public void loadInstructions(File instructionFile) throws IOException {
        m_instructionList = Files.readAllLines(instructionFile.toPath());

        // validate
        for(String line : m_instructionList) {
            parseCommand(line, true);
        }
    }

    /**
     * parses a line that is a command. Optionally executed the command.
     * @param line the command passed in
     * @param dryRun true if the command is to NOT be executed (validation), false to execute the command
     * @throws IOException
     */
    protected void parseCommand(String line, boolean dryRun) throws IOException {
        boolean hasMoved = false;
        boolean hasFired = false;
        for (String instruction : line.trim().split("\\s+")) {
            if (Constants.DIRECTION_LIST.contains(instruction) && !hasMoved) {
                hasMoved = true;
                if (!dryRun) {
                    moveShip(instruction);
                }
            } else if (Constants.ACTION_LIST.contains(instruction) && !hasFired) {
                hasFired = true;
                if (!dryRun) {
                    fireVolley(instruction);
                }
            } else if(instruction.isEmpty()) {
                //do nothing
            } else {
                throw new IOException("Improper Instruction '" + instruction + "'");
            }
        }
    }

    /**
     * Call this method after all commands have been executed to ensure the proper depth of the ship relative to the mines
     */
    public void decremementTimes() {
        m_mineList.stream().filter(Mine::isAlive).forEach(Mine::survivedWave);
    }

    /**
     * Draw the current grid based on each live mines position relative to the ship, the ships location being the center point of the drawn grid
     * @param debugShip debug option to display the ships current location as an '$' if true, false to display as the expected '.'
     */
    public List<String> drawTerminal(boolean debugShip) {
        // determine the size of a quadrant by getting the maximum delta X and Y distances
        int maxDeltaX = 0;
        int maxDeltaY = 0;
        for (Mine mine : m_mineList) {
            if (mine.isAlive()) {
                int deltaX = Math.abs(((int)m_ship.getX()) - ((int)mine.getLocation().getX()));
                if (deltaX > maxDeltaX) {
                    maxDeltaX = deltaX;
                }
                int deltaY = Math.abs(((int)m_ship.getY()) - ((int)mine.getLocation().getY()));
                if (deltaY > maxDeltaY) {
                    maxDeltaY = deltaY;
                }
            }
        }

        List<String> displayGraph = new LinkedList<>();
        // draw the grid
        for (int currentYCoord = ((int)m_ship.getY()) - maxDeltaY; currentYCoord <= ((int)m_ship.getY()) + maxDeltaY; currentYCoord++) {
            String currentRow = "";
            for (int currentXCoord = ((int)m_ship.getX()) - maxDeltaX; currentXCoord <= ((int)m_ship.getX()) + maxDeltaX; currentXCoord++) {
                // determine if we're drawing a mine or a blank space (don't draw ship)
                boolean mineFound = false;
                for (Mine mine : m_mineList) {
                    if ((int)mine.getLocation().getX() == currentXCoord && ((int)mine.getLocation().getY()) == currentYCoord && mine.isAlive()) {
                        // we should display the mine's value
                        currentRow += mine.getDisplayDistance();
                        mineFound = true;
                        break;
                    }
                }
                if (!mineFound) {
                    // check for ship for debug purposes only
                    boolean hasPrintedShip = false;
                    if (debugShip) {
                        if (((int)m_ship.getX()) == currentXCoord && ((int)m_ship.getY()) == currentYCoord) {
                            currentRow += "$";
                            hasPrintedShip = true;
                        }
                    }

                    // print a blank space
                    if (! hasPrintedShip) {
                        currentRow += ".";
                    }
                }
            }
            displayGraph.add(currentRow);
        }
        return displayGraph;
    }

    /**
     * Parse the current list of live mines to see if the ship has sunk passed a mine of the z-axis
     * @return true if there is an unexploded mine above the ship, false if otherwise
     */
    public boolean checkForMineExplosion() {
        for (Mine mine : m_mineList) {
            if (mine.didMineExplode()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Parse the list of mines looking for any mines that have yet to be hit with a missle volley
     * @return true if there is at least 1 unexploded mine, false if no live mines have been found
     */
    public boolean checkForLiveMines() {
        for(Mine mine : m_mineList) {
            if(mine.isAlive()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine the number of command lines have yet to be executed by the simulation
     * @param currentCommandIndex the index of the current instruction being processed from the script file
     * @return the offset of the number of commands executed vs the commands yet to be run
     */
    public int getCommandsRemaining(int currentCommandIndex) {
        return m_instructionList.size() - currentCommandIndex;
    }

    /**
     * Main method for use in the simulation, generates the output to display including the step count, current graph,
     * current loaded instruction from the script file, and the resulting graph after the instructions execution.
     * @param debugShip true to display the ships current location as an '$', false to treat the ships location as an empty space in the graph
     * @return a list of Strings that are intended to be displayed to the user for each turn.
     * @throws Exception
     */
    public List<String> displayAndExecuteTurn(boolean debugShip) throws Exception {
        List<String> display = new LinkedList<>();
        display.add("Step " + (instructionCount + 1));
        display.add("");

        // display map
        drawTerminal(debugShip).stream().forEach(display::add);

        display.add("");

        // execute command
        executeTurn(instructionCount).stream().forEach(display::add);

        // decrement any remaining mines' TTL
        decremementTimes();

        // display new map
        drawTerminal(debugShip).stream().forEach(display::add);

        // increment step counter and check for exit conditions
        instructionCount++;
        //terminal cases: no remaining mines, pass or explode a mine, no more commands to execute
        if (!checkForLiveMines() || checkForMineExplosion() || getCommandsRemaining(instructionCount) == 0) {
            isComplete = true;
        }

        return display;
    }
}
