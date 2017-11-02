import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Main extends BasicGame{
	
	public static List<Cell> cells = new ArrayList<Cell>();
	public static List<Food> food = new ArrayList<Food>();
	
	public static int FOOD_AMOUNT = 50;
	public static int CELL_AMOUNT = 100;
	
	public Main(String title) {
		super(title);
	}

	public static void main(String[] args) {
		
		try { 
		    AppGameContainer container = new AppGameContainer(new Main("Neural Simulator")); 
		    container.setDisplayMode(1270,720,false); 
		    container.setTargetFrameRate(60);
		    container.start(); 
		} catch (SlickException e) { 
		    e.printStackTrace(); 
		}
		
	}
	
	public void render(GameContainer arg0, Graphics g) throws SlickException {

		for(int i = 0; i < cells.size(); i++)
			cells.get(i).render(g);
		
		for(int i = 0; i < food.size(); i++)
			food.get(i).render(g);
		
	}

	public void init(GameContainer arg0) throws SlickException {
		
		for(int i = 0; i < FOOD_AMOUNT; i++) {
			food.add(new Food(new Random().nextInt(1270), new Random().nextInt(720)));
		}
		
		for(int i = 0; i < CELL_AMOUNT; i++) {
			cells.add(new Cell(new Random().nextInt(1270), new Random().nextInt(720)));
		}
		
	}

	public void update(GameContainer arg0, int delta) throws SlickException {
	
		for(int i = 0; i < cells.size(); i++)
			cells.get(i).update();
		
	}
	
	public static void eatFood(Food tempFood) {
		
		food.remove(tempFood);
		food.add(new Food(new Random().nextInt(1270), new Random().nextInt(720))); 
		
	}
	
}