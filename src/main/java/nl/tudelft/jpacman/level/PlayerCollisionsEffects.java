package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.npc.Ghost;

import java.util.List;

public class PlayerCollisionsEffects {

    private final List<Ghost> ghosts;
    private final Board board;

    public PlayerCollisionsEffects(List<Ghost> ghosts, Board board){
        this.ghosts = ghosts;
        this.board = board;
    }

    public List<Ghost> getGhosts() {
        return ghosts;
    }

    public Board getBoard() {
        return board;
    }

    public void playerColliding(Player player, Unit collidedOn) {
        if (collidedOn instanceof Ghost) {
            player.playerVersusGhost((Ghost) collidedOn, this);
        }
        if (collidedOn instanceof Pellet) {
            ((Pellet) collidedOn).playerVersusPellet(player);
        }
        if(collidedOn instanceof PowerPill){
            player.playerVersusPowerPill((PowerPill) collidedOn, this);
        }
    }
}
