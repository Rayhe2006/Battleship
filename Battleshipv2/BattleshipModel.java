import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * BattleshipModel Class
 * Manages the entire game of Battleship, for both the player and enemy computer
 * Last Modified: 01/24/2024
 * 
 * @author Aareez K. (creator) and Rayhan E. (added some methods)
 */
public class BattleshipModel extends Object {
  // Variable Declarations
  private BattleshipView gameView; // The View
  private int[][] playerShipGrid = new int[10][10]; // Player's ship grid
  private int[][] playerHitsAndMisses = new int[10][10]; // Player's hits and misses
  private int[][] playerShipPlacingGrid = new int[10][10]; // Used to temporarily store player's ship when they are is placing it
  private int[][] computerShipGrid = new int[10][10]; // Computer's ship grid
  private int[][] computerShipPlacingGrid = new int[10][10];
  private int[][] computerHitsAndMisses = new int[10][10]; // Computer's hits and misses
  private int numPlayerShips; // Amount of ships the player has
  private int numComputerShips; // Amount of ships the Computer has
  private int numRounds; // Number of rounds to play
  private int roundNum = 1; // The current round number
  private String placingShipDirection = "Horizontal"; // Player's ship direction when the Player is placing their ships at the beginning of the game
  private int placingShipRow; // Player's ship Row when the Player is placing their ship
  private int placingShipColumn; // Player's ship Column when the Player is placing their ship
  private int placingShipType; // Player's ship Type that the Player is placing
  private int computerInitialRowHit; // For the Computer to know what row their initial missile hit
  private int computerInitialColumnHit; // For the Computer to know what column their initial missile hit
  private int computerRowHit; // For the Computer to know what row their missile hit after 
  private int computerColumnHit; // For the Computer to know what column their missile hit after
  private String computerShipHitDirection = "None"; // For the Computer to know what direction the Player ship is in for the next missiles it launches
  private boolean winner; // The winner of the round, is true if it is the player.
  private int playerWins = 0;
  private int computerWins = 0;
  // CONSTANTS to refer ship names and hits and misses to simple numbers
  // Public, but final, so they can be refered to in other classes
  public final int DESTROYER = 1; // Takes 2 grid spaces
  public final int SUBMARINE = 2; // Takes 3 grid spaces
  public final int CRUISER = 3; // Takes 3 grid spaces
  public final int BATTLESHIP = 4; // Takes 4 grid spaces
  public final int CARRIER = 5; // Takes 5 grid spaces
  public final int HIT = 6; // Missile Hit
  public final int MISS = 7; // Missile Miss
  public final int DESTROYED_SHIP = 8; // Ship destroyed on a Grid
  public final int EMPTY = 0;
  // CONSTANTS to refer to the Game State
  // Public but final as well
  public final int STATE_PLACING_SHIPS = 100;
  public final int STATE_IN_GAME = 200;
  public final int STATE_GAME_OVER = 300;
  public final int STATE_START_GAME = 400;
  public final int STATE_SHOW_ROUND_STATS = 500;
  public final int STATE_SHOW_OLD_GAME_STATS = 600;
  private int gameState = STATE_START_GAME;
  private String log;
  private String computerLog;
  private String totalLog = "Log: ";
  private String roundInfo;
  private PrintWriter out;
  private int gameNumber = 1;
  private File currentFile;
  private String oldGameStatString;
  /**
   * Creates a default generator
   */
  public BattleshipModel() {
    super();
  }

  /**
   * Sets the view for the generator
   * @param BattleshipView view       The battleship view
   */
  public void setGUI(BattleshipView view) {
    this.gameView = view;
  }

  /** Updates the view in the GUI */
  public void updateView() {
    this.gameView.update();
  }

  // GET METHODS

  /** Returns the Player's shipGrid 
   * @returns The Player's shipGrid as 2D array
  */
  public int[][] getPlayerShipGrid() {
    return this.playerShipGrid;
  }

