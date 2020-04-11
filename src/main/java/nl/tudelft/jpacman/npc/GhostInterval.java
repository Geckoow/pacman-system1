package nl.tudelft.jpacman.npc;

import java.util.Random;

public class GhostInterval {
    /**
     * The base move interval of the ghost.
     */
    private final int moveInterval;
    /**
     * The random variation added to the {@link #moveInterval}.
     */
    private final int intervalVariation;

    public GhostInterval(int intervalVariation, int moveInterval) {
        this.intervalVariation = intervalVariation;
        this.moveInterval = moveInterval;
    }

    /**
     * The time that should be taken between moves.
     *
     * @return The suggested delay between moves in milliseconds.
     */
    public long getInterval() {
        return this.moveInterval + new Random().nextInt(this.intervalVariation);
    }
}
