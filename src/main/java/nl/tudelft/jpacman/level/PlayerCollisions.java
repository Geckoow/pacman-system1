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
    private PlayerCollisionsEffects versus;

    public PlayerCollisions(List<Ghost> ghosts, Board board){
        this.ghosts = ghosts;
        this.board = board;
        this.versus = new PlayerCollisionsEffects(ghosts, board);
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
            versus.playerVersusGhost(player, (Ghost) collidedOn);
        }
        if (collidedOn instanceof Pellet) {
            versus.playerVersusPellet(player, (Pellet) collidedOn);
        }
        if(collidedOn instanceof PowerPill){
            versus.playerVersusPowerPill(player, (PowerPill) collidedOn);
        }
    }

    private void ghostColliding(Ghost ghost, Unit collidedOn) {
        if (collidedOn instanceof Player) {
            versus.playerVersusGhost((Player) collidedOn, ghost);
        }
    }

    private void pelletColliding(Pellet pellet, Unit collidedOn) {
        if (collidedOn instanceof Player) {
            versus.playerVersusPellet((Player) collidedOn, pellet);
        }
    }

    private void powerPillColliding(PowerPill powerPill, Unit collidedOn) {
        if(collidedOn instanceof Player){
            versus.playerVersusPowerPill((Player) collidedOn, powerPill);
        }
    }


}
