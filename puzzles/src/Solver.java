import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {


    private boolean isSolvable;
    private BoardNode solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Illegal initial board\n");
        }
        MinPQ<BoardNode> search_node = new MinPQ<BoardNode>();
        isSolvable = false;
        solution = null;
        search_node.insert(new BoardNode(null, initial));
        while (!search_node.isEmpty()) {
            BoardNode current = search_node.delMin();
            if (current.getNode().isGoal()) {
                isSolvable = true;
                solution = current;
                break;
            }
            for (Board it : current.getNode().neighbors()) {
                BoardNode snode = new BoardNode(current, it);
                BoardNode retriever = current.previous_node;


                boolean addNode = true;
                for (BoardNode sn : search_node) {
                    if (snode.isEqual(sn)) {
                        if (snode.compareTo(sn) < 0) {
                            addNode = false;
                            break;
                        } else {
                            addNode = false;
                            break;
                        }
                    }
                }
//                while (retriever != null) {
//                    if (snode.isEqual(retriever)) {
//                        addNode = false;
//                        break;
//                    }
//                    retriever = retriever.previous_node;
//                }
                if (addNode) {
                    search_node.insert(snode);
                }
            }

        }

    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return solution.getMoves();
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        Stack<Board> solution_step = new Stack<Board>();
        BoardNode step = solution;
        while (step != null) {
            solution_step.push(step.getNode());
            step = step.previous_node;
        }
        return solution_step;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private class BoardNode implements Comparable<BoardNode> {
        private final Board node;
        private final int priority;
        private final int moves;
        private final BoardNode previous_node;

        BoardNode(BoardNode pn, Board bd) {
            node = bd;
            previous_node = pn;
            if (pn == null) {
                moves = 0;
            } else {
                moves = pn.getMoves() + 1;
            }
            priority = moves + node.manhattan();
        }

//        public void resetBoardNode(BoardNode bd) {
//            node = bd.getNode();
//            moves = bd.getMoves();
//            priority = bd.getPriority();
//            previous_node = bd.getPrevious_node();
//        }

        @Override
        public int compareTo(BoardNode o) {
            return this.priority - o.getPriority();
        }

        int getPriority() {
            return priority;
        }

        int getMoves() {
            return moves;
        }

        Board getNode() {
            return node;
        }

        BoardNode getPrevious_node() {
            return previous_node;
        }

        boolean isEqual(BoardNode x) {
            return this.node.equals(x.getNode());
        }
    }

}
