import java.awt.*;
import javax.swing.*;

/**
 * BattleshipView class
 * The view which has all the GUI components
 * Last Modified: 01/24/2024
 * @author Rayhan.E (creator) & Aareez K. (revamped ui and cleaned code slightly)
 */
public class BattleshipView extends JPanel {
  //Instance variables
  private BattleshipModel battleshipModel; //The game model
 
  private ShipGrid playerShipGrid; // A ship grid for the player
  private ShipGrid playerHitsAndMisses;// A hit and misses grid for the player
  // First screen
  private JTextField roundsOfPlay = new JTextField(2); // TextField for the amount of rounds the user wants to play
  private JButton startGame = new JButton(); // =]utton to start the game
  private JPanel oldGameStatsPanel = new JPanel(); //The panel that the old game stats JButton
  private JButton oldGameStats = new JButton("View Old Game Stats"); // Button to display the old game stats
 
  // Game info
  private JTextField colAndRow = new JTextField(2); // JTextField for the column & row
  private JButton launchMissile = new JButton("Launch Missile"); // Button to launch a missile
  private JLabel roundNumberLabel = new JLabel("Round: "); // Displays the number of rounds
  private JLabel logLabel = new JLabel("Logs:"); // Logs the computer and player actions
 
  private JButton restartGame = new JButton("Restart Game"); // Restarts the game
  private JPanel instructionsPanel = new JPanel();

  private JPanel endRoundStats = new JPanel();
  private JLabel numComputerShips = new JLabel("Num Computer Ships: ");
  private JLabel numPlayerShips = new JLabel("Num Computer Ships: ");
  private JLabel numberOfComputerShips = new JLabel("Number of Computer Ships: ");
  private JLabel numberOfPlayerShips = new JLabel("Number of Player Ships: ");
  private JTextArea logArea = new JTextArea(5,5);
  private JLabel roundsLeft = new JLabel();
  private JLabel playerWins = new JLabel();
  private JLabel computerWins = new JLabel();
  private JLabel computerLog = new JLabel();
  private JTextField gameNumber = new JTextField(20);
  private JTextArea oldGameStatsArea = new JTextArea(5,5);
  private JScrollPane oldGScrollPane = new JScrollPane(oldGameStatsArea);
  private JPanel labelsPanel = new JPanel();
  private JButton exitOldGameStats = new JButton("Exit");

  // Panels that will be changed in update method
  private JPanel startPanel = new JPanel();
  private JPanel gameInfo = new JPanel();
  private JPanel battleScreen = new JPanel();
  private JPanel placeShipsPanel = new JPanel();
  private JPanel gameNumPanel = new JPanel();
  private JFrame mainFrame;
  /**
   * Default constructor
   * Links the component to the Model
   * 
   * @param model The current Game model
   * @author Rayhan.E
   */
  public BattleshipView(BattleshipModel model, JFrame mainFrame) {
    super();
    this.battleshipModel = model;
    this.mainFrame = mainFrame;
    this.battleshipModel.setGUI(this);
    KeyController keyController = new KeyController(model);
    this.addKeyListener(keyController);
    this.setFocusable(true);
    this.playerShipGrid = new ShipGrid(this.battleshipModel, true, this.battleshipModel.getPlayerShipGrid(), this.battleshipModel.getPlayerShipPlacingGrid(), this.battleshipModel.getComputerHitandMisses());
    this.playerHitsAndMisses = new ShipGrid(this.battleshipModel, false, this.battleshipModel.getPlayerHitsAndMisses(), null, null);

   
    // Debugging line below
    // this.playerHitsAndMisses = new ShipGrid(this.battleshipModel, true, this.battleshipModel.getPlayerHitsAndMisses(), this.battleshipModel.getComputerHitandMisses());
   
    //this.computerShipGrid = new ShipGrid(this.battleshipModel, false, this.battleshipModel.getComputerShipGrid(), this.battleshipModel.getComputerShipPlacingGrid());
    //this.computerShipGrid = new ShipGrid(this.battleshipModel, false, this.battleshipModel.getComputerShipGrid(), this.battleshipModel.getComputerShipPlacingGrid());
    this.layoutView();
    this.registerControllers();
    this.update();
  }

