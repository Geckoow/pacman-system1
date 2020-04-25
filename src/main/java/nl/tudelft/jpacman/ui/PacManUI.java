package nl.tudelft.jpacman.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import nl.tudelft.jpacman.LevelInformation;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.game.GameFactory;
import nl.tudelft.jpacman.level.MapParser;
import nl.tudelft.jpacman.ui.ScorePanel.ScoreFormatter;

/**
 * The default JPacMan UI frame. The PacManUI consists of the following
 * elements:
 *
 * <ul>
 * <li>A score panel at the top, displaying the score of the player(s).
 * <li>A board panel, displaying the current level, i.e. the board and all units
 * on it.
 * <li>A button panel, containing all buttons provided upon creation.
 * </ul>
 *
 * @author Jeroen Roosen
 */
public class PacManUI extends JFrame {

    /**
     * Caption for the default stop button.
     */
    private static final String STOP_CAPTION = "Stop";

    /**
     * Caption for the default start button.
     */
    private static final String START_CAPTION = "Start";

    /**
     * Caption for the default back button.
     */
    private static final String BACK_CAPTION = "Back";

    /**
     * Default serialisation UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The desired frame rate interval for the graphics in milliseconds, 40
     * being 25 fps.
     */
    private static final int FRAME_INTERVAL = 40;

    public final int classicalCaller = 0;

    public final int campaignCaller = 1;

    /**
     * The panel displaying the player scores.
     */
    private ScorePanel scorePanel;

    /**
     * The panel displaying the game.
     */
    private BoardPanel boardPanel;

    private JPanel mainMenubuttons;

    private final ScoreFormatter scoreFormatter;

    private final PacKeyListener keys;

    private int actualCaller;

    private ArrayList<LevelInformation> classicalLevel;

    private ArrayList<LevelInformation> campaignLevel;

    private String actualCampaign;

    private final GameFactory gamefactory;

    private final MapParser mapParser;

    private ActualGame actualGame = null;

    private final MenuParser parser = new MenuParser();

    private ScheduledExecutorService service;

    /**
     * Creates a new UI for a JPac-Man game.
     *
     * @param scoreFormatter The formatter used to display the current score.
     */
    public PacManUI(ScoreFormatter scoreFormatter, GameFactory gameFactory, MapParser mapParser) {
        super("JPac-Man");


        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createMainMenuB();

        Map<Integer, Action> keyMappings = addSinglePlayerKeys();

        this.mapParser = mapParser;
        this.gamefactory = gameFactory;

        this.scoreFormatter = scoreFormatter;
        this.keys = new PacKeyListener(keyMappings);

        Container contentPanel = getContentPane();
        contentPanel.setLayout(new BorderLayout());

        this.classicalLevel = parser.listSimplesLevels();

        mainMenu();


    }

    /**
     * Create the button for the main menu.
     */
    public void createMainMenuB() {
        LinkedHashMap<String, Action> mainMenubuttons = new LinkedHashMap<>();
        mainMenubuttons.put("Classical", this::simpleMenu);
        mainMenubuttons.put("Campaign", this::campaignMenu);
        mainMenubuttons.put("Reinitialize", this::reinitializeInformation);
        this.mainMenubuttons = new ButtonPanel(mainMenubuttons, this);
    }

    /**
     * reinitialize all score to 0.
     */
    public void reinitializeInformation(){
        parser.reinitialize();
        this.classicalLevel = parser.listSimplesLevels();
    }

    /**
     * Put the Main Menu.
     */
    public void mainMenu() {
        getContentPane().removeAll();
        Container contentPanel = getContentPane();
        contentPanel.add(mainMenubuttons, BorderLayout.CENTER);
        pack();
    }

    /**
     * Adds key events UP, DOWN, LEFT and RIGHT to a game.
     * <p>
     * The {@link PacManUiBuilder} that will provide the UI.
     */
    protected Map<Integer, Action> addSinglePlayerKeys() {
        Map<Integer, Action> keyMappings = new HashMap<>();

        keyMappings.put(KeyEvent.VK_UP, moveTowardsDirection(Direction.NORTH));
        keyMappings.put(KeyEvent.VK_DOWN, moveTowardsDirection(Direction.SOUTH));
        keyMappings.put(KeyEvent.VK_LEFT, moveTowardsDirection(Direction.WEST));
        keyMappings.put(KeyEvent.VK_RIGHT, moveTowardsDirection(Direction.EAST));

        return keyMappings;
    }

