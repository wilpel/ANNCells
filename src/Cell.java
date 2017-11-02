import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Cell {

	private float x, y, size = 10;
	private float movingSpeed = 0.8f;
	private float health = 100;
	
	private Rectangle hitbox = new Rectangle(0, 0, 0, 0);

	public Cell(float x, float y) {

		this.x = x;
		this.y = y;

	}

	public void moveX(float value) {
		
		if(x+value>1270||x+value<0) {
			return;
		}
		
		x+=value*movingSpeed;
	}

	public void moveY(float value) {
		
		if(y+value>720||y+value<0) {
			return;
		}
		
		y+=value*movingSpeed;
	}
	
	public void update() {
		moveX(new Random().nextFloat()-0.5f);
		moveY(new Random().nextFloat()-0.5f);
		
		hitbox.setBounds(x, y, size, size);
		
		Food currentFood = PhysicsHandeler.isCollidingWithFood(this);
		
		if(currentFood != null) {
			

			Main.eatFood(currentFood);
			
		}
		
	}
	
	public void render(Graphics g) {

		g.setColor(Color.red);
		g.fillOval(x, y, size, size);
		
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

	public float getMovingSpeed() {
		return movingSpeed;
	}

	public void setMovingSpeed(float movingSpeed) {
		this.movingSpeed = movingSpeed;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	

}
