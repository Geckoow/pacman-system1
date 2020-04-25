package nl.tudelft.jpacman.ui;

import java.awt.GridLayout;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import nl.tudelft.jpacman.level.Player;

/**
 * A panel consisting of a column for each player, with the numbered players on
 * top and their respective scores underneath.
 *
 * @author Jeroen Roosen 
 *
 */
public class ScorePanel extends JPanel {

    /**
     * Default serialisation ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The map of players and the labels their scores are on.
     */
    private final Map<Player, JLabel> scoreLabels;

    /**
     * The map of players and the labels their lives are on.
     */
    private final Map<Player, JLabel> livesLabels;

    /**
     * The default way in which the score is shown.
     */
    public static final ScoreFormatter DEFAULT_SCORE_FORMATTER =
        (Player player) -> String.format("Score: %3d", player.getScore());

    /**
     * The default way in which the lives is shown.
     */
    public static final ScoreFormatter DEFAULT_Lives_FORMATTER =
        (Player player) -> String.format("Lives: %3d", player.getLives());

    /**
     * The way to format the score information.
     */
    private ScoreFormatter scoreFormatter = DEFAULT_SCORE_FORMATTER;

    /**
     * The way to format the lives information.
     */
    private final ScoreFormatter livesFormatter = DEFAULT_Lives_FORMATTER;

    /**
     * Creates a new score panel with a column for each player.
     *
     * @param players
     *            The players to display the scores of.
     */
    public ScorePanel(List<Player> players) {
        super();
        assert players != null;

        setLayout(new GridLayout(3, players.size()));

        for (int i = 1; i <= players.size(); i++) {
            add(new JLabel("Player " + i, JLabel.CENTER));
        }
        scoreLabels = new LinkedHashMap<>();
        livesLabels = new LinkedHashMap<>();
        for (Player player : players) {
            JLabel scoreLabel = new JLabel("0", JLabel.CENTER);
            int num = player.getLives();
            JLabel livesLabel = new JLabel(Integer.toString(num), JLabel.CENTER);
            livesLabels.put(player, livesLabel);
            scoreLabels.put(player, scoreLabel);
            add(scoreLabel);
            add(livesLabel);
        }
    }

    /**
     * Refreshes the scores of the players.
     */
    protected void refresh() {
        for (Map.Entry<Player, JLabel> entry : scoreLabels.entrySet()) {
            String score = getStringScore(entry);
            entry.getValue().setText(score);
        }

        for (Map.Entry<Player, JLabel> entry : livesLabels.entrySet()) {
            String lives = getLivesScore(entry);
            entry.getValue().setText(lives);
        }
    }

    private String getStringScore(Map.Entry<Player, JLabel> entry) {
        Player player = entry.getKey();
        String score = "";
        if (!player.isAlive()) {
            score = "You died. ";
        }
        score += scoreFormatter.format(player);
        return score;
    }

    private String getLivesScore(Map.Entry<Player, JLabel> entry) {
        Player player = entry.getKey();
        String lives = "";
        lives += livesFormatter.format(player);
        return lives;
    }


    /**
     * Provide means to format the score or lives for a given player.
     */
    public interface ScoreFormatter {

        /**
         * Format the score of a given player.
         * @param player The player and its score
         * @return Formatted score.
         */
        String format(Player player);
    }

    /**
     * Let the score panel use a dedicated score formatter.
     * @param scoreFormatter Score formatter to be used.
     */
    public void setScoreFormatter(ScoreFormatter scoreFormatter) {
        assert scoreFormatter != null;
        this.scoreFormatter = scoreFormatter;
    }


}
