
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
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

	public Brain brain = new Brain();

	public boolean givenBirth = false;

	private Rectangle hitbox = new Rectangle(0, 0, 0, 0);

	public Gene gene = new Gene();

	public float movingX, movingY;

	public List<Vector2f> trail = new ArrayList<Vector2f>();
	
	public Cell(float x, float y, String lastname) {

		NAME = Names.getFirstName() + " " + lastname;

		this.x = x;
		this.y = y;

	}

	public void moveX(float value) {
		movingX = value;

		trail.add(new Vector2f(x+-(value*10), y));
		
		if(trail.size()>50)
			trail.remove(0);
		
		if (x + value > 1270 && value > 0) {
			return;
		} else if (x + value < 0 && value < 0) {
			return;
		}

		x += (value * gene.speed) / ((gene.size) / 80);
	}

	public void moveY(float value) {
		movingY = value;

		trail.add(new Vector2f(x, y+-(value*10)));
		
		if(trail.size()>50)
			trail.remove(0);
		
		if (y + value > 720 && value > 0) {
			return;
		} else if (y + value < 0 && value < 0) {
			return;
		}

		y += (value * gene.speed) / ((gene.size) / 50);
	}

	public void die() {
		Main.cells.remove(this);
		Main.log = NAME + " died..";
	}

	boolean left, up;
	int timePerformed = 0;

	public void update(int delta) {

		brain.update(this);

		if (health < 0)
			die();

		setLifeTime(getLifeTime() + 0.00002f);
	}

	public void lateUpdate() {

		Food currentFood = PhysicsHandeler.isCollidingWithFood(this);
		Cell currentCell = PhysicsHandeler.isCollidingWithOtherCell(this);

		health -= getLifeTime() / 5;

		if (currentFood != null && health < 100) {

			health += 40;
			Main.eatFood(currentFood);

		}

		if (currentCell != null/* &&!givenBirth */) {

			if (health > 60 && getLifeTime() > 1.5) {

				giveBirth(this, currentCell, NAME.split(" ")[1]);
				givenBirth = true;
			}

		}

		hitbox.setBounds(x, y, gene.size, gene.size);
	}

	public void render(Graphics g) {

		g.setColor(new Color(R, G, B));
		g.fillOval(x, y, gene.size, gene.size);

//		for (int i = 0; i < trail.size(); i++) {
//			g.setColor(new Color(R, G, B));
//			g.fillOval(trail.get(i).x, trail.get(i).y, gene.size, gene.size);
//		}

		g.setColor(new Color(1 - health / 100, health / 100, 0));
		g.fillRect(x, y - 8, health / 100 * gene.size, 5);
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
		Main.HIGHEST_GEN++;
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

	public float getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(float lifeTime) {
		this.lifeTime = lifeTime;
	}

}
