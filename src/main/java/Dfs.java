import java.util.Iterator;
import java.util.LinkedList;

public class Dfs {
    private LinkedList<Integer> adjLists[];
    private boolean visited[];


   public Dfs(int vertices) {
        adjLists = new LinkedList[vertices];
        visited = new boolean[vertices];

        for (int i = 0; i < vertices; i++)
            adjLists[i] = new LinkedList<Integer>();
    }


    void addEdge(int src, int dest) {
        adjLists[src].add(dest);
    }


    void algorithm(int vertex) {
        visited[vertex] = true;
        System.out.print(vertex + " ");

        Iterator<Integer> ite = adjLists[vertex].listIterator();
        while (ite.hasNext()) {
            int adj = ite.next();
            if (!visited[adj])
                algorithm(adj);
        }
    }

}