  /** Returns the Player's missile hits and misses 
   * @returns the Player's missile hits and misses as 2D array
  */
  public int[][] getPlayerHitsAndMisses() {
    return this.playerHitsAndMisses;
  }

  /** Returns the Player's ship placing grid
   * @returns the Player's ship placing grid as 2D array
  */
  public int[][] getPlayerShipPlacingGrid() {
    return this.playerShipPlacingGrid;
  }

  /** Returns the Computer's shipGrid 
   * @returns the Computer's shipGrid as 2D array
  */
  public int[][] getComputerShipGrid() {
    return this.computerShipGrid;
  }

  /** Returns the Computer's ship placing grid 
   * @returns the Computer's ship placing grid as 2D array
  */
  public int[][] getComputerShipPlacingGrid() {
    return this.computerShipPlacingGrid;
  }

  /** Returns the Computer's missile hits and misses 
   * @returns the Computer's missile hits and misses as 2D array
  */
  public int[][] getComputerHitandMisses() {
    return this.computerHitsAndMisses;
  }

  /** Returns the winner of the round or game 
   * @returns the Computer's missile hits and misses as 2D array
  */
  public boolean getWinner() {
    return this.winner;
  }

  /** Returns the Player's current ship direction */
  public String getPlacingShipDirection() {
    return this.placingShipDirection;
  }

  /** Returns the Player's current ship direction that the computer needs to bomb you LOL!!
   *  (not to be confused with getShipDirection();)
  */
  public String getComputerShipHitDirection() {
    return this.computerShipHitDirection;
  }

  /** The current game state */
  public int getGameState() {
    return this.gameState;
  }

  // PLAYER SHIP PLACING METHODS
  // When the player needs to place his ships before the game begins.

  /** Returns the Player's current ship direction */
  public int getPlacingShipRow() {
    return this.placingShipRow;
  }

  /** Returns the Player's current ship direction */
  public int getPlacingShipColumn() {
    return this.placingShipColumn;
  }

  /** Returns the Ship the Player is currently placing */
  public int getPlacingShipType() {
    return this.placingShipType;
  }

  /** Checks if a ship is destroyed
   * Checks by checking if the ship of shipType is not occupying any grid spaces
   * @param grid The grid to check
   * @param shipType The type of Ship to check
   * @returns a true or false depending if the ship is confirmed destroyed
   * @author Rayhan E.
   */
  public boolean isShipDestroyed(int[][] grid, int shipType) {
    int shipLength = 0;
    for (int x = 0; x < grid.length; x++) {
      for (int y = 0; y < grid.length; y++) {
        if (grid[x][y] == shipType) {
          shipLength++;
        }
      }
    }
    if (shipLength == 0) {
      return true;
    } else {
      return false;
    }
  }


  /** Starts placing a ship of shipType on the Player's grid
   * @param shipType The type of Ship to start placing
   */
  public void startPlacingShip(int shipType) {
    for (int x = 0; x < playerShipPlacingGrid.length; x++) {
      for (int y = 0; y < playerShipPlacingGrid.length; y++) {
        this.playerShipPlacingGrid[x][y] = 0;
      }
    } // Clears the ship grid.
    playerShipPlacingGrid[0][0] = shipType; // Sets [x][y] to the shipType
    this.placingShipType = shipType;
    this.placingShipRow = 0;
    this.placingShipColumn = 0;
    this.placingShipDirection = "Horizontal";
    this.movePlayerShip(this.placingShipType, "None");
    this.updateView();
  }

  /** Stops placing ships and sets the game state to STATE_IN_GAME
   * @param shipType The type of Ship to start placing
   */
  public void stopPlacingShips() {
    for (int x = 0; x < playerShipPlacingGrid.length; x++) {
      for (int y = 0; y < playerShipPlacingGrid.length; y++) {
        this.playerShipPlacingGrid[x][y] = 0;
      }
    } // Clears the ship grid.
    this.placingShipType = 0;
    this.placingShipRow = 0;
    this.placingShipColumn = 0;
    this.numPlayerShips = 5;
    this.log = "Type a letter then number, \nthen hit 'Launch Missile'! (eg., A1)";
    this.gameState = STATE_IN_GAME;
  }

