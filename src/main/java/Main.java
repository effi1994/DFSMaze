import javax.swing.*;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        String algorithmString = JOptionPane.showInputDialog("Choose algorithm  2 - DFS");
        String sizeAsString = JOptionPane.showInputDialog("Choose maze size: ");
        int size = Integer.parseInt(sizeAsString);

        int algorithm = Integer.parseInt(algorithmString);
        Maze maze = new Maze(algorithm, size, 0, 0);
        maze.checkWayOut();
    }

}
