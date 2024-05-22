/**
 * MINESWEEPER GAME
 * 
 * Author: Andre Kenji Sato
 * Version: Beta 1.0
 * 
 * (N) No rights reserved. 2022.
 */

import java.util.Scanner;

/**
 * Game class.
 * Manages the main game.
 */
public class Game {
    static Scanner scan = new Scanner(System.in);

    /**
     * Gets the player name. It cannot contain the character ',', sicnce its used as a separator
     * in the "scoreboard.csv".
     * 
     * @return The player's name.
     */
    public static String Player_Name() {
        String player;
        
        System.out.print("Welcome to Mineswepper!" + '\n' + 
                         "Please insert your name: ");
        player = scan.nextLine();

        while (player.contains(",") == true) {
            System.out.println();
            System.out.println("Sorry! But your name cannot contain the character ','");
            System.out.print("Please insert a new name: ");
            player = scan.nextLine();
        }

        return player;
    }

    /**
     * Lets the player select between 5 levels of difficulty and a custom one. 
     * 
     * @param minefield The board used in the game.
     */
    public static void Difficulty_Selection(Board minefield) {
        boolean valid_difficulty = false;
        int difficulty;

        while (valid_difficulty == false) {
            System.out.print("Select your difficulty..." + '\n' +
                             "0: Custom." + '\n' +                   
                             "1: Very Easy." + '\n' +
                             "2: Easy." + '\n' +
                             "3: Normal." + '\n' +
                             "4: Hard." + '\n' +
                             "5: Very Hard." + '\n');
            String difficulty_input = scan.next();
            
            if (difficulty_input.matches("\\d")) {
                difficulty = Integer.parseInt(difficulty_input);

                if (difficulty == 0) {
                    minefield.Set_Custom_Difficulty(scan);
                    valid_difficulty = true;
                } else {
                    valid_difficulty = minefield.Set_Difficulty(difficulty);
                }
            }

            if (valid_difficulty == false) {
                System.out.println('\n' + "Invalid difficulty! Please insert a new value." + '\n');
            }
        }

        System.out.println();
    }

    /**
     * Option for the player to choose his/her next action. Option 0 clears a space, option 1
     * places a flag and option 2 removes a flag.
     * 
     * @return The option chosen by the player.
     */
    public static int Option_Selection() {
        boolean valid_option = false;
        int option = -1;

        while (valid_option == false) {
            System.out.println();
            System.out.print("List of actions: " + '\n' +
                             "0: Clear a space." + '\n' +                   
                             "1: Insert flag." + '\n' +
                             "2: Remove flag." + '\n');
            String option_input = scan.next();

            if (option_input.matches("\\d")) {
                option = Integer.parseInt(option_input);
                if (option >= 0 && option <= 2) valid_option = true;
            }

            if (valid_option == false) {
                System.out.println('\n' + "Invalid option! Please select a new option." + '\n');
            }
        }

        return option;
    }

    /**
     * Lets the user choose the desired space coordinates for his/her next action. Then, the 
     * function validates the coordinates.
     * 
     * @param minefield The board used in the game.
     * @return Integer array with the selected coordinates.
     */
    public static int[] Space_Selection(Board minefield) {
        boolean valid_coord = false;
        String x_input, y_input;
        int[] coord = new int[2];
        
        while (valid_coord == false) {
            System.out.print("Insert a Row. Row must be between 0 and " + 
                             (minefield.Display_Size() - 1) + ": ");
            x_input = scan.next();

            System.out.print("Insert a Column. Column must be between 0 and " + 
                             (minefield.Display_Size() - 1) + ": ");
            y_input = scan.next();

            if (x_input.matches("\\d+") && y_input.matches("\\d+")) {
                coord[0] = Integer.parseInt(x_input);
                coord[1] = Integer.parseInt(y_input);
                valid_coord = minefield.Valid_Coords(coord[0], coord[1]);
            }

            if (valid_coord == false) {
                System.out.println("Invalid value(s). Please insert new coordinates.");
            }
        }

        return coord;
    }