  /** Moves the Player's ship of shipType by a direction
   * @param shipType The type of Ship to start move on the grid
   * @param direction the direction to move the ship by
   */
  public void movePlayerShip(int shipType, String direction) {
    for (int x = 0; x < this.playerShipPlacingGrid.length; x++) {
      for (int y = 0; y < this.playerShipPlacingGrid.length; y++) {
        this.playerShipPlacingGrid[x][y] = 0;
      }
    }

    int gridSpacesToTake = shipType; // Ships take the same space their integer takes so we can do this

    if (shipType == SUBMARINE || shipType == DESTROYER) { // These ships in particular take 1 more space than their integer
      gridSpacesToTake++;
    }
    try {
      if ((direction.equals("Down")) && (!((this.placingShipColumn + 1) >= 10))) { // If ship needs to move UP and row it needs to move to is not 10 or greater
        this.placingShipColumn++;
      } else if ((direction.equals("Up")) && (!((this.placingShipColumn - 1) < 0))) { // If ship needs to move DOWN and row it needs to move to is not 0 or less
        this.placingShipColumn--;
      } else if ((direction.equals("Left")) && (!((this.placingShipRow - 1) < 0))) { // If ship needs to move LEFT and column it needs to move to is not 0 or less
        this.placingShipRow--;
      } else if ((direction.equals("Right")) && (!((this.placingShipRow + 1) >= 10))) { // If ship needs to move RIGHT and column it needs to move to is not 10 or greater
        this.placingShipRow++;
      } //else { throw new ArithmeticException(); } // If this test passed and didn't throw ArithmeticException()
      // we can now set the array

      if (this.placingShipDirection.equals("Horizontal")) {
        for (int x = this.placingShipRow; x < this.placingShipRow + gridSpacesToTake; x++) {
          if (x >= 0 && x <= 9) {
            this.playerShipPlacingGrid[x][this.placingShipColumn] = shipType;
          }
        }
      } else if (this.placingShipDirection.equals("Vertical")) {
        for (int y = this.placingShipColumn; y < this.placingShipColumn + gridSpacesToTake; y++) {
          if (y >= 0 && y <= 9) {
            this.playerShipPlacingGrid[this.placingShipRow][y] = shipType;
          }
        }
      }
      this.updateView();
    } catch (java.lang.ArithmeticException e) {
    }
  }

  /** Sets the PlacingShipType that the Player is placing
   * @param shipType the type of Ship to place
   */
  public void setPlacingShipType(int shipType) {
    this.placingShipType = shipType;
    this.updateView();
  }

  /** Sets the PlacingShip Direction that the Player is placing */
  public void changePlacingShipDirection() {
    if (this.placingShipDirection.equals("Horizontal")) {
      this.placingShipDirection = "Vertical";
    } else {
      this.placingShipDirection = "Horizontal";
    }
    //this.placingShipDirection = direction;
    this.movePlayerShip(this.placingShipType, "None");
    this.updateView();
  }

  // GAME METHODS

  /** Starts a game using the number of rounds to play
   * @param numberOfRounds the number of rounds to play
   */
  public void startGame(int numberOfRounds)
  {
    this.generateComputerShipGrid();
    this.numRounds = numberOfRounds;
    this.gameState = (this.STATE_PLACING_SHIPS);
    this.startPlacingShip(DESTROYER);
    this.updateView();

  }

