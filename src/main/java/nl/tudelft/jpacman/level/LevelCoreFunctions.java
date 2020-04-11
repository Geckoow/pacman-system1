package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.npc.Ghost;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

public class LevelCoreFunctions {
    private LevelPlayerInfo levelPlayerInfo;
    private LevelRunning levelRunning;
    /**
     * The lock that ensures moves are executed sequential.
     */
    private final Object moveLock = new Object();
    /**
     * The table of possible collisions between units.
     */
    private final CollisionMap collisions;

    public LevelCoreFunctions(List<Square> startPositions, CollisionMap collisionMap) {
        this.levelPlayerInfo = new LevelPlayerInfo(startPositions);
        this.levelRunning = new LevelRunning();
        this.collisions = collisionMap;
    }

    public Map<Ghost, ScheduledExecutorService> getNpcs() {
        return levelRunning.getNpcs();
    }

    public boolean getInProgress() {
        return levelRunning.getInProgress();
    }

    public void setInProgress(boolean inProgress) {
        levelRunning.setInProgress(inProgress);
    }

    public void setStartSquareIndex(int startSquareIndex) {
        levelPlayerInfo.setStartSquareIndex(startSquareIndex);
    }

    public void setNpcMove(NPCMove npcMove) {
        levelRunning.setNpcMove(npcMove);
    }

    public void setPlayerMove(PlayerMove playerMove) {
        levelRunning.setPlayerMove(playerMove);
    }

    /**
     * Moves the unit into the given direction if possible and handles all
     * collisions.
     *
     * @param unit      The unit to move.
     * @param direction The direction to move the unit in.
     */
    public void move(Unit unit, Direction direction, LevelCore levelFunctions) {
        assert unit != null;
        assert direction != null;
        assert unit.hasSquare();

        if (!levelRunning.getInProgress()) {
            return;
        }

        synchronized (moveLock) {
            unit.setDirection(direction);
            Square location = unit.getSquare();
            Square destination = location.getSquareAt(direction);

            if (destination.isAccessibleTo(unit)) {
                List<Unit> occupants = destination.getOccupants();
                unit.occupy(destination);
                for (Unit occupant : occupants) {
                    collisions.collide(unit, occupant);
                }
            }
            levelFunctions.updateObservers();
        }
    }

    /**
     * Starts or resumes this level, allowing movement and (re)starting the
     * NPCs.
     */
    public void start(LevelCore levelFunctions) {
        levelRunning.start(levelFunctions, this.levelPlayerInfo.getPlayers());
    }

    /**
     * Stops or pauses this level, no longer allowing any movement on the board
     * and stopping all NPCs.
     */
    public void stop() {
        levelRunning.stop();
    }

    /**
     * Registers a player on this level, assigning him to a starting position. A
     * player can only be registered once, registering a player again will have
     * no effect.
     *
     * @param player The player to register.
     */
    public void registerPlayer(Player player) {
        levelPlayerInfo.registerPlayer(player);
    }

    /**
     * Returns <code>true</code> iff at least one of the players in this level
     * is alive.
     *
     * @return <code>true</code> if at least one of the registered players is
     * alive.
     */
    public boolean isAnyPlayerAlive() {
        return levelPlayerInfo.isAnyPlayerAlive();
    }
}
