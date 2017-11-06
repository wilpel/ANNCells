import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.encog.EncogError;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;

public class BrainANN extends Brain {

	public static ArrayList<NetworkScore> scores = new ArrayList<NetworkScore>();

	public static LandGen nearestFood;

	public BasicNetwork network;

	private NormalizedField healthField;

	private NormalizedField positionField;

	private NormalizedField smellFoodField;

	public BrainANN() {

		healthField = new NormalizedField(NormalizationAction.Normalize, "health", 1, 0, -0.9, 0.9);

		positionField = new NormalizedField(NormalizationAction.Normalize, "position", 1, Integer.MIN_VALUE, -0.9, 0.9);

		smellFoodField = new NormalizedField(NormalizationAction.Normalize, "smellFood", 1, 0, -0.9, 0.9);

		network = createNetwork();

		System.out.println(network.getLayerNeuronCount(1));
		for (int i = 0; i < network.getLayerNeuronCount(1); i++) {
			// System.out.println("i: "+i);
			double currentWeight = network.getWeight(1, i, 0);
			// System.out.println(currentWeight);
			network.setWeight(1, i, 0, currentWeight * (new Random().nextFloat() + 0.5));
			// System.out.println(network.getWeight(1, i, 0));
		}

	}

	public BrainANN(BasicNetwork network) {

		healthField = new NormalizedField(NormalizationAction.Normalize, "health", 1, 0, -0.9, 0.9);

		positionField = new NormalizedField(NormalizationAction.Normalize, "position", 1, Integer.MIN_VALUE, -0.9, 0.9);

		smellFoodField = new NormalizedField(NormalizationAction.Normalize, "smellFood", 1, 0, -0.9, 0.9);

		if (new Random().nextBoolean()) {
			for (int i = 0; i < network.getLayerNeuronCount(1); i++) {
				double currentWeight = network.getWeight(1, i, 0);
				network.setWeight(1, i, 0, currentWeight * (new Random().nextFloat() + 0.5));
			}
		}

		this.network = network;
		// new BrainANN();
	}

	public static BasicNetwork createNetwork()

	{

		FeedForwardPattern pattern = new FeedForwardPattern();

		pattern.setInputNeurons(6);

		pattern.addHiddenLayer(8);
		pattern.addHiddenLayer(8);

		pattern.setOutputNeurons(4);

		pattern.setActivationFunction(new ActivationTANH());

		BasicNetwork network = (BasicNetwork) pattern.generate();

		network.reset();

		return network;

	}

	MLData input = new BasicMLData(6);

	public void update(Cell cell) {

		input.setData(0, this.positionField.normalize(cell.getX()));
		input.setData(1, this.positionField.normalize(cell.getY()));
		input.setData(2, this.smellFoodField.normalize(nearestFood == null ? 0 : nearestFood.x));
		input.setData(3, this.smellFoodField.normalize(nearestFood == null ? 0 : nearestFood.y));
		input.setData(4, this.healthField.normalize(cell.getHealth()));
		input.setData(5, this.healthField.normalize(cell.getEnergy()));

		// input.setData(1, this.smellFoodField.normalize(smellNearestFoodDist(cell,
		// cell.gene.smell)));

		try {
			MLData output = this.network.compute(input);

			if (output.getData(0) > 0) {
				wonderAround(cell, (float) output.getData(0));
			} else if (output.getData(1) > 0) {
				searchForFood(cell);
			} else if (output.getData(2) > 0) {
				searchForCell(cell);
			}

			LandGen currentFood = PhysicsHandeler.isCollidingWithTile(cell.getHitbox(), LandGen.GRASS);
			Cell currentCell = PhysicsHandeler.isCollidingWithOtherCell(cell);

			if (currentFood != null && cell.health < 100) {

				cell.setHealth(cell.getHealth() + 2);
				currentFood.eat();

			}
			if (currentCell != null) {
				// Bytte kontakt från parning till mord och gjorde att dem reproducerade med
				// sig själv ovan
				if (output.getData(3) > 0) {
					if (cell.getHealth() > 60 && cell.getLifeTime() > 3) {
						cell.givenBirth = true;
						Cell.giveBirth(cell, currentCell, cell.NAME.split(" ")[1]); // Hitta bättre lösning så de
																					// inte parar med
						// sig själv.
					}
				} else if (currentCell.gene.size < cell.gene.size) {

					if (!currentCell.NAME.split(" ")[1].equals(cell.NAME.split(" ")[1])) {

						currentCell.die();
						cell.setHealth(cell.getHealth() + 20);
						cell.gene.size+=0.1;
						Main.log = "Got killed";
					}
				}

				// if (health > 60 && lifeTime > 1.5) {

				// giveBirth(this, currentCell, NAME.split(" ")[1]);
				// }

			}

		} catch (EncogError e) {
			e.printStackTrace();
		}

	}

	public void lateUpdate(Cell cell) {
		nearestFood = smellNearestFood(cell, cell.gene.smell);
	}

	public static BrainANN crossover(BrainANN a, BrainANN b) {

		BasicNetwork network = createNetwork();

		for (int i = 0; i < network.getLayerNeuronCount(1); i++) {
			double currentWeight = (a.network.getWeight(1, i, 0) + b.network.getWeight(1, i, 0)) / 2;
			network.setWeight(1, i, 0, currentWeight);
		}

		BrainANN tempBrain = new BrainANN(a.network);

		return tempBrain;
	}

}
