import java.util.Stack;

/**
 * Solves a Sudoku board.
 * 
 * @author Luke Kelly
 * @version 1.0
 */
public class SudokuSolver {
    public static final int EMPTY_CELL = 0; // Place holder for an Empty cell
    private static final int SIZE_ROW_COLUMN = 9;
    /**
     * Solves the given Sudoku, preconditions: 
     * <ul>
     * <li>1. Empty space is represented by 0s.</li>
     * <li>2. The sudoku is not already invalid(No duplicates in a row, column, or 3x3 box,except zeros).</li>
     * <li>3. The sudoku is a normal 9x9 sudoku.</li>
     * </ul>
     * @return a solved sudoku is the sudoku is solvable, null if not.
     */
    public static int[][] solveSudoku(int[][] original) {
        // Clone the original so I don't change the original array
        int[][] workingSudoku = cloneSudoku(original);
        // Find all the empty indexes in the array, this simplifies the algorithm.
        Stack<Integer[]> allEmptyIndexes = findAllEmptySpaces(workingSudoku);
        // Solve it, if they method returns false the sudoku is unsolvable.
        if (solveSudoku(workingSudoku, allEmptyIndexes)) {
            return workingSudoku;
        } else {
            return null;
        }
    }

    /**
     * Recursively solves the sudoku. 
     * 
     * @param workingSudoku   the Sudoku we are trying to solve
     * @param allEmptyIndexes a Stack holding all the empty locations
     * @return true if the sudoku is solved, false if not.
     */
    private static boolean solveSudoku(int[][] workingSudoku, Stack<Integer[]> allEmptyIndexes) {
        /* Algorithm Explanation:
            1. Grab the first index that is empty from the stack,
            2. Grab all information from that index.
            3. Loop through all the numbers from 1-9 and see if they fit work in the empty space.
                i.  If the number is allowed, put it in that index, and restart the loop from the next index.
                    a. If the recursive call returns true then we know that number works in that location return true;
                    b. If the recursive call returns false then we know that number does not work,
                       place a EMPTY_CELL back in that location and continue back at step 3.
                ii. If none of the numbers work, we put this index back in the stack, and return false
            4. If at top of stack and return false then we know all possible permutations were tried return false
               else we return true because we have a solved puzzle.
         */
        while (!allEmptyIndexes.isEmpty()) {
            int rowIndex = 0;
            int colIndex = 0;
            // 1.
            Integer[] workingIndex = allEmptyIndexes.pop();
            // 2.
            int row = workingIndex[rowIndex];
            int col = workingIndex[colIndex];
            // 3.
            for (int number = 1; number <= SIZE_ROW_COLUMN; number++) {
                
                if (isNumberAllowed(workingSudoku, row, col, number)) {
                    // 3 i.
                    workingSudoku[row][col] = number;
                    if (solveSudoku(workingSudoku, allEmptyIndexes)) {
                        // 3 i a.
                        return true;
                    } else {
                        // 3 i b.
                        workingSudoku[row][col] = EMPTY_CELL;
                    }
                }

            }
            // 3 ii.
            allEmptyIndexes.push(workingIndex);
            return false;
        }
        return true;
    }

    /**
     * Checks if that number is already in the row, column, 3x3 box that our index is
     * in.
     * 
     * @param workingSudoku the Sudoku we are working in
     * @param rowIndex      the index of the row we are working in
     * @param colIndex      the index of the column we are working in
     * @param number        the number we are trying to place there
     * @return true if the number works, false if it doesn't.
     */
    private static boolean isNumberAllowed(int[][] workingSudoku, int rowIndex, int colIndex, int number) {
        return !(containsInRow(workingSudoku, rowIndex, number) || 
                 containsInCol(workingSudoku, colIndex, number) || 
                 containsInBox(workingSudoku, rowIndex, colIndex, number));
    }

    /**
     * Checks if the given number exists in this row.
     * 
     * @param sudoku   the Sudoku we are working in.
     * @param rowIndex the index of the row.
     * @param number   The number we are checking against.
     * @return true if the number is in there, false if not.
     */
    private static boolean containsInRow(int[][] sudoku, int rowIndex, int number) {
        for (int i = 0; i < SIZE_ROW_COLUMN; i++) {
            if (sudoku[rowIndex][i] == number) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given number exists in this column.
     * 
     * @param sudoku   the Sudoku we are working in.
     * @param colIndex the index of the column.
     * @param number   The number we are checking against.
     * @return true if the number is in there, false if not.
     */
    private static boolean containsInCol(int[][] sudoku, int colIndex, int number) {
        for (int i = 0; i < SIZE_ROW_COLUMN; i++) {
            if (sudoku[i][colIndex] == number) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given number exists in the box.
     * 
     * @param sudoku the Sudoku we are working in.
     * @param row    the index of the row.
     * @param col    the index of the column.
     * @param number The number we are checking against.
     * @return true if the number is in there, false if not.
     */
    private static boolean containsInBox(int[][] sudoku, int row, int col, int number) {
        int sizeOfBox = 3;
        int r = row - row % sizeOfBox;
        int c = col - col % sizeOfBox;
        for (int i = r; i < r + sizeOfBox; i++) {
            for (int j = c; j < c + sizeOfBox; j++) {
                if (sudoku[i][j] == number) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Finds all the empty indexes in the Sudoku.
     * 
     * @param sudoku the sudoku we are searching through.
     * @return A stack with all the empty indexes in the Sudoku.
     */
    private static Stack<Integer[]> findAllEmptySpaces(int[][] sudoku) {
        Stack<Integer[]> allIndexes = new Stack<>();
        /*
         * Loop last index of the Sudoku backwards so when I start going through
         * them I start at the first one.
         */
        int size = sudoku.length - 1;
        for (int outer = size; outer >= 0; outer--) {
            for (int inner = size; inner >= 0; inner--) {
                if (sudoku[outer][inner] == 0) {
                    allIndexes.push(new Integer[] { outer, inner, 1 });
                }
            }

        }
        return allIndexes;
    }

    /**
     * Clones the given sudoku, so we can make changes with out affecting the
     * original.
     * 
     * @param original the original Sudoku
     * @return a copy of the original.
     */
    private static int[][] cloneSudoku(int[][] original) {
        int sizeSudoku = original.length;
        int[][] newArr = new int[sizeSudoku][sizeSudoku];
        for (int i = 0; i < sizeSudoku; i++) {
            newArr[i] = original[i].clone();
        }
        return newArr;
    }
}