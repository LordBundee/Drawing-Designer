/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drawingdesigner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import javax.swing.*;

/**
 *
 * @author Troy Vaughn
 */
public class DrawingDesigner extends JFrame implements KeyListener, ActionListener
{
    private int sizeX;
    private int sizeY;
    
    private String dataFileName = "Drawing.CSV";
    
    private Color currentColor = Color.WHITE;
    private Boolean isMouseDown = false;
    
    private JTextField[][] grid;
    
    private JButton btnClear,btnRotate,btnFlipHori,btnFlipVert,btnSaveRAF,btnSave,btnExit;
    private JButton btnPenMode, btnBoxMode, btnSwapMode;
    private JButton btnRed,btnBlue,btnYellow,btnGreen,btnEraser;
    
    private IDrawingMode drawMode;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        DrawingDesigner myDrawingDesigner = new DrawingDesigner();
        myDrawingDesigner.run();         
    }
    
     private void run()
    {
        CalculateGridSize();
        setBounds(20,20,900,700);
        setTitle("Drawing Designer");
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                saveToCSVFile();
                System.exit(0);
            }
        });
        
        createGUI();
        setDefaults();
        setVisible(true);   
    }
     
    private void CalculateGridSize()
    {
        try
        {
            BufferedReader read = new BufferedReader(new FileReader(dataFileName));              
            if((read.readLine()) != null)
            {
                String temp[] = read.readLine().split(",");
                sizeX = temp.length;
            }
            sizeY = sizeX;
            read.close();
        } catch (Exception e)
        {
            System.err.println("Error Reading File Size: " + e.getMessage());
        }
    } 
     
    //<editor-fold defaultstate="collapsed" desc="GUI Setup"> 
     private void createGUI()
    {
        grid = new JTextField[sizeX][sizeY];
        SpringLayout mySpringLayout = new SpringLayout();
        setLayout(mySpringLayout);
        createDrawingGrid(mySpringLayout);
        createButtons(mySpringLayout);
        setButtonPreferences();
        
        readFileFromCSV();
        //readFileFromRAF();   
    }
     
    private void setDefaults()
    {
        currentColor = Color.BLUE;
        drawMode = new PenDrawingMode();
    }
     
    private void createDrawingGrid(SpringLayout layout)
    {
        for (int y = 0; y < sizeY; y++)
        {
            for (int x = 0; x < sizeX; x++)
            {
                int xPos = x * 19 + 200;
                int yPos = y * 19 + 10 ;   
                grid[x][y] = LibraryComponents.LocateAJTextField(this,this, layout,0, xPos, yPos);
                grid[x][y].setPreferredSize(new Dimension(20,20));
                grid[x][y].setBackground(Color.WHITE);
                grid[x][y].setEditable(false);
                grid[x][y].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent ae)
                {
                    isMouseDown = true;
                    drawMode.SetColor(currentColor);
                    drawMode.StartDrawing((JTextField)ae.getSource());
                }
                @Override
                public void mouseReleased(MouseEvent ae)
                {
                    drawMode.EndDrawing((JTextField)ae.getSource());
                    isMouseDown = false;
                }
                @Override
                public void mouseEntered(MouseEvent ae)
                {
                    if (isMouseDown)
                    {
                    drawMode.ContinueDrawing((JTextField)ae.getSource());   
                    } 
                }
                });
               
            }
        }   
    }
    
    private void createButtons(SpringLayout layout)
    {
        btnClear = LibraryComponents.LocateAJButton(this, this, layout, "Clear", 20, 620, 110, 25);
        btnRotate = LibraryComponents.LocateAJButton(this, this, layout, "Rotate90", 140, 620, 110, 25);
        btnFlipHori = LibraryComponents.LocateAJButton(this, this, layout, "FlipHorizontal", 260,620, 110, 25);
        btnFlipVert = LibraryComponents.LocateAJButton(this, this, layout, "FlipVertical", 380, 620, 110, 25);
        btnSave = LibraryComponents.LocateAJButton(this, this, layout, "Save", 500, 620, 110, 25);
        btnSaveRAF = LibraryComponents.LocateAJButton(this, this, layout, "SaveRAF", 620, 620, 110, 25);
        btnExit = LibraryComponents.LocateAJButton(this, this, layout, "Exit", 740, 620, 110, 25); 
        
        btnPenMode = LibraryComponents.LocateAJButton(this, this, layout, "Pen Mode", 20, 20, 110, 25); 
        btnBoxMode = LibraryComponents.LocateAJButton(this, this, layout, "Box Mode", 20, 50, 110, 25); 
        btnSwapMode = LibraryComponents.LocateAJButton(this, this, layout, "Color Swap", 20, 80, 110, 25); 
        
        btnRed = LibraryComponents.LocateAJButton(this, this, layout, "", 20, 110, 150, 50);
        btnBlue = LibraryComponents.LocateAJButton(this, this, layout, "", 20, 170, 150, 50);
        btnYellow = LibraryComponents.LocateAJButton(this, this, layout, "", 20, 230, 150, 50);
        btnGreen = LibraryComponents.LocateAJButton(this, this, layout, "", 20, 290, 150, 50); 
        btnEraser = LibraryComponents.LocateAJButton(this, this, layout, "", 20, 350, 50, 50); 
       
    } 
    
    private void setButtonPreferences()
    {
        btnRed.setBackground(Color.RED);
        btnBlue.setBackground(Color.BLUE);
        btnYellow.setBackground(Color.YELLOW);
        btnGreen.setBackground(Color.GREEN);
    }
    //</editor-fold>
    
    @Override
    public void keyTyped(KeyEvent ke){}
   
    @Override
    public void keyPressed(KeyEvent ke){}
 
    @Override
    public void keyReleased(KeyEvent ke){}

    //<editor-fold defaultstate="collapsed" desc="Button Events"> 
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getSource() == btnClear)
        {
            clearDrawing();
        }
        
        if (ae.getSource() == btnExit)
        {
            saveToCSVFile();
            System.exit(0);
        }
        
        if (ae.getSource() == btnPenMode)
        {
            drawMode = new PenDrawingMode();
        }
        
        if (ae.getSource() == btnBoxMode)
        {
            drawMode = new SquareDrawingMode(grid, sizeX, sizeY);
        }
        
        if (ae.getSource() == btnSwapMode)
        {
            drawMode = new ColorSwapDrawingMode(grid, sizeX, sizeY);
        }
        
        if (ae.getSource() == btnRed)
        {
            currentColor = Color.RED;
        }
        
        if (ae.getSource() == btnBlue)
        {
            currentColor = Color.BLUE;
        }
        
        if (ae.getSource() == btnYellow)
        {
            currentColor = Color.YELLOW;
        }
        
        if (ae.getSource() == btnGreen)
        {
            currentColor = Color.GREEN;
        }
        
        if (ae.getSource() == btnEraser)
        {
            currentColor = Color.WHITE;
        }
        
        if (ae.getSource() == btnRotate)
        {
            rotateDrawing();
        }
        if (ae.getSource() == btnFlipHori)
        {
            flipHorizontally();
        }
        if (ae.getSource() == btnFlipVert)
        {
            flipVertically();
        }
        if (ae.getSource() == btnSaveRAF)
        {
            writeFileToRAF();
        }
        if (ae.getSource() == btnSave)
        {
            saveToCSVFile();
            saveToTXTFile();
            saveToABCFile();
            
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Drawing Manipulation"> 
    private void clearDrawing()
    {
        for (int i = 0; i < sizeY; i++)
        {
            for (int j = 0; j < sizeX; j++)
            {
                grid[i][j].setBackground(Color.WHITE);
            }
        }
    }
    
    private void flipHorizontally()
    {
        Color[][] temp = createTempArray();
        
        for (int i = 0; i < sizeY; i++)
        {
            for (int j = 0; j < sizeX; j++)
            {   
                grid[(sizeX-1) - j][i].setBackground(temp[j][i]);
            }
        }
    }
    
    private void flipVertically()
    {
        Color[][] temp = createTempArray();
        
        for (int i = 0; i < sizeY; i++)
        {
            for (int j = 0; j < sizeX; j++)
            {
                grid[j][(sizeY - 1) - i].setBackground(temp[j][i]);
            }
        }
    }
    
    private void rotateDrawing()
    {
        Color[][] temp = createTempArray();
        for (int i = 0; i < sizeY; i++)
        {
            for (int j = 0; j < sizeX; j++)
            {
                grid[(sizeX -1) - j][i].setBackground(temp[i][j]);
            }
        }
    }
    
    private Color[][] createTempArray()
    {
        Color[][] temp = new Color[sizeX][sizeY];
        for (int i = 0; i < sizeX ; i++)
        {
            for (int j = 0; j < sizeY ; j++)
            {
                temp[j][i] = grid[j][i].getBackground();
                
            }
        }
        return temp;
    }
    
    //</editor-fold> 
    
    //<editor-fold defaultstate="collapsed" desc="File Management">  
      public void readFileFromCSV()
    {
        try
        {
            BufferedReader read = new BufferedReader(new FileReader(dataFileName));
            for (int i = 0; i < sizeY; i++)
            {
                String temp[] = read.readLine().split(",");
                for (int j = 0; j < sizeX; j++)
                {
                    grid[j][i].setBackground(new Color(Integer.parseInt(temp[j])));
                }
            }
            read.close();
        }
        catch(Exception e)
        {
            System.err.println("Error Reading .CSV File: " + e.getMessage());
        }
    }
    
    private void saveToCSVFile()
    {
        try
        {
            BufferedWriter out = new BufferedWriter(new FileWriter(dataFileName));
                    
            for (int i = 0; i < sizeY; i++)
            {
                String saveString = "";
                
                for (int j = 0; j < sizeX; j++)
                {
                    saveString += grid[j][i].getBackground().getRGB();
                    if (j < sizeX -1)
                    {
                        saveString += ",";
                    }
                }
                out.write(saveString);
                out.newLine();
            }
            out.close();
        }
        catch(Exception e)
        {
            System.err.println("Error Writing .CSV File: " + e.getMessage());
        }  
    }
    
    private void saveToTXTFile()
    {
        try
        {
        BufferedWriter out = new BufferedWriter(new FileWriter("drawing.TXT"));
        
        for (int i = 0; i < sizeY; i++)
            { 
                for (int j = 0; j < sizeX; j++)
                {
                       out.write((j+1) + "," + (i+1) + "," + grid[j][i].getBackground().getRGB());
                       out.newLine(); 
                } 
            }
            out.close();
        }
        catch(Exception e)
        {
            System.err.println("Error Writing .TXT File: " + e.getMessage());
        }
    }
 
    private void saveToABCFile()
    {
        try
        {
            BufferedWriter out = new BufferedWriter(new FileWriter("drawing.ABC"));
            int count = 0;
            Color current;
            Color previous = null;
            
            for (int i = 0; i < sizeY; i++)
            {
                previous = null;
                for (int j = 0; j < sizeX; j++)
                {
                    current = grid[j][i].getBackground();
                    if (previous != null)
                    {
                        
                        if (current.getRGB() == previous.getRGB())
                        {
                            count++;
                        }  
                        else
                        {
                            count++;
                            out.write(count + "," + previous.getRGB() + ",   ");
                            count = 0;
                           
                        }
                        if(j == sizeX - 1)
                        {
                            count++;
                            out.write(count + "," + current.getRGB());
                            count = 0;
                        }
                    } 
                    previous = current;
                }
                out.newLine();
            }
        out.close();   
        }
        catch(Exception e)
        {
            System.err.println("Error Writing .ABC File: " + e.getMessage());
        }
    }
    
       private void writeFileToRAF()
    {
        try
        {
            RandomAccessFile raf = new RandomAccessFile("test.RAF","rw"); /// "rw" indicates write mode to RAF constructor
            
            int count = 0;
            
            for (int i = 0; i < sizeY; i++)
            {
                for (int j = 0; j < sizeX; j++)
                {
                    int value = grid[j][i].getBackground().getRGB();
                    raf.seek(count * 20); //Sets poingter position in RAF file for current entry
                    raf.writeUTF(Integer.toString(value)); //writeUTF Writes value as string, use other methods for different values
                    count++;
                }
            }
            raf.close();         
        }
        catch(Exception e)
        {
            System.err.println("Error Writing to .RAF File: " + e.getMessage());
        }
    }
    
    private void readFileFromRAF()
    {
        try
        {
           String testRecord = null;
           RandomAccessFile raf = new RandomAccessFile("test.RAF","rw");
           int count = 0;
           
           for (int i = 0; i < sizeY; i++)
           {
                for (int j = 0; j < sizeX; j++)
                {
                    raf.seek(count * 20); //Sets pointer position for next read
                    grid[j][i].setBackground(new Color(Integer.parseInt(raf.readUTF())));
                    //raf.readUTF(); 
                    count++;
                }
           }
           raf.close(); 
        }
        catch(Exception e)
        {
            System.err.println("Error Reading >RAF File: " + e.getMessage());
        } 
    }
    //</editor-fold> 
}
