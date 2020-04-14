package nl.tudelft.jpacman.level;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.Ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ElementParser {
        /**
         * The factory that creates the levels.
         */
        private final LevelFactory levelCreator;

        /**
         * The factory that creates the squares and board.
         */
        private final BoardFactory boardCreator;

        /**
         * Creates a new map parser.
         *
         * @param levelFactory
         *            The factory providing the NPC objects and the level.
         * @param boardFactory
         *            The factory providing the Square objects and the board.
         */
        public ElementParser(LevelFactory levelFactory, BoardFactory boardFactory) {
            this.levelCreator = levelFactory;
            this.boardCreator = boardFactory;
        }

        /**
         * Parses the text representation of the board into an actual level.
         *
         * <ul>
         * <li>Supported characters:
         * <li>' ' (space) an empty square.
         * <li>'#' (bracket) a wall.
         * <li>'.' (period) a square with a pellet.
         * <li>'P' (capital P) a starting square for players.
         * <li>'G' (capital G) a square with a ghost.
         * </ul>
         *
         * @param map
         *            The text representation of the board, with map[x][y]
         *            representing the square at position x,y.
         * @return The level as represented by this text.
         */
        public Level parseMap(char[][] map) {
            int width = map.length;
            int height = map[0].length;

            Square[][] grid = new Square[width][height];

            List<Ghost> ghosts = new ArrayList<>();
            List<Square> startPositions = new ArrayList<>();

            makeGrid(map, grid, ghosts, startPositions);

            Board board = boardCreator.createBoard(grid);
            return levelCreator.createLevel(board, ghosts, startPositions);
        }

        private void makeGrid(char[][] map, Square[][] grid, List<Ghost> ghosts, List<Square> startPositions) {
            for (int x = 0; x < map.length; x++) {
                for (int y = 0; y < map[0].length; y++) {
                    char c = map[x][y];
                    addSquare(grid, ghosts, startPositions, x, y, c);
                }
            }
        }

        /**
         * Adds a square to the grid based on a given character. These
         * character come from the map files and describe the type
         * of square.
         *
         * @param grid
         *            The grid of squares with board[x][y] being the
         *            square at column x, row y.
         * @param ghosts
         *            List of all ghosts that were added to the map.
         * @param startPositions
         *            List of all start positions that were added
         *            to the map.
         * @param x
         *            x coordinate of the square.
         * @param y
         *            y coordinate of the square.
         * @param c
         *            Character describing the square type.
         */
        protected void addSquare(Square[][] grid, List<Ghost> ghosts,
                                 List<Square> startPositions, int x, int y, char c) {
            switch (c) {
                case ' ':
                    grid[x][y] = boardCreator.createGround();
                    break;
                case '#':
                    grid[x][y] = boardCreator.createWall();
                    break;
                case '.':
                    Square pelletSquare = boardCreator.createGround();
                    grid[x][y] = pelletSquare;
                    levelCreator.createPellet().occupy(pelletSquare);
                    break;
                case 'G':
                    Square ghostSquare = makeGhostSquare(ghosts, levelCreator.createGhost(), x, y);
                    grid[x][y] = ghostSquare;
                    break;
                case 'P':
                    Square playerSquare = boardCreator.createGround();
                    grid[x][y] = playerSquare;
                    startPositions.add(playerSquare);
                    break;
                case 'o':
                    Square powerpillSquare = boardCreator.createGround();
                    grid[x][y] = powerpillSquare;
                    levelCreator.createPowerPill().occupy(powerpillSquare);
                    break;
                case 'f':
                    Square fruitSquare = boardCreator.createGround();
                    grid[x][y] = fruitSquare;
                    levelCreator.createFruit().occupy(fruitSquare);
                    break;
                default:
                    throw new PacmanConfigurationException("Invalid character at "
                        + x + "," + y + ": " + c);
            }
        }

        /**
         * creates a Square with the specified ghost on it
         * and appends the placed ghost into the ghost list.
         *
         * @param ghosts all the ghosts in the level so far, the new ghost will be appended
         * @param ghost the newly created ghost to be placed
         * @return a square with the ghost on it.
         */
        protected Square makeGhostSquare(List<Ghost> ghosts, Ghost ghost, int startingX, int startingY) {
            Square ghostSquare = boardCreator.createGround();
            ghosts.add(ghost);
            ghost.occupy(ghostSquare);
            ghost.setPosition(startingX, startingY);
            return ghostSquare;
        }

        /**
         * @return the BoardCreator
         */
        protected BoardFactory getBoardCreator() {
            return boardCreator;
        }
}
