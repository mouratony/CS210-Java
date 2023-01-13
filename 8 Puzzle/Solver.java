import dsa.LinkedStack;
import dsa.MinPQ;
import stdlib.In;
import stdlib.StdOut;

// A data type that implements the A* algorithm for solving the 8-puzzle and its generalizations.
public class Solver {
    // Minimum number of moves needed to solve the board.
    private int moves;
    // Sequence of boards in a shortest of the solution.
    private final LinkedStack<Board> solution;

    // Finds a solution to the initial board using the A* algorithm.
    public Solver(Board board) {
        // Corner cases.
        if (board == null) {
            throw new NullPointerException("board is null");
        }
        if (!board.isSolvable()) {
            throw new IllegalArgumentException("board is unsolvable");
        }
        // Initialize the instance variables
        solution = new LinkedStack<Board>();
        moves = 0;
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        pq.insert(new SearchNode(board, 0, null));
        // As long as pq is not empty.
        while (!pq.isEmpty()) {
            // Remove the smallest node.
            SearchNode node = pq.delMin();
            // Check if the board of the node is goal, if it is
            // insert it into the stack and all the previous boards.
            if (node.board.isGoal()) {
                moves = node.moves;
                for (SearchNode n = node; n.previous != null; n = n.previous) {
                    solution.push(n.board);
                }
                break;
            } else {
                // Iterate through the boards neighbors. Insert, a new node
                // into pq, for each neighbor that is different from the previous
                // board.(insert SearchNode(neighbor, node.moves + 1, node)).
                for (Board neighbor : node.board.neighbors()) {
                    Board prev = node.previous == null ? null : node.previous.board;
                    if (!neighbor.equals(prev)) {
                        pq.insert(new SearchNode(neighbor, node.moves + 1, node));
                    }
                }
            }
        }
    }

    // Returns the minimum number of moves needed to solve the initial board.
    public int moves() {
        return moves;
    }

    // Returns a sequence of boards in a shortest solution of the initial board.
    public Iterable<Board> solution() {
        return solution;
    }

    // A data type that represents a search node in the grame tree. Each node includes a
    // reference to a board, the number of moves to the node from the initial node, and a
    // reference to the previous node.
    private class SearchNode implements Comparable<SearchNode> {
        // The board represented by this node.
        private Board board;
        // Number of moves it took to get to this node.
        private int moves;
        // The previous search node.
        private final SearchNode previous;

        // Constructs a new search node.
        public SearchNode(Board board, int moves, SearchNode previous) {
            // Initialize instance variables.
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }

        // Returns a comparison of this node and other based on the following sum:
        // Manhattan distance of the board in the node + the # of moves to the node
        public int compareTo(SearchNode other) {
            // Since integer is a primitive data type,
            // return the difference of the sum of manhattan distance + # of moves
            int a = this.board.manhattan() + this.moves;
            int b = other.board.manhattan() + other.moves;
            return a-b;
        }
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        if (initial.isSolvable()) {
            Solver solver = new Solver(initial);
            StdOut.printf("Solution (%d moves):\n", solver.moves());
            StdOut.println(initial);
            StdOut.println("----------");
            for (Board board : solver.solution()) {
                StdOut.println(board);
                StdOut.println("----------");
            }
        } else {
            StdOut.println("Unsolvable puzzle");
        }
    }
}
