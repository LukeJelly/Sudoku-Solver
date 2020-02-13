import java.util.Deque;
import java.util.LinkedList;

/**
 * Solves a Sudoku board.
 * 
 * @author Luke Kelly
 * @version 1.0
 */
public class SudokuSolver {
    public static final int EMPTY_CELL = 0;
    private static final int SIZE_ROW_COLUMN = 9; 
    private static int[][] workingSudoku;
    private static Deque<Integer[]> allEmptyIndexes;

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
        cloneOriginalSudoku(original);
        findAllEmptySpacesInSudoku();
        if (solveSudoku()) {
            return workingSudoku;
        } else {
            return null;
        }
    }

    private static void cloneOriginalSudoku(int[][] original) {
        int sizeSudoku = original.length;
        int[][] newArr = new int[sizeSudoku][sizeSudoku];
        for (int i = 0; i < sizeSudoku; i++) {
            newArr[i] = original[i].clone();
        }
        workingSudoku = newArr;
    }

    private static void findAllEmptySpacesInSudoku() {
        allEmptyIndexes = new LinkedList<>();
        /*
         * Loop last index of the Sudoku backwards so when I start going through
         * them I start at the first one.
         */
        int size = workingSudoku.length - 1;
        for (int outer = size; outer >= 0; outer--) {
            for (int inner = size; inner >= 0; inner--) {
                if (workingSudoku[outer][inner] == 0) {
                    allEmptyIndexes.push(new Integer[] { outer, inner, 1 });
                }
            }

        }
    }

    /**
     * Uses recursive backtracking to find a possible solution for the given
     * Sudoku.
     * 
     * @param workingSudoku   the Sudoku we are trying to solve
     * @param allEmptyIndexes a Stack holding all the empty locations
     * @return true if the sudoku is solved, false if not.
     */
    private static boolean solveSudoku() {
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
            int colIndex = 1;
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
                    if (solveSudoku()) {
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

    private static boolean isNumberAllowed(int[][] workingSudoku, int rowIndex, int colIndex, int number) {
        return !(numberInRow(workingSudoku, rowIndex, number) || 
                 numberInCol(workingSudoku, colIndex, number) || 
                 numberIn3By3Box(workingSudoku, rowIndex, colIndex, number));
    }

    private static boolean numberInRow(int[][] sudoku, int rowIndex, int number) {
        for (int i = 0; i < SIZE_ROW_COLUMN; i++) {
            if (sudoku[rowIndex][i] == number) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean numberInCol(int[][] sudoku, int colIndex, int number) {
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
    private static boolean numberIn3By3Box(int[][] sudoku, int row, int col, int number) {
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
}