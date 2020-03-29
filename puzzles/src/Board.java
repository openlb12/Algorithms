import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public final class Board {
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    private final int[] boardTiles;
    private final int boardWidth;
    private final int blackTileX;
    private final int blackTileY;
    private final int hamming;
    private final int manhattan;
    private final boolean isGoal;

    public Board(int[][] tiles) {
        boardWidth = tiles.length;
        boardTiles = new int[boardWidth * boardWidth];
        int hamDistance = 0;
        int manhatDistance = 0;
        int blackX = boardWidth - 1;
        int blackY = boardWidth - 1;
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardWidth; j++) {
                if (tiles[i][j] == 0) {
                    blackX = i;
                    blackY = j;
                    boardTiles[i * boardWidth + j] = tiles[i][j];
                    continue;
                }
                if (tiles[i][j] != (i * boardWidth + j + 1) % boardTiles.length) {
                    hamDistance++;
                    manhatDistance += Math.abs((tiles[i][j] - 1) / boardWidth - i)
                            + Math.abs((tiles[i][j] - 1) % boardWidth - j);
                }
                boardTiles[i * boardWidth + j] = tiles[i][j];
            }
        }
        blackTileX = blackX;
        blackTileY = blackY;
        hamming = hamDistance;
        manhattan = manhatDistance;
        isGoal = (hamming == 0);
    }

    // string representation of this board
    public String toString() {
        StringBuilder boardInfo = new StringBuilder();
        boardInfo.append(boardWidth + "\n");
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardWidth; j++) {
                boardInfo.append(String.format("%2d ", boardTiles[i * boardWidth + j]));
            }
            boardInfo.append("\n");
        }
        return boardInfo.toString();
    }

    // board dimension n
    public int dimension() {
        return boardWidth;
    }


    private int dimenToLine(int i, int j) {
        return i * boardWidth + j;
    }


    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;

    }

    // is this board the goal board?
    public boolean isGoal() {
        return isGoal;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.blackTileX != this.blackTileX) return false;
        if (that.blackTileY != this.blackTileY) return false;
        return Arrays.equals(that.boardTiles, this.boardTiles);
    }

    private int[] tileExch(int loc1, int loc2) {
        int[] switchBoard = Arrays.copyOf(boardTiles, boardTiles.length);
        switchBoard[loc1] = boardTiles[loc2];
        switchBoard[loc2] = boardTiles[loc1];
        return switchBoard;
    }

    private int[][] boardGenerate(int[] list) {
        int[][] board = new int[boardWidth][boardWidth];
        for (int i = 0; i < list.length; i++) {
            board[i / boardWidth][i % boardWidth] = list[i];
        }
        return board;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int idx = dimenToLine(blackTileX, blackTileY);
        Stack<Board> neighbor = new Stack<Board>();

        for (int i = -1; i <= 1; i += 2) {
            if (blackTileX + i >= boardWidth || blackTileX + i < 0) continue;
            int neighborLoc = dimenToLine(blackTileX + i, blackTileY);
            neighbor.push(new Board(boardGenerate(tileExch(idx, neighborLoc))));
        }
        for (int i = -1; i <= 1; i += 2) {
            if (blackTileY + i >= boardWidth || blackTileY + i < 0) continue;
            int neighborLoc = dimenToLine(blackTileX, blackTileY + i);
            neighbor.push(new Board(boardGenerate(tileExch(idx, neighborLoc))));
        }
        return neighbor;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int switch1 = dimenToLine((blackTileX + 1) % boardWidth, blackTileY);
        int switch2 = dimenToLine((blackTileX + 1) % boardWidth, (blackTileY + 1) % boardWidth);
        return new Board(boardGenerate(tileExch(switch1, switch2)));

    }


    // unit testing (not graded)
    public static void main(String[] args) {

        // for each command-line argument
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            // solve the slider puzzle
            Board initial = new Board(tiles);
            StdOut.print(initial);
            StdOut.println("Distance of Hamming: " + initial.hamming());
            StdOut.println("Distance of Manhattan: " + initial.manhattan());

            StdOut.print(initial.twin());
            for (Board it : initial.neighbors()) {
                StdOut.print(it);
            }


        }
    }
}
