package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.npc.Ghost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

public class LevelRunning {
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
    private NPCMove npcMove;
    private PlayerMove playerMove;

    public LevelRunning() {
        this.npcs = new HashMap<>();
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

    public void setNpcMove(NPCMove npcMove) {
        this.npcMove = npcMove;
    }

    public void setPlayerMove(PlayerMove playerMove) {
        this.playerMove = playerMove;
    }

    /**
     * Starts or resumes this level, allowing movement and (re)starting the
     * NPCs.
     */
    public void start(LevelCore levelFunctions, List<Player> thisPlayers) {
        synchronized (startStopLock) {
            if (inProgress) {
                return;
            }
            npcMove.startNPCs(npcs);
            playerMove.startPlayer(thisPlayers, plays);
            inProgress = true;
            levelFunctions.updateObservers();
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
}
