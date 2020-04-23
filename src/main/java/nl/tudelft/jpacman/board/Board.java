package nl.tudelft.jpacman.board;


import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.Ghost;

import java.util.Random;

/**
 * A top-down view of a matrix of {@link Square}s.
 *
 * @author Jeroen Roosen 
 */
public class Board {

    /**
     * The grid of squares with board[x][y] being the square at column x, row y.
     */
    private final Square[][] board;

    private int distanceOfWall = 4;

    /**
     * Creates a new board.
     *
     * @param grid
     *            The grid of squares with grid[x][y] being the square at column
     *            x, row y.
     */
    @SuppressWarnings("PMD.ArrayIsStoredDirectly")
    Board(Square[][] grid) {
        assert grid != null;
        this.board = grid;
        assert invariant() : "Initial grid cannot contain null squares";
    }

    /**
     * Whatever happens, the squares on the board can't be null.
     * @return false if any square on the board is null.
     */
    protected final boolean invariant() {
        for (Square[] row : board) {
            for (Square square : row) {
                if (square == null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * draw a int
     * @param min min int
     * @param max max int
     * @return a random int
     */
    public int randomPlace(int min, int max){
        Random rand = new Random();
        return rand.nextInt((max-min)+1)+min;
    }

    /**
     *return a square at a distance safe of ghost.
     */
    public Square findSafeSquare(Unit n){
        Square safe = board[randomPlace(0,getWidth()-1)][randomPlace(0,getHeight()-1)];
        while(!noGhost(safe, n, distanceOfWall) || !safe.isAccessibleTo(n) || !accessibleCoins(safe, n)){
            safe = board[randomPlace(0,getWidth()-1)][randomPlace(0,getHeight()-1)];
        }
        return safe;
    }

    /**
     * recursive search to find out if there are ghost.
     * @param s square where to look.
     * @param num how many square to look.
     * @return true if there is no ghost in < num square. false otherwise.
     */
    public boolean noGhost(Square s, Unit n, int num){
        if(s != null){
            if(!s.isAccessibleTo(n)){
                return true;
            }else{
                for(Unit occupant: s.getOccupants()){
                    if(occupant instanceof Ghost){
                        return false;
                    }
                }
                if(num == 0){
                    return true;
                }else{
                    return(noGhost(s.getSquareAt(Direction.EAST), n,num-1)&&
                    noGhost(s.getSquareAt(Direction.WEST), n,num-1)&&
                        noGhost(s.getSquareAt(Direction.NORTH), n,num-1)&&
                        noGhost(s.getSquareAt(Direction.SOUTH), n,num-1));
                }
            }
        }else{
            return true;
        }
    }

    /**
     * Find if it's possible to access to a coin from the square S
     * @param s square
     * @param n Unit
     * @return true if it's possible to fin a pellet, false otherwise.
     */
    public boolean accessibleCoins(Square s, Unit n){
        if(s != null){
            if(s.isAccessibleTo(n)) {
                for (Unit elem : s.getOccupants()) {
                    if (elem instanceof Pellet) {
                        return true;
                    }
                }
                return (accessibleCoins(s.getSquareAt(Direction.EAST),n) &&
                    accessibleCoins(s.getSquareAt(Direction.WEST),n) &&
                    accessibleCoins(s.getSquareAt(Direction.SOUTH),n) &&
                    accessibleCoins(s.getSquareAt(Direction.NORTH),n));
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * Returns the number of columns.
     *
     * @return The width of this board.
     */
    public int getWidth() {
        return board.length;
    }

    /**
     * Returns the number of rows.
     *
     * @return The height of this board.
     */
    public int getHeight() {
        return board[0].length;
    }

    /**
     * Returns the square at the given <code>x,y</code> position.
     *
     * Precondition: The <code>(x, y)</code> coordinates are within the
     * width and height of the board.
     *
     * @param x
     *            The <code>x</code> position (column) of the requested square.
     * @param y
     *            The <code>y</code> position (row) of the requested square.
     * @return The square at the given <code>x,y</code> position (never null).
     */
    public Square squareAt(int x, int y) {
        assert withinBorders(x, y);
        Square result = board[x][y];
        assert result != null : "Follows from invariant.";
        return result;
    }

    /**
     * Determines whether the given <code>x,y</code> position is on this board.
     *
     * @param x
     *            The <code>x</code> position (row) to test.
     * @param y
     *            The <code>y</code> position (column) to test.
     * @return <code>true</code> iff the position is on this board.
     */
    public boolean withinBorders(int x, int y) {
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
    }

    public Square[][] getBoard() {
        return board;
    }

}
