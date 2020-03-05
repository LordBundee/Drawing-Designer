/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drawingdesigner;

import java.awt.Color;
import javax.swing.JTextField;

/**
 *
 * @author Troy
 */
public class ColorSwapDrawingMode implements IDrawingMode
{
    private JTextField[][] myGrid;
    private Color oldColor;
    private Color newColor;
    private JTextField target;
    
    private int gridX;
    private int gridY;
    
    public ColorSwapDrawingMode(JTextField[][] grid, int sizeX, int sizeY)
    {
        myGrid = grid;
        gridX = sizeX;
        gridY = sizeY;
    }

    @Override
    public void StartDrawing(Object source)
    {
         target = (JTextField)source;
         oldColor =target.getBackground();
         
         for (int i = 0; i < gridX; i++)
        {
            for (int j = 0; j < gridY; j++)
            {
                System.out.println(i + " , " + j);
                if (myGrid[i][j].getBackground() == oldColor)
                {
                    myGrid[i][j].setBackground(newColor);
                    
                }
            }
        }
    }

    @Override
    public void ContinueDrawing(Object source){}
   

    @Override
    public void EndDrawing(Object source)
    {
        
    }

    @Override
    public void SetColor(Object source)
    {
        newColor = (Color)source;
    }
    
}
