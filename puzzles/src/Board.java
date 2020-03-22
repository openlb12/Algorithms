import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    private final int[] board_tiles;
    private final int BOARD_WIDTH;
    private int black_tile_x;
    private int black_tile_y;

    public Board(int[][] tiles) {
        BOARD_WIDTH = tiles.length;
        board_tiles = new int[BOARD_WIDTH * BOARD_WIDTH];
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (tiles[i][j] == 0) {
                    black_tile_x = i;
                    black_tile_y = j;
                }
                board_tiles[i * BOARD_WIDTH + j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder board_info = new StringBuilder();
        board_info.append(BOARD_WIDTH + "\n");
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                board_info.append(String.format("%2d ", board_tiles[i * BOARD_WIDTH + j]));
            }
            board_info.append("\n");
        }
        return board_info.toString();
    }

    // board dimension n
    public int dimension() {
        return BOARD_WIDTH;
    }


    private int dimen_to_line(int i, int j) {
        return i * BOARD_WIDTH + j;
    }

    private int[] id_to_loc(int id) {
        if (id == 0) {
            return new int[]{BOARD_WIDTH - 1, BOARD_WIDTH - 1};
        } else {
            return new int[]{(id - 1) / BOARD_WIDTH, (id - 1) % BOARD_WIDTH};
        }
    }

    private int manhanttan_distance(int p1, int p2) {
        int[] loc1 = id_to_loc(p1);
        int[] loc2 = id_to_loc(p2);
        int distance = 0;
        for (int i = 0; i < loc1.length; i++) {
            distance += Math.abs(loc1[i] - loc2[i]);
        }
        return distance;
    }

    // number of tiles out of place
    public int hamming() {
        int ham_distance = 0;
        for (int i = 0; i < board_tiles.length - 1; i++) {
            if (board_tiles[i] != (i + 1) % board_tiles.length) {
                ham_distance++;
            }
        }
        return ham_distance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan_distance = 0;
        for (int i = 0; i < board_tiles.length; i++) {
            if (board_tiles[i] == 0) continue;
            manhattan_distance += manhanttan_distance(board_tiles[i], (i + 1) % board_tiles.length);
        }
        return manhattan_distance;

    }

    // is this board the goal board?
    public boolean isGoal() {
        if (hamming() == 0) {
            return true;
        } else {
            return false;
        }
    }

    // does this board equal y?
    public boolean equals(Object y) {
        return this.toString() == y.toString();
    }

    private int[] tile_exch(int loc1, int loc2) {
        int[] switch_board = Arrays.copyOf(board_tiles, board_tiles.length);
        switch_board[loc1] = board_tiles[loc2];
        switch_board[loc2] = board_tiles[loc1];
        return switch_board;
    }

    private int[][] board_generate(int[] list) {
        int[][] board = new int[BOARD_WIDTH][BOARD_WIDTH];
        for (int i = 0; i < list.length; i++) {
            board[i / BOARD_WIDTH][i % BOARD_WIDTH] = list[i];
        }
        return board;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int idx = -1;
        Stack<Board> neighbor = new Stack<Board>();
        while (board_tiles[++idx] != 0) ;
        int[] loc = id_to_loc(idx + 1);

        for (int i = -1; i <= 1; i += 2) {
            if (loc[0] + i >= BOARD_WIDTH || loc[0] + i < 0) continue;
            int neighbor_loc = dimen_to_line(loc[0] + i, loc[1]);
            neighbor.push(new Board(board_generate(tile_exch(idx, neighbor_loc))));
        }
        for (int i = -1; i <= 1; i += 2) {
            if (loc[1] + i >= BOARD_WIDTH || loc[1] + i < 0) continue;
            int neighbor_loc = dimen_to_line(loc[0], loc[1] + i);
            neighbor.push(new Board(board_generate(tile_exch(idx, neighbor_loc))));
        }
        return neighbor;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int switch1 = dimen_to_line((black_tile_x + 1) % BOARD_WIDTH, black_tile_y);
        int switch2 = dimen_to_line((black_tile_x + 1) % BOARD_WIDTH, (black_tile_y + 1) % BOARD_WIDTH);
        return new Board(board_generate(tile_exch(switch1, switch2)));

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
            StdOut.println(initial.hamming());
            StdOut.println(initial.manhattan());

            StdOut.print(initial.twin());
            for (Board it : initial.neighbors()) {
                StdOut.print(it);
            }


        }
    }
}