  /** Player launching a missile to computers grid 
   * @param row the Row to launch at
   * @param column the column to launch at
   * @author Rayhan E.
  */
  public void playerLaunchMissile(int row, int column) {
    int shipType = computerShipGrid[row][column];
    // If there is a ship there

    if (computerShipGrid[row][column] != 0 && computerShipGrid[row][column] != this.DESTROYED_SHIP) {
      this.playerHitsAndMisses[row][column] = this.HIT;
      this.log = "You hit a ship!";
      this.totalLog = this.totalLog + "\n" + "Player hit a ship at (" + row + ", " + column + ")";
      this.computerShipGrid[row][column] = DESTROYED_SHIP;
      if (isShipDestroyed(this.computerShipGrid, shipType) == true) {
        this.log = ("Enemy ship has been destroyed!");
        this.totalLog = this.totalLog + "\n" + "Player destroyed a ship";
        this.numComputerShips--;

        //If there are no computer ships left
        if (this.numComputerShips == 0) {
          // Sets player to winner
          this.roundInfo = this.roundInfo + "\n" + "****************************"+"\n"+"Round " + this.getRoundsNum() + "\n" + this.totalLog;
          this.roundInfo = this.roundInfo + "\n" + "\n"+"Number of player ships: " + this.getNumPlayerShips();
          this.roundInfo = this.roundInfo + "\n" + "Player Won!";
          this.numRounds--;
          this.roundNum++;
          this.playerWins++;
          //Check if there are no rounds left
          if (this.numRounds == 0) {
            this.gameState = STATE_SHOW_ROUND_STATS;
            this.updateView();
            this.gameState = STATE_GAME_OVER;
            this.writeGameStatsToFile();
            this.restartGame();
          }
          //Go back to placing ships to start a new round
          else {
            this.newRound();
          }
        }
      }
      // Checks if there are no computer ships left which means that the player won
    } else if (computerShipGrid[row][column] == 0) {
      this.playerHitsAndMisses[row][column] = this.MISS;
      this.log = ("You missed.");
      this.totalLog = this.totalLog + "\n" + "Player missed";
    } else if (computerShipGrid[row][column] == 8) {
      this.log = ("You already launched a missile there!");
    }
    this.computerDecideMissile();
    this.updateView();
  }