    private Action moveTowardsDirection(Direction direction) {
        return () -> {
            assert actualGame != null;
            actualGame.getActualGame().move(actualGame.getActualGame().getSinglePlayer(), direction);
        };
    }

    /**
     * The menu of classical game
     */
    public void simpleMenu() {
        actualCaller = -1;
        getContentPane().removeAll();
        removeKeyListener(keys);
        Map<String, Action> buttons = new LinkedHashMap<>();


        LinkedHashMap<String, Action> backButton = new LinkedHashMap<>();
        backButton.put(BACK_CAPTION, this::mainMenu);

        int i = 0;
        LevelInformation actualLevel = classicalLevel.get(i);

        Action changeLevel = new LaunchGame(i, this.classicalCaller, this);
        buttons.put(getLevelName(actualLevel.getLevel()) + " score:" + actualLevel.getPoint(), changeLevel);

        while (actualLevel.getPoint() > 0 && i + 1 < classicalLevel.size()) {
            i++;
            actualLevel = classicalLevel.get(i);
            changeLevel = new LaunchGame(i, this.classicalCaller, this);
            buttons.put(getLevelName(actualLevel.getLevel()) + " score:" + actualLevel.getPoint(), changeLevel);
        }
        Container contentPanel = getContentPane();
        JPanel buttonPanel = new ButtonPanel(buttons, this);
        JPanel backPanel = new ButtonPanel(backButton, this);
        contentPanel.add(buttonPanel, BorderLayout.CENTER);
        contentPanel.add(backPanel, BorderLayout.SOUTH);

        pack();
    }

    /**
     * @param file ncomplet file name.
     * @return name of level
     */
    public String getLevelName(String file) {
        String[] s = file.split("\\.");
        return s[0];
    }

    /**
     * Menu of all campaign
     */
    public void campaignMenu() {
        actualCaller = -1;
        reinitialize();

        Map<String, Action> buttons = new LinkedHashMap<>();


        LinkedHashMap<String, Action> backButton = new LinkedHashMap<>();
        backButton.put(BACK_CAPTION, this::mainMenu);

        MenuParser parser = new MenuParser();
        ArrayList<String> campaign = parser.listCampaign();

        for (String elem : campaign) {
            Action changeCampaign = new SelectCampaign(elem);
            buttons.put(elem, changeCampaign);
        }

        Container contentPanel = getContentPane();
        JPanel buttonPanel = new ButtonPanel(buttons, this);
        JPanel backPanel = new ButtonPanel(backButton, this);
        contentPanel.add(buttonPanel, BorderLayout.CENTER);
        contentPanel.add(backPanel, BorderLayout.SOUTH);
        pack();
    }

    /**
     *
     */
    public void reinitialize() {
        removeKeyListener(keys);
        getContentPane().removeAll();
    }

    /**
     * selection of levels for a campaign
     *
     * @param campaign campaign
     */
    public void selectLevelCampaign(String campaign) {
        reinitialize();
        actualCampaign = campaign;

        Map<String, Action> buttons = new LinkedHashMap<>();


        LinkedHashMap<String, Action> backButton = new LinkedHashMap<>();
        backButton.put(BACK_CAPTION, this::campaignMenu);

        MenuParser parser = new MenuParser();

        campaignLevel = parser.listCampaignLevels(campaign);

        int size = campaignLevel.size() - 1;
        for (int i = 0; i < size; i++) {
            Action changeLevel = new LaunchGame(i, this.campaignCaller, this);
            String name = i + ": " + getLevelName(campaignLevel.get(i).getLevel());
            name += " " + campaignLevel.get(i).getPoint() + "/3 stars";;
            buttons.put(name, changeLevel);
        }
        if (unlockBonus()) {
            Action changeLevel = new LaunchGame(size, this.campaignCaller, this);
            String name = "Bonus: " + size ;
            name += ": " + getLevelName(campaignLevel.get(size).getLevel());
            name += " " + campaignLevel.get(size).getPoint() + "/3 stars";
            buttons.put(name, changeLevel);
        }

        Container contentPanel = getContentPane();
        JPanel buttonPanel = new ButtonPanel(buttons, this);
        JPanel backPanel = new ButtonPanel(backButton, this);
        contentPanel.add(buttonPanel, BorderLayout.CENTER);
        contentPanel.add(backPanel, BorderLayout.SOUTH);
        pack();

    }

