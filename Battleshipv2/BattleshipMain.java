import javax.swing.UIManager.*;
import javax.swing.*;
import java.awt.*;

/** Battleship Program
  * Play a game of Battleship against a smart Computer
  * @since 01/24/2024
  * @author Aareez K, Rayhan E.
  */

public class BattleshipMain {
  public static void main(String[] args) {
    // Code from https://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/nimbus.html
    try {
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (Exception e) {
      // If Nimbus is not available, you can set the GUI to another look and feel.
    }

    BattleshipModel model = new BattleshipModel(); // Creates a new BattleshipModel
    JFrame frame = new JFrame("Battleship Java");
    KeyController keyController = new KeyController(model);
    BattleshipView view = new BattleshipView(model, frame); // Creates a new BattleshipView and passes the model
    frame.addKeyListener(keyController);
    frame.setFocusable(true);
    frame.setContentPane(view);
    frame.pack();
    frame.setLocation(0, 0);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    frame.setSize(600,500);
  }
}