  /**
   * Draws the layout for the GUI
   * @author Rayhan.E
   */
  public void layoutView() {
    // Setting the main layout to a border layout
    this.setLayout(new CardLayout());
    // First screen
    JPanel startGamePanel = new JPanel();
    JPanel statsPanel = new JPanel();
    JLabel title = new JLabel("Battleship Java");
    JPanel titlePanel = new JPanel();
    JPanel roundsOfPlayPanel = new JPanel();
    JLabel roundsLabel = new JLabel("Rounds to Play:");
    JLabel enterGameNum = new JLabel("Enter Game Number: ");
    JPanel enterGameNumPanel = new JPanel();
    Font basicFont = new Font("Arial", Font.PLAIN, 20);
    JPanel buttonPanel = new JPanel();
    // Second screen
    JPanel gridPanel = new JPanel();
    JPanel shipGridPanel = new JPanel();
    JPanel hitsAndMissesPanel = new JPanel();
    // First screen
    // Setting the main first screen panel to a box layout
    startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.PAGE_AXIS));
    // Setting font of a JLabel
    roundsLabel.setFont(basicFont);
    // Setting the font of the JTextField
    this.roundsOfPlay.setFont(basicFont);
    // Setting preferred sizes of JComponents
    this.roundsOfPlay.setPreferredSize(new Dimension(100, 100));
    oldGameStats.setPreferredSize(new Dimension(600, 100));
    oldGameStats.setFont(new Font("Arial", Font.PLAIN, 40));

    // Adding components to JPanel so that we can modify its font
    roundsOfPlayPanel.add(roundsOfPlay);
    oldGameStatsPanel.add(oldGameStats);

    // Setting the title panel, which is used for the title to a flow layout
    titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

    // Setting font and size and, alligning the title to the middle
    title.setFont(new Font("SansSerif", Font.BOLD, 40));
    title.setHorizontalAlignment(JLabel.CENTER);
    title.setVerticalAlignment(JLabel.CENTER);

    // Adding Battleship Java title to a title JPanel
    titlePanel.add(title);

    // Setting the Start game buttons size, border, backround, text and if its
    startGame.setPreferredSize(new Dimension(500, 200));
    startGame.setBorder(BorderFactory.createEtchedBorder());
    startGame.setBackground(new Color(50, 168, 88));
    startGame.setText("Start Game");
    startGame.setFont(new Font("SansSerif", Font.PLAIN, 30));

    // Setting the start game panel which is the panel that contain0s the start game
    startGamePanel.setLayout(new BoxLayout(startGamePanel, BoxLayout.PAGE_AXIS));

    // Adding Start Game Button to button panel
    buttonPanel.add(startGame);

    // Adding title panel and buttonpanel to startGame panel
    startGamePanel.add(titlePanel);
    startGamePanel.add(buttonPanel);

    // Adding stats related JComponents on the first screen to the stats panel
    statsPanel.add(roundsLabel);
    statsPanel.add(roundsOfPlayPanel);
    statsPanel.add(oldGameStatsPanel);

    // Adding panels to the main panel for the first screen
    startPanel.add(startGamePanel); // The Title and the Start game button
    startPanel.add(statsPanel); // The Rounds of play JLabel, TextField and View Old Game stats Button

    // Second screen grids
    // Setting borders to grid panels
    shipGridPanel.setBorder(BorderFactory.createTitledBorder("Your Ship Grid"));
    hitsAndMissesPanel.setBorder(BorderFactory.createTitledBorder("Hits and Misses"));

    // Adding ship grids to their panels(Added to panels to be able to add a border to them
    shipGridPanel.add(this.playerShipGrid);
    hitsAndMissesPanel.add(this.playerHitsAndMisses);

    // Adding both grid panels to a main grid panel
    gridPanel.add(shipGridPanel);
    gridPanel.add(hitsAndMissesPanel);
    //Setting the gridPanel border
    gridPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,2));
    // Second screen placing ships panel
    // Setting place ships panel to a box layout
    placeShipsPanel.setLayout(new BoxLayout(placeShipsPanel, BoxLayout.PAGE_AXIS));

    // Setting the game info JPanel to a box layout
    gameInfo.setLayout(new BoxLayout(gameInfo, BoxLayout.PAGE_AXIS));
    // Setting its border
    gameInfo.setBorder(BorderFactory.createTitledBorder("Game Info"));
    gameInfo.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
    // Adding game info to its JPanel
    gameInfo.add(this.roundNumberLabel);
    gameInfo.add(this.logLabel);
    gameInfo.add(this.computerLog);
    gameInfo.add(this.numPlayerShips);
    gameInfo.add(this.numComputerShips);
    gameInfo.add(this.colAndRow);
    gameInfo.add(this.launchMissile);
    gameInfo.add(this.restartGame);


    //Creating a a splitPane so that scaling issues are fixed
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    splitPane.setTopComponent(gridPanel);
    splitPane.setBottomComponent(gameInfo);
    splitPane.setResizeWeight(0.8); // 80% top (grids), 20% bottom (info)
    splitPane.setDividerSize(4);
    splitPane.setContinuousLayout(true);

    //Making the instructionsPanel look better
    instructionsPanel.setLayout(new BoxLayout(instructionsPanel, BoxLayout.Y_AXIS));
    instructionsPanel.setBackground(new Color(30,50,90));
    instructionsPanel.setPreferredSize(new Dimension(100, 120));

    JLabel instructionsLabel = new JLabel("Welcome to Battleship!");
    instructionsLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
    instructionsLabel.setForeground(Color.WHITE);
    instructionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel line1 = new JLabel("Use arrow keys to move ships around.");
    line1.setFont(new Font("SansSerif", Font.BOLD, 20));
    line1.setForeground(Color.WHITE);
    line1.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel line2 = new JLabel("Use the r key to rotate ships and enter key to place them");
    line2.setFont(new Font("SansSerif", Font.BOLD, 20));
    line2.setForeground(Color.WHITE);
    line2.setAlignmentX(Component.CENTER_ALIGNMENT);

    instructionsPanel.add(instructionsLabel);
    instructionsPanel.add(Box.createVerticalStrut(15)); // blank line
    instructionsPanel.add(line1);
    instructionsPanel.add(Box.createVerticalStrut(10)); // another blank line
    instructionsPanel.add(line2);

    battleScreen.setLayout(new BorderLayout());
    battleScreen.add(splitPane, BorderLayout.CENTER);
    battleScreen.add(instructionsPanel, BorderLayout.SOUTH);

    //Show old game stats
    oldGScrollPane.setPreferredSize(new Dimension(450,110));
    enterGameNumPanel.add(enterGameNum);
    enterGameNumPanel.add(gameNumber);
    gameNumPanel.add(enterGameNumPanel);
    gameNumPanel.add(oldGScrollPane);
    gameNumPanel.add(exitOldGameStats);
    oldGameStatsArea.setEnabled(true);
    // Adding start screen and battle screen to the View
    this.add(startPanel, "Start");
    this.add(battleScreen, "Battle");
    this.add(gameNumPanel, "Stats");
    // Visibility statements used to switch views
    startPanel.setVisible(true);
    gameInfo.setVisible(false);
    placeShipsPanel.setVisible(false);
    battleScreen.setVisible(false);
    gameNumPanel.setVisible(false);

    this.displayEndRoundStats();
    endRoundStats.setVisible(false);
  }

  /**
   * Assigns the controllers to the buttons.
   * @author Rayhan.E
   */
  public void registerControllers() {
    //Creating Start Controller
    StartController startController = new StartController(this.battleshipModel, this.roundsOfPlay, this.gameNumber);
    //Adding action listener to its components(JButton, Restart Game button, old game stats)
    this.startGame.addActionListener(startController);
    this.restartGame.addActionListener(startController);
    this.oldGameStats.addActionListener(startController);
    this.gameNumber.addActionListener(startController);
    this.exitOldGameStats.addActionListener(startController);

    //Creating old game stats controller
    OldGameStats oldGameStatsController = new OldGameStats(this.battleshipModel, this.gameNumber);
    this.gameNumber.addActionListener(oldGameStatsController);
    //Adding action listener to JTextField
    //Creating hits and misses controller
    HitsAndMissesController hitsAndMissesController = new HitsAndMissesController(battleshipModel, this.colAndRow);
    this.launchMissile.addActionListener(hitsAndMissesController);
    this.colAndRow.addActionListener(hitsAndMissesController);
  }

  /**
   * Updates JComponents when they need to be changed
   * @author Rayhan.E
   */
  public void update() {
    this.setFocusable(true);
    //Updating the players ship grid and hits and misses grid
    this.playerShipGrid.updateGrid(this.battleshipModel.getPlayerShipGrid(), this.battleshipModel.getPlayerShipPlacingGrid());
    this.playerHitsAndMisses.updateGrid(this.battleshipModel.getPlayerHitsAndMisses(), this.battleshipModel.getComputerShipGrid());
    //Setting all game info to its appropriate text by getting information from the model
    this.oldGameStatsArea.setText(this.battleshipModel.getOldGameStatString());
    this.logLabel.setText(this.battleshipModel.getLog());
    //Setting the game info JLabel for number of ships
    this.numComputerShips.setText("Number of computer ships: " + this.battleshipModel.getNumComputerShips());
    this.numPlayerShips.setText("Number of player ships: " + this.battleshipModel.getNumPlayerShips());
    //Setting the round info JLabel for number of ships
    this.numberOfComputerShips.setText("Number of computer ships: " + this.battleshipModel.getNumComputerShips());
    this.numberOfPlayerShips.setText("Number of player ships: " + this.battleshipModel.getNumPlayerShips());
    this.roundsLeft.setText("Rounds left: " + this.battleshipModel.getNumRounds());
    this.logArea.setText(this.battleshipModel.getTotalLog());
    this.roundNumberLabel.setText("Round: " + this.battleshipModel.getRoundsNum());
    this.playerWins.setText("Player wins: " + this.battleshipModel.getPlayerWins());
    this.computerWins.setText("Computer win: " + this.battleshipModel.getComputerWins());
    this.computerLog.setText(this.battleshipModel.getComputerLog());
    // Visiblity set by Game State
    //If the game state is in the start game game state
    if (this.battleshipModel.getGameState() == this.battleshipModel.STATE_START_GAME) {
      this.startPanel.setVisible(true);
      this.gameInfo.setVisible(false);
      this.placeShipsPanel.setVisible(false);
      this.battleScreen.setVisible(false);
      gameNumPanel.setVisible(false);
      //Else if the game state is in the placing ships game state
    } else if (this.battleshipModel.getGameState() == this.battleshipModel.STATE_PLACING_SHIPS) {
      this.startPanel.setVisible(false);
      this.gameInfo.setVisible(false);
      this.placeShipsPanel.setVisible(true);
      this.battleScreen.setVisible(true);
      gameNumPanel.setVisible(false);
      this.instructionsPanel.setVisible(true);
      //Else if the game state is in in game state
    } else if (this.battleshipModel.getGameState() == this.battleshipModel.STATE_IN_GAME) {
      this.startPanel.setVisible(false);
      this.gameInfo.setVisible(true);
      this.placeShipsPanel.setVisible(false);
      this.battleScreen.setVisible(true);
      gameNumPanel.setVisible(false);
      this.instructionsPanel.setVisible(false);
      //Else if the game state is in the game over state
    } else if (this.battleshipModel.getGameState() == this.battleshipModel.STATE_GAME_OVER) {
      this.startPanel.setVisible(true);
      this.gameInfo.setVisible(false);
      this.placeShipsPanel.setVisible(false);
      this.battleScreen.setVisible(false);
      gameNumPanel.setVisible(false);
      //Else if the game state is in the game over state
    } else if (this.battleshipModel.getGameState() == this.battleshipModel.STATE_SHOW_ROUND_STATS) {
      this.endRoundStats.setVisible(true);
      JOptionPane.showMessageDialog(null, this.endRoundStats, "End Round Stats", JOptionPane.INFORMATION_MESSAGE);
    }
    else if(this.battleshipModel.getGameState() == this.battleshipModel.STATE_SHOW_OLD_GAME_STATS)
    {
      this.startPanel.setVisible(false);
      this.gameInfo.setVisible(false);
      this.placeShipsPanel.setVisible(false);
      this.battleScreen.setVisible(false);
      gameNumPanel.setVisible(true);
    }
    

  }
/**
   * Displays the end round stats 
   * @author Rayhan E.
   */
  public void displayEndRoundStats() {
    JScrollPane logJScrollPane = new JScrollPane(this.logArea);
    logJScrollPane.setPreferredSize(new Dimension(450,110));
  
    this.logArea.setEditable(false);
    labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.Y_AXIS));
    labelsPanel.add(numberOfPlayerShips);
    labelsPanel.add(numberOfComputerShips);
    labelsPanel.add(roundsLeft);
    labelsPanel.add(playerWins);
    labelsPanel.add(computerWins);
    endRoundStats.setLayout(new BoxLayout(endRoundStats, BoxLayout.X_AXIS));
    endRoundStats.add(labelsPanel);
    endRoundStats.add(logJScrollPane);
  }
}