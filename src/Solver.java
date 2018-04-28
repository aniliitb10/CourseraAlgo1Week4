import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Iterator;

public class Solver
{
  private MinPQ<SearchNode> _minPq = new MinPQ<>();
  private ArrayList<Board> _solutions = new ArrayList<>();

  private class SolutionIterator implements Iterable<Board>
  {
    ArrayList<Board> _solutionsForIteration;
    SolutionIterator(ArrayList<Board> solutions_)
    {
      _solutionsForIteration = solutions_;
    }

    @Override
    public Iterator<Board> iterator()
    {
      return _solutionsForIteration.iterator();
    }
  }

  private class SearchNode implements Comparable<SearchNode>
  {
    private Board _board;
    private int   _numOfMovesMade;
    private Board _predecessorBoard;

    SearchNode(Board board_, int numOfMovesMade_, Board predecessorBoard_)
    {
      _board = board_;
      _numOfMovesMade = numOfMovesMade_;
      _predecessorBoard = predecessorBoard_;
    }

    public int getNumOfMovesMade()
    {
      return _numOfMovesMade;
    }

    public ArrayList<SearchNode> getAllNeighbourSearchNode()
    {
      ArrayList<SearchNode> allNeighbours = new ArrayList<>();
      for (Board board : _board.neighbors())
      {
        if (board.equals(this._predecessorBoard)) continue;
        allNeighbours.add(new SearchNode(board, (_numOfMovesMade + 1), this._board));
      }

      return allNeighbours;
    }

    public int priority()
    {
      return (_numOfMovesMade + _board.manhattan());
    }

    public boolean isGoal()
    {
      return _board.isGoal();
    }

    public Board getBoard() { return _board; }

    @Override
    public int compareTo(SearchNode that)
    {
      return this.priority() - that.priority();
    }
  }

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial)
  {
    SearchNode searchNode = new SearchNode(initial, 0, null);
    _minPq.insert(searchNode);
    SearchNode minSearchNode = _minPq.delMin();
    _solutions.add(minSearchNode.getBoard());
    while(!minSearchNode.isGoal())
    {
      for (SearchNode neighbourSearchNode : minSearchNode.getAllNeighbourSearchNode())
      {
        _minPq.insert(neighbourSearchNode);
      }

      minSearchNode = _minPq.delMin();
      _solutions.add(minSearchNode.getBoard());
    }
  }

  // is the initial board solvable?+
  public boolean isSolvable()
  {
    return true; // need to fix this
  }

  // min number of moves to solve initial board; -1 if unsolvable
  public int moves()
  {
    return (_solutions.size() - 1);
  }

  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution()
  {
    return new SolutionIterator(this._solutions);
  }

  // solve a slider puzzle (given below)
  public static void main(String[] args)
  {

  }

}
