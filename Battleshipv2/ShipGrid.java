import java.awt.*;
import javax.swing.*;

/**
 * ShipGrid class
 * A custom class designed to draw the shipGrid onto a JComponent
 * @since 01/24/2024
 * @author Rayhan.E (creator) and Aareez K. (added extra parameters and fixed drawing slightly)
 */
public class ShipGrid extends JComponent 
{
    private int[][] shipArray;
    private int[][] shipPlacingArray;
    private int[][] shipCHitAndMisses; // ship Computer Hit and Misses
    private BattleshipModel model;
    private boolean hasExtraGrids;

    /** Creates a new ShipGrid
     * @param model the BattleshipModel 
     * @param hasExtraGrid boolean checking if there were extraGrids passed
     * @param shipArray the ship Array to view
     * @param shipPlacingArray the ship Placing array to view
     * @param computerHitsAndMisses the Computer's hit and misses to view
    */
    public ShipGrid(BattleshipModel model, boolean hasExtraGrids, int[][] shipArray, int[][] shipPlacingArray, int[][] computerHitsAndMisses) 
    {
     super();
     this.setPreferredSize(new Dimension(400,400));
     this.model = model;
     if (hasExtraGrids == true) {
        this.hasExtraGrids = true;
        this.shipArray = shipArray;
        this.shipPlacingArray = shipPlacingArray;
        this.shipCHitAndMisses = computerHitsAndMisses;
     } else { // Otherwise its a hits and misses array
        this.hasExtraGrids = false;
        this.shipArray = shipArray;
     }
    }
    
    
    /** Updates the grid by passing updated Arrays
     * @param shipArray the shipArray to update 
     * @param shipPlacingArray the shipPlacingArray to update
    */
    public void updateGrid(int[][] shipArray, int[][] shipPlacingArray) {
        this.shipArray = shipArray;
        //this.shipArray = new int[10][10];
        //this.shipArray[5][5] = 1;
        this.shipPlacingArray = shipPlacingArray;
        repaint();
    }

    /** paints the JComponent by drawing a ship grid on it
     * @param g the Graphics class
     * @Override
    */
    public void paintComponent(Graphics g) 
    {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
        
            //Enable high-quality rendering
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            double scaleX = (double)this.getWidth()/22.0;
            double scaleY = (double)this.getHeight()/22.0;
            
            g2.setStroke(new BasicStroke(1.0F / this.getWidth()));
            g2.scale(scaleX,scaleY);

            ImageIcon waterIcon = new ImageIcon("water.png");
            Image waterImage = waterIcon.getImage();
            for(int row = 0;row<this.shipArray.length;row++)
            {
                for(int col = 0;col<this.shipArray[row].length;col++)
                {
                    if(shipArray[row][col] >= 1 && shipArray[row][col] <= 5)
                    {
                      g2.setColor(Color.GRAY);
                      g2.fillRect(2+row*2,2+col*2,2,2);
                    }
                    else if(shipArray[row][col] == this.model.HIT)
                    {
                        g2.drawImage(waterImage,2+row*2,2+col*2,2,2,this);
                        g2.setColor(Color.RED);
                        g2.fillOval(2+row*2,2+col*2,2,2);
                    }
                    else if(shipArray[row][col] == this.model.MISS)
                    {
                        g2.drawImage(waterImage,2+row*2,2+col*2,2,2,this);
                        g2.setColor(Color.WHITE);
                        g2.fillOval(2+row*2,2+col*2,2,2);
                    }
                    else if(shipArray[row][col] == this.model.EMPTY)
                    {
                        g2.drawImage(waterImage,2+row*2,2+col*2,2,2,this);
                    }
                    else if(shipArray[row][col] == this.model.DESTROYED_SHIP)
                    {
                        g2.setColor(Color.GRAY);
                        g2.fillRect(2+row*2,2+col*2,2,2);
                        g2.setColor(Color.RED);
                        g2.fillOval(2+row*2,2+col*2,2,2);
                    }
                }
            }
            
            if (this.hasExtraGrids == true) {
                for(int row = 0; row < this.shipPlacingArray.length; row++)
                {
                    for(int col = 0; col < this.shipPlacingArray.length; col++)
                    {
                        if (shipPlacingArray[row][col] >= 1 && shipPlacingArray[row][col] <= 5) {
                            g2.setColor(new Color(255, 105, 5));
                            g2.fillRect(2 + row * 2, 2 + col * 2, 2, 2);

                        // Lines below are for debugging
                        // We used this to spy on the Computer's hits and misses before adding it properly
                        // This code below will never run unless you change it in the View, by passing a ComputerShipGrid or ComputerHitsAndMisses in the last parameter of shipGrid
                        // this.playerHitsAndMisses = new ShipGrid(this.battleshipModel, true, this.battleshipModel.getPlayerHitsAndMisses(), this.battleshipModel.getComputerHitandMisses() );
                        } else if (shipPlacingArray[row][col] == this.model.HIT) {
                            g2.drawImage(waterImage, 2 + row * 2, 2 + col * 2, 2, 2, this);
                            g2.setColor(Color.RED);
                            g2.fillOval(2 + row * 2, 2 + col * 2, 2, 2);
                        } else if (shipPlacingArray[row][col] == this.model.MISS) {
                            g2.drawImage(waterImage, 2 + row * 2, 2 + col * 2, 2, 2, this);
                            g2.setColor(Color.WHITE);
                            g2.fillOval(2 + row * 2, 2 + col * 2, 2, 2);
                        }
                        
                    }
                }
                
                for (int row = 0; row < this.shipCHitAndMisses.length; row++) {
                    for (int col = 0; col < this.shipCHitAndMisses.length; col++) {
                        if (shipCHitAndMisses[row][col] == this.model.MISS) {
                            g2.drawImage(waterImage, 2 + row * 2, 2 + col * 2, 2, 2, this);
                            g2.setColor(Color.WHITE);
                            g2.drawOval(2 + row * 2, 2 + col * 2, 2, 2);
                        }
                    }
                } 
            }

            g2.setColor(Color.BLACK);
            for(int row = 0;row<22;row= row+2)
            {
                for(int col = 0;col<22;col=col+2)
                {
                    g2.drawRect(row,col,2,2);
                }
            }
            g2.drawRect(0,0,22,22);
            Font coord = new Font("Arial",Font.ITALIC,2);
            
            g2.setFont(coord);
            g2.setColor(Color.BLACK);
            
            g2.drawString("A", 2, 2);
            g2.drawString("B", 4, 2);
            g2.drawString("C", 6, 2);
            g2.drawString("D", 8, 2);
            g2.drawString("E", 10, 2);
            g2.drawString("F", 12, 2);
            g2.drawString("G", 14, 2);
            g2.drawString("H", 16, 2);
            g2.drawString("I", 19, 2);
            g2.drawString("J", 21, 2);

            g2.drawString("0",0,4);
            g2.drawString("1",0,6);
            g2.drawString("2",0,8);
            g2.drawString("3",0,10);
            g2.drawString("4",0,12);
            g2.drawString("5",0,14);
            g2.drawString("6",0,16);
            g2.drawString("7",0,18);
            g2.drawString("8",0,20);
            g2.drawString("9", 0, 22);
            //Clean up Graphics copy
            g2.dispose();
    }
}
