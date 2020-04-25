package nl.tudelft.jpacman.level;

public class Checker {
    public static void checkMap(char[][] board){

        Position playerPosition = checkNPCANDPC(board);
        if(playerPosition == null){
            try {
                throw new Exception("No player in the map");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        assert playerPosition != null;
        if(!checkAccessCoin(board,playerPosition)){
            try {
                throw new Exception("Fruit, PowerPill or Gom inaccessible");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * look if the number of ghost and player is correct
     * @param board the board game
     * @return true if the nuber is correct, false otherwise.
     */
    private static Position checkNPCANDPC(char[][] board){
        int numberOfGhost = 0;
        int numberOfPlayer = 0;
        Position playerPosition = null;
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j< board[0].length; j++){
                if(board[i][j] == 'G'){
                    numberOfGhost ++;
                    if(numberOfGhost > 4){
                        return null;
                    }
                }else if(board[i][j] == 'P'){
                    numberOfPlayer++;
                    playerPosition = new Position(i,j);
                    if(numberOfPlayer > 1){
                        return null;
                    }
                }
            }
        }
        if(numberOfGhost ==4 && numberOfPlayer ==1){
            return playerPosition;
        }
        return null;
    }

    /**
     * check if all coin is accessible from the player
     * @param board the board
     * @param player the player position
     * @return true if all the coin, fruit and power Pill are accessible.
     */
    private static boolean checkAccessCoin(char[][] board, Position player){
        boolean[][] checkBoard = new boolean[board.length][board[0].length];
        checkBoard[player.x][player.y] = true;
        move(board,checkBoard,player);
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j<board[0].length; j++){
                if(board[i][j] == '.' || board[i][j] == 'f' || board[i][j] == 'o'){
                    if(!checkBoard[i][j]){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Find the accessible place of the player
     * @param board the board
     * @param checkBoard checking Board
     * @param position actual position "of the player"
     */
    private static void move(char[][]board, boolean[][] checkBoard, Position position){
        int x = position.x;
        int y = position.y;
        int xRight = x+1;
        if (xRight >= board.length) {
            xRight = 0;
        }
        checkingBoard(xRight,y, checkBoard, board);

        int xLeft = x-1;
        if (!(0 <= xLeft)) xLeft = board.length - 1;
        checkingBoard(xLeft,y, checkBoard, board);

        int yUP = y+1;
        if (yUP >= board[0].length) {
            yUP = 0;
        }
        checkingBoard(x,yUP, checkBoard, board);

        int yDown = y-1;
        if (!(0 <= yDown)) yDown = board[0].length - 1;
        checkingBoard(x,yDown, checkBoard, board);
    }

    /**
     * @param x position x in the board
     * @param y position y in the board
     * @param checkBoard checking board
     * @param board board
     */
    private static void checkingBoard(int x, int y, boolean[][] checkBoard, char[][] board){
        if(!checkBoard[x][y]){
            checkBoard[x][y] = true;
            if(board[x][y] != '#'){
                move(board,checkBoard,new Position(x,y));
            }
        }
    }

    private static class Position{
        public int x;
        public int y;
        public Position(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
}
