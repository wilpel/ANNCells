import java.util.Random;

public class Gene {

	public float smell = 500;
	public float aggression = 1f;
	public float speed = 0.6f;
	public float size = 10;
	public float foodCapacity = 100;
	public float fightFlightPrio = 0; //If larger the ZERO kill bitches.
	
	public static int MUTATION_DIVIDER = 10;
	
	public static Gene mixGene(Gene a, Gene b) {
		
		Gene newGene = new Gene();
		newGene.fightFlightPrio = ((a.fightFlightPrio+b.fightFlightPrio)/2)+(new Random().nextFloat()-0.5f)/MUTATION_DIVIDER;
		newGene.smell = ((a.smell+b.smell)/2)+(new Random().nextFloat()-0.5f)/MUTATION_DIVIDER;
		newGene.aggression = ((a.aggression+b.aggression)/2)+(new Random().nextFloat()-0.5f)/MUTATION_DIVIDER;
		newGene.size = ((a.size+b.size)/2)+(new Random().nextFloat()-0.5f)*5;
		newGene.speed = ((a.speed+b.speed)/2)+(new Random().nextFloat()-0.5f)/MUTATION_DIVIDER;
		newGene.foodCapacity = ((a.foodCapacity+b.foodCapacity)/2)+(new Random().nextFloat()-0.5f)/MUTATION_DIVIDER;
		
		
		return newGene;
	}
	
}
