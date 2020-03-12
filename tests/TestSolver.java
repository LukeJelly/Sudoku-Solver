import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Ignore;
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
        String command = "python ";
        String locationOfFile = "\"C:\\Users\\LukeJ\\OneDrive\\Documents\\Programming\\Java\\Sudoku Solver SA\\src\\Generator.py\"";

        Process generator;
        try {
            generator = Runtime.getRuntime().exec(command + locationOfFile);
            generator.waitFor();
        } catch (IOException e) {
            fail("Could not run the python script");
        } catch (InterruptedException e) {
            fail("The process was interrupted");
        }

        File puzzle = new File("C:\\Users\\LukeJ\\OneDrive\\Documents\\"+
                               "Programming\\Java\\Sudoku Solver SA\\puzzle.txt");

        try {
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
            fail("Puzzle file was not created.");
        }
    }

    @Ignore("Test is used to debug generator.")
    @Test
    public void testFileGeneratorWorking() {
        generateRandomSudoku();
    }

    @Test
    public void testSolver() {
        int numTests = 1;
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
        for (int row = 0; row < DIMENSIONS_OF_SUDOKU; row++) {
            // Add one to DIMENSIONS_OF_SUDOKU because the number 9 is allowed.
            boolean[] hasBeenUsed = new boolean[DIMENSIONS_OF_SUDOKU + 1];
            for (int col = 0; col < DIMENSIONS_OF_SUDOKU; col++) {
                if (!checkValidValue(hasBeenUsed, compSudoku, row, col)){
                    return false;
                }
            }
        }

        // Check if all column are correct second.
        for (int row = 0; row < DIMENSIONS_OF_SUDOKU; row++) {
            // Add one to DIMENSIONS_OF_SUDOKU because the number 9 is allowed.
            boolean[] hasBeenUsed = new boolean[DIMENSIONS_OF_SUDOKU + 1];
            for (int col = 0; col < DIMENSIONS_OF_SUDOKU; col++) {
                //swap row and col because I want to check columns not rows.
                if (!checkValidValue(hasBeenUsed, compSudoku, col, row)){
                    return false;
                }
            }
        }
        return true;
    }


    private boolean checkValidValue(boolean[] hasBeenUsed, int[][] compSudoku, int row, int col) {
        int value = compSudoku[col][row];
        if (hasBeenUsed[value] == true)
            return false;
        
        if (value > DIMENSIONS_OF_SUDOKU || value < 1)
            return false;

        hasBeenUsed[value] = true;
        return true;
        
    }

    /**
     * Checks if the given sudoku is the same as the original. 
     */
    private boolean checkSameSudoku(int[][] compSudoku) {
        for (int outer = 0; outer < DIMENSIONS_OF_SUDOKU; outer++) {
            for (int inner = 0; inner < DIMENSIONS_OF_SUDOKU; inner++) {
                /*
                 * If there isn't and empty space in the original at this index
                 * check this location against the completed Sudoku.
                 */
                if (sudoku[outer][inner] != EMPTY_SPACE) {
                    if (sudoku[outer][inner] != compSudoku[outer][inner])
                        return false;
                }
            }
        }
        return true;
    }

}