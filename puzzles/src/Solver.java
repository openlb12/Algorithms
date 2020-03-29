import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private boolean isSolvable;
    private int pqNumber;
    private int solutionMoves;
    private Stack<Board> solutionStep;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Illegal initial board\n");
        }
        isSolvable = false;
        BoardNode solution = null;
        pqNumber = 0;

        MinPQ<BoardNode> searchNode = new MinPQ<BoardNode>();
        MinPQ<BoardNode> twinSearchNode = new MinPQ<BoardNode>();
        searchNode.insert(new BoardNode(null, initial));
        twinSearchNode.insert(new BoardNode(null, initial.twin()));
        while (!searchNode.isEmpty()) {
            BoardNode current = searchNode.delMin();
            if (current.getNode().isGoal()) {
                isSolvable = true;
                solution = current;
                break;
            }
            BoardNode retriever = current.previousNode;
            for (Board it : current.getNode().neighbors()) {
                BoardNode snode = new BoardNode(current, it);
                if (!snode.isEqual(retriever)) {
                    searchNode.insert(snode);
                    pqNumber++;
                }
            }
            if (!twinSearchNode.isEmpty()) {
                BoardNode currentTwin = twinSearchNode.delMin();
                if (currentTwin.getNode().isGoal()) {
                    isSolvable = false;
                    solution = null;
                    break;
                }
                BoardNode retrieverTwin = currentTwin.previousNode;
                for (Board it : currentTwin.getNode().neighbors()) {
                    BoardNode snode = new BoardNode(currentTwin, it);
                    if (!snode.isEqual(retrieverTwin)) {
                        twinSearchNode.insert(snode);
                        pqNumber++;
                    }
                }
            }
        }

        if (solution == null) {
            solutionMoves = -1;
            solutionStep = null;
        } else {
            solutionMoves = solution.getMoves();
            solutionStep = new Stack<Board>();
            BoardNode step = solution;
            while (step != null) {
                solutionStep.push(step.getNode());
                step = step.previousNode;
            }
        }
    }

    private boolean quickSolvableCheck(Board init) {
        String[] input = init.toString().split("[ \n]+");
        int boardWidth = Integer.parseInt(input[0]);
        int blackTileX = 0;
        int inverNum = 0;
        int[] boardTiles = new int[boardWidth * boardWidth];
        for (int i = 0; i < boardTiles.length; i++) {
            boardTiles[i] = Integer.parseInt(input[1 + i]);
            if (boardTiles[i] == 0) {
                blackTileX = i / boardWidth;
                continue;
            }
            for (int j = 0; j < i; j++) {
                if (boardTiles[j] == 0) continue;
                if (boardTiles[j] > boardTiles[i]) {
                    inverNum++;
                }
            }

        }
        return (inverNum + (boardWidth - 1 - blackTileX) * (boardWidth - 1)) % 2 == 0;


    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return solutionMoves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return solutionStep;
    }

    private int getPqNumber() {
        return pqNumber;
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
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
            StdOut.println("PQ number of searching  = " + solver.getPqNumber());
        } else {
            for (Board board : solver.solution())
                StdOut.println(board);
            StdOut.println("Minimum number of moves = " + solver.moves());
            StdOut.println("PQ number of searching  = " + solver.getPqNumber());
        }
    }

    private class BoardNode implements Comparable<BoardNode> {
        private final Board node;
        private final int manhPriority;
        private final int moves;
        private final BoardNode previousNode;

        BoardNode(BoardNode pn, Board bd) {
            node = bd;
            previousNode = pn;
            if (pn == null) {
                moves = 0;
            } else {
                moves = pn.getMoves() + 1;
            }
            manhPriority = moves + node.manhattan();
        }


        //        @Override
//        public int compareTo(BoardNode bn) {
//            if (this.manhPriority == bn.getManhPriority()) {
//                return this.hammPriority - bn.getHammPriority();
//            }
//            return this.manhPriority - bn.getManhPriority();
//        }
//        @Override
//        public int compareTo(BoardNode bn) {
//            return this.hammPriority - bn.getHammPriority();
//        }
        @Override
        public int compareTo(BoardNode bn) {
            if (this.manhPriority == bn.getManhPriority()) {
                return bn.getMoves() - moves;
            } else {
                return this.manhPriority - bn.getManhPriority();
            }
        }


        int getManhPriority() {
            return manhPriority;
        }

        int getMoves() {
            return moves;
        }

        Board getNode() {
            return node;
        }

        BoardNode getPreviousNode() {
            return previousNode;
        }

        boolean isEqual(BoardNode x) {
            if (x == null) return false;
            return this.node.equals(x.getNode());
        }
    }
}
