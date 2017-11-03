import java.util.Random;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

public class Brain {

	public Food nearestSmelledFood;

	
	public Brain() {

		
	}

	public void update(Cell cell) {

		if (cell.getHealth() < 100) {
			searchForFood(cell);
		}else {
			wonderAround(cell);
		}
		
		if(cell.getLifeTime() > 0.6&&cell.getHealth()>60) {
			searchForMate(cell);
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
			cell.moveX(new Random().nextFloat());
		else
			cell.moveX(-new Random().nextFloat());

		if (up)
			cell.moveY((new Random().nextFloat()));
		else
			cell.moveY(-(new Random().nextFloat()));

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
