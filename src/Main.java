import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Main extends BasicGame{
	
	public static List<Cell> cells = new ArrayList<Cell>();
	public static List<Food> food = new ArrayList<Food>();
	
	public static int FOOD_AMOUNT = 100;
	public static int CELL_AMOUNT = 10;
	
	public static String log = "Nothing yet..";
	
    private static AppGameContainer container;

	public Main(String title) {
		super(title);
	}

	public static void main(String[] args) {
		
		try { 
			container = new AppGameContainer(new Main("Neural Simulator")); 
		    container.setDisplayMode(1270,720,false); 
		    container.setAlwaysRender(true);
		    container.setTargetFrameRate(60);
		    container.start(); 
		} catch (SlickException e) { 
		    e.printStackTrace(); 
		}
		
	}
	
	public float cameraX, cameraY;
	public void render(GameContainer arg0, Graphics g) throws SlickException {

		g.translate(cameraX, cameraY);
		
		for(int i = 0; i < cells.size(); i++)
			cells.get(i).render(g);
		
		for(int i = 0; i < food.size(); i++)
			food.get(i).render(g);
		g.resetTransform();
		
		g.setColor(new Color(1f, 1f, 1f, 0.2f));
		g.fillRect(0, 0, 1270, 50);
		g.setColor(Color.green);
		g.drawString(log, 10, 30);
		
	}

	public void init(GameContainer arg0) throws SlickException {
		
		Names.init();
		
		for(int i = 0; i < FOOD_AMOUNT; i++) {
			food.add(new Food(new Random().nextInt(1270), new Random().nextInt(720)));
		}
		
		for(int i = 0; i < CELL_AMOUNT; i++) {
			cells.add(new Cell(new Random().nextInt(1270), new Random().nextInt(720), Names.getLastName()));
		}
		
	}

	public void update(GameContainer arg0, int delta) throws SlickException {
	
		container.setTitle("Neural Simulator | Leading Family: "+getTopFamily()+" | Total: "+cells.size());
		
		for(int i = 0; i < cells.size(); i++)
			cells.get(i).update(delta);
		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			cameraY+=10f;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			cameraY-=10f;
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			cameraX+=10f;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			cameraX-=10f;
		}
		
	}
	
	public static void eatFood(Food tempFood) {
		
		food.remove(tempFood);
		food.add(new Food(new Random().nextInt(1270), new Random().nextInt(720))); 
		
	}
	
	public static String getTopFamily(){
		HashMap<String, Integer> map = new HashMap<String, Integer>();
 		
		for(int i = 0; i < cells.size(); i++) {
			
			try {
			map.put((cells.get(i).NAME.split(" ")[1]),map.get(cells.get(i).NAME.split(" ")[1])+1);
			}catch(Exception e) {
				map.put((cells.get(i).NAME.split(" ")[1]), 0);
			}
		}
		
		String name = null;
		int largest = 0;
		for (String key : map.keySet()) {
		    if(map.get(key)>largest) {
		    	largest = map.get(key);
		    	name = key+" | "+largest;
		    }
		}
		
		
		return name;
	}
	
}
