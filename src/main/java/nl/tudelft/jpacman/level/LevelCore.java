package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Level.LevelObserver;
import nl.tudelft.jpacman.npc.Ghost;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

public class LevelCore {
    private final LevelCoreFunctions levelFeatures;
    /**
     * The objects observing this level.
     */
    private final Set<LevelObserver> observers;
    private final Level level;

    public LevelCore(Level level, List<Square> startPositions, CollisionMap collisionMap) {
        this.levelFeatures = new LevelCoreFunctions(startPositions, collisionMap);
        this.level = level;
        this.observers = new HashSet<>();
    }

    public Map<Ghost, ScheduledExecutorService> getNpcs() {
        return levelFeatures.getNpcs();
    }

    public boolean getInProgress() {
        return levelFeatures.getInProgress();
    }

    public void setInProgress(boolean inProgress) {
        levelFeatures.setInProgress(inProgress);
    }

    public void setStartSquareIndex(int startSquareIndex) {
        levelFeatures.setStartSquareIndex(startSquareIndex);
    }

    public Set<LevelObserver> getObservers() {
        return observers;
    }

    public void setNpcMove(NPCMove npcMove) {
        levelFeatures.setNpcMove(npcMove);
    }

    public void setPlayerMove(PlayerMove playerMove) {
        levelFeatures.setPlayerMove(playerMove);
    }

    /**
     * Moves the unit into the given direction if possible and handles all
     * collisions.
     *
     * @param unit      The unit to move.
     * @param direction The direction to move the unit in.
     */
    public void move(Unit unit, Direction direction) {
        levelFeatures.move(unit, direction, this);
    }

    /**
     * Starts or resumes this level, allowing movement and (re)starting the
     * NPCs.
     */
    public void start() {
        levelFeatures.start(this);
    }

    /**
     * Stops or pauses this level, no longer allowing any movement on the board
     * and stopping all NPCs.
     */
    public void stop() {
        levelFeatures.stop();
    }

    /**
     * Registers a player on this level, assigning him to a starting position. A
     * player can only be registered once, registering a player again will have
     * no effect.
     *
     * @param player The player to register.
     */
    public void registerPlayer(Player player) {
        levelFeatures.registerPlayer(player);
    }

    /**
     * Returns <code>true</code> iff at least one of the players in this level
     * is alive.
     *
     * @return <code>true</code> if at least one of the registered players is
     * alive.
     */
    public boolean isAnyPlayerAlive() {
        return levelFeatures.isAnyPlayerAlive();
    }

    /**
     * Removes an observer if it was listed.
     *
     * @param observer The observer to be removed.
     */
    public void removeObserver(LevelObserver observer) {
        observers.remove(observer);
    }

    /**
     * Updates the observers about the state of this level.
     */
    public void updateObservers() {
        if (!levelFeatures.isAnyPlayerAlive()) {
            for (LevelObserver observer : observers) {
                observer.levelLost();
            }
        }
        if (level.remainingPellets() == 0) {
            for (LevelObserver observer : observers) {
                observer.levelWon();
            }
        }
    }
}
