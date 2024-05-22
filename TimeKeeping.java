/**
 * MINESWEEPER GAME
 * 
 * Author: Andre Kenji Sato
 * Version: Beta 1.0
 * 
 * (N) No rights reserved. 2022.
 */

import java.time.*;

/**
 * TimeKeepinng class. 
 * Keeps track of starting and finishing times.
 */
public class TimeKeeping {
    private LocalTime clock;
    private LocalDate date;
    private int[] start_time = new int[3]; 
    private int[] finish_time = new int[3];
    private int start_day, finish_day;
    private double hours, minutes, seconds;

    /**
     * Empty constructor.
     */
    public TimeKeeping() {}

    /**
     * Stores the starting and the ending time of a game.
     * Index 0 stores the hours, 1 stores the minutes and 2 stores the seconds.
     * 
     * @param status indicates if the time to be stored is the starting or the ending time.
     */
    public void Update_Time(String status) {
        clock = LocalTime.now();
        date = LocalDate.now();

        if (status == "start") {
            start_time[0] = clock.getHour();
            start_time[1] = clock.getMinute();
            start_time[2] = clock.getSecond();
            start_day = date.getDayOfYear();
        }

        if (status == "finish") {
            finish_time[0] = clock.getHour();
            finish_time[1] = clock.getMinute();
            finish_time[2] = clock.getSecond();
            finish_day = date.getDayOfYear();
        }
        
    }

    /**
     * Prints the hours, minutes and seconds taken for the player to finish the game.
     */
    public void Time_Taken() {
        System.out.print("Total game time: ");

        int hour = finish_time[0] - start_time[0];
        int min = finish_time[1] - start_time[1];
        int sec = finish_time[2] - start_time[2];

        if (hour >= 0 && start_day != finish_day) {
            hour = 24;
            System.out.print("More than 24 hours.");
            return ;
        }

        if (sec < 0) {
            sec += 60;
            min += -1;
        }

        if (min < 0) {
            min += 60;
            hour += -1;
        }

        if (hour < 0) hour += 24;
        this.hours = hour;
        this.minutes = min;
        this.seconds = sec;

        if (hour != 0) {
            if (hour == 1) {
                System.out.print("1 hour ");
            } else {
                System.out.print((int) hour + " hours ");
            }
        }

        if (min != 0) {
            if (hour != 0) System.out.print(" ");

            if (min == 1) {
                System.out.print("1 minute ");
            } else {
                System.out.print((int) min + " minutes ");
            }
        }

        if (sec != 0) {
            if (hour != 0 || min != 0) System.out.print(" ");
            
            if (sec == 1) {
                System.out.print("1 second");
            } else {
                System.out.print((int) sec + " secounds");
            }
        }

        System.out.println(".");
    }

    /**
     * Returns the time it took to finish a game in minutes.
     * 
     * @return Time in minutes.
     */
    public double Game_Duration() {
        return hours * 60 + minutes + seconds / 60;
    }
}