    /**
     * The player sweeps the board, until he/she steps on a mine (defeat == true), or he/she clears
     * all spaces without mines and flags all spaces with mines(victory == true). 
     * 
     * @param minefield The board used in the game.
     * @return If the player won the game.
     */
    public static boolean MineSweep(Board minefield) {
        boolean victory = false;
        boolean defeat = false;

        minefield.Print_Board();

        while (victory == false && defeat == false) {   
            //OBS: coord[0] stores the line (x) and coord[1] stores the column (y).
            int coord[] = new int[2];
            int option;

            option = Option_Selection();
            coord = Space_Selection(minefield);

            switch (option) {
                case 0:
                    minefield.Clear_Space(coord[0], coord[1]);
                    break;
                case 1:
                    minefield.Add_Flag(coord[0], coord[1]);
                    break;
                case 2:
                    minefield.Remove_Flag(coord[0], coord[1]);
                    break;
                default:
                    System.err.println("ERROR CODE 5: Option " + option + " is invalid!");
            }

            System.out.println();
            minefield.Print_Board();
            System.out.println();
            defeat = minefield.Defeat(coord[0], coord[1]);

            if (defeat == false) victory = minefield.Victory();
        }

        return victory;
    }

    /**
     * Allows the player to include his/her score into a scoreboard, and displays the top 10 
     * scores with the players that achieved them.
     * 
     * @param score The current scoreboard.
     * @param name The player's name.
     */
    public static void New_Score(Scoreboard score, String name) {
        boolean valid_input = false;

        while (valid_input == false) {
            System.out.print("Do you wish to add your name and score on the Scoreboard?" + '\n' +
                             "Yes: 1" + '\n' +
                             "No: 2" + '\n');
            String input = scan.next();

            if (input.matches("\\d")) {
                if (Integer.parseInt(input) == 1) {
                    valid_input = true;
                    score.Write_Score(name);
                    score.Display_Top10();
                }

                if (Integer.parseInt(input) == 2) {
                    valid_input = true;
                    score.Display_Top10();
                }
            }

            if (valid_input == false) {
                System.out.println("Invalid option! Please select a new option.");
            }

            System.out.println("");
        }
    }

    /**
     * Allows the player to play another round of Minesweeper or end the program. 
     * 
     * @param name The player's name.
     * @return If the player wishes to play another round.
     */
    public static boolean Another_Game(String name) {
        boolean valid_input = false;
        String input;

        while (valid_input == false) {
            System.out.print(name + ", do you wish to play another round?" + '\n' + 
                             "Yes: 1" + '\n' +
                             "No: 2" + '\n');
            input = scan.next();

            if (input.matches("\\d")) {
                if (Integer.parseInt(input) == 1) return true;
                if (Integer.parseInt(input) == 2) return false;
            }

            if (valid_input == false) {
                System.out.println("Invalid option! Please select a new option.");
            }

            System.out.println("");
        }

        return false;
    }

    /**
     * MAIN FUNCTION!
     */
    public static void main(String[] args) {
        Board minefield = new Board();
        Scoreboard score = new Scoreboard();
        TimeKeeping time = new TimeKeeping();
        
        boolean victory = false;
        boolean play = true;
        String name;

        while (play == true) {
            name = Player_Name();
            System.out.println('\n' + "Starting the game, " + name + "..." + '\n');
            System.out.println("Good luck!");

            time.Update_Time("start");
            Difficulty_Selection(minefield);
            victory = MineSweep(minefield);     
            time.Update_Time("finish");

            if (victory == true) {
                System.out.println("Congratulations! You cleared the whole field!");
            } else {
                System.out.println("BOOM!! Er... You lost.");
                System.out.println("Revealing the whole minefield: ");
                System.out.println("");
                minefield.Print_AnswerBoard();
            }

            System.out.println();
            time.Time_Taken();
            score.Calculate_Score(minefield, time, victory);
            if (victory == true) New_Score(score, name);
            play = Another_Game(name);
        }

        System.out.println("Closing software... Thank you for playing!");
        System.out.println("Check my github for other games/programs!");
        scan.close();
    }
}
