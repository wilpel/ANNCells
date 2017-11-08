import java.util.Random;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

public class Brain {

	public LandGen nearestSmelledFood;
	public Cell nearestSmelledCell;
	public static LandGen nearestFood;

	public Brain() {

	}

	public void update(Cell cell) {

		handlePrio(cell);
	}

	public void handlePrio(Cell cell) {
		if (cell.getLifeTime() > 5) {
			searchForMate(cell);
		} else {
			if (cell.gene.fightFlightPrio > 0) {
				searchForCell(cell);
			} else {
				// searchForFood(cell);

				//
				wonderAround(cell, cell.gene.speed);
			}
		}
	}

	// Takes in this.cell
	public void searchForCell(Cell cell) {
		float sizeProcentThisComparedToFoundCell; // Is it bas to have this local?
		nearestSmelledCell = smellNearestCell(cell, cell.gene.smell);

		if (nearestSmelledCell != null) {
			sizeProcentThisComparedToFoundCell = (cell.gene.size / nearestSmelledCell.gene.size) * 100;
			// Köra random 0-100/agression, om den landar inom procent range attackera.
			// gör temp lösning nedan utan aggression och probabilitet
			if (sizeProcentThisComparedToFoundCell > 100) {
				if (nearestSmelledCell.getX() < cell.getX() + cell.gene.size / 2) {
					cell.moveX(-cell.gene.speed);
				} else {
					cell.moveX(cell.gene.speed);
				}

				if (nearestSmelledCell.getY() < cell.getY() + cell.gene.size / 2) {
					cell.moveY(-cell.gene.speed);
				} else {
					cell.moveY(cell.gene.speed);
				}
			} else {
				// searchForFood(cell);
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

	public void turnToFood(Cell cell, float value) {

		if (nearestFood == null)
			return;

		float angle = PhysicsHandeler.getTargetAngle(cell.getX(), cell.getY(), nearestFood.x, nearestFood.y);

		if (cell.getRotation() > angle)
			cell.rotate(-(cell.gene.speed * (10 * value)));
		else
			cell.rotate(cell.gene.speed * (10 * value));

	}

	public void turnToCell(Cell cell, float value) {

		if (nearestSmelledCell == null)
			return;

		float angle = PhysicsHandeler.getTargetAngle(cell.getX(), cell.getY(), nearestSmelledCell.getX(),
				nearestSmelledCell.getY());

		if (cell.getRotation() > angle)
			cell.rotate(-(cell.gene.speed * (10 * value)));
		else
			cell.rotate(cell.gene.speed * (10 * value));

	}

	boolean left, up;
	int movingCounter = 0;
	int movingCounterCap = new Random().nextInt(30);

	public void wonderAround(Cell cell, float spd) {

		if (left)
			cell.moveX(cell.gene.speed * spd);
		else
			cell.moveX(-cell.gene.speed * spd);

		if (up)
			cell.moveY((cell.gene.speed * spd));
		else
			cell.moveY(-(cell.gene.speed * spd));

		if (movingCounter > movingCounterCap) {

			left = new Random().nextBoolean();
			up = new Random().nextBoolean();

			movingCounter = 0;
			movingCounterCap = new Random().nextInt(100);
		}

		movingCounter++;
	}

	public static LandGen smellNearestFood(Cell cell, float smell) {

		int x1 = 0, y1 = 0;
		float nearest = 999999999;

		for (int x = 0; x < Main.landmass.length; x++) {
			for (int y = 0; y < Main.landmass[0].length; y++) {
				try {
					if (Main.landmass[x][y].id != LandGen.GRASS)
						continue;

					if (Math.sqrt((Main.landmass[x][y].x - cell.getX()) * (Main.landmass[x][y].x - cell.getX())
							+ (Main.landmass[x][y].y - cell.getY())
									* (Main.landmass[x][y].y - cell.getY())) < nearest) {
						nearest = (float) Math.sqrt((Main.landmass[x][y].x - cell.getX())
								* (Main.landmass[x][y].x - cell.getX())
								+ (Main.landmass[x][y].y - cell.getY()) * (Main.landmass[x][y].y - cell.getY()));
						x1 = x;
						y1 = y;
					}
				} catch (Exception e) {
				}
			}
		}

		if (nearest > smell) {
			return null;
		}

		return Main.landmass[x1][y1];

	}

	public static float smellNearestFoodDist(Cell cell, float smell) {

		float nearest = 999999999;

		for (int x = 0; x < Main.landmass.length; x++) {
			for (int y = 0; y < Main.landmass[0].length; y++) {

				if (Main.landmass[x][y].id != LandGen.GRASS)
					continue;

				if (Math.sqrt((Main.landmass[x][y].x - cell.getX()) * (Main.landmass[x][y].x - cell.getX())
						+ (Main.landmass[x][y].y - cell.getY()) * (Main.landmass[x][y].y - cell.getY())) < nearest) {
					nearest = (float) Math
							.sqrt((Main.landmass[x][y].x - cell.getX()) * (Main.landmass[x][y].x - cell.getX())
									+ (Main.landmass[x][y].y - cell.getY()) * (Main.landmass[x][y].y - cell.getY()));
				}
			}
		}

		return nearest;

	}

	public static Cell smellNearestCell(Cell cell, float smell) {

		int index = 0;
		float nearest = 999999999;

		for (int i = 0; i < Main.cells.size(); i++) {

			try {
				if (i == Main.cells.indexOf(cell))
					continue;

				if (Math.sqrt((Main.cells.get(i).getX() - cell.getX()) * (Main.cells.get(i).getX() - cell.getX())
						+ (Main.cells.get(i).getY() - cell.getY())
								* (Main.cells.get(i).getY() - cell.getY())) < nearest) {
					nearest = (float) Math.sqrt((Main.cells.get(i).getX() - cell.getX())
							* (Main.cells.get(i).getX() - cell.getX())
							+ (Main.cells.get(i).getY() - cell.getY()) * (Main.cells.get(i).getY() - cell.getY()));
					index = i;
				}
			} catch (Exception e) {
			}

		}

		if (nearest > smell) {
			return null;
		}

		return Main.cells.get(index);

	}

	public static float smellNearestCellDist(Cell cell, float smell) {

		float nearest = 100;
		try {
			for (int i = 0; i < Main.cells.size(); i++) {

				if (i == Main.cells.indexOf(cell))
					continue;

				if (Math.sqrt((Main.cells.get(i).getX() - cell.getX()) * (Main.cells.get(i).getX() - cell.getX())
						+ (Main.cells.get(i).getY() - cell.getY())
								* (Main.cells.get(i).getY() - cell.getY())) < nearest) {
					nearest = (float) Math.sqrt((Main.cells.get(i).getX() - cell.getX())
							* (Main.cells.get(i).getX() - cell.getX())
							+ (Main.cells.get(i).getY() - cell.getY()) * (Main.cells.get(i).getY() - cell.getY()));

				}

			}
		} catch (Exception e) {

		}
		return nearest;

	}

	public static float senseNoiseOfTile(int x, int y) {

		if (x > Main.landmass.length || y > Main.landmass[0].length || x < 0 || y < 0)
			return 0;

		try {
		return (float) Main.landmass[x][y].noise;
		}catch(Exception e) {
			return 0;
		}

	}

}
