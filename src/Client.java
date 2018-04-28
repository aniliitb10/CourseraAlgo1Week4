import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Client
{
  public static void main(String[] args)
  {
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] blocks = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        blocks[i][j] = in.readInt();
    Board board = new Board(blocks);
//    System.out.println(board);
//    System.out.println("hamming: " + board.hamming() + ", manhattan: " + board.manhattan());
//    System.out.println("A twin:\n" + board.twin());
//
//    System.out.println("Following are the neighbours:\n");
//    for (Board b : board.neighbors())
//    {
//      System.out.println(b);
//    }

    // solve the puzzle
    Solver solver = new Solver(board);

    // print solution to standard output
    if (!solver.isSolvable())
      StdOut.println("No solution possible");
    else
    {
      StdOut.println("Minimum number of moves = " + solver.moves());
//      for (Board solutionBoard : solver.solution())
//        StdOut.println(solutionBoard);

    }
  }
}
