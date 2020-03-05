/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drawingdesigner;

/**
 *
 * @author Troy
 */
public interface IDrawingMode
{
    public void StartDrawing(Object source);
    
    public void ContinueDrawing(Object source);
    
    public void EndDrawing(Object source);   
    
    public void SetColor(Object source);
}