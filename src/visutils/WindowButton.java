package visutils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * Class to alter button style on main GUI
 *
 */
public class WindowButton extends JButton
{
	private static final long serialVersionUID = 0;
	
	// Colour when pressed
	private Color pressedBackgroundColor = Color.WHITE;
	
	/** Constructor */
	public WindowButton(String text)
	{
		super(text);
		this.setBackground(Color.BLACK);
		this.setForeground(Color.WHITE);
		this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		this.setPreferredSize(new Dimension(110,25));
	}
	
	 /** Override paintComponent to change style and animate button */
	 @Override
     protected void paintComponent(Graphics g) 
	 {
		 if (getModel().isPressed()) 
		 {
             g.setColor(pressedBackgroundColor);
         } 
		 else 
		 {
             g.setColor(getBackground());
         }
         g.fillRect(0, 0, getWidth(), getHeight());
         super.paintComponent(g);
	 }

}
