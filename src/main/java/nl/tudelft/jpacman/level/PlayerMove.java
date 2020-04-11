package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Direction;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlayerMove {
    private Level level;

    public PlayerMove(Level level){
        this.level = level;
    }


    /**
     * Starts all NPC movement scheduling.
     */
    protected void startPlayer(List<Player> players, Map<Player, ScheduledExecutorService> plays) {
        for (final Player player : players) {
            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

            service.schedule(new PlayerMoveTask(service, player),
                player.getInterval() / 2, TimeUnit.MILLISECONDS);

            plays.put(player, service);
        }
    }

    /**
     * Stops all NPC movement scheduling and interrupts any movements being
     * executed.
     */
    protected void stopPlayer(Map<Player, ScheduledExecutorService> plays) {
        for (Map.Entry<Player, ScheduledExecutorService> entry : plays.entrySet()) {
            ScheduledExecutorService schedule = entry.getValue();
            assert schedule != null;
            schedule.shutdownNow();
        }
    }
    /**
     * A task that moves an NPC and reschedules itself after it finished.
     *
     * @author Jeroen Roosen
     */
    private final class PlayerMoveTask implements Runnable {

        /**
         * The service executing the task.
         */
        private final ScheduledExecutorService service;

        /**
         * The player to move.
         */
        private final Player player;

        /**
         * Creates a new task.
         *
         * @param service
         *            The service that executes the task.
         * @param player
         *            The NPC to move.
         */
        PlayerMoveTask(ScheduledExecutorService service, Player player) {
            this.service = service;
            this.player = player;
        }

        @Override
        public void run() {
            Direction nextMove = player.getDirection();
            if (nextMove != null) {
                level.move(player, nextMove);
            }
            long interval = player.getInterval();
            service.schedule(this, interval, TimeUnit.MILLISECONDS);
        }
    }
}
