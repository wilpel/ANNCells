import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Food {

	private float x, y, size = 32;
	private float foodLeft = 1;

	boolean hasGrowed = false;

	private Rectangle hitbox = new Rectangle(0, 0, 0, 0);

	public Food(float x, float y) {

		this.x = x;
		this.y = y;

	}

	public void render(Graphics g) {

		g.setColor(new Color(0f, 0.2f, 0f, foodLeft));
		g.fillRect(x, y, size, size);

		hitbox.setBounds(x, y, size, size);

		if (foodLeft < 0)
			Main.eatFood(this);

		if (!hasGrowed) {
			if (new Random().nextInt(1000) < 10) {

				Main.food.add(new Food(x+new Random().nextInt((int) (size*2))-size, y+new Random().nextInt((int) (size*2))-size));
				hasGrowed = true;

			}
		}

	}

	public void eat() {
		foodLeft -= 0.1f;
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
