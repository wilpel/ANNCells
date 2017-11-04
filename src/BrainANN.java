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
	
	
	public BasicNetwork network;

	private NormalizedField healthField;

	private NormalizedField positionField;

	private NormalizedField smellFoodField;

	public BrainANN() {

		healthField = new NormalizedField(NormalizationAction.Normalize, "health", 1, 0, -0.9, 0.9);

		positionField = new NormalizedField(NormalizationAction.Normalize, "position",1,
				Integer.MIN_VALUE, -0.9, 0.9);

		smellFoodField = new NormalizedField(NormalizationAction.Normalize, "smellFood", 1, 0, -0.9,
				0.9);

		network = createNetwork();

		System.out.println(network.getLayerNeuronCount(1));
		for (int i = 0; i < network.getLayerNeuronCount(1); i++) {
			//System.out.println("i: "+i);
			double currentWeight = network.getWeight(1, i, 0);
			//System.out.println(currentWeight);
			network.setWeight(1, i, 0, currentWeight * (new Random().nextFloat() + 0.5));
			//System.out.println(network.getWeight(1, i, 0));
		}

	}

	public BrainANN(BasicNetwork network) {

		healthField = new NormalizedField(NormalizationAction.Normalize, "health", 1, 0, -0.9, 0.9);

		positionField = new NormalizedField(NormalizationAction.Normalize, "position", 1,
				Integer.MIN_VALUE, -0.9, 0.9);

		smellFoodField = new NormalizedField(NormalizationAction.Normalize, "smellFood", 1, 0, -0.9,0.9);

		if(new Random().nextBoolean()) {
		for (int i = 0; i < network.getLayerNeuronCount(1); i++) {
			double currentWeight = network.getWeight(1, i, 0);
			network.setWeight(1, i, 0, currentWeight * (new Random().nextFloat()+0.5));
		}
		}
		
		
		this.network = network;
		// new BrainANN();
	}

	public static BasicNetwork createNetwork()

	{


		FeedForwardPattern pattern = new FeedForwardPattern();

		pattern.setInputNeurons(4);

		pattern.addHiddenLayer(5);

		pattern.setOutputNeurons(2);

		pattern.setActivationFunction(new ActivationTANH());

		BasicNetwork network = (BasicNetwork)pattern.generate();

		network.reset();

		return network;

	}

	@Override
	public void update(Cell cell) {

		MLData input = new BasicMLData(4);

		Food nearestFood = smellNearestFood(cell, Integer.MAX_VALUE);
		
		input.setData(0, this.positionField.normalize(cell.getX()));
		input.setData(1, this.positionField.normalize(cell.getY()));
		input.setData(2, this.smellFoodField.normalize(nearestFood.getX()));
		input.setData(3, this.smellFoodField.normalize(nearestFood.getY()));

		//input.setData(1, this.smellFoodField.normalize(smellNearestFoodDist(cell, cell.gene.smell)));

		try {
			MLData output = this.network.compute(input);

			
			cell.moveX((float) output.getData(0)*cell.gene.speed);
			cell.moveY((float) output.getData(1)*cell.gene.speed);
		} catch (EncogError e) {
			e.printStackTrace();
		}

	}

}
