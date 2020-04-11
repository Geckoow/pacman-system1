package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.Ghost;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlayerCollisionsEffects {

    private final List<Ghost> ghosts;
    private final Board board;

    public PlayerCollisionsEffects(List<Ghost> ghosts, Board board){
        this.ghosts = ghosts;
        this.board = board;
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
        int timer = getTimer(player);
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.schedule(new PowerPillTask(executorService, player, ghosts), timer, TimeUnit.SECONDS);

    }

    private int getTimer(Player player) {
        int timer = 7;
        if(player.getPowerPillEaten() < 3)
            timer = 5;
        return timer;
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
