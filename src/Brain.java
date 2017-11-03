import java.util.Random;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

public class Brain {

	public Food nearestSmelledFood;
	public Cell nearestSmelledCell;

	
	public Brain() {

		
	}

	public void update(Cell cell) {

		handlePrio(cell);
	}
	
	public void handlePrio(Cell cell) {
		if (cell.getLifeTime() > 5) {
			searchForMate(cell);
		}
		else {
			if (cell.gene.fightFlightPrio > 0) {
				searchForCell(cell);
			}
			else {
				searchForFood(cell);
<<<<<<< HEAD
				wonderAround(cell);
=======
>>>>>>> a87a83505a2878f8fe265992c6527aa299974fc6
			}
		}
	}
	
	//Takes in this.cell
	public void searchForCell(Cell cell) {
		float sizeProcentThisComparedToFoundCell; //Is it bas to have this local?
		nearestSmelledCell = smellNearestCell(cell, cell.gene.smell);

		if (nearestSmelledCell != null) {
			sizeProcentThisComparedToFoundCell = (cell.gene.size/nearestSmelledCell.gene.size)*100;
			//Köra random 0-100/agression, om den landar inom procent range attackera.
			//gör temp lösning nedan utan aggression och probabilitet
			if (sizeProcentThisComparedToFoundCell > 100) {
				if (nearestSmelledCell.getX() < cell.getX()+cell.gene.size/2) {
					cell.moveX(-cell.gene.speed);
				} else {
					cell.moveX(cell.gene.speed);
				}

				if (nearestSmelledCell.getY() < cell.getY()+cell.gene.size/2) {
					cell.moveY(-cell.gene.speed);
				} else {
					cell.moveY(cell.gene.speed);
				}
			}
			else {
				searchForFood(cell);
			}
		}
	}

	public void searchForMate(Cell cell) {
		
		
		try {

			Cell nearestMate = smellNearestCell(cell, cell.gene.smell);
			
			if (nearestMate != null) {
				if (nearestMate.getX() < cell.getX() + cell.gene.size / 2) {
					cell.moveX(-cell.gene.speed);
				} else {
					cell.moveX(cell.gene.speed);
				}

				if (nearestMate.getY() < cell.getY() + cell.gene.size / 2) {
					cell.moveY(-cell.gene.speed);
				} else {
					cell.moveY(cell.gene.speed);
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		
	}
	
	public void searchForFood(Cell cell) {

		if (cell.getHealth() >= cell.gene.foodCapacity)
			return;

		try {

			nearestSmelledFood = smellNearestFood(cell, cell.gene.smell);

			if (nearestSmelledFood != null) {
				if (nearestSmelledFood.getX() < cell.getX() + cell.gene.size / 2) {
					cell.moveX(-cell.gene.speed);
				} else {
					cell.moveX(cell.gene.speed);
				}

				if (nearestSmelledFood.getY() < cell.getY() + cell.gene.size / 2) {
					cell.moveY(-cell.gene.speed);
				} else {
					cell.moveY(cell.gene.speed);
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	boolean left, up;
	int movingCounter = 0;
	int movingCounterCap = new Random().nextInt(30);
	public void wonderAround(Cell cell) {
		
		if (left)
			cell.moveX(cell.gene.speed);
		else
			cell.moveX(-cell.gene.speed);

		if (up)
			cell.moveY((cell.gene.speed));
		else
			cell.moveY(-(cell.gene.speed));

		if (movingCounter > movingCounterCap) {

			left = new Random().nextBoolean();
			up = new Random().nextBoolean();

			movingCounter = 0;
			movingCounterCap = new Random().nextInt(100);
		}

		movingCounter++;
	}

	public static Food smellNearestFood(Cell cell, float smell) {

		int index = 0;
		float nearest = 999999999;

		for (int i = 0; i < Main.food.size(); i++) {

			if (Math.sqrt((Main.food.get(i).getX() - cell.getX()) * (Main.food.get(i).getX() - cell.getX())
					+ (Main.food.get(i).getY() - cell.getY()) * (Main.food.get(i).getY() - cell.getY())) < nearest) {
				nearest = (float) Math
						.sqrt((Main.food.get(i).getX() - cell.getX()) * (Main.food.get(i).getX() - cell.getX())
								+ (Main.food.get(i).getY() - cell.getY()) * (Main.food.get(i).getY() - cell.getY()));
				index = i;
			}

		}

		if (nearest > smell) {
			return null;
		}

		return Main.food.get(index);

	}
	
	public static Cell smellNearestCell(Cell cell, float smell) {

		int index = 0;
		float nearest = 999999999;

		for (int i = 0; i < Main.cells.size(); i++) {

			if(i==Main.cells.indexOf(cell))
				continue;
			
			if (Math.sqrt((Main.cells.get(i).getX() - cell.getX()) * (Main.cells.get(i).getX() - cell.getX())
					+ (Main.cells.get(i).getY() - cell.getY()) * (Main.cells.get(i).getY() - cell.getY())) < nearest) {
				nearest = (float) Math
						.sqrt((Main.cells.get(i).getX() - cell.getX()) * (Main.cells.get(i).getX() - cell.getX())
								+ (Main.cells.get(i).getY() - cell.getY()) * (Main.cells.get(i).getY() - cell.getY()));
				index = i;
			}

		}

		if (nearest > smell) {
			return null;
		}

		return Main.cells.get(index);

	}

}
