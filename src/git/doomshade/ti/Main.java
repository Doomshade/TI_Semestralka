package git.doomshade.ti;

import javax.swing.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        MainFrame frame = new MainFrame("TI");

        frame.init();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static int validateInput(String s) {
        if (s.equalsIgnoreCase("exit")) return -1;

        return 1;
    }
}
