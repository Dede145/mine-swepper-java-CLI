/**
 * MINESWEEPER GAME
 * 
 * Author: Andre Kenji Sato
 * Version: Beta 1.0
 * 
 * (N) No rights reserved. 2022.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

/**
 * Scoreboard class. 
 * Writes and reads the scoreboard.
 */
public class Scoreboard {
    private File file_all = new File("scores/scoreboard.csv");
    private File file_top10 = new File("scores/top10.csv");
    private int score = 0;
    private String name;

    /**
     * Empty Constructor.
     */
    public Scoreboard() {}

    /**
     * Calculates the player's score. The score is 0 if the player lost the game.
     * 
     * @param minefield The board used in the game.
     * @param time TimeKepping class.
     * @param victory If the player won the game.
     */
    public void Calculate_Score(Board minefield, TimeKeeping time, boolean victory) {
        if (victory == true) {
            double mine_score = 100 * minefield.Display_Quant_Mines();
            double time_score = 0;

            switch (minefield.Display_Difficulty()) {
                case 1:
                case 2:
                    time_score = mine_score - 2500 * time.Game_Duration();
                    break;
                case 3:
                case 4:
                    time_score = mine_score - 2300 * time.Game_Duration();
                    break;
                case 5:
                    time_score = mine_score - 2100 * time.Game_Duration();
                    break;
                case 0:
                    time_score = mine_score - 2100 * time.Game_Duration();
                    break;
            }

            System.out.println("Mine Score: " + (int) mine_score);
            System.out.println("Time Score: " + time_score);

            if (time_score < 0) {
                time_score = 0;
            }

            this.score = (int) (mine_score + time_score);
        }

        System.out.println("Your Score: " + score + '\n');
    }

    /**
     * Writes a new score to the scoreboard. Skips if the score is 0.
     * 
     * @param name The player's name.
     */
    public void Write_Score(String name) {  
        if (this.score == 0) return ;

        try (FileWriter writer = new FileWriter(file_all, true)) {
            this.name = name;
            writer.write(this.name + "," + this.score + ",");
        } catch (IOException e) {
            System.err.println("ERROR CODE 2: Scoreboard.csv can't be written!");
            return ;
        }

        Update_Top10();
    }

    /**
     * Rewrites the "top10.csv" when the new score is one of the 10 largest.
     * 
     * @param top_players The new top10 players, ordered from the 1st to the 10th.
     * @param top_scores The new top10 scores, ordered from the 1st to the 10th.
     */
    public void Write_Top10(String[] top_players, int[] top_scores) {
        try (FileWriter writer = new FileWriter(file_top10, false)) {
            for (int i = 0; i < top_players.length; i++) {
                writer.write(top_players[i] + "," + top_scores[i] + "," + "\n");
            }   
        } catch (IOException e) {
            System.err.println("ERROR CODE 2: Top10.csv can't be written!");
            return ;
        }
    }

    /**
     * Checks if the new score is one of the 10 largest, and updates "top10.csv" if that's the 
     * case.
     */
    public void Update_Top10() {
        String[] top_players = new String[10];
        int[] top_scores = new int[10];
        boolean top10_changed = false;

        try (Scanner scanner = new Scanner(file_top10)) {
            scanner.useLocale(Locale.ITALY);
            scanner.useDelimiter(",");

            for (int i = 0; i < 10; i++) {
                top_players[i] = scanner.next();
                top_scores[i] = scanner.nextInt();

                if (top_scores[i] < this.score) {
                    String s = top_players[i];
                    top_players[i] = this.name;
                    this.name = s;
                    int x = top_scores[i];
                    top_scores[i] = this.score;
                    this.score = x;
                    top10_changed = true;
                }
                scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println("ERROR CODE 3: top10.csv couldn't be found!");
            System.err.println("Cannot update top10.csv score!");
            return ;
        } catch (InputMismatchException e) {
            System.err.println("ERROR CODE 4: Non-Integer score found in top10.csv!");
            System.err.println("Cannot update top10.csv score!");
            return ;
        }

        if (top10_changed) Write_Top10(top_players, top_scores);
    }

    /**
     * Prints the 10 top scores, stored in "top10.csv".
     */
    public void Display_Top10() {
        try (Scanner scanner = new Scanner(file_top10)) {
            scanner.useLocale(Locale.ITALY);
            scanner.useDelimiter(",");

            System.out.println('\n' + "Top 10 scores!" + '\n');

            for (int line = 0; line < 10; line++) {
                System.out.print("Player: " + scanner.next());
                System.out.println("; Score: " + scanner.nextInt());
                scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println("ERROR CODE 3: Scoreboard.csv couldn't be found!");
            System.err.println("Cannot show the scoreboard!");
            return ;
        } catch (InputMismatchException e) {
            System.err.println("ERROR CODE 4: Non-Integer score found in Scoreboard.csv!");
            System.err.println("Cannot show the scoreboard!");
            return ;
        }
    }
}