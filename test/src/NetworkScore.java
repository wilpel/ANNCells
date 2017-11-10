import java.util.Comparator;

import org.encog.neural.networks.BasicNetwork;

public class NetworkScore implements Comparator<NetworkScore>{

	public int score;
	public BasicNetwork network;

	public NetworkScore(BasicNetwork network, int score) {

		this.network = network;
		this.score = score;

	}

	@Override
	public int compare(NetworkScore o1, NetworkScore o2) {
		// TODO Auto-generated method stub
		return o1.score-o2.score;
	}


}
