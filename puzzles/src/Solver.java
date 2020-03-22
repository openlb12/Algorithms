import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private MinPQ<Board> game_tree = new MinPQ<Board>();

    private Stack<Board>

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Illegal initial board\n");
        }
        game_tree.insert(initial);
        while (!game_tree.min().isGoal()) {
            Board stepBoard = game_tree.delMin();
            for (Board item : stepBoard.neighbors()) {
                if (!item.equals(initial)) {
                    game_tree.insert(item);
                }
            }
        }
    }


    // is the initial board solvable? (see below)
    public boolean isSolvable()

    // min number of moves to solve initial board
    public int moves()

    // sequence of boards in a shortest solution
    public Iterable<Board> solution()

    // test client (see below)
    public static void main(String[] args)

}