    public boolean unlockBonus() {
        int starNumber = 0;
        for (int i = 0; i < campaignLevel.size() - 1; i++) {
            starNumber += campaignLevel.get(i).getPoint();
        }
        return (starNumber == 3 * (campaignLevel.size() - 1));
    }

    /**
     * initialize the game play
     *
     * @param level  the id of level
     * @param caller ethe caller of level (classical or campaign).
     */
    public void gameUI(int level, int caller) {
        actualCaller = caller;
        getContentPane().removeAll();
        if (actualCaller == classicalCaller) {
            actualGame = new ClassicalGame(level, classicalLevel, gamefactory, mapParser);
        }
        if (actualCaller == campaignCaller) {
            actualGame = new CampaignGame(level, campaignLevel, gamefactory, mapParser);
        }
        actualGame.makeGame();


        launchAGame();
    }

    /**
     * Launch a game with the actualGame.
     */
    public void launchAGame() {
        getContentPane().removeAll();
        addKeyListener(keys);
        Map<String, Action> buttons = createGUIButtons(actualGame.getActualGame(), actualCaller);

        JPanel buttonPanel = new ButtonPanel(buttons, this);

        scorePanel = new ScorePanel(actualGame.getActualGame().getPlayers());
        if (this.scoreFormatter != null) {
            scorePanel.setScoreFormatter(this.scoreFormatter);
        }

        boardPanel = new BoardPanel(actualGame.getActualGame());

        Container contentPanel = getContentPane();
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        contentPanel.add(scorePanel, BorderLayout.NORTH);
        contentPanel.add(boardPanel, BorderLayout.CENTER);
        pack();

        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this::nextFrame, 0, FRAME_INTERVAL, TimeUnit.MILLISECONDS);
    }


    /**
     * create the buttons for the game
     *
     * @param game actual game
     * @return the button for the game
     */
    public Map<String, Action> createGUIButtons(Game game, int caller) {
        Map<String, Action> buttons = new LinkedHashMap<>();
        buttons.put(STOP_CAPTION, game::stop);
        buttons.put(START_CAPTION, game::start);
        Action endgame = new EndGame(actualGame.getActualGame(), caller);
        buttons.put(BACK_CAPTION, endgame);

        return buttons;
    }

    /**
     * Starts the "engine", the thread that redraws the interface at set
     * intervals.
     */
    public void start() {
        setVisible(true);
    }

    /**
     * Draws the next frame, i.e. refreshes the scores and game.
     */
    private void nextFrame() {
        boardPanel.repaint();
        scorePanel.refresh();
        if (actualGame.getActualGame().isWon()) {
            service.shutdown();
            actualGame.getActualGame().stop();
            boolean end = actualGame.nextLevel();
            if (actualCaller == classicalCaller) {
                parser.saveClassicalLevel(this.classicalLevel);
            } else if (actualCaller == campaignCaller) {
                parser.saveCampaignLevel(this.campaignLevel, this.actualCampaign);
            }
            if (!end) {
                if (actualCaller == classicalCaller) {
                    simpleMenu();
                } else if (actualCaller == campaignCaller) {
                    campaignMenu();
                }
            } else {
                launchAGame();
            }
        } else if (actualGame.getActualGame().isLost()) {
            service.shutdown();
            if (actualCaller == classicalCaller) {
                simpleMenu();
            } else if (actualCaller == campaignCaller) {
                campaignMenu();
            }
        }
    }

    public static class LaunchGame implements Action {

        final int level;
        final int caller;
        final PacManUI pacmanUI;

        public LaunchGame(int level, int caller, PacManUI pacmanUI) {
            this.level = level;
            this.caller = caller;
            this.pacmanUI = pacmanUI;
        }

        @Override
        public void doAction() {
            pacmanUI.gameUI(level, caller);
        }
    }

    public class EndGame implements Action {
        final int caller;
        final Game game;

        public EndGame(Game game, int caller) {
            this.game = game;
            this.caller = caller;
        }

        @Override
        public void doAction() {
            game.stop();
            if (caller == classicalCaller) {
                simpleMenu();
            } else if (caller == campaignCaller) {
                campaignMenu();
            }
        }
    }

    public class SelectCampaign implements Action {
        final String campaign;

        public SelectCampaign(String campaign) {
            this.campaign = campaign;
        }

        @Override
        public void doAction() {
            selectLevelCampaign(campaign);
        }
    }


}

