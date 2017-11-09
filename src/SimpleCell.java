import java.util.Random;

import org.encog.neural.networks.BasicNetwork;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

public class SimpleCell extends Cell {

	public SimpleCell(float x, float y, String lastname) {
		super(x, y, lastname);

		// System.out.println("Constructor!");

	}

	public void speak(Cell cell) {
		this.sent_msg = this.gene.size;
		cell.listen(sent_msg);
	}
	
	public void comunicate() {
		float smell_ = this.gene.smell;
		Cell coms_cell = this.brain.smellNearestCell(this, smell_);
		speak(coms_cell);
	}
	
	public void die() {

		if (Main.cells.size() < 2) {

				for (int j = 0; j < 10; j++) {
					
					for(int i = Main.deadcells.size(); i > Main.deadcells.size()-5 ; i--) {
					SimpleCell tempCell = new SimpleCell(new Random().nextInt(Main.width), new Random().nextInt(Main.height),
							Names.getLastName());
					
					tempCell.brain = new BrainANN((BasicNetwork) ((BrainANN)Main.deadcells.get(i).brain).network.clone());
					
					Main.cells.add(tempCell);
					}
				}
				
				for(int i = 0; i < 5; i++) {
					SimpleCell tempCell = new SimpleCell(new Random().nextInt(Main.width), new Random().nextInt(Main.height),
							Names.getLastName());
					
					tempCell.brain = new BrainANN();
					
					Main.cells.add(tempCell);
				}
				
				Main.deadcells.clear();
				
		}

		Main.deadcells.add(this);
		Main.cells.remove(this);
		Main.log = NAME + " died..";

	}

	boolean left, up;
	int timePerformed = 0;

	public void update(int delta) {
		

		// System.out.println("updating");

		brain.update(this);

		if (health < 0) {

			die();

		}

		setLifeTime(getLifeTime() + 0.001f);
		health -= 0.005f;

		if (getLifeTime() > 3.5)
			health -= 0.01f;

		if(PhysicsHandeler.isCollidingWithTile(getHitbox(), LandGen.WATER)!=null){
			health-=0.1f;
		}
		
		energy += 0.1f;

	}

	public void lateUpdate() {

		((BrainANN) brain).lateUpdate(this);

		getHitbox().setBounds(getX(), getY(), gene.size, gene.size);
	}

	public void render(Graphics g) {

		if (getX() + 10 < -Main.cameraX || getX() > -Main.cameraX + Main.width || getY() - 1 < -Main.cameraY
				|| getY() > -Main.cameraY + Main.height) {
			// System.out.println("outside!");
			return;
		}
		
		
		// System.out.println("Rendering on: "+getX()+" "+getY());
		g.pushTransform();
		g.rotate(getX() + gene.size / 2, getY() + gene.size / 2, getRotation());

		g.setColor(new Color(R, G, B));
		g.fillOval(getX(), getY(), gene.size, gene.size);

		g.setColor(Color.black);
		g.drawOval(getX(), getY(), gene.size, gene.size);

		g.setColor(new Color(R, G, B));
		g.drawLine(getX() + gene.size / 2, getY() + gene.size / 2, (getX() + gene.size / 2)-gene.size, getY() - 20);
		g.drawLine(getX() + gene.size / 2, getY() + gene.size / 2, (getX() + gene.size / 2)+gene.size, getY() - 20);
		g.popTransform();

		g.setColor(new Color(1 - health / 100, health / 100, 0));
		g.fillRect(getX(), getY() - 8, health / 100 * gene.size, 5);

	}

	public void giveBirth(Cell a, Cell b, String lastname) {
		SimpleCell newCell = new SimpleCell(a.getX(), a.getY(), lastname);

		int birthAmount = new Random().nextInt(10);

		for (int i = 0; i < birthAmount; i++) {
			newCell.gene = Gene.mixGene(a.gene, b.gene);
			newCell.brain = BrainANN.crossover(((BrainANN) a.brain), ((BrainANN) b.brain));
			Main.cells.add(newCell);
		}

		a.health = 10;

		Main.log = a.NAME + " gave birth to " + birthAmount + " new cells";
		Main.HIGHEST_GEN++;
	}

}