  /**
   * Method for the computer to decide where to launch a missile at the player's ship grid
   * @author Aareez K.
   */
  public void computerDecideMissile() {
    // If Computer doesn't have a direction set yet, launch randomly
    // However, quickly check if computerInitalRowHit wasn't set to an emergency reset integer 
    // See computerLaunchMissile to see why this is.
    if (this.computerInitialRowHit == 999) {
      this.computerShipHitDirection = "None"; // Reset Ship Hit Direction that was confirmed
    }

    if (this.gameState == this.STATE_IN_GAME) {
      if (this.computerShipHitDirection.equals("None")) {
        System.out.println("computerShipHitDirection: None");
        int row = this.getRandomRow();
        int column = this.getRandomColumn();
        while (computerHitsAndMisses[row][column] == this.HIT || computerHitsAndMisses[row][column] == this.MISS) {
          // If the Computer's row and column end up already being a hit/miss choose a new
          // row and column
          row = this.getRandomRow();
          column = this.getRandomColumn();
        }
        if (this.computerLaunchMissile(row, column) == true) { // If the missile launched hit
          this.computerShipHitDirection = "Up"; // Set Confirmed hit direction to up
          // Set computer confirmed hit row and column
          this.computerInitialColumnHit = column;
          this.computerInitialRowHit = row;
          this.computerColumnHit = column;
          this.computerRowHit = row;
        } else {
          this.computerShipHitDirection = "None";
        }
        // else if the Direction is up...
      } else if (this.computerShipHitDirection.equals("Up")) {
        System.out.println("computerShipHitDirection: Up");
        this.computerColumnHit--; // Decrease column to go UP
        if (this.computerColumnHit >= 0 && this.computerColumnHit <= 9) { // If it's not out of bounds 
          if (this.computerLaunchMissile(this.computerRowHit, this.computerColumnHit) == true) { // If the missile launched hit
            // Do nothing, we good
          } else {
            // Switch directions because we missed
            this.computerShipHitDirection = "Down";
            this.computerRowHit = this.computerInitialRowHit;
            this.computerColumnHit = this.computerInitialColumnHit;
          }
        } else { // Switch directions because the row is out of bounds
          this.computerShipHitDirection = "Down";
          this.computerRowHit = this.computerInitialRowHit;
          this.computerColumnHit = this.computerInitialColumnHit;
          this.computerDecideMissile(); // Decide again
        }
      } else if (this.computerShipHitDirection.equals("Down")) {
        System.out.println("computerShipHitDirection: Down");
        this.computerColumnHit++; // Increase column to go DOWN
        if (this.computerColumnHit >= 0 && this.computerColumnHit <= 9) { // If it's not out of bounds 
          if (this.computerLaunchMissile(this.computerRowHit, this.computerColumnHit) == true) { // If the missile launched hit
            // Do nothing, we good
          } else {
            // Switch directions because we missed
            this.computerShipHitDirection = "Left";
            this.computerRowHit = this.computerInitialRowHit;
            this.computerColumnHit = this.computerInitialColumnHit;
          }
        } else { // Switch directions because the row is out of bounds
          this.computerShipHitDirection = "Left";
          this.computerRowHit = this.computerInitialRowHit;
          this.computerColumnHit = this.computerInitialColumnHit;
          this.computerDecideMissile(); // Decide again
        }
      } else if (this.computerShipHitDirection.equals("Left")) {
        System.out.println("computerShipHitDirection: Left");
        this.computerRowHit--; // Decrease row to go LEFT
        if (this.computerRowHit >= 0 && this.computerRowHit <= 9) { // If it's not out of bounds 
          if (this.computerLaunchMissile(this.computerRowHit, this.computerColumnHit) == true) { // If the missile launched hit
            // Do nothing, we good
          } else {
            // Switch directions because we missed
            this.computerShipHitDirection = "Right";
            this.computerRowHit = this.computerInitialRowHit;
            this.computerColumnHit = this.computerInitialColumnHit;
          }
        } else { // Switch directions because the row is out of bounds
          this.computerShipHitDirection = "Right";
          this.computerRowHit = this.computerInitialRowHit;
          this.computerColumnHit = this.computerInitialColumnHit;
          this.computerDecideMissile(); // Decide again
        }
      } else if (this.computerShipHitDirection.equals("Right")) {
        System.out.println("computerShipHitDirection: Right");
        this.computerRowHit++; // Increase row to go RIGHT
        if (this.computerRowHit >= 0 && this.computerRowHit <= 9) { // If it's not out of bounds 
          if (this.computerLaunchMissile(this.computerRowHit, this.computerColumnHit) == true) { // If the missile launched hit
            // Do nothing, we good
          } else {
            // Switch directions because we missed
            this.computerShipHitDirection = "Up";
            this.computerRowHit = this.computerInitialRowHit;
            this.computerColumnHit = this.computerInitialColumnHit;
          }
        } else { // Switch directions because the row is out of bounds
          this.computerShipHitDirection = "Up";
          this.computerRowHit = this.computerInitialRowHit;
          this.computerColumnHit = this.computerInitialColumnHit;
          this.computerDecideMissile(); // Decide again
        }
      }

      // If there's no player ships left
      if (this.numPlayerShips == 0) {
        // Sets player to winner
        this.roundInfo = this.roundInfo + "\n" + "Round " + this.getRoundsNum() + "\n" + this.totalLog;
        this.roundInfo = this.roundInfo + "\n" + "Number of player ships: " + this.getNumPlayerShips();
        this.roundInfo = this.roundInfo + "\n" + "Computer Won!";
        this.numRounds--;
        this.roundNum++;
        this.computerWins++;
        this.computerShipHitDirection = "None"; // Deciding from random again
        // Check if there are no rounds left
        if (this.numRounds == 0) {
          this.gameState = STATE_SHOW_ROUND_STATS;
          this.updateView();
          this.gameState = STATE_GAME_OVER;
          this.writeGameStatsToFile();
          this.restartGame();
        }
        // Go back to placing ships to start a new round
        else {
          this.newRound();
        }
      }
    }

  }

