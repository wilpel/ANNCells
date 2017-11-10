import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Names {

	public static List<String> names = new ArrayList<String>();
	public static List<String> surnames = new ArrayList<String>();
	
	public static void init() {
		
		try (BufferedReader br = new BufferedReader(new FileReader(new File("res/firstname.db")))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       names.add(line.split(" ")[0].toLowerCase());
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try (BufferedReader br = new BufferedReader(new FileReader(new File("res/lastnames.db")))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       surnames.add(line.split(" ")[0].toLowerCase());
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		
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
