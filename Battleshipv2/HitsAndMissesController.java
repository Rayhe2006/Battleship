import javax.swing.*;
import java.awt.event.*;

/**
 * HitsAndMissesController Class
 * Manages Player hits and misses, by taking a coordinate and then launching the missile to it.
 * Last Modified: 01/24/2024
 * @author Rayhan E.
 */
public class HitsAndMissesController implements ActionListener {
    private BattleshipModel model;
    ShipGrid grid;
    private JTextField colAndRow;

    /** Starts a HitsAndMissesController
     * @param model the BattleshipModel
     * @param colAndRow the column and row to launch at 
     */
    public HitsAndMissesController(BattleshipModel model, JTextField colAndRow) {
        this.model = model;
        this.colAndRow = colAndRow;
    }

    /** Runs code on button press
     * @param e The button pressed
     */
    public void actionPerformed(ActionEvent e) {
        int column = 0;
        int row = 0;
        String[] alphabet = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
        try {
            row = Character.getNumericValue(this.colAndRow.getText().charAt(1));
            // Loops to check if the column(since its a letter) is in the array at which its
            // position is gained
            for (int x = 0; x < alphabet.length; x++) {
                if (alphabet[x].equalsIgnoreCase(String.valueOf(this.colAndRow.getText().charAt(0)))) {
                    column = x;
                    break;
                }
            }
            System.out.println("Launching missile at row: " + row + ", column: " + column);
            this.model.playerLaunchMissile(column, row);
            this.colAndRow.setText("");

        } catch (NumberFormatException f) {
            this.colAndRow.selectAll();
        }
    }
}
