package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ai.RandomAi;
import nl.tudelft.jpacman.npc.ai.ScaredAi;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
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
    private final GhostFactory ghostFact;
    private Timer timer;

    public PlayerCollisions(List<Ghost> ghosts, GhostFactory ghostFactory){
        this.ghosts = ghosts;
        this.ghostFact = ghostFactory;
        this.timer = new Timer();
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
            ghosts.get(i).setScared(true);
            ghosts.get(i).addAi(new ScaredAi(ghosts.get(i)));
            ghosts.get(i).switchSprite();
        }
    }
}
