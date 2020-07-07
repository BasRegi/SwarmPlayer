package visutils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;

import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 * Class to alter basic slider GUI
 * @author basilregi
 *
 */
public class SliderUI extends BasicSliderUI
{
	/** Colour of slider track */
	public static Color rangeColor = Color.CYAN;
	
	/** Size of stroke */
	private final BasicStroke stroke = new BasicStroke(3f);
	
	/** Change shape of slider block */
	private int shape = 0;

	public SliderUI(JSlider b) 
	{
		super(b);
	}
	
	@Override
	protected void calculateThumbSize() {
	    super.calculateThumbSize();
	    thumbRect.setSize(thumbRect.width, thumbRect.height);
	}
	
	@Override
	protected Dimension getThumbSize() {
		
	    return new Dimension(15,15);
	}

	private Shape createThumbShape(int width, int height) {
		Shape thumbShape = null;
		if(shape == 0)
		{
			thumbShape = new Ellipse2D.Double(0, 0, width, height);
		}
		else if(shape == 1)
		{
			thumbShape = new Rectangle(0, 0, width, height);
		}
	    return thumbShape;
	}
	
	public void setShape(int val)
	{
		shape = val;
	}

	
	@Override
	public void paintTrack(Graphics g) {
	    Graphics2D g2d = (Graphics2D) g;
	    Stroke old = g2d.getStroke();
	    Color oldColor = g2d.getColor();
	    g2d.setStroke(stroke);
	    g2d.setPaint(Color.WHITE);
	    Rectangle trackBounds = trackRect;
	    if (slider.getOrientation() == SwingConstants.HORIZONTAL) {
	        g2d.drawLine(trackRect.x, trackRect.y + trackRect.height / 2, 
	                trackRect.x + trackRect.width, trackRect.y + trackRect.height / 2);
	        int lowerX = thumbRect.width / 2;
	        int upperX = thumbRect.x + (thumbRect.width / 2);
	        int cy = (trackBounds.height / 2) - 2;
	        g2d.translate(trackBounds.x, trackBounds.y + cy);
	        g2d.setColor(rangeColor);
	        g2d.drawLine(lowerX - trackBounds.x, 2, upperX - trackBounds.x, 2);
	        g2d.translate(-trackBounds.x, -(trackBounds.y + cy));
	        g2d.setColor(oldColor);
	    } 
	    g2d.setStroke(old);
	}
	
	/** Overrides superclass method to do nothing.  Thumb painting is handled
	 * within the <code>paint()</code> method.*/
	@Override
	public void paintThumb(Graphics g) {
	    Rectangle knobBounds = thumbRect;
	    int w = knobBounds.width;
	    int h = knobBounds.height;      
	    Graphics2D g2d = (Graphics2D) g.create();
	    Shape thumbShape = createThumbShape(w-1, h-1);
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	        RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.translate(knobBounds.x, knobBounds.y);
	    g2d.setColor(Color.WHITE);
	    g2d.fill(thumbShape);

	    g2d.setColor(Color.BLACK);
	    g2d.draw(thumbShape);
	    g2d.dispose();
	}

}
