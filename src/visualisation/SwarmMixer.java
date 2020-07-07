package visualisation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Hashtable;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import swarm.BoidWorld;
import visutils.SliderUI;

/**
 * Class to create SwarmMixer window
 *
 */
public class SwarmMixer extends JFrame
{
	private static final long serialVersionUID = 0;
	
	/** Number of values in sliders */
	private int values = 20;

	/**
	 * Constructor
	 * @param world Boid World Model
	 */
	public SwarmMixer(BoidWorld world)
	{
		this.setSize(new Dimension(700,500));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.getContentPane().setBackground(Color.BLACK);
		this.setLayout(new GridBagLayout());
		
		Hashtable<Integer, JLabel> intvalues = new Hashtable<Integer, JLabel>();
		
		//Set slider labels
		for(int i = 0; i < values + 1; i++)
		{
			JLabel label = new JLabel("" + i);
			label.setForeground(Color.WHITE);
			intvalues.put(i, label);
		}
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(20,20,0,0);
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridwidth = GridBagConstraints.REMAINDER;
		c2.weightx = 1.0;
		c2.weighty = 1.0;
		
		//Title
		JLabel title = new JLabel("SWARM MIXER");
		title.setFont(new Font(title.getFont().getFontName(), Font.PLAIN, 28));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setForeground(Color.WHITE);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 0;
		this.add(title,c);
		
		//Max Speed Label and Slider
		JLabel speed = new JLabel("MAX BOID SPEED");
		speed.setForeground(Color.WHITE);
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		this.add(speed, c);
		JSlider speedslider = new JSlider(new DefaultBoundedRangeModel((int)world.getMaxSpeed(), 0, 0, values));
		speedslider.setOrientation(JSlider.HORIZONTAL);
		speedslider.setBackground(Color.BLACK);
		speedslider.setUI(new SliderUI(speedslider));
		speedslider.setLabelTable(intvalues);
		speedslider.setPaintTicks(true);
		speedslider.setPaintLabels(true);
		speedslider.setPreferredSize(new Dimension(500,50));
		speedslider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e)
			{
				world.setMaxSpeed(((JSlider)e.getSource()).getValue());
			}
		});
		c2.gridx = 1;
		c2.gridy = c.gridy;
		this.add(speedslider, c2);
		
		//Max Force label and Slider
		JLabel force = new JLabel("MAX BOID FORCE");
		force.setForeground(Color.WHITE);
		c.gridy++;
		this.add(force, c);
		JSlider forceslider = new JSlider(new DefaultBoundedRangeModel((int)(world.getMaxForce()*10), 0, 0, values));
		forceslider.setOrientation(JSlider.HORIZONTAL);
		forceslider.setBackground(Color.BLACK);
		forceslider.setUI(new SliderUI(forceslider));
		forceslider.setLabelTable(intvalues);
		forceslider.setPaintTicks(true);
		forceslider.setPaintLabels(true);
		forceslider.setPreferredSize(new Dimension(500,50));
		forceslider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e)
			{
				world.setMaxForce(((JSlider)e.getSource()).getValue()/10.0);
			}
		});
		c2.gridy++;
		this.add(forceslider, c2);
		
		//Separation label and slider
		JLabel separation = new JLabel("SEPARATION");
		separation.setForeground(Color.WHITE);
		c.gridy++;
		this.add(separation, c);
		JSlider sepslider = new JSlider(new DefaultBoundedRangeModel((int)(10 * world.getDefaultSep()), 0, 0, values));
		sepslider.setOrientation(JSlider.HORIZONTAL);
		sepslider.setUI(new SliderUI(sepslider));
		sepslider.setBackground(Color.BLACK);
		sepslider.setLabelTable(intvalues);
		sepslider.setPaintTicks(true);
		sepslider.setPaintLabels(true);
		sepslider.setPreferredSize(new Dimension(500,50));
		sepslider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e)
			{
				world.setDefaultSep(((JSlider)e.getSource()).getValue()/10.0);
			}
		});
		c2.gridy++;
		this.add(sepslider, c2);
		
		//Alignment label and slider
		JLabel alignment = new JLabel("ALIGNMENT");
		alignment.setForeground(Color.WHITE);
		c.gridy++;
		this.add(alignment, c);
		JSlider alignslider = new JSlider(new DefaultBoundedRangeModel((int)(10 * world.getDefaultAlign()), 0, 0, values));
		alignslider.setOrientation(JSlider.HORIZONTAL);
		alignslider.setUI(new SliderUI(alignslider));
		alignslider.setBackground(Color.BLACK);
		alignslider.setLabelTable(intvalues);
		alignslider.setPaintTicks(true);
		alignslider.setPaintLabels(true);
		alignslider.setPreferredSize(new Dimension(500,50));
		alignslider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e)
			{
				world.setDefaultAlign(((JSlider)e.getSource()).getValue()/10.0);
			}
		});
		c2.gridy++;
		this.add(alignslider, c2);
		
		//Cohesion label and slider
		JLabel cohesion = new JLabel("COHESION");
		cohesion.setForeground(Color.WHITE);
		c.gridy++;
		this.add(cohesion, c);
		JSlider cohslider = new JSlider(new DefaultBoundedRangeModel((int)(10 * world.getDefaultCoh()), 0, 0, values));
		cohslider.setOrientation(JSlider.HORIZONTAL);
		cohslider.setUI(new SliderUI(cohslider));
		cohslider.setBackground(Color.BLACK);
		cohslider.setLabelTable(intvalues);
		cohslider.setPaintTicks(true);
		cohslider.setPaintLabels(true);
		cohslider.setPreferredSize(new Dimension(500,50));
		cohslider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e)
			{
				world.setDefaultCoh(((JSlider)e.getSource()).getValue()/10.0);
			}
		});
		c2.gridy++;
		this.add(cohslider, c2);
		
		this.setVisible(true);
	}
}