  /**
   * Method for the computer to launch a missile at the player's ship grid
   * @param row The row to launch at
   * @param column The column to launch at
   * @returns a true or false depending on if the missile launched hit a ship
   * @author Aareez K.
   */
  public boolean computerLaunchMissile(int row, int column) {
    boolean missileHit = false;
    System.out.println("Computer selected row: " + row + ", column: " + column);
    int shipType = playerShipGrid[row][column]; // Get the shipType on that row and column
    if (this.gameState == this.STATE_IN_GAME) {
      if (playerShipGrid[row][column] != this.EMPTY && playerShipGrid[row][column] != this.DESTROYED_SHIP) {
        // If the ocean isn't empty and occupies a ship there that isn't DESTROYED!!!
        // then
        this.computerHitsAndMisses[row][column] = this.HIT;
        this.computerLog = "Computer hit one of your ships!";
        this.totalLog = this.totalLog + "\n" + "Computer hit Player ship at (" + row + ", " + column + ")";
        this.playerShipGrid[row][column] = this.DESTROYED_SHIP;
      
        // Check if that players ship is destroyed
        if (this.isShipDestroyed(this.playerShipGrid, shipType) == true) {
          this.computerLog = ("Computer destroyed one of your ships!");
          this.totalLog = this.totalLog + "\n" + "Computer destroyed Player ship";
          this.numPlayerShips--;
          this.computerShipHitDirection = "None"; // Deciding from random again
        }
        missileHit = true;
      } else if (playerShipGrid[row][column] == this.EMPTY) { // If it missed instead of hitting a ship
        this.computerHitsAndMisses[row][column] = this.MISS;
        this.computerLog = ("Computer missed a missile.");
        this.totalLog = this.totalLog + "\n" + "Computer missed";
        missileHit = false;
      } else {
        System.out.println("Computer got stuck or lost!");
        // Computer got lost. Set to an emergency reset integer so the Computer doesn't get stuck in a loop.
        this.computerInitialRowHit = 999;
        // Can't do 'this.computerShipHitDirection = "None";' because of the way the code is supposed to go.
      }
    }
    this.updateView();
    return missileHit;
  }

  /** Generates a randomized computer ship grid 
   * @author Rayhan E.
  */
  public void generateComputerShipGrid() {
    for (int x = 1; x <= 5; x++) {
      do {
        this.placingShipRow = this.getRandomRow();
        this.placingShipColumn = this.getRandomColumn();
        this.placingShipDirection = this.getRandomDirection();
        System.out.println("Trying to place ship " + x + " at: (" + placingShipRow + ", " + placingShipColumn
            + ") direction: " + placingShipDirection);
      } while ((this.isShipPlacementValid(x, this.computerShipPlacingGrid, this.computerShipGrid) == false));
      this.placeShip(x, false, this.computerShipPlacingGrid, this.computerShipGrid);
    }
    this.numComputerShips = 5;

    this.updateView();
  }

  /** Generates a random row 
   * @author Rayhan E.
  */
  public int getRandomRow() {
    return (int) (Math.random() * (10));
  }

  /**Generates a random column 
   * @author Rayhan E.
  */
  public int getRandomColumn() {
    return (int) (Math.random() * (10));
  }

  /**Generates a random direction 
   * @author Rayhan E.
  */
  public String getRandomDirection() {
    int random = (int) (Math.random() * (2 - 1 + 1) + 1);
    if (random == 1) {
      return "Horizontal";
    } else if (random == 2) {
      return "Vertical";
    }
    return "Horizontal";
  }

