import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Iterator;

public class Solver
{
  private MinPQ<SearchNode> _minPq = new MinPQ<>();
  private boolean _solvable;
  private SearchNode finalSearchNode = null;
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
    Board _board;
    int   _numOfMovesMade;
    SearchNode _preSearchNode;

    SearchNode(Board board_)
    {
      this(board_, 0, null);
    }

    SearchNode(Board board_, int numOfMovesMade_, SearchNode preSearchNode_)
    {
      _board = board_;
      _numOfMovesMade = numOfMovesMade_;
      _preSearchNode = preSearchNode_;
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
      return Integer.compare(this.priority(), that.priority());
    }
  }

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial)
  {
    Board initialTwin = initial.twin();
    SearchNode inSearchNode = new SearchNode(initial, 0, null);
    SearchNode inSearchNodeTwin = new SearchNode(initialTwin, 0, null);

    MinPQ<SearchNode> minPQTwin = new MinPQ<>();
    _minPq.insert(inSearchNode);
    minPQTwin.insert(inSearchNodeTwin);

    SearchNode minSearchNode = _minPq.delMin();
    SearchNode minSearchNodeTwin = minPQTwin.delMin();

    while((!minSearchNode.isGoal()) && (!minSearchNodeTwin.isGoal()))
    {
//      _minPq = new MinPQ<>();
      for (Board board : minSearchNode.getBoard().neighbors())
      {
        if ((minSearchNode._preSearchNode == null) || (!board.equals(minSearchNode._preSearchNode._board)))
        {
          SearchNode sn = new SearchNode(board);
          sn._preSearchNode = minSearchNode;
          sn._numOfMovesMade = minSearchNode._numOfMovesMade + 1;
          _minPq.insert(sn);
        }
      }

//      minPQTwin = new MinPQ<>();
      for (Board board : minSearchNodeTwin.getBoard().neighbors())
      {
        if ((minSearchNodeTwin._preSearchNode == null) || (!board.equals(minSearchNodeTwin._preSearchNode._board)))
        {
          SearchNode snt = new SearchNode(board);
          snt._preSearchNode = minSearchNodeTwin;
          snt._numOfMovesMade = minSearchNodeTwin._numOfMovesMade + 1;
          minPQTwin.insert(snt);
        }
      }

      minSearchNode = _minPq.delMin();
      minSearchNodeTwin = minPQTwin.delMin();
    }

    if (minSearchNode.isGoal())
    {
      _solvable = true;
      finalSearchNode = minSearchNode;

      for (SearchNode s = minSearchNode; s != null; s = minSearchNode._preSearchNode)
      {
        _solutions.add(s._board);
      }
    }
  }

  // is the initial board solvable?+
  public boolean isSolvable()
  {
    return _solvable;
  }

  // min number of moves to solve initial board; -1 if unsolvable
  public int moves()
  {
    return (this.isSolvable() ? (_solutions.size() - 1) : -1);
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
