package visutils;

/**
 * Class to hold a 2D vector 
 * @author basilregi
 *
 */
public class Vector2D 
{
	private double x,y;
	
	/** Constructor */
	public Vector2D(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	/** Getter for X */
	public double getX()
	{
		return x;
	}
	
	/** Getter for Y */
	public double getY()
	{
		return y;
	}
	
	/** Function to create clone of vector */
	public Vector2D copy()
	{
		return new Vector2D(x,y);
	}
	
	/** Setter for X */
	public void setX(double x)
	{
		this.x = x;
	}
	
	/** Setter for Y */
	public void setY(double y)
	{
		this.y = y;
	}
	
	/** Function to add a vector to this one */
	public void add(Vector2D vect)
	{
		x += vect.getX();
		y += vect.getY();
	}
	
	/** Function to subtract a vector from this one */
	public void sub(Vector2D vect)
	{
		x -= vect.getX();
		y -= vect.getY();
	}
	
	/** Function to divide a non-zero value from this vector */
	public void div(double val)
	{
		if(val != 0)
		{
			x = x/val;
			y = y/val;
		}
	}
	
	/** Function to multiple co-ordinates of this vector by a value */
	public void mult(double val)
	{
		x *= val;
		y *= val;
	}
	
	/** Function to find magnitude of vector */
	public double mag()
	{
		return Math.sqrt((x*x) + (y*y));
	}
	
	/** Function to normalise vector */
	public void normalise()
	{
		double mag = mag();
		if(mag > 0)
		{
			x = x/mag;
			y = y/mag;
		}
	}
	
	/** Function to scale vector to a given magnitude */
	public void setMag(double mag)
	{
		normalise();
		x *= mag;
		y *= mag;
	}
	
	/** Function to limit vector magnitude if it is above it */
	public void limit(double limit)
	{
		if(mag() > limit)
		{
			setMag(limit);
		}
	}
	
	/** Function to inverse direction of vector */
	public void inverseDir()
	{
		x = -x;
		y = -y;
	}
	
	/** Function to find distance between this vector and another one */
	public double distance(Vector2D vect)
	{
		return Math.sqrt(((x - vect.getX()) * (x - vect.getX())) + ((y - vect.getY()) * (y - vect.getY())));
	}
	
	/** Function to find minimal direction vector from this one to another vector in wrap around world*/
	public Vector2D wraparoundDir(Vector2D vect, int maxHeight, int maxWidth)
	{
		double diffx = vect.getX() - x;
		double diffy = vect.getY() - y;
		
		if(Math.abs(diffx) > maxWidth/2)
		{
			if(x <= maxWidth/2)
			{
				diffx -= maxWidth;
			}
			else
			{
				diffx += maxWidth;
			}
		}
		
		if(Math.abs(diffy) > maxHeight/2)
		{
			if(y <= maxHeight/2)
			{
				diffy -= maxHeight;
			}
			else
			{
				diffy += maxHeight;
			}
		}
		
		return new Vector2D(diffx, diffy);
	}
	
	/** Function to find position of other boid which produces minimal distance */
	public Vector2D wraparoundPos(Vector2D vect, int maxHeight, int maxWidth)
	{
		Vector2D position = vect.copy();
		
		double diffx = vect.getX() - x;
		double diffy = vect.getY() - y;
		
		if(Math.abs(diffx) > maxWidth/2)
		{
			if(x <= maxWidth/2)
			{
				position.setX(position.getX() - maxWidth);
			}
			else
			{
				position.setX(position.getX() + maxWidth);
			}
		}
		
		if(Math.abs(diffy) > maxHeight/2)
		{
			if(y <= maxHeight/2)
			{
				position.setY(position.getY() - maxHeight);
			}
			else
			{
				position.setY(position.getY() + maxHeight);
			}
		}
		
		return position;
	}
	
	/** Override toString to print vector nicely */
	@Override
	public String toString()
	{
		return ("<" + x +"," + y + ">");
	}
	
	/** Override equals to be able to compare vectors */
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Vector2D))
		{
			return false;
		}
		
		Vector2D vect = (Vector2D)obj;
		
		return ((vect.getX() == x) && (vect.getY() == y));
	}

}
