package simulator;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class DrawCircle extends JFrame {
    public DrawCircle(int argR, int argX, int argY, String argColor) {

    }

    public static void main(String[] arguments) 
    {
    	int xposition = 0;
    	int yposition = 0;
    	CirclePanel1 circleList[] = new CirclePanel1[20];
    	
    	JFrame animationscreen = new JFrame();
    	animationscreen.setSize(350, 250);
    	animationscreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	for(int i = 0; i < 15; i++)
    	{
    		//circleList[i] = new DrawCircle(10, xposition, yposition, "#AA0096");
            circleList[i] = CirclePanel(10, xposition, yposition, "#AA0096");
            Container pane = animationscreen.getContentPane();
            pane.add(circleList[i]);
            pane.repaint();
            animationscreen.repaint();
            animationscreen.setVisible(true);
    		xposition = xposition + 5;
    		
    		if(xposition == 50)
    		{
    			xposition = 0;
    			yposition = yposition + 10;
    		}
    	}
    	   
    }

	private static CirclePanel1 CirclePanel(int i, int xposition,
			int yposition, String string) {
		// TODO Auto-generated method stub
		return null;
	}
}

class CirclePanel1 extends JPanel {
    int radius, x, y;
    Color color;

    public CirclePanel1(int inRadius, int inX, int inY, String inColor) {
        super();
        /*if (inRadius == null)
            radius = 100;
        if (inX == null)
            x = 110;
        if (inY == null)
            y = 110;
        if (inColor == null)
            color = Color.blue;*/
        try 
        {
            radius = inRadius;
            x = inX;
            y = inY;
            color = Color.decode(inColor);
        }
        
        catch (NumberFormatException e) 
        {
            System.out.println("Error" + e.getMessage());
        }
    }

    public void CirclePanel111(int inRadius, int xposition, int yposition,
			String inColor) {
		// TODO Auto-generated constructor stub
	}

	public void CirclePanel11(int inRadius, int xposition, int yposition,
			String inColor) {
		// TODO Auto-generated constructor stub
	}

	public void CirclePanel(int inRadius, int xposition, int yposition,
			String inColor) {
		// TODO Auto-generated constructor stub
	}

	public void paintComponent(Graphics comp) {
        Graphics2D comp2D = (Graphics2D)comp;
        comp2D.setColor(Color.white);
        comp2D.fillRect(0, 0, getSize().width, getSize().height);
        comp2D.setColor(color);
        Ellipse2D.Float circle = new Ellipse2D.Float(x, y, radius, radius);
        comp2D.fill(circle);
    }
}