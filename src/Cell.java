import java.awt.Font;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Cell {

	public String NAME = Names.getName();

	private float x, y;

	public float R = new Random().nextFloat(), G = new Random().nextFloat(), B = new Random().nextFloat();

	private float health = 60;
	private float lifeTime = 0f;

	private int movingCounter = 0;

	public static Brain brain = new Brain();

	private Rectangle hitbox = new Rectangle(0, 0, 0, 0);

	public Gene gene = new Gene();

	public Cell(float x, float y, String lastname) {

		NAME = Names.getFirstName() + " " + lastname;

		this.x = x;
		this.y = y;

	}

	public void moveX(float value) {

//		if (x + value > 1270 || x + value < 0) {
//			return;
//		}

		x += (value * gene.speed)/((gene.size)/30);
	}

	public void moveY(float value) {

//		if (y + value > 720 || y + value < 0) {
//			return;
//		}

		y += (value * gene.speed)/((gene.size)/30);
	}

	public void die() {
		Main.cells.remove(this);
		Main.log = NAME + " died..";
	}

	boolean left, up;
	int timePerformed = 0;

	public void update(int delta) {
		timePerformed++;

		if (left)
			moveX(new Random().nextFloat());
		else
			moveX(-new Random().nextFloat());

		if (up)
			moveY((new Random().nextFloat()));
		else
			moveY(-(new Random().nextFloat()));

		hitbox.setBounds(x, y, gene.size, gene.size);

		brain.update(this);

		if (timePerformed > 6) {
			Food currentFood = PhysicsHandeler.isCollidingWithFood(this);
			Cell currentCell = PhysicsHandeler.isCollidingWithOtherCell(this);

			health -= lifeTime;

			if (currentFood != null) {

				health += 50;
				Main.eatFood(currentFood);

			}

			if (currentCell != null) {

				if (health > 60 && lifeTime > 1.5) {

					// if (!currentCell.NAME.split(" ")[1].equals(NAME.split("
					// ")[1])||Main.cells.size()<10) {
					//

					giveBirth(this, currentCell, NAME.split(" ")[1]);
				}
				// }

			}

			timePerformed = 0;
		}
		if (health < 0)
			die();

		if (movingCounter > 100) {

			left = new Random().nextBoolean();
			up = new Random().nextBoolean();

			movingCounter = 0;

		}

		lifeTime += 0.001f;
		movingCounter++;
	}

	public void render(Graphics g) {

		if((x+gene.size/2)<0||(x+gene.size/2)>1270||(y+gene.size/2)<0||(y+gene.size/2)>720)
			return;
		
		// g.setColor(new Color(1 - health / 100, health / 100, 0));
		// g.drawString("Name: " + NAME, x, y - size + 10);
		g.setColor(new Color(R, G, B));
		g.fillOval(x, y, gene.size, gene.size);
		g.setColor(Color.white);
		g.drawOval(x, y, gene.size, gene.size);

	}

	public static void giveBirth(Cell a, Cell b, String lastname) {
		Cell newCell = new Cell(a.getX(), a.getY(), lastname);

		int birthAmount = new Random().nextInt(3);

		for (int i = 0; i < birthAmount; i++) {
			newCell.gene = Gene.mixGene(a.gene, b.gene);
			Main.cells.add(newCell);
		}

		a.health = 10;
		
		Main.log = a.NAME + " gave birth to " + birthAmount + " new cells";

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
