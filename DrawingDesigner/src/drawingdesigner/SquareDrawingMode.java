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

public class SquareDrawingMode implements IDrawingMode
{
    private Color currentColor;
    private JTextField target;
    private JTextField[][] grid;
    private Color[][] tempGrid;
    
    private int gridX;
    private int gridY;
    
    private int startSquareX;
    private int startSquareY;
    private int endSquareX;
    private int endSquareY;
    
    public SquareDrawingMode(JTextField[][] myGrid, int sizeX, int sizeY)
    {
        grid = myGrid;
        gridX = sizeX;
        gridY = sizeY;
    }

    @Override
    public void StartDrawing(Object source)
    {
        target = (JTextField)source;
        tempGrid = createTempArray();
        
        for (int i = 0; i < gridX; i++)
        {
            for (int j = 0; j < gridY; j++)
            {
                if (grid[i][j] == target)
                {
                    startSquareX = i;
                    startSquareY = j;
                }
            }
        }
    }

    @Override
    public void ContinueDrawing(Object source)
    {
        refreshGrid();
        
        target = (JTextField)source;
        for (int i = 0; i < gridX; i++)
        {
            for (int j = 0; j < gridY; j++)
            {
                if (grid[i][j] == target)
                {
                    endSquareX = i;
                    endSquareY = j;
                }
            }
        }
        DrawSquare();
    }
    
    @Override
    public void EndDrawing(Object source)
    {
        target = (JTextField)source;

        DrawSquare();
    }

    @Override
    public void SetColor(Object source)
    {
        currentColor = (Color)source;   
    }
    
    private void DrawSquare()
    {
        int lowerX;
        int upperX;
        int lowerY;
        int upperY;
        
        if (startSquareX > endSquareX)
        {
            lowerX = endSquareX;
            upperX = startSquareX;
        }
        else
        {
            lowerX = startSquareX;
            upperX = endSquareX;
        }
        
        if (startSquareY > endSquareY)
        {
            lowerY = endSquareY;
            upperY = startSquareY;
        }
        else
        {
            lowerY = startSquareY;
            upperY = endSquareY;
        }
        
        for (int i = 0; i < gridX; i++)
        {
            for (int j = 0; j < gridY; j++)
            {
                if ((i >= lowerX  && i <= upperX)&&(j >= lowerY  && j <= upperY))
                {
                        grid[i][j].setBackground(currentColor);
                }
            }
        }
    } 
    
    private Color[][] createTempArray()
    {
        Color[][] temp = new Color[gridX][gridY];
        for (int i = 0; i < gridX ; i++)
        {
            for (int j = 0; j < gridY ; j++)
            {
                temp[j][i] = grid[j][i].getBackground();
            }
        }
        return temp;
    }
    
    private void refreshGrid()
    {
        for (int i = 0; i < gridX; i++)
        {
            for (int j = 0; j < gridY; j++)
            {
                grid[i][j].setBackground(tempGrid[i][j]);
            }
        }
    }
}
