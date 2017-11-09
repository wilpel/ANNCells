
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.encog.neural.networks.BasicNetwork;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Cell {
	
	public float sent_msg;
	public float rcvd_msg;
	
	public String NAME = Names.getName();
	public String lastname;

	public int networkScore = 0;

	private float x, y;
	private float rotation;
	
	public float R = new Random().nextFloat(), G = new Random().nextFloat(), B = new Random().nextFloat();

	float health = 120;
	float energy = 100;
	
	private float lifeTime = 0f;

	private int movingCounter = 0;

	public Brain brain = new BrainANN();

	public boolean givenBirth = false;

	private Rectangle hitbox = new Rectangle(0, 0, 0, 0);

	public Gene gene = new Gene();

	public float movingX, movingY;

	public List<Vector2f> trail = new ArrayList<Vector2f>();

	public Cell(float x, float y, String lastname) {

		this.lastname = lastname;
		NAME = Names.getFirstName() + " " + lastname;

		this.x = x;
		this.y = y;

	}
	public float listen(float rcvd_msg) {
		return rcvd_msg;
	}
	public void rotate(float value) {
		
		if(getRotation() < 0)
			setRotation(360);
		
		if(getRotation() > 360)
			setRotation(0);
		
		setRotation(Math.round(getRotation()+value));
	}
	
	public void move(float value) {
		if(energy>0) {
			
		LandGen walkingTile = PhysicsHandeler.isCollidingWithTile(getHitbox(), LandGen.WATER);
			
		
		if(walkingTile != null) {
			energy-=value*5;
			value/=2;
		}else { 
			energy-=value*2;
		}
		
		x += gene.speed*value * Math.sin(Math.toRadians(-1*getRotation()));
	    y += gene.speed*value * Math.cos(Math.toRadians(-1*getRotation()));
	    
	    
		}
	}
	
	public void moveX(float value) {
		if(energy>0) {
		movingX = value;
		trail.add(new Vector2f(x + -(value * 10), y));

		if (trail.size() > 50)
			trail.remove(0);

		// if (x + value > 1270 && value > 0) {
		// return;
		// } else if (x + value < 0 && value < 0) {
		// return;
		// }
		energy-=value;
		x += (value * gene.speed);
		}
	}

	public void moveY(float value) {
		if(energy>0) {
		movingY = value;
		trail.add(new Vector2f(x, y + -(value * 10)));

		if (trail.size() > 50)
			trail.remove(0);

		// if (y + value > 720 && value > 0) {
		// return;
		// } else if (y + value < 0 && value < 0) {
		// return;
		// }

		energy-=value;
		y += (value * gene.speed);
		}
	}

	public void die() {
	
	}

	public void update(int delta) {
		
	}

	public void lateUpdate() {
		
	}

	public void render(Graphics g) {
		
	}

	public void giveBirth(Cell a, Cell b, String lastname) {
		Cell newCell = new Cell(a.getX(), a.getY(), lastname);

		int birthAmount = new Random().nextInt(5);

		for (int i = 0; i < birthAmount; i++) {
			newCell.gene = Gene.mixGene(a.gene, b.gene);
			newCell.brain = BrainANN.crossover(((BrainANN)a.brain),((BrainANN)b.brain));
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
	
	public float getEnergy() {
		return energy;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

}
