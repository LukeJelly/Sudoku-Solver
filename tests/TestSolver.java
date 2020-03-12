import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

/**
 * Simple test class to test my Solve Sudoku.
 * 
 * @author Luke Kelly
 * @version 1.0
 */
public class TestSolver {
    // The size of a sudoku
    private int DIMENSIONS_OF_SUDOKU = 9;
    // Integer representation of an empty space
    private int EMPTY_SPACE = 0;
    // The original sudoku generated every time.
    int[][] sudoku = new int[DIMENSIONS_OF_SUDOKU][DIMENSIONS_OF_SUDOKU];

    /*
     * Generates a random sudoku from the python file I found online.
     */
    @Before
    public void generateRandomSudoku() {
        // Python command to tell terminal
        String command = "python ";
        // Location of the file.
        String pathToFile = "\"C:\\Users\\LukeJ\\OneDrive\\Documents\\Programming\\Java\\Sudoku Solver SA\\src\\Generator.py\"";

        // The process that will run to create the file.
        Process generator;
        try {
            // Run python script to generate a sudoku file.
            generator = Runtime.getRuntime().exec(command + pathToFile);
            /*
             * Wait for file to be created, the python process will close itself 
             * when it is done.
             */
            generator.waitFor();
        } catch (IOException e) {
            /*
             * Used in the testCreateFile test to make sure the generator is 
             * working properly
             */
            fail("Could not run the python script");
        } catch (InterruptedException e) {
            /*
             * Used in the testCreateFile test to make sure the generator is 
             * working properly
             */
            fail("The process was interrupted");
        }

        // File location of the file
        File puzzle = new File("C:\\Users\\LukeJ\\OneDrive\\Documents\\"+
                               "Programming\\Java\\Sudoku Solver SA\\puzzle.txt");

        try {
            // Scanner to read through the file.
            Scanner inFile = new Scanner(puzzle);
            int outer = 0;
            while (inFile.hasNextLine()) {
                String line = inFile.nextLine();
                for (int i = 0; i < DIMENSIONS_OF_SUDOKU; i++) {
                    // Stores the int value of the char in the array.
                    sudoku[outer][i] = (line.charAt(i) - '0');
                }
                outer++;
            }
            inFile.close();
        } catch (FileNotFoundException e) {
            /*
             * Used in the testCreateFile test to make sure the generator is 
             * working properly
             */
            fail("Puzzle file was not created.");
        }
    }

    @Test
    /**
     * Test to make sure the file generator is working properly.
     */
    public void testCreateFile() {
        generateRandomSudoku();
    }

    @Test
    public void testSolver() {
        int numTests = 10;
        for (int testNumber = 0; testNumber < numTests; testNumber++) {
            generateRandomSudoku();
            int[][] solvedSudoku = SudokuSolver.solveSudoku(this.sudoku);
            if (!checkSameSudoku(solvedSudoku))
                fail("The sudoku was not the same as the original " + testNumber);
            if (!isComplete(solvedSudoku)) {
                for (int[] integers : solvedSudoku) {
                    System.out.println(Arrays.toString(integers));
                }
                fail("The sudoku was not correct " + testNumber);
            }
        }
    }

    /**
     * Checks if the given sudoku is complete and that there are no duplicate
     * numbers.
     */
    private boolean isComplete(int[][] compSudoku) {
        // Check if all rows are correct first.
        for (int outer = 0; outer < DIMENSIONS_OF_SUDOKU; outer++) {
            // Add one to DIMENSIONS_OF_SUDOKU because the number 9 is allowed.
            boolean[] hasBeenUsed = new boolean[DIMENSIONS_OF_SUDOKU + 1];
            for (int inner = 0; inner < DIMENSIONS_OF_SUDOKU; inner++) {
                int value = compSudoku[outer][inner];
                boolean check = checkValue(hasBeenUsed, value);
                if (!check)
                    return false;
            }
        }

        // Check if all column are correct second.
        for (int outer = 0; outer < DIMENSIONS_OF_SUDOKU; outer++) {
            // Add one to DIMENSIONS_OF_SUDOKU because the number 9 is allowed.
            boolean[] hasBeenUsed = new boolean[DIMENSIONS_OF_SUDOKU + 1];
            for (int inner = 0; inner < DIMENSIONS_OF_SUDOKU; inner++) {
                int value = compSudoku[inner][outer];
                boolean check = checkValue(hasBeenUsed, value);
                if (!check)
                    return false;
            }
        }
        /* If we get here then we know all column and rows are correct */
        return true;
    }


    private boolean checkValue(boolean[] hasBeenUsed, int value) {
        boolean returnedValue = true;
        //If the value has already been used.
        if (hasBeenUsed[value] == true) {
            returnedValue = false;
        /*If the value is less than max number allowed
          If the value is less than one
          If the value is less is an Empty space.
          return false.*/
        
        } else if (value > DIMENSIONS_OF_SUDOKU || value < 1 || value == EMPTY_SPACE) {
            returnedValue = false;
        } else {
            //Allowed number store it so it can't be used again.
            hasBeenUsed[value] = true;
            //ReturnedValue is already set to true so don't change it.
        }
        return returnedValue;
    }

    /**
     * Checks if the given sudoku is the same as the original. 
     */
    private boolean checkSameSudoku(int[][] compSudoku) {
        //Loop through every index.
        for (int outer = 0; outer < DIMENSIONS_OF_SUDOKU; outer++) {
            for (int inner = 0; inner < DIMENSIONS_OF_SUDOKU; inner++) {
                /*
                 * If there isn't and empty space in the original at this index
                 * check this location against the completed Sudoku.
                 */
                if (sudoku[outer][inner] != EMPTY_SPACE) {
                    if (sudoku[outer][inner] != compSudoku[outer][inner])
                    //If they are not the same return false.
                        return false;
                }
            }
        }
        return true;
    }

}