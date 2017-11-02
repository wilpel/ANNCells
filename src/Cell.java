import java.awt.Font;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Cell {

	public String NAME = Names.getName();

	private float x, y, size = 10;
	private float movingSpeed = 1f;

	private float health = 60;
	private float lifeTime = 0f;

	private int movingCounter = 0;

	public static Brain brain = new Brain();

	private Rectangle hitbox = new Rectangle(0, 0, 0, 0);

	public Cell(float x, float y, String lastname) {

		NAME = Names.getFirstName() + " " + lastname;

		this.x = x;
		this.y = y;

	}

	public void moveX(float value) {

		if (x + value > 1270 || x + value < 0) {
			return;
		}

		x += value * movingSpeed;
	}

	public void moveY(float value) {

		if (y + value > 720 || y + value < 0) {
			return;
		}

		y += value * movingSpeed;
	}

	boolean left, up;

	public void update() {

		if (left)
			moveX(new Random().nextFloat());
		else
			moveX(-new Random().nextFloat());

		if (up)
			moveY((new Random().nextFloat()));
		else
			moveY(-(new Random().nextFloat()));

		hitbox.setBounds(x, y, size, size);

		brain.update(this);

		Food currentFood = PhysicsHandeler.isCollidingWithFood(this);
		Cell currentCell = PhysicsHandeler.isCollidingWithOtherCell(this);

		health -= lifeTime;

		if (currentFood != null) {

			health+=50;
			Main.eatFood(currentFood);

		}

		if (currentCell != null) {

			if (health > 60&&lifeTime>1.5) {

//				if (!currentCell.NAME.split(" ")[1].equals(NAME.split(" ")[1])||Main.cells.size()<10) {
//				

					int birthAmount = new Random().nextInt(3);
					for (int i = 0; i < birthAmount; i++) {

						giveBirth(this, currentCell, NAME.split(" ")[1]);
					}
					
					health = 0;
					
				}else {
					System.out.println("Incest!");
				}
			//}

		}

		if (health < 0)
			Main.cells.remove(this);

		
		if (movingCounter > 100) {

			left = new Random().nextBoolean();
			up = new Random().nextBoolean();

			movingCounter = 0;

		}

		lifeTime+=0.005f;
		movingCounter++;
	}

	public void render(Graphics g) {

//		g.setColor(new Color(1 - health / 100, health / 100, 0));
//		g.drawString("Name: " + NAME, x, y - size + 10);
		g.setColor(new Color(health / 100, 0, 0));
		g.fillOval(x, y, size, size);
		g.setColor(Color.white);
		g.drawOval(x, y, size, size);

	}
	
	public static void giveBirth(Cell a, Cell b, String lastname) {
		Cell newCell = new Cell(a.getX(), a.getY(), lastname);
		
		newCell.movingSpeed = ((a.movingSpeed + b.movingSpeed)/2)+(new Random().nextFloat()-0.5f)/100;
		newCell.size = ((a.size+b.size)/2)+(new Random().nextFloat()-0.5f)/100;
		
		Main.cells.add(newCell);
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
