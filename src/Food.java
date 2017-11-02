import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Food {

	private float x, y;
	
	private Rectangle hitbox = new Rectangle(0, 0, 0, 0);
	
	public Food(float x, float y) {
		
		this.x = x;
		this.y = y;
		
	}

	public void render(Graphics g) {
		
		g.setColor(Color.green);
		g.fillOval(x, y, 2, 2);
		
		hitbox.setBounds(x,y,2,2);
		
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
}
