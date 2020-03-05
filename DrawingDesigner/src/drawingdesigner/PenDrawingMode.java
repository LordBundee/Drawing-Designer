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
public class PenDrawingMode implements IDrawingMode
{
    private JTextField target;
    private Color currentColor = Color.BLUE;
    
    @Override
    public void StartDrawing(Object source)
    {
        target = (JTextField)source;
        target.setBackground(currentColor);
    }

    @Override
    public void ContinueDrawing(Object source)
    {
        target = (JTextField)source;
        target.setBackground(currentColor);
    }

    @Override
    public void EndDrawing(Object source)
    {
        target = null;
    }

    @Override
    public void SetColor(Object source)
    {
        currentColor = (Color)source;
    }
    
}
