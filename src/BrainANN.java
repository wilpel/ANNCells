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

		for (int j = 0; j < network.getLayerCount()-1; j++) {
			for (int i = 0; i < network.getLayerNeuronCount(j); i++) {
				
				try {
				double weight = network.getWeight(j, i, i+1)*(new Random().nextFloat()+0.5f);
				network.setWeight(j, i, i+1, weight);
				}catch(Exception e) {
					
				}
			}
		}
	}

	public BrainANN(BasicNetwork network) {

		healthField = new NormalizedField(NormalizationAction.Normalize, "health", 1000, 0, 0.9, -0.9);

		positionField = new NormalizedField(NormalizationAction.Normalize, "position", 1, Integer.MIN_VALUE, 0.9, -0.9);

		sensorField = new NormalizedField(NormalizationAction.Normalize, "sensor", 1, -1, 0.9, -0.9);

		for (int j = 0; j < network.getLayerCount()-1; j++) {
			for (int i = 0; i < network.getLayerNeuronCount(j); i++) {
				
				try {
				double weight = network.getWeight(j, i, i+1)*0.9f + Math.random() * (0.9f - 1.1f);
				network.setWeight(j, i, i+1, weight);
				}catch(Exception e) {
					
				}
			}
		}
		this.network = network;
		// new BrainANN();
	}

	public static BasicNetwork createNetwork()

	{

		int layers = new Random().nextInt(10);

		FeedForwardPattern pattern = new FeedForwardPattern();

		pattern.setInputNeurons(4);

		for (int i = 0; i < layers; i++) {

			pattern.setOutputNeurons(new Random().nextInt(100));

		}

		pattern.setOutputNeurons(6);
		
		pattern.setActivationFunction(new ActivationReLU(10, -10));

		BasicNetwork network = (BasicNetwork) pattern.generate();

		network.reset();

		return (BasicNetwork) network.clone();

	}

	
	public static BasicNetwork createNetwork(int layers)

	{

		FeedForwardPattern pattern = new FeedForwardPattern();

		pattern.setInputNeurons(4);

		for (int i = 0; i < layers; i++) {

			pattern.setOutputNeurons(new Random().nextInt(100));

		}

		pattern.setOutputNeurons(6);
		
		pattern.setActivationFunction(new ActivationReLU(1, -1));

		BasicNetwork network = (BasicNetwork) pattern.generate();

		network.reset();

		return (BasicNetwork) network.clone();

	}

	MLData input = new BasicMLData(5);

	public void update(Cell cell) {

		input.setData(0, this.positionField.normalize(cell.getRotation()));
		input.setData(1, this.positionField.normalize(nearestSmelledCell==null?0:PhysicsHandeler.getTargetAngle(cell.getX(), cell.getY(), nearestSmelledCell.getX(), nearestSmelledCell.getY())));
		input.setData(2, this.healthField.normalize(cell.getEnergy()));
		input.setData(3, this.sensorField.normalize(senseNoiseOfTile((int)cell.getX()/Main.size, (int)cell.getY()/Main.size)));
		input.setData(4, this.healthField.normalize(cell.getHealth()));
		
		
		// input.setData(1, this.smellFoodField.normalize(smellNearestFoodDist(cell,
		// cell.gene.smell)));

		try {
			MLData output = this.network.compute(input);

			// if (output.getData(0) > 0) {
			// wonderAround(cell, (float) output.getData(0));
			// } else if (output.getData(1) > 0) {
			// searchForFood(cell);
			// } else if (output.getData(2) > 0) {
			// searchForCell(cell);
			// }

			cell.move((float) output.getData(0));
			cell.rotate((float) output.getData(1));
			cell.rotate((float) -output.getData(2));

	
			if(output.getData(4) > 0&&nearestFood != null) {
				turnToFood(cell, (float) output.getData(4));
			}
			
			if(output.getData(5) > 0) {
				turnToCell(cell, (float) output.getData(5));
			}
			
			LandGen currentFood = PhysicsHandeler.isCollidingWithTile(cell.getHitbox(), LandGen.GRASS);
			Cell currentCell = PhysicsHandeler.isCollidingWithOtherCell(cell);

			if (currentFood != null && cell.health < 100) {

				cell.setHealth(cell.getHealth() + 2);
				currentFood.eat();

			} 
			if (smellNearestFoodDist(cell, cell.gene.smell) < 50&&currentCell!=null) {
				// Bytte kontakt från parning till mord och gjorde att dem reproducerade med
				// sig själv ovan
				
				
				if (output.getData(3) > 0) {
					if (cell.getHealth() > 60 && cell.getLifeTime() > 1.5) {
						cell.givenBirth = true;
						cell.giveBirth(cell, currentCell, cell.NAME.split(" ")[1]);
						cell.setHealth(cell.getHealth()-40);
					}
				
				}else {	
				if (currentCell.gene.size < cell.gene.size) {

					if (!currentCell.NAME.split(" ")[1].equals(cell.NAME.split(" ")[1])) {

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

		} catch (EncogError e) {
			e.printStackTrace();
		}

	}

	public void lateUpdate(Cell cell) {
		nearestFood = smellNearestFood(cell, cell.gene.smell);
		nearestSmelledCell = smellNearestCell(cell,cell.gene.smell);
	}

	public static BrainANN crossover(BrainANN a, BrainANN b) {

		BasicNetwork network = createNetwork(a.network.getLayerCount());

		BrainANN tempBrain = new BrainANN(a.network);

		if (new Random().nextBoolean()) {
			for (int i = 0; i < network.getLayerTotalNeuronCount(1) / 2; i++) {
				try {
				double currentWeight = (new Random().nextBoolean() ? a.network.getWeight(1, i, 0)
						: b.network.getWeight(1, i, 0));
				network.setWeight(1, i, 0, currentWeight);
				}catch(Exception e) {
					
				}
				}
		}

		return tempBrain;
	}

}
