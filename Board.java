/**
 * MINESWEEPER GAME
 * 
 * Author: Andre Kenji Sato
 * Version: Beta 1.0
 * 
 * (N) No rights reserved. 2022.
 */

// For Beta 1.1: Show all nearby '*' spaces and numbered spaces if the inputted space is '*'.
import java.util.Random;
import java.util.Scanner;

/**
 * Board class. 
 * Manages the game board.
 */
public class Board {
    private int size;
    private int quant_mines;
    private char answer_board[][];
    private char board[][];
    private int mines[][];
    private int difficulty;

    /**
     * Sets a blank board and answerboard.
     */
    private void Blank_Boards() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                answer_board[i][j] = '0';
                board[i][j] = '?';
            }
        }
    }

    /**
     * Checks if the line (x) and column (y) coordinates given by the function Set_Mines are 
     * unique, meaning that they aren't of a space which already has a mine.
     * 
     * @param mines_placed  The number of mines already placed on the board.
     * @param x The generated line coordinates by Set_Mines.
     * @param y The generated column coordinates by Set_Mines.
     * @return  Whether or not the given coordinates are unique.
     */
    private boolean Valid_Mine_Coord(int mines_placed, int x, int y) {
        if (mines_placed == 0) return true;

        for (int i = 0; i < mines_placed; i++) {
            if (mines[i][0] == x && mines[i][1] == y) return false;
        }

        return true;
    }

    /**
     * Updates the answer_board, with X indicating a mine and numbers indicating the number of 
     * mines near a space.
     * 
     * @param x The line where the mine is located.
     * @param y The column where the mine is located.
     */
    private void Update_Answer(int x, int y) {
        answer_board[x][y] = 'X';

        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i == -1 || i == this.size) break;
                if (j != -1 && j != this.size && answer_board[i][j] != 'X') answer_board[i][j]++;
            }
        }
    }

    /**
     * Sets mines coordinates, validates whether they are unique and updates the answer board. The 
     * mines[i][0] stores the x position and the mines[i][1] stores the y position.
     */
    private void Set_Mines() {
        Random rng = new Random();
        boolean valid_pos = false;

        for (int i = 0; i < this.quant_mines; i++) {
            do {
                mines[i][0] = rng.nextInt(this.size);
                mines[i][1] = rng.nextInt(this.size);
                valid_pos = Valid_Mine_Coord(i, mines[i][0], mines[i][1]);
            } while (valid_pos == false);

            Update_Answer(mines[i][0], mines[i][1]);
            valid_pos = false;
        }
    }

    /**
     * Sets the mine density on the board, which determines the number of mines in the board. The 
     * mine density per difficulty is as follows:
     * 
     * Difficulty 0: From 0.125 to 0.15.
     * Difficulty 1: From 0.125 to 0.15.
     * Difficulty 2: From 0.15 to 0.2.
     * Difficulty 3: From 0.2 to 0.25.
     * Difficulty 4: From 0.25 to 0.35.
     * 
     * @param difficulty    The inputted difficulty by the user.
     * @return  Mine density. Returns -1, if difficulty is not between 0 and 4.
     */
    private double Set_Mine_Density(int difficulty) {
        Random rng = new Random();

        switch(difficulty) {
            case 1:
            case 2:
                return 0.125 + 0.0025 * rng.nextInt(11);
            case 3:
                return 0.15 + 0.005 * rng.nextInt(11);
            case 4:
                return 0.2 + 0.005 * rng.nextInt(11);
            case 5:
                return 0.25 + 0.01 * rng.nextInt(11);
        }

        return -1;
    }

    /**
     * Sets a custom difficulty.
     * 
     * @param scan Scanner used for user inputs.
     */
    public void Set_Custom_Difficulty(Scanner scan) {
        System.out.println('\n' + "Custom Difficulty selected..." + '\n');
        boolean valid_input = false;

        while (valid_input == false) {
            System.out.println("What's the desired board size?" + '\n' +
                                "The board will be a square with the inputed size." + '\n' +
                                "The size must be between 5 and 99.");
            String size_input = scan.next();

            if (size_input.matches("\\d+") == false) {
                System.out.println("Invalid size. Please insert a new value.");
            } else {
                this.size = Integer.parseInt(size_input);

                if (this.size < 5 || this.size > 99) {
                    System.out.println('\n' + "The board can't be of size " + this.size + 
                                       ". Please insert a new value." + '\n');
                } else {
                    valid_input = true;
                }
            }
        }

        board = new char[this.size][this.size];
        answer_board = new char[this.size][this.size];
        mines = new int[quant_mines][2];
        Blank_Boards();
        Set_Mines();
    }

    /**
     * Sets the board and the number of mines given the desired level of difficulty by the player,
     * ranging from 0 (the easiest) to 4 (the hardest).
     * 
     * @param difficulty    Integer with the desired difficulty.
     * @return  If the inputted difficulty is within 0 to 4. If Set_Mines_Density returns -1 
     * (ERROR CODE 1), immediatily returns false.
     */
    public boolean Set_Difficulty(int difficulty) {
        double mine_density;
        
        switch(difficulty) {
            case 1:
                this.size = 15;
                break;
            case 2:
                this.size = 20;
                break;
            case 3:
                this.size = 25;
                break;
            case 4:
                this.size = 30;
                break;
            case 5:
                this.size = 35;
                break;
        }

        if (difficulty < 1 || difficulty > 5) return false;

        board = new char[this.size][this.size];
        answer_board = new char[this.size][this.size];
        mine_density = Set_Mine_Density(difficulty);

        if (mine_density == -1) {
            System.err.println("ERROR CODE 1: Invalid mine density!");
            System.err.println("If you see this error, contact the developer immediatily.");
            return false;
        }

        this.difficulty = difficulty;
        this.quant_mines = (int) (Math.pow(this.size, 2) * mine_density);
        mines = new int[quant_mines][2];
        Blank_Boards();
        Set_Mines();
        return true;
    }

    /**
     * Checks if the inputted coordinates are within the board's range (between 0 and the size of 
     * the board - 1).
     * 
     * @param x Line coordinates of the space.
     * @param y Column coordinates of the space.
     * @return  If both coordinates are within range.
     */
    public boolean Valid_Coords(int x, int y) {
        if (x < 0 || x >= this.size) return false;
        if (y < 0 || y >= this.size) return false;

        return true;
    }

    /**
     * Clears a space, revealing it to the player. If it doesn't have a mine and any mines nearby
     * (indicated by a '0' in answer_board), display a '*' and also makes the neighbouring spaces
     * visible. If the inputted coordinates are of a space that was already revealed, the function 
     * informs the player to input different coordinates. If it has a flag, it tells the player to
     * remove the flag in order to clear the space.
     * 
     * @param x Line coordinates of the space.
     * @param y Column coordinates of the space.
     */
    public void Clear_Space(int x, int y) {
        if (board[x][y] == '*' || (board[x][y] >= '1' && board[x][y] <= '9')) {
            System.out.println("Row [" + x + "], Column [" + y + "] was already revealed.");
            System.out.println("Please insert another set of coordinates.");
            return ;
        }

        if (board[x][y] == 'F') {
            System.out.println("Row [" + x + "], Column [" + y + "] contains a flag.");
            System.out.println("Please remove the flag before clearing the space.");
            return ;
        }

        if ((answer_board[x][y] >= '1' && answer_board[x][y] <= '9') || 
            answer_board[x][y] == 'X') {
            board[x][y] = answer_board[x][y];
            return ;
        }

        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i == -1 || i == this.size || j == this.size) break;
                if (j == -1) j++;

                if (answer_board[i][j] == '0') {
                    board[i][j] = '*';
                } else {
                    board[i][j] = answer_board[i][j];
                }
            }
        }
    }

    /**
     * Adds a flag to a space. It the space has already been revealed, the function informs the 
     * player that a flag cannot be placed on a cleared space. If it already has a flag, the
     * function informs the player of it.
     * 
     * @param x Line coordinates of the space.
     * @param y Column coordinates of the space.
     */
    public void Add_Flag(int x, int y) {
        if (board[x][y] == '*' || (board[x][y] >= '1' && board[x][y] <= '9')) {
            System.out.println("Row [" + x + "], Column [" + y + "] was already revealed.");
            System.out.println("Cannot insert a flag in this space.");
            return ;
        }

        if (board[x][y] == 'F') {
            System.out.println("Row [" + x + "], Column [" + y + "] already contains a flag.");
            return ;
        }
        
        board[x][y] = 'F';
    }

    /**
     * Removes a flag from a space. If it doesn't have a flag, the function informs the player to 
     * input different coordinates.
     * 
     * @param x Line coordinates of the space.
     * @param y Column coordinates of the space.
     */
    public void Remove_Flag(int x, int y) {
        if (board[x][y] != 'F') {
            System.out.println("Row [" + x + "], Column [" + y + "] doesn't contain a flag.");
            System.out.println("Please insert another set of coordinates.");
            return ;
        }

        board[x][y] = '?';
    }

    /**
     * Checks if the player has inputted coordinates which contained a mine. If so, he/she loses
     * the game.
     * 
     * @param x Line coordinates of the space.
     * @param y Column coordinates of the space.
     * @return  If the square contained a mine.
     */
    public boolean Defeat(int x, int y) {
        return (board[x][y] == 'X');
    }

    /**
     * Checks if the player has cleared all spaces without a mine and added a flag to all spaces
     * with a mine. If so, he/she wins the game.
     * 
     * @return If all spaces without a mine have been cleared.
     */
    public boolean Victory() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (board[i][j] == '?' && answer_board[i][j] != 'X') return false;
                if (board[i][j] != 'F' && answer_board[i][j] == 'X') return false;
            }
        }

        return true;
    }

    /**
     * Prints the gameboard with line (x) and column (y) indexes.
     */
    public void Print_Board() {
        System.out.print("// ");

        for (int y = 0; y < this.size; y++) {
            if (y < 10) System.out.print("0");
            System.out.print(y + " ");
        }

        System.out.println("");

        for (int x = 0; x < this.size; x++) {
            if (x < 10) System.out.print("0");
            System.out.print(x + " ");

            for (int y = 0; y < this.size; y++) {
                System.out.print(" " + board[x][y] + " ");
            }

            System.out.println("");
        }
    }

    /**
     * Prints the answer_board with line (x) and column (y) indexes.
     */
    public void Print_AnswerBoard() {
        System.out.print("// ");

        for (int y = 0; y < this.size; y++) {
            if (y < 10) System.out.print("0");
            System.out.print(y + " ");
        }

        System.out.println("");

        for (int x = 0; x < this.size; x++) {
            if (x < 10) System.out.print("0");
            System.out.print(x + " ");

            for (int y = 0; y < this.size; y++) {
                if (answer_board[x][y] == '0') {
                    System.out.print(" * ");
                } else {
                    System.out.print(" " + answer_board[x][y] + " ");
                }
            }

            System.out.println("");
        }
    }

    /**
     * Returns the board's size.
     */
    public int Display_Size() {
        return this.size;
    }

    /**
     * Returns the game's difficulty.
     */
    public int Display_Difficulty() {
        return this.difficulty;
    }

    /**
     * Returns the number of mines.
     */
    public int Display_Quant_Mines() {
        return this.quant_mines;
    }
}