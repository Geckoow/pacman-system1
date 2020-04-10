package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ai.RandomAi;
import nl.tudelft.jpacman.npc.ai.ScaredAi;
import nl.tudelft.jpacman.npc.ghost.Blinky;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static jdk.nashorn.internal.objects.Global.print;

/**
 * A simple implementation of a collision map for the JPacman player.
 * <p>
 * It uses a number of instanceof checks to implement the multiple dispatch for the 
 * collisionmap. For more realistic collision maps, this approach will not scale,
 * and the recommended approach is to use a {@link CollisionInteractionMap}.
 *
 * @author Arie van Deursen, 2014
 *
 */

public class PlayerCollisions implements CollisionMap {

    private final List<Ghost> ghosts;
    private final Board board;

    public PlayerCollisions(List<Ghost> ghosts, Board board){
        this.ghosts = ghosts;
        this.board = board;
    }
    @Override
    public void collide(Unit mover, Unit collidedOn) {
        if (mover instanceof Player) {
            playerColliding((Player) mover, collidedOn);
        }
        else if (mover instanceof Ghost) {
            ghostColliding((Ghost) mover, collidedOn);
        }
        else if (mover instanceof Pellet) {
            pelletColliding((Pellet) mover, collidedOn);
        }
        else if(mover instanceof PowerPill){
            powerPillColliding((PowerPill) mover, collidedOn);
        }
    }

    private void playerColliding(Player player, Unit collidedOn) {
        if (collidedOn instanceof Ghost) {
            playerVersusGhost(player, (Ghost) collidedOn);
        }
        if (collidedOn instanceof Pellet) {
            playerVersusPellet(player, (Pellet) collidedOn);
        }
        if(collidedOn instanceof PowerPill){
            playerVersusPowerPill(player, (PowerPill) collidedOn);
        }
    }

    private void ghostColliding(Ghost ghost, Unit collidedOn) {
        if (collidedOn instanceof Player) {
            playerVersusGhost((Player) collidedOn, ghost);
        }
    }

    private void pelletColliding(Pellet pellet, Unit collidedOn) {
        if (collidedOn instanceof Player) {
            playerVersusPellet((Player) collidedOn, pellet);
        }
    }

    private void powerPillColliding(PowerPill powerPill, Unit collidedOn) {
        if(collidedOn instanceof Player){
            playerVersusPowerPill((Player) collidedOn, powerPill);
        }
    }

    /**
     * Actual case of player bumping into ghost or vice versa.
     *
     * @param player The player involved in the collision.
     * @param ghost The ghost involved in the collision.
     */
    public void playerVersusGhost(Player player, Ghost ghost) {
        if(ghost.isScared() == false)
            player.setAlive(false);
        else{
            int kill = player.getKill();
            player.setKill(kill+1);
            player.addPoints((int) (Math.pow(2, player.getKill())*100));
            ghost.leaveSquare();

            Square s = board.getGhostRespawn();
            ghost.occupy(s);
            board.getBoard()[board.getHeight()/2][board.getWidth()/2] = s;
            ghost.reverseScared();
            //ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

            //executorService.schedule(new RespawnGhostTask(executorService, ghost), 5, TimeUnit.SECONDS);
        }
    }

    /**
     * Actual case of player consuming a pellet.
     *
     * @param player The player involved in the collision.
     * @param pellet The pellet involved in the collision.
     */
    public void playerVersusPellet(Player player, Pellet pellet) {
        pellet.leaveSquare();
        player.addPoints(pellet.getValue());
    }
    /**
     * Actual case of player consuming a powerpill.
     *
     * @param player The player involved in the collision.
     * @param powerPill The powerpill involved in the collision.
     */
    public void playerVersusPowerPill(Player player, PowerPill powerPill){
        powerPill.leaveSquare();
        player.addPoints(powerPill.getValue());
        for(int i = 0; i < ghosts.size(); i++){
            ghosts.get(i).scared();
        }
        int timer = 7;
        if(player.getPowerPillEaten() < 3)
            timer = 5;
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.schedule(new PowerPillTask(executorService, player, ghosts), timer, TimeUnit.SECONDS);

    }

    /**
     * A task that moves an NPC and reschedules itself after it finished.
     *
     * @author Jeroen Roosen
     */
    private final class RespawnGhostTask implements Runnable {

        /**
         * The service executing the task.
         */
        private final ScheduledExecutorService service;

        /**
         * The player to move.
         */
        private final Ghost ghost;

        /**
         * Creates a new task.
         *
         * @param service
         *            The service that executes the task.
         * @param ghost
         *            The NPC to move.
         */
        RespawnGhostTask(ScheduledExecutorService service, Ghost ghost) {
            this.service = service;
            this.ghost = ghost;
        }

        @Override
        public void run() {
            Square s = board.getGhostRespawn();
            ghost.occupy(s);
            board.getBoard()[board.getHeight()/2][board.getWidth()/2] = s;
        }
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
        }
    }
}