  /** Places the ship of shipType down on a grid that is either the Player's or Computer's
   * @author Aareez K. (Creator) and Rayhan E. (added boolean, placingArray and gridArray parameters)
   * @param shipType the ship of Type to place down
   * @param isPlayerShip if the ship being placed belongs to the Player
   * @param placingArray the placingShipArray
   * @param gridArray the grid to place the ship on
   */
  public void placeShip(int shipType, boolean isPlayerShip, int[][] placingArray, int[][] gridArray) {
    if (this.isShipPlacementValid(shipType, placingArray, gridArray) == true) { // Check if the placement is valid
      int gridSpacesToTake = shipType; // Ships take the same space their integer takes so we can do this

      if (shipType == SUBMARINE || shipType == DESTROYER) { // These ships in particular take 1 more space than their integer
        gridSpacesToTake++;
      }

      // Sets the ship down in playerShipGrid
      if (this.placingShipDirection.equals("Horizontal")) {
        for (int x = this.placingShipRow; x < this.placingShipRow + gridSpacesToTake; x++) {
          gridArray[x][this.placingShipColumn] = shipType;
        }
      } else if (this.placingShipDirection.equals("Vertical")) {
        for (int y = this.placingShipColumn; y < this.placingShipColumn + gridSpacesToTake; y++) {
          gridArray[this.placingShipRow][y] = shipType;
        }
      }
      if (isPlayerShip) {
        if (shipType < CARRIER) {
          this.startPlacingShip(shipType + 1);
        } else {
          this.stopPlacingShips();
        }

      }

      this.updateView();
    }
  }

  /** Checks if the PlayerShipPlacement is valid before it can actually place the ship on the Player's grid.
  * @param shipType   The shipType, which points to an integer (1,2,3,4,5)
  * @param placingArray The ship Placing array
  * @param gridArray the grid array to check on
  * @returns a true or false depending on if the ship placement ended up being valid
  * @author Aareez K. (Creator) & Rayhan E. (modified a bit)
  */
  public boolean isShipPlacementValid(int shipType, int[][] placingArray, int[][] gridArray) {
    boolean shipPlacementValid = true; // True by default, will turn false if either exception is thrown and caught
    int gridSpacesToTake = shipType; // Ships take the same space their integer takes so we can do this

    if (shipType == SUBMARINE || shipType == DESTROYER) { // These ships in particular take 1 more space than their integer
      gridSpacesToTake++;
    }

    try { // First loop to test if the grid spaces the ship wants to occupy isnt out of bounds
      if (this.placingShipDirection.equals("Horizontal")) {
        for (int x = this.placingShipRow; x < this.placingShipRow + gridSpacesToTake; x++) {
          placingArray[x][this.placingShipColumn] = shipType;
        }
      } else if (this.placingShipDirection.equals("Vertical")) {
        for (int y = this.placingShipColumn; y < this.placingShipColumn + gridSpacesToTake; y++) {
          placingArray[this.placingShipRow][y] = shipType;
        }
      } // If this loop passed, the ship placing is good and we can check the next thing
      // Which is to check if there is already a ship there

      for (int x = 0; x < placingArray.length; x++) {
        for (int y = 0; y < placingArray.length; y++) {
          if (gridArray[x][y] != 0 && placingArray[x][y] == shipType) {
            throw new ArithmeticException(); // Throws an exception if second loop doesn't pass
          }
        }
      } // If this loop passed, the ship placing is good to go, and we can return true!
    } catch (java.lang.ArithmeticException e) {
      // send error to game info put some info here
      for (int x = 0; x < placingArray.length; x++) {
        for (int y = 0; y < placingArray.length; y++) {
          placingArray[x][y] = 0;
        }
      }
      placingArray[this.placingShipRow][this.placingShipColumn] = shipType;
      shipPlacementValid = false;
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // send error to game info put some info here
      for (int x = 0; x < placingArray.length; x++) {
        for (int y = 0; y < placingArray.length; y++) {
          placingArray[x][y] = 0;
        }
      }
      placingArray[this.placingShipRow][this.placingShipColumn] = shipType;
      shipPlacementValid = false;
    }

    return shipPlacementValid; // Should run placePlayerShip after this from controller.
  }

