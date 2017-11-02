import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Names {

	public static List<String> names = new ArrayList<String>();
	public static List<String> surnames = new ArrayList<String>();
	
	public static void init() {
		
		names.add("Homer");
		names.add("Peter");
		names.add("Bob");
		names.add("John");
		names.add("Jack");
		names.add("Rick");
		names.add("Morty");
		names.add("Megan");
		names.add("Lily");
		names.add("Marge");
		names.add("Bailey");
		
		surnames.add("Simpson");
		surnames.add("Griffin");
		surnames.add("Stone");
		surnames.add("Croft");
		surnames.add("Blue");
		surnames.add("Black");
		surnames.add("Longstone");
		surnames.add("Cage");
		surnames.add("Hanks");
		surnames.add("Starfish");
		
		
		
	}
	
	public static String getName() {
		return names.get(new Random().nextInt(names.size()))+" "+surnames.get(new Random().nextInt(surnames.size()));
	}
	
	public static String getFirstName() {
		return names.get(new Random().nextInt(names.size()));
	}
	
	public static String getLastName() {
		return surnames.get(new Random().nextInt(surnames.size()));
	}
	
}
