package nl.tudelft.jpacman.level;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.sprite.AnimatedSprite;
import nl.tudelft.jpacman.sprite.Sprite;

/**
 * A player operated unit in our game.
 *
 * @author Jeroen Roosen 
 */
public class Player extends Unit {

    /**
     * The amount of points accumulated by this player.
     */
    private int score;

    private int kill;

    private int powerPillEaten;

    /**
     * The variation in intervals, this makes the ghosts look more dynamic and
     * less predictable.
     */
    private static final int INTERVAL_VARIATION = 50;

    /**
     * The base movement interval.
     */
    private static final int MOVE_INTERVAL = 250;

    /**
     * The animations for every direction.
     */
    private final Map<Direction, Sprite> sprites;

    /**
     * The animation that is to be played when Pac-Man dies.
     */
    private final AnimatedSprite deathSprite;

    /**
     * <code>true</code> iff this player is alive.
     */
    private boolean alive;

    private final int POWERPILL_NUMBER = 4;

    private ScheduledExecutorService powerPillEat;

    /**
     * Creates a new player with a score of 0 points.
     *
     * @param spriteMap
     *            A map containing a sprite for this player for every direction.
     * @param deathAnimation
     *            The sprite to be shown when this player dies.
     */
    protected Player(Map<Direction, Sprite> spriteMap, AnimatedSprite deathAnimation) {
        this.score = 0;
        this.kill = 0;
        this.powerPillEaten = POWERPILL_NUMBER;
        this.powerPillEat = null;
        this.alive = true;
        this.sprites = spriteMap;
        this.deathSprite = deathAnimation;
        deathSprite.setAnimating(false);
    }

    /**
     * Returns whether this player is alive or not.
     *
     * @return <code>true</code> iff the player is alive.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Sets whether this player is alive or not.
     *
     * @param isAlive
     *            <code>true</code> iff this player is alive.
     */
    public void setAlive(boolean isAlive) {
        if (isAlive) {
            deathSprite.setAnimating(false);
        }
        if (!isAlive) {
            deathSprite.restart();
        }
        this.alive = isAlive;
    }

    /**
     * Returns the amount of points accumulated by this player.
     *
     * @return The amount of points accumulated by this player.
     */
    public int getScore() {
        return score;
    }

    public int getKill() {
        return kill;
    }

    public int getPowerPillEaten() {
        return powerPillEaten;
    }

    public void setKill(int kill) {
        this.kill = kill;
    }

    @Override
    public Sprite getSprite() {
        if (isAlive()) {
            return sprites.get(getDirection());
        }
        return deathSprite;
    }

    /**
     * The time that should be taken between moves.
     *
     * @return The suggested delay between moves in milliseconds.
     */
    public long getInterval() {
        return MOVE_INTERVAL + new Random().nextInt(INTERVAL_VARIATION);
    }

    /**
     * Adds points to the score of this player.
     *
     * @param points
     *            The amount of points to add to the points this player already
     *            has.
     */
    public void addPoints(int points) {
        score += points;
    }

    public void collide(Unit collidedOn, PlayerCollisions playerCollisions) {
        playerCollisions.versus.playerColliding(this, collidedOn);
    }

    int getTimer() {
        int timer = 7;
        if(getPowerPillEaten() < 3)
            timer = 5;
        return timer;
    }

    /**
     * Actual case of player bumping into ghost or vice versa.
     *
     * @param ghost The ghost involved in the collision.
     * @param playerCollisionsEffects
     */
    public void playerVersusGhost(Ghost ghost, PlayerCollisionsEffects playerCollisionsEffects) {
        if(ghost.isScared() == false)
            setAlive(false);
        else{
            int kill = getKill();
            setKill(kill+1);
            addPoints((int) (Math.pow(2, getKill())*100));
            ghost.leaveSquare();

            Square s = playerCollisionsEffects.getBoard().getGhostRespawn();
            ghost.occupy(s);
            playerCollisionsEffects.getBoard().getBoard()[playerCollisionsEffects.getBoard().getHeight()/2][playerCollisionsEffects.getBoard().getWidth()/2] = s;
            ghost.reverseScared();
            //ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

            //executorService.schedule(new RespawnGhostTask(executorService, ghost), 5, TimeUnit.SECONDS);
        }
    }

    /**
     * Actual case of player consuming a powerpill.
     *
     * @param powerPill The powerpill involved in the collision.
     * @param playerCollisionsEffects
     */
    public void playerVersusPowerPill(PowerPill powerPill, PlayerCollisionsEffects playerCollisionsEffects){
        powerPill.leaveSquare();
        addPoints(powerPill.getValue());
        if(powerPillEat != null)
            powerPillEat.shutdownNow();
        for(int i = 0; i < playerCollisionsEffects.getGhosts().size(); i++){
            playerCollisionsEffects.getGhosts().get(i).scared();
        }
        int timer = getTimer();
        powerPillEat = Executors.newSingleThreadScheduledExecutor();

        powerPillEat.schedule(new PowerPillTask(powerPillEat, this, playerCollisionsEffects.getGhosts()), timer, TimeUnit.SECONDS);

    }
    /**
     * A task that moves an NPC and reschedules itself after it finished.
     *
     * @author Jeroen Roosen
     */
    private final class PowerPillTask implements Runnable {

        /**
         * The service executing the task.
         */
        private final ScheduledExecutorService service;

        /**
         * The player to move.
         */
        private final List<Ghost> ghosts;

        private final Player player;

        /**
         * Creates a new task.
         *
         * @param service
         *            The service that executes the task.
         * @param ghosts
         *            The NPC to move.
         */
        PowerPillTask(ScheduledExecutorService service, Player player, List<Ghost> ghosts) {
            this.service = service;
            this.player = player;
            this.ghosts = ghosts;
        }

        @Override
        public void run() {
            for(int i = 0; i < ghosts.size(); i++){
                ghosts.get(i).reverseScared();
            }
            player.setKill(0);
            powerPillEat = null ;
        }
    }
}