  /** Starts a new round by resetting all grids and setting gameState 
   * @author Rayhan E.
  */
  public void newRound() {
    this.gameState = STATE_SHOW_ROUND_STATS;
    this.updateView();
    this.gameState = STATE_PLACING_SHIPS;
    //Resetting Grids
    for (int x = 0; x < playerShipPlacingGrid.length; x++) {
      for (int y = 0; y < playerShipPlacingGrid.length; y++) {
        this.computerShipGrid[x][y] = 0;
        this.computerShipPlacingGrid[x][y] = 0;
        this.computerHitsAndMisses[x][y] = 0;
        this.playerShipGrid[x][y] = 0;
        this.playerHitsAndMisses[x][y] = 0;
      }
    } // Clears the ship grid

    this.generateComputerShipGrid();
    this.startPlacingShip(DESTROYER);
  }

  /** Restarts the game by resetting all grids and information
   * @author Rayhan E.
  */
  public void restartGame() {
    this.gameState = STATE_START_GAME;
    for (int x = 0; x < this.playerShipPlacingGrid.length; x++) {
      for (int y = 0; y < this.playerShipPlacingGrid.length; y++) {
        this.computerShipGrid[x][y] = 0;
        this.computerShipPlacingGrid[x][y] = 0;
        this.computerHitsAndMisses[x][y] = 0;
        this.playerShipGrid[x][y] = 0;
        this.playerHitsAndMisses[x][y] = 0;
      }
    } // Clears the ship grid
    this.numPlayerShips = 0;
    this.numComputerShips = 0;
    this.roundNum = 1;
    this.numRounds = 0;
    this.playerWins = 0;
    this.computerWins = 0;
    this.log = "";
    this.computerLog = "";
    this.totalLog = "Log: ";
    this.roundInfo = "";
    this.updateView();
  }

  /** Gets the log string about the Player's action */
  public String getLog() {
    return this.log;
  }

  /** Gets the log string about the Computer's action */
  public String getComputerLog() {
    return this.computerLog;
  }

  /** Sets the state of the game to an integer
   * @param gameState an integer related to the gameState
   */
  public void setGameState(int gameState) {
    this.gameState = gameState;
    this.updateView();
  }

  /** Get the current round number */
  public int getRoundsNum() {
    return this.roundNum;
  }

  /** Get the amount of Computer ships left */
  public int getNumComputerShips() {
    return this.numComputerShips;
  }

  /** Get the amount of Player ships left */
  public int getNumPlayerShips() {
    return this.numPlayerShips;
  }

  /** Get the amount of rounds left */
  public int getNumRounds() {
    return this.numRounds;
  }

  /** Get the total log of the game */
  public String getTotalLog() {
    return this.totalLog;
  }

  /** Gets the amount of times the Player has won in the game */
  public int getPlayerWins() {
    return this.playerWins;
  }

  /** Gets the amount of times the Computer has won in the game */
  public int getComputerWins() {
    return this.computerWins;
  }

  /** Gets the last game statistics file */
  public File getLastFile(){
    return this.currentFile;
  } 

  /** Gets the game you want to look at */
  public int getGameNumber()
  {
    return this.gameNumber;
  }

  /** Writes the game statistics (totalLog) to a file 
   * @author Rayhan E.
  */
  public void writeGameStatsToFile() {
    String filename = "Gamestats" + this.gameNumber + ".txt";
    this.currentFile = new File(filename);
    try {
      this.out = new PrintWriter(this.currentFile);
      out.println(this.roundInfo);
      this.gameNumber++;
      out.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace(); // Print the exception details for debugging
    }
  }

  /** Sets the old game statistics 
   * @param gameStatNum
  * @author Rayhan E.
  */
  public void setOldGameStats(int gameStatNum) {
    String filename = "Gamestats" + gameStatNum + ".txt";
    this.oldGameStatString = ""; // Initialize the string

    try {
      File file = new File(filename);
      Scanner in = new Scanner(file);

      while (in.hasNextLine()) {
        this.oldGameStatString += "\n" + in.nextLine();
      }

      in.close();
    } catch (FileNotFoundException e) {
      // Handle the exception, e.g., print an error message
      e.printStackTrace();
    }
    this.updateView();
  }

  public String getOldGameStatString() {
    return this.oldGameStatString;
  }

}
