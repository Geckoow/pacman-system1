package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.npc.Ghost;

import java.util.List;

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
    final PlayerCollisionsEffects versus;

    public PlayerCollisions(List<Ghost> ghosts, Board board){
        this.ghosts = ghosts;
        this.board = board;
        this.versus = new PlayerCollisionsEffects(ghosts, board);
    }
    @Override
    public void collide(Unit mover, Unit collidedOn) {
        mover.collide(collidedOn, this);
    }

    public void ghostColliding(Ghost ghost, Unit collidedOn) {
        if (collidedOn instanceof Player) {
            ((Player) collidedOn).playerVersusGhost(ghost, versus);
        }
    }

    public void powerPillColliding(PowerPill powerPill, Unit collidedOn) {
        if(collidedOn instanceof Player){
            ((Player) collidedOn).playerVersusPowerPill(powerPill, versus);
        }
    }


}
