# Battleship Java

A desktop Battleship game written in **Java** with a **Swing** graphical interface. You place your fleet on a 10x10 grid, then trade missiles with a computer opponent that hunts for your ships and follows up after a hit. Games can run for several rounds, and the results of each game are saved to a text file you can review later.

## Features

- **Player vs. computer** on a 10x10 grid with the classic five-ship fleet
- **Keyboard ship placement**: move, rotate, and drop each ship with the arrow keys, `R`, and `Enter`; invalid placements (off the grid or overlapping another ship) are rejected
- **Randomly placed enemy fleet**, regenerated every round
- **Computer opponent** that fires randomly until it scores a hit, then probes the neighbouring squares to follow the ship until it is sunk
- **Multi-round matches**: choose how many rounds to play, with a wins tally and an end-of-round summary showing the log
- **Game history**: the log of each finished game is written to `Gamestats<N>.txt` and can be opened again from the start screen with *View Old Game Stats*
- Custom-drawn grids (`ShipGrid`) with a water texture, grey ships, red hits, and white misses, plus a live log of the last player and computer moves

## How to Play

1. Enter the number of rounds you want to play and click **Start Game**.
2. **Place your ships.** Five ships are placed one at a time, in this order:

   | Ship | Length |
   | --- | --- |
   | Destroyer | 2 |
   | Submarine | 3 |
   | Cruiser | 3 |
   | Battleship | 4 |
   | Carrier | 5 |

   | Key | Action |
   | --- | --- |
   | Arrow keys | Move the ship around the grid |
   | `R` | Rotate between horizontal and vertical |
   | `Enter` | Place the ship (rejected if it is off the grid or overlaps another ship) |

3. **Launch missiles.** Type a coordinate in the box, a letter for the column followed by a digit for the row (for example `A1` or `J9`; letters run A to J and digits 0 to 9), then press **Launch Missile** or `Enter`. The computer fires back after every shot.
4. Sink all five enemy ships before the computer sinks yours. After the last round, the game saves its log and returns to the start screen.

On the grids, a red dot is a hit, a white dot is a miss, and grey squares are ships.

## Getting Started

### Requirements

- Java Development Kit (JDK) 8 or newer. The game uses only the standard library (Swing/AWT), so there is nothing else to install.

### Build and run

The game loads `water.png` and writes its stat files using relative paths, so **run it from inside the `Battleshipv2` folder**:

```bash
cd Battleshipv2
javac *.java
java BattleshipMain
```

You can also open the `Battleshipv2` folder in an IDE such as VS Code or IntelliJ and run `BattleshipMain`, as long as the working directory is `Battleshipv2`.

## How It Works

The project follows a **Model-View-Controller** structure.

| File | Role |
| --- | --- |
| `BattleshipMain.java` | Entry point: sets the Nimbus look and feel, builds the window, and wires the model, view, and keyboard controller together |
| `BattleshipModel.java` | The model: all game state and rules, including ship placement and validation, missile resolution, win detection, round handling, and reading and writing stats files |
| `BattleshipView.java` | The view: a `CardLayout` with the start, battle, and old-stats screens, which redraws whenever the model changes |
| `ShipGrid.java` | A custom `JComponent` that draws a 10x10 grid with ships, hits, misses, water, and coordinate labels |
| `KeyController.java` | Handles the keyboard during ship placement (arrows, `R`, `Enter`) |
| `HitsAndMissesController.java` | Turns the typed coordinate (such as `A1`) into a missile launch |
| `StartController.java` | Handles Start Game, Restart Game, View Old Game Stats, and Exit |
| `OldGameStats.java` | Loads a previous game's stats file by game number |
| `PlaceshipsController.java` | Button-based ship placement from an earlier version of the interface (no longer wired up) |
| `water.png` | Water texture for the grids |

Each grid is a `10x10` integer array, and the model uses named constants for ship types, hit, miss, and destroyed-ship cells, and for the game state (start screen, placing ships, in game, round stats, game over, old stats). The view reads the model and repaints whenever the model changes.

### The computer opponent

The computer plays in two modes:

1. **Hunt:** it picks random squares it has not tried yet until a shot hits a ship.
2. **Target:** once it hits, it remembers that square and probes in a fixed order: up, then down, then left, then right. It keeps going in a direction while the shots keep hitting, and switches to the next direction when it misses or reaches the edge of the grid. When a ship is destroyed, it goes back to hunting.

The enemy fleet is placed by repeatedly picking a random position and direction until the placement is valid.

### Saved game logs

When the final round ends, the model writes a summary of each round (the move log, the number of ships left, and who won) to `Gamestats<N>.txt` in the working directory. Use **View Old Game Stats** on the start screen and enter the game number to read one back.

## Project Structure

```
Battleship/
├── README.md
└── Battleshipv2/
    ├── BattleshipMain.java
    ├── BattleshipModel.java
    ├── BattleshipView.java
    ├── ShipGrid.java
    ├── KeyController.java
    ├── HitsAndMissesController.java
    ├── StartController.java
    ├── OldGameStats.java
    ├── PlaceshipsController.java
    └── water.png
```

## Possible Improvements

- Make the computer smarter: probability-based targeting, parity (checkerboard) hunting, and handling ships that touch each other
- Let the player click on the grid to fire instead of typing coordinates
- Add difficulty levels, sound effects, and an on-screen win or loss animation
- Save stats in a dedicated folder and load water.png from the classpath so the game can run from any directory

## Contributors

Built by **Rayhan Etwaree** and **Aareez K.**

- **Rayhan Etwaree:** Swing view and custom grid rendering, keyboard and button controllers, enemy ship generation, player missile logic, ship-destroyed detection, round handling, and the saved stats and old-stats viewer
- **Aareez K.:** core game model, ship placement validation, the computer's missile targeting, and the interface revamp
