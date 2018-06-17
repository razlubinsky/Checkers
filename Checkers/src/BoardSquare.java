import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

public class BoardSquare extends JComponent
{

	private static final long serialVersionUID = 1L;
	private static int width = 100;
    private static int height = 100;
	
    private int x; //x position of the rectangle measured from top left corner
    private int y; //y position of the rectangle measured from top left corner
    private Type SquareType = Type.BLANK; //type of piece 

    public Type getSqareType()
    {
    	return this.SquareType;
    }
    public void setRow(int Row)
    {
    	x = Row;
    }
    public void setCol(int Col)
    {
    	y = Col;
    }
    public int getRow()
    {
    	return this.x;
    }
    public int getCol()
    {
    	return this.y;
    }
    
    public BoardSquare(int p, int q, Type type)
    {
        
        this.setPreferredSize(new Dimension(width, height));
        
        x = p;
        y = q;
        setType(type);

    }
    public void setType(Type type)
    {
        SquareType = type;
    }
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        Rectangle box = new Rectangle(x,y,width,height);
        g2.draw(box);
        g2.setPaint(Color.DARK_GRAY);
        g2.fill(box);
        int ovalWidth = width -8;
        int ovalHeight = ovalWidth;
        if(SquareType == Type.BLACK)
        {
            g2.setColor(Color.CYAN);
            g2.fillOval(x, y, ovalWidth, ovalHeight);
            g2.drawOval(x, y, ovalWidth, ovalHeight);
        }

        else if(SquareType == Type.RED)
        {
            g2.setColor(Color.MAGENTA);
            g2.fillOval(x, y, ovalWidth, ovalHeight);
            g2.drawOval(x, y, ovalWidth, ovalHeight);
        }
        else if(SquareType == Type.QUEEN_RED)
        {
            g2.setColor(Color.ORANGE);
            g2.fillOval(x, y, ovalWidth, ovalHeight);
            g2.drawOval(x, y, ovalWidth, ovalHeight);
        }
        else if(SquareType == Type.QUEEN_BLACK)
        {
            g2.setColor(Color.BLUE);
            g2.fillOval(x, y, ovalWidth, ovalHeight);
            g2.drawOval(x, y, ovalWidth, ovalHeight);
        }
        else if(SquareType == Type.BLINK_BLANK)
        {
	        g2.draw(box);
	        g2.setPaint(Color.DARK_GRAY);
	        g2.fill(box);
	       	g2.setColor(Color.GREEN);
	        //g2.fillOval(x, y, ovalWidth, ovalHeight);
            g2.drawOval(x+35, y+35, ovalWidth-70, ovalHeight-70);
        }
        else if (SquareType == Type.QUEEN_BLACK_BLINK|| SquareType == Type.QUEEN_RED_BLINK)
        {
        	g2.draw(box);
            g2.setPaint(Color.GREEN);
            g2.fill(box);
            if(SquareType == Type.QUEEN_RED_BLINK)
            	g2.setColor(Color.ORANGE);
            if(SquareType == Type.QUEEN_BLACK_BLINK) 
            	g2.setColor(Color.BLUE);
            g2.fillOval(x, y, ovalWidth, ovalHeight);
            g2.drawOval(x, y, ovalWidth, ovalHeight);
        }
        else if (SquareType == Type.BLINK_BLACK || SquareType == Type.BLINK_RED)
        {
        	g2.draw(box);
            g2.setPaint(Color.GREEN);
            g2.fill(box);
            if(SquareType == Type.BLINK_RED)
            	g2.setColor(Color.MAGENTA);
            if(SquareType == Type.BLINK_BLACK) 
            	g2.setColor(Color.CYAN);
            g2.fillOval(x, y, ovalWidth, ovalHeight);
            g2.drawOval(x, y, ovalWidth, ovalHeight);
        }
        
    }

    
}
       