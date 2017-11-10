import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.encog.EncogError;
import org.encog.engine.network.activation.ActivationReLU;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.genetic.mutate.MutateShuffle;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;

public class BrainANN extends Brain {

	public static ArrayList<NetworkScore> scores = new ArrayList<NetworkScore>();

	public BasicNetwork network;

	private NormalizedField healthField;

	private NormalizedField positionField;

	private NormalizedField sensorField;

	public static MutateShuffle mutationShuffle = new MutateShuffle();

	public BrainANN() {

		healthField = new NormalizedField(NormalizationAction.Normalize, "health", 1000, 0, 0.9, -0.9);

		positionField = new NormalizedField(NormalizationAction.Normalize, "position", 1, Integer.MIN_VALUE, 0.9, -0.9);

		sensorField = new NormalizedField(NormalizationAction.Normalize, "sensor", 1, -1, 0.9, -0.9);

		network = createNetwork();

	}

	public BrainANN(BasicNetwork network) {

		healthField = new NormalizedField(NormalizationAction.Normalize, "health", 1000, 0, 0.9, -0.9);

		positionField = new NormalizedField(NormalizationAction.Normalize, "position", 1, Integer.MIN_VALUE, 0.9, -0.9);

		sensorField = new NormalizedField(NormalizationAction.Normalize, "sensor", 1, -1, 0.9, -0.9);

		for (int j = 0; j < network.getLayerCount() - 1; j++) {
			for (int i = 0; i < network.getLayerNeuronCount(j); i++) {

				try {
					double mutation = -1 + Math.random() * (1 - -1);
					double weight = network.getWeight(j, i, i + 1) * mutation;
					System.out.println("mutation: " + mutation);
					network.setWeight(j, i, i + 1, weight);
				} catch (Exception e) {

				}
			}
		}
		this.network = network;
		// new BrainANN();
	}

	public static BasicNetwork createNetwork()

	{

		int layers = 4;

		FeedForwardPattern pattern = new FeedForwardPattern();

		pattern.setInputNeurons(4);

		for (int i = 0; i < layers; i++) {

			pattern.setOutputNeurons(100);

		}

		pattern.setOutputNeurons(4);

		pattern.setActivationFunction(new ActivationReLU(10, -10));

		BasicNetwork network = (BasicNetwork) pattern.generate();

		network.reset();

		return (BasicNetwork) network.clone();

	}

	MLData input = new BasicMLData(5);

	public void update(Cell cell) {

		input.setData(0, this.positionField.normalize(cell.getRotation()));
		input.setData(1,
				this.positionField.normalize(nearestSmelledCell == null ? 0
						: PhysicsHandeler.getTargetAngle(cell.getX(), cell.getY(), nearestSmelledCell.getX(),
								nearestSmelledCell.getY())));
		input.setData(2, this.healthField.normalize(cell.getEnergy()));
		input.setData(3, this.sensorField.normalize(senseNoiseOfTile(cell)));
		input.setData(4, this.healthField.normalize(cell.getHealth()));

		// input.setData(1, this.smellFoodField.normalize(smellNearestFoodDist(cell,
		// cell.gene.smell)));

		try {

			LandGen currentFood = PhysicsHandeler.isCollidingWithTile(cell.getHitbox(), LandGen.GRASS);
			Cell currentCell = PhysicsHandeler.isCollidingWithOtherCell(cell);

			MLData output = this.network.compute(input);

			cell.rotate((float) output.getData(0));
			cell.move((float) output.getData(1));


			if (output.getData(3) > 0) {
				if (cell.getHealth()-20 > 40 && cell.getLifeTime() > 1.5) {
					cell.givenBirth = true;
					cell.giveBirth(cell, cell, cell.NAME.split(" ")[1]);
					cell.setHealth(cell.getHealth() - 20);
				}
			}

			if (currentFood != null && cell.health < cell.gene.foodCapacity) {

				currentFood.eat(cell);

			}
			
			if (smellNearestFoodDist(cell, cell.gene.smell) < 100 && currentCell != null) {

				if (output.getData(3) > 0) {
					if (currentCell.gene.size < cell.gene.size) {

						if (!currentCell.NAME.split(" ")[1].equals(cell.NAME.split(" ")[1])) {

							System.out.println("Killed cell!");

							currentCell.die();
							cell.setHealth(cell.getHealth() + 20);
							cell.gene.size += 0.1;
							Main.log = "Got killed";
						}
					}
				}
				// if (health > 60 && lifeTime > 1.5) {

				// giveBirth(this, currentCell, NAME.split(" ")[1]);
				// }

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void lateUpdate(Cell cell) {
		nearestFood = smellNearestFood(cell, cell.gene.smell);
		nearestSmelledCell = smellNearestCell(cell, cell.gene.smell);
	}

	public static BrainANN crossover(BrainANN a, BrainANN b) {

		BasicNetwork network = createNetwork();

		BrainANN tempBrain = new BrainANN(a.network);

		if (new Random().nextBoolean()) {
			for (int i = 0; i < network.getLayerTotalNeuronCount(1) / 2; i++) {
				try {
					double currentWeight = (new Random().nextBoolean() ? a.network.getWeight(1, i, 0)
							: b.network.getWeight(1, i, 0));
					network.setWeight(1, i, 0, currentWeight);
				} catch (Exception e) {

				}
			}
		}

		return tempBrain;
	}

}
