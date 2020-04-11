package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.npc.Ghost;

import java.util.List;

/**
 * A level of Pac-Man. A level consists of the board with the players and the
 * AIs on it.
 *
 * @author Jeroen Roosen
 */
@SuppressWarnings("PMD.TooManyMethods")
public class Level {
    private LevelFunctions levelProduct;

    /**
     * The board of this level.
     */
    private final Board board;

    /**
     * Creates a new level for the board.
     *
     * @param board          The board for the level.
     * @param ghosts         The ghosts on the board.
     * @param startPositions The squares on which players start on this board.
     * @param collisionMap   The collection of collisions that should be handled.
     */
    public Level(Board board, List<Ghost> ghosts, List<Square> startPositions,
                 CollisionMap collisionMap) {
        this.levelProduct = new LevelFunctions(this, startPositions, collisionMap);
        assert board != null;
        assert ghosts != null;
        assert startPositions != null;

        this.board = board;
        levelProduct.setInProgress(false);
        for (Ghost ghost : ghosts) {
            levelProduct.getNpcs().put(ghost, null);
        }
        levelProduct.setStartSquareIndex(0);

        levelProduct.setNpcMove(new NPCMove(this));
        levelProduct.setPlayerMove(new PlayerMove(this));
    }

    /**
     * Adds an observer that will be notified when the level is won or lost.
     *
     * @param observer The observer that will be notified.
     */
    public void addObserver(LevelObserver observer) {
        levelProduct.getObservers().add(observer);
    }

    /**
     * Removes an observer if it was listed.
     *
     * @param observer The observer to be removed.
     */
    public void removeObserver(LevelObserver observer) {
        levelProduct.removeObserver(observer);
    }

    /**
     * Registers a player on this level, assigning him to a starting position. A
     * player can only be registered once, registering a player again will have
     * no effect.
     *
     * @param player The player to register.
     */
    public void registerPlayer(Player player) {
        levelProduct.registerPlayer(player);
    }

    /**
     * Returns the board of this level.
     *
     * @return The board of this level.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Moves the unit into the given direction if possible and handles all
     * collisions.
     *
     * @param unit      The unit to move.
     * @param direction The direction to move the unit in.
     */
    public void move(Unit unit, Direction direction) {
        levelProduct.move(unit, direction);
    }

    /**
     * Starts or resumes this level, allowing movement and (re)starting the
     * NPCs.
     */
    public void start() {
        levelProduct.start();
    }

    /**
     * Stops or pauses this level, no longer allowing any movement on the board
     * and stopping all NPCs.
     */
    public void stop() {
        levelProduct.stop();
    }

    /**
     * Returns whether this level is in progress, i.e. whether moves can be made
     * on the board.
     *
     * @return <code>true</code> iff this level is in progress.
     */
    public boolean isInProgress() {
        return levelProduct.getInProgress();
    }

    /**
     * Returns <code>true</code> iff at least one of the players in this level
     * is alive.
     *
     * @return <code>true</code> if at least one of the registered players is
     * alive.
     */
    public boolean isAnyPlayerAlive() {
        return levelProduct.isAnyPlayerAlive();
    }

    /**
     * Counts the pellets remaining on the board.
     *
     * @return The amount of pellets remaining on the board.
     */
    public int remainingPellets() {
        Board board = getBoard();
        int pellets = 0;
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                for (Unit unit : board.squareAt(x, y).getOccupants()) {
                    if (unit instanceof Pellet) {
                        pellets++;
                    }
                }
            }
        }
        assert pellets >= 0;
        return pellets;
    }

    /**
     * An observer that will be notified when the level is won or lost.
     *
     * @author Jeroen Roosen
     */
    public interface LevelObserver {

        /**
         * The level has been won. Typically the level should be stopped when
         * this event is received.
         */
        void levelWon();

        /**
         * The level has been lost. Typically the level should be stopped when
         * this event is received.
         */
        void levelLost();
    }
}
