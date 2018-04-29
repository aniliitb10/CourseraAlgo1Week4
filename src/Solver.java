import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver
{
  private MinPQ<SearchNode> _minPq = new MinPQ<>();
  private boolean _solvable;
  private Board _initial;

  private class SearchNode implements Comparable<SearchNode>
  {
    private Board _board;
    private int   _numOfMovesMade;
    private SearchNode _preSearchNode;

    SearchNode(Board board_, int numOfMovesMade_, SearchNode preSearchNode_)
    {
      _board = board_;
      _numOfMovesMade = numOfMovesMade_;
      _preSearchNode = preSearchNode_;
    }

    public int priority() { return (_numOfMovesMade + _board.manhattan()); }

    public boolean isGoal() { return _board.isGoal(); }

    public Board getBoard() { return _board; }

    @Override
    public int compareTo(SearchNode that) { return Integer.compare(this.priority(), that.priority()); }
  }

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial)
  {
    if (initial == null)
    {
      throw new java.lang.IllegalArgumentException();
    }

    _initial = initial;
    Board initialTwin = initial.twin();

    SearchNode searchNode = new SearchNode(initial, 0, null);
    SearchNode searchNodeTwin = new SearchNode(initialTwin, 0, null);

    _minPq.insert(searchNode);
    MinPQ<SearchNode> minPQTwin = new MinPQ<>();
    minPQTwin.insert(searchNodeTwin);

    SearchNode minSearchNode;
    SearchNode minSearchNodeTwin;

    while((!_minPq.min().isGoal()) && (!minPQTwin.min().isGoal()))
    {
      minSearchNode = _minPq.delMin();
      minSearchNodeTwin = minPQTwin.delMin();

      for (Board neighbour : minSearchNode._board.neighbors())
      {
        if ((minSearchNode._numOfMovesMade == 0) || (!neighbour.equals(minSearchNode._preSearchNode._board)))
        {
          _minPq.insert(new SearchNode(neighbour, minSearchNode._numOfMovesMade + 1, minSearchNode));
        }
      }

      for (Board neighbour : minSearchNodeTwin._board.neighbors())
      {
        if ((minSearchNodeTwin._numOfMovesMade == 0) || (!neighbour.equals(minSearchNodeTwin._preSearchNode._board)))
        {
          minPQTwin.insert(new SearchNode(neighbour, minSearchNodeTwin._numOfMovesMade + 1, minSearchNodeTwin));
        }
      }
    }

    if (_minPq.min().isGoal())
    {
      _solvable = true;
    }
  }

  // is the initial board solvable?+
  public boolean isSolvable() { return _solvable; }

  // min number of moves to solve initial board; -1 if unsolvable
  public int moves() { return (this.isSolvable() ?  _minPq.min()._numOfMovesMade : -1); }

  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution()
  {
    if (!this.isSolvable()) return null;

    Stack<Board> stackSolution  = new Stack<>();
    SearchNode current = _minPq.min();
    while (current._preSearchNode!=null) {
      stackSolution.push(current._board);
      current = current._preSearchNode;
    }
    stackSolution.push(_initial);
    return stackSolution;
  }

  // solve a slider puzzle (given below)
  public static void main(String[] args)
  {

  }
}