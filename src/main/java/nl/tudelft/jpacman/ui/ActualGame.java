package nl.tudelft.jpacman.ui;

import nl.tudelft.jpacman.LevelInformation;
import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.game.GameFactory;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.MapParser;

import java.io.IOException;
import java.util.ArrayList;

public abstract class ActualGame {
    private int nbActualGame;

    private final ArrayList<LevelInformation> listOfLevel;

    private Game actualGame = null;

    private final GameFactory gamefactory;

    private final MapParser mapParser;

    private int lives = 3;

    public ActualGame(int nbActualGame, ArrayList<LevelInformation> listOfLevel,GameFactory gamefactory,MapParser mapParser){
        assert listOfLevel != null;

        this.nbActualGame = nbActualGame;
        this.listOfLevel = listOfLevel;
        this.gamefactory = gamefactory;
        this.mapParser = mapParser;
    }

    /*
    *create a new game.
    * @return true if a new game is created , false otherwise
     */
    public boolean makeGame(){
        if(nbActualGame < listOfLevel.size()){
            String levelFile = listOfLevel.get(nbActualGame).getLevel();
            Level level = makeLevel(levelFile);
            actualGame = this.gamefactory.createSinglePlayerGame(level);
            actualGame.getSinglePlayer().setLives(lives);
            return true;
        }
        return false;
    }

    /**
     * change level
     * @return true if the level is change, false otherwise.
     */
    public abstract boolean nextLevel();

    public abstract void changeScore();

    public void incrementLevel(){
        changeScore();
        int lives = actualGame.getSinglePlayer().getLives();
        if(lives > 3) this.lives = lives;
        this.nbActualGame ++;
    }

    /**
     * Creates a new level. By default this method will use the map parser to
     * parse the default board stored in the <code>board.txt</code> resource.
     *
     * @return A new level.
     */
    public Level makeLevel(String level) {
        try {
            return this.mapParser.parseMap(level);
        } catch (IOException e) {
            throw new PacmanConfigurationException(
                "Unable to create level, name = " + level, e);
        }
    }

    public Game getActualGame(){
        return actualGame;
    }

    public ArrayList<LevelInformation> getLevelList(){
        return this.listOfLevel;
    }


    /**
     * @return the number of the actual game.
     */
    public int getNbActualGame(){
        return this.nbActualGame;
    }
}
