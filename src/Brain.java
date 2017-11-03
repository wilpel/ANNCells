import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.MLMethod;
import org.encog.ml.MLResettable;
import org.encog.ml.MethodFactory;
import org.encog.ml.genetic.MLMethodGeneticAlgorithm;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
import org.encog.neural.pattern.FeedForwardPattern;

public class Brain {

	public Food nearestSmelledFood;
	

	public Brain() {

	}

	public void update(Cell cell) {

		searchForFood(cell);

	}
	
	public void searchForFood(Cell cell) {
		nearestSmelledFood = smellNearestFood(cell, cell.gene.smell);

		if (nearestSmelledFood != null) {
			if (nearestSmelledFood.getX() < cell.getX()+cell.gene.size/2) {
				cell.moveX(-cell.gene.speed);
			} else {
				cell.moveX(cell.gene.speed);
			}

			if (nearestSmelledFood.getY() < cell.getY()+cell.gene.size/2) {
				cell.moveY(-cell.gene.speed);
			} else {
				cell.moveY(cell.gene.speed);
			}
		}
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

}
