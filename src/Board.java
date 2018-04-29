import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.Arrays;
import java.util.Iterator;

public class Board {

  private int[][] _blocks;
  private int _hamming;
  private int _manhattan;

  private static int[][] deepCopy2DArray(int[][] arr_)
  {
    final int [][] copy = new int[arr_.length][];
    for (int index = 0; index < arr_.length; ++index)
    {
      copy[index] = Arrays.copyOf(arr_[index], arr_[index].length);
    }

    return copy;
  }

  private class BoardNeighbourIterator implements Iterable<Board>
  {
    private int _zRow; //0th based index
    private int _zCol; //0th based index
    private int _dimension;
    private ArrayList<Board> _boardList;

    BoardNeighbourIterator(int[][] blocks_)
    {
      _dimension = blocks_.length;
      _boardList = new ArrayList<>();
      for (int row = 0; row < _dimension; ++row)
      {
        for (int col = 0; col < _dimension; ++col)
        {
          if (blocks_[row][col] == 0)
          {
            _zRow = row;
            _zCol = col;
            break;
          }
        }
      }

      // exchange with upper block
      if (_zRow > 0)
      {
        createAndAppendNewBoard(blocks_, (_zRow - 1), _zCol, _zRow, _zCol);
      }
      // exchange with lower block
      if(_zRow < (_dimension - 1))
      {
        createAndAppendNewBoard(blocks_, (_zRow + 1), _zCol, _zRow, _zCol);
      }
      // left block
      if(_zCol > 0)
      {
        createAndAppendNewBoard(blocks_, _zRow, (_zCol - 1), _zRow, _zCol);
      }
      // right block
      if (_zCol < (_dimension - 1))
      {
        createAndAppendNewBoard(blocks_, _zRow, (_zCol + 1), _zRow, _zCol);
      }
    }

    private void createAndAppendNewBoard(int[][] blocks_, int row1_, int col1_, int row2_, int col2_)
    {
      int [][] blocks = deepCopy2DArray(blocks_);
      exchange(blocks, row1_, col1_, row2_, col2_);
      _boardList.add(new Board(blocks));
    }

    @Override
    public Iterator<Board> iterator()
    {
      return _boardList.iterator();
    }
  }

  // construct a board from an n-by-n array of blocks
  // (where blocks[i][j] = block in row i, column j)

  private int abs(int num_)
  {
    return ((num_ < 0) ? (-1 * num_) : num_);
  }

  public Board(int[][] blocks_)
  {
    if (blocks_ == null)
    {
      throw new java.lang.IllegalArgumentException("null argument");
    }

    _blocks = deepCopy2DArray(blocks_);

    // determining _hamming and _manhattan during construction
    for (int row = 0; row < dimension(); ++row)
    {
      for (int col = 0; col < dimension(); ++col)
      {
        int num = _blocks[row][col];
        if (num == 0) continue;

        int zerothBasedCorrectColumn = (num - 1) % dimension();
        int zerothBasedCorrectRow    = (num - 1) / dimension();
        _manhattan += (abs(row - zerothBasedCorrectRow) + abs(col - zerothBasedCorrectColumn));

        int actualPosition =  row * dimension() + col + 1;
        _hamming += ((actualPosition == num) ? 0 : 1);
      }
    }
  }

  // board dimension n
  public int dimension() { return _blocks.length; }

  // number of blocks out of place
  public int hamming() { return _hamming; }

  // sum of Manhattan distances between blocks and goal
  public int manhattan() { return _manhattan; }

  // is this board the goal board?
  public boolean isGoal() { return ((_hamming == 0) && (_manhattan == 0)); }

  private static void exchange(int[][] arr_, int row1_, int col1_, int row2_, int col2_)
  {
    int tmp = arr_[row1_][col1_];
    arr_[row1_][col1_] = arr_[row2_][col2_];
    arr_[row2_][col2_] = tmp;
  }

  // a board that is obtained by exchanging any pair of blocks
  public Board twin()
  {
    int [][] arr = deepCopy2DArray(this._blocks);
    if ((arr[0][0] != 0) && (arr[0][1] != 0))
    {
      exchange(arr, 0,0,0,1);
    }
    else if ((arr[0][0] != 0) && (arr[1][0] != 0))
    {
      exchange(arr, 0,0,1,0);
    }
    else // none of [0][1] and [1][0] are zeros
    {
      exchange(arr, 0,1,1,0);
    }

    return new Board(arr);
  }

  // does this board equal y?
  @Override
  public boolean equals(Object y)
  {
    if (y == null) return false;

    if (!(y instanceof Board)) return false;
    Board that = (Board)y;

    if ((that.dimension() != this.dimension()) || (that.manhattan() != this.manhattan()) ||
      (that.hamming() != this.hamming()))
    {
      return false;
    }

    for (int row = 0; row < dimension(); ++row)
    {
      for (int col = 0; col < dimension(); ++col)
      {
        if (that._blocks[row][col] != this._blocks[row][col])
        {
          return false;
        }
      }
    }
    return true;
  }

  // all neighboring boards
  public Iterable<Board> neighbors()
  {
    return new BoardNeighbourIterator(this._blocks);
  }

  // string representation of this board (in the output format specified below)
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(this.dimension() + "\n");

    for (int row = 0; row < dimension(); ++row)
    {
      for (int col = 0; col < dimension(); ++col)
      {
        sb.append(" " + _blocks[row][col]);
      }
      sb.append("\n");
    }

    return sb.toString();
  }

  // unit tests (not graded)
  public static void main(String[] args)
  {
  }
}