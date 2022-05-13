
import com.sun.javafx.collections.MappingChange;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Maze extends JFrame {

    private int[][] values;
    private boolean[][] visited;
    private int startRow;
    private int startColumn;
    private ArrayList<JButton> buttonList;
    private int rows;
    private int columns;
    private boolean backtracking;
    private int algorithm;

    public Maze(int algorithm, int size, int startRow, int startColumn) {
        this.algorithm = algorithm;
        Random random = new Random();
        this.values = new int[size][];
        for (int i = 0; i < values.length; i++) {
            int[] row = new int[size];
            for (int j = 0; j < row.length; j++) {
                if (i > 1 || j > 1) {
                    row[j] = random.nextInt(8) % 7 == 0 ? Definitions.OBSTACLE : Definitions.EMPTY;
                } else {
                    row[j] = Definitions.EMPTY;
                }
            }
            values[i] = row;
        }
        values[0][0] = Definitions.EMPTY;
        values[size - 1][size - 1] = Definitions.EMPTY;
        this.visited = new boolean[this.values.length][this.values.length];
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.buttonList = new ArrayList<>();
        this.rows = values.length;
        this.columns = values.length;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setLocationRelativeTo(null);
        GridLayout gridLayout = new GridLayout(rows, columns);
        this.setLayout(gridLayout);
        for (int i = 0; i < rows * columns; i++) {
            int value = values[i / rows][i % columns];
            JButton jButton = new JButton(String.valueOf(i));
            if (value == Definitions.OBSTACLE) {
                jButton.setBackground(Color.BLACK);
            } else {
                jButton.setBackground(Color.WHITE);
            }
            this.buttonList.add(jButton);
            this.add(jButton);
        }
        this.setVisible(true);
        this.setSize(Definitions.WINDOW_WIDTH, Definitions.WINDOW_HEIGHT);
        this.setResizable(false);
    }

    public void checkWayOut() {
        new Thread(() -> {
            boolean result = false;
            switch (this.algorithm) {
                case Definitions.ALGORITHM_BRUTE_FORCE:
                    break;
                case Definitions.ALGORITHM_DFS:
                  result=  run();
                    break;
                case Definitions.ALGORITHM_BFS:
                    break;
            }
            JOptionPane.showMessageDialog(null,  result ? "FOUND SOLUTION" : "NO SOLUTION FOR THIS MAZE");

        }).start();
    }


    public void setSquareAsVisited(int x, int y, boolean visited) {
        try {
            if (visited) {
                if (this.backtracking) {
                    Thread.sleep(Definitions.PAUSE_BEFORE_NEXT_SQUARE * 5);
                    this.backtracking = false;
                }
                this.visited[x][y] = true;
                for (int i = 0; i < this.visited.length; i++) {
                    for (int j = 0; j < this.visited[i].length; j++) {
                        if (this.visited[i][j]) {
                            if (i == x && y == j) {
                                this.buttonList.get(i * this.rows + j).setBackground(Color.RED);
                            } else {
                                this.buttonList.get(i * this.rows + j).setBackground(Color.BLUE);
                            }
                        }
                    }
                }
            } else {
                this.visited[x][y] = false;
                this.buttonList.get(x * this.columns + y).setBackground(Color.WHITE);
                Thread.sleep(Definitions.PAUSE_BEFORE_BACKTRACK);
                this.backtracking = true;
            }
            if (!visited) {
                Thread.sleep(Definitions.PAUSE_BEFORE_NEXT_SQUARE / 4);
            } else {
                Thread.sleep(Definitions.PAUSE_BEFORE_NEXT_SQUARE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean run() {
        boolean exit=false;
        Stack<JButton> myStack = new Stack<>();
        myStack.add(this.buttonList.get(0));
        while (!myStack.isEmpty()) {
            JButton currentNode = myStack.pop();
            if (!isVisited(currentNode)) {
                int arrayXAndY[]=getXAndY(currentNode);
                setSquareAsVisited(arrayXAndY[0],arrayXAndY[1],true);
                for (JButton neighbor : getNeighbors(currentNode)) {
                    if (neighbor !=null){
                        int neighborXY[]=getXAndY(neighbor);
                        if (neighbor.getBackground()!=Color.black){
                            if (!isVisited(neighbor)) {
                                myStack.add(neighbor);
                            }
                        }

                    }

                }
            }
            if (visited[visited.length-1][visited.length-1]){
                exit=true;
                break;
            }
        }
      return exit;
    }

    public boolean isVisited(JButton currentNode){

        int array[]=getXAndY(currentNode);

        return this.visited[array[0]][array[1]] ;

    }

    public int[] getXAndY(JButton currentNode){
        Map<JButton,int[]> nodeCurrent=new HashMap<>();
        int j=0;
        int k=0;
        for (int i = 0; i < this.buttonList.size(); i++) {

            nodeCurrent.put(this.buttonList.get(i), new int[]{k, j});
            j++;
            if (j==this.visited.length){
                k++;
                j=0;
            }


        }
        return nodeCurrent.get(currentNode);
    }

   public JButton[] getNeighbors(JButton nodeJButton){
        int arrayNode[]=getXAndY(nodeJButton);
        int x=arrayNode[1]; int y=arrayNode[0];
        JButton node[]=new JButton[8];
       JButton arrayJButton[][]=getNodeMitarc();
       if (x==0){
           if (y==0){
               node[0]=arrayJButton[0][1];
               node[1] =arrayJButton[1][0];
           }else if (y==visited.length-1){
               node[0]=arrayJButton[visited.length-2][0];
               node[1]=arrayJButton[visited.length-1][1];
           }else {
               node[0]=arrayJButton[y+1][0];
               node[1]=arrayJButton[y-1][0];
               node[2]=arrayJButton[y][x+1];
           }
       }else if (x==visited.length-1){
           if (y==0){
               node[0]=arrayJButton[0][x-1];
               node[1]=arrayJButton[1][x];
           }else if (y==visited.length-1){
               node[0]=arrayJButton[y][x-1];
               node[1]=arrayJButton[y-1][y];
           }else {
               node[0]=arrayJButton[y-1][x];
               node[1]=arrayJButton[y+1][x];
               node[2]=arrayJButton[y][x-1];
           }
       }else {
           if (y==0){
               node[0]=arrayJButton[y][x+1];
               node[1]=arrayJButton[y][x-1];
               node[2]=arrayJButton[y+1][x];
           }else if (y==visited.length-1){
               node[0]=arrayJButton[y][x+1];
               node[1]=arrayJButton[y][x-1];
               node[2]=arrayJButton[y-1][x];
           }else {
               node[0]=arrayJButton[y+1][x];
               node[1]=arrayJButton[y-1][x];
               node[2]=arrayJButton[y][x+1];
               node[3]=arrayJButton[y][x-1];
           }
       }
       System.out.println(nodeJButton.getText());

       for (int i = 0; i < node.length; i++) {
           if (node[i] !=null)
           System.out.println("name" + node[i].getText());

       }


       return node;
    }

    public JButton[][] getNodeMitarc(){
        JButton arrayJButton[][]=new JButton[visited.length][visited.length];
        int j=0;
        int k=0;
        for (int i = 0; i < this.buttonList.size(); i++) {

            arrayJButton[k][j]=this.buttonList.get(i);
            j++;
            if (j==this.visited.length){
                k++;
                j=0;
            }

        }

        return  arrayJButton;
    }

}
