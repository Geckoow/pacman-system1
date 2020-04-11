package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Level.LevelObserver;
import nl.tudelft.jpacman.npc.Ghost;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;

public class LevelFunctions {
    /**
     * The lock that ensures moves are executed sequential.
     */
    private final Object moveLock = new Object();
    /**
     * The lock that ensures starting and stopping can't interfere with each
     * other.
     */
    private final Object startStopLock = new Object();
    /**
     * The NPCs of this level and, if they are running, their schedules.
     */
    private final Map<Ghost, ScheduledExecutorService> npcs;
    /**
     * The NPCs of this level and, if they are running, their schedules.
     */
    private final Map<Player, ScheduledExecutorService> plays;
    /**
     * <code>true</code> iff this level is currently in progress, i.e. players
     * and NPCs can move.
     */
    private boolean inProgress;
    /**
     * The squares from which players can start this game.
     */
    private final List<Square> startSquares;
    /**
     * The start current selected starting square.
     */
    private int startSquareIndex;
    /**
     * The players on this level.
     */
    private final List<Player> players;
    /**
     * The table of possible collisions between units.
     */
    private final CollisionMap collisions;
    /**
     * The objects observing this level.
     */
    private final Set<LevelObserver> observers;
    private NPCMove npcMove;
    private PlayerMove playerMove;
    private Level level;

    public LevelFunctions(Level level, List<Square> startPositions, CollisionMap collisionMap) {
        this.level = level;
        this.npcs = new HashMap<>();
        this.startSquares = startPositions;
        this.players = new ArrayList<>();
        this.collisions = collisionMap;
        this.observers = new HashSet<>();
        this.plays = new HashMap<>();
    }

    public Map<Ghost, ScheduledExecutorService> getNpcs() {
        return npcs;
    }

    public boolean getInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public void setStartSquareIndex(int startSquareIndex) {
        this.startSquareIndex = startSquareIndex;
    }

    public Set<LevelObserver> getObservers() {
        return observers;
    }

    public void setNpcMove(NPCMove npcMove) {
        this.npcMove = npcMove;
    }

    public void setPlayerMove(PlayerMove playerMove) {
        this.playerMove = playerMove;
    }

    /**
     * Moves the unit into the given direction if possible and handles all
     * collisions.
     *
     * @param unit      The unit to move.
     * @param direction The direction to move the unit in.
     */
    public void move(Unit unit, Direction direction) {
        assert unit != null;
        assert direction != null;
        assert unit.hasSquare();

        if (!inProgress) {
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
            updateObservers();
        }
    }

    /**
     * Starts or resumes this level, allowing movement and (re)starting the
     * NPCs.
     */
    public void start() {
        synchronized (startStopLock) {
            if (inProgress) {
                return;
            }
            npcMove.startNPCs(npcs);
            playerMove.startPlayer(players, plays);
            inProgress = true;
            updateObservers();
        }
    }

    /**
     * Stops or pauses this level, no longer allowing any movement on the board
     * and stopping all NPCs.
     */
    public void stop() {
        synchronized (startStopLock) {
            if (!inProgress) {
                return;
            }
            npcMove.stopNPCs(npcs);
            playerMove.stopPlayer(plays);
            inProgress = false;
        }
    }

    /**
     * Registers a player on this level, assigning him to a starting position. A
     * player can only be registered once, registering a player again will have
     * no effect.
     *
     * @param player The player to register.
     */
    public void registerPlayer(Player player) {
        assert player != null;
        assert !startSquares.isEmpty();

        if (players.contains(player)) {
            return;
        }
        players.add(player);
        Square square = startSquares.get(startSquareIndex);
        player.occupy(square);
        startSquareIndex++;
        startSquareIndex %= startSquares.size();
    }

    /**
     * Returns <code>true</code> iff at least one of the players in this level
     * is alive.
     *
     * @return <code>true</code> if at least one of the registered players is
     * alive.
     */
    public boolean isAnyPlayerAlive() {
        for (Player player : players) {
            if (player.isAlive()) {
                return true;
            }
        }
        return false;
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
        if (!isAnyPlayerAlive()) {
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
