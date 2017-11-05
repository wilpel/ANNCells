import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
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
	public static int CELL_AMOUNT = 25;
	
	public static int HIGHEST_GEN = 0;
	
	public static String log = "Nothing yet..";
	
	public static boolean isRendering = true;
	
    private static AppGameContainer container;
    
    public static List<Integer> populationGraph = new ArrayList<Integer>();

	public Main(String title) {
		super(title);
	}

	public static void main(String[] args) {
		
		

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				lateUpdate();
			}
		}).start();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				secondUpdate();
			}
		}).start();
		
		
		try { 
			container = new AppGameContainer(new Main("Neural Simulator")); 
		    container.setDisplayMode(1270,720,false); 
		    container.setAlwaysRender(true);
		    container.setTargetFrameRate(60);
		    //container.setVSync(true);
		    container.start(); 
		} catch (SlickException e) { 
		    e.printStackTrace(); 
		}
		
		
		
	}
	
	public static float cameraX, cameraY;
	public void render(GameContainer arg0, Graphics g) throws SlickException {

		if(isRendering) {
		g.translate(cameraX, cameraY);
		
		for(int i = 0; i < food.size(); i++)
			food.get(i).render(g);
		
		for(int i = 0; i < cells.size(); i++)
			cells.get(i).render(g);

		
		g.resetTransform();
		
		renderStatistics(g);
		}
	}
	
	public void renderStatistics(Graphics g) {
		
		g.setColor(new Color(1f, 1f, 1f, 0.2f));
		g.fillRect(0, 0, 1270, 50);
		g.setColor(Color.green);
		g.drawString(log, 10, 30);
		
		g.setColor(new Color(1f, 1f, 1f, 0.2f));
		g.fillRect(0, 60, 300, 200);
		g.setColor(Color.green);
		g.drawString("Population", 10, 70);
		
		g.setColor(Color.blue);
		for(int i = 0; i < populationGraph.size(); i++) {
			try {
			g.drawLine((i-1)*10, 250-populationGraph.get(i-1)/5, i*10, 250-populationGraph.get(i-1)/5);
			}catch(Exception e) {}
		}
		
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
	
		container.setTitle("Neural Simulator | Leading Family: "+getTopFamily()+" | Total: "+cells.size()+" | Highest gen: "+HIGHEST_GEN);
		
		for(int i = 0; i < cells.size(); i++)
			cells.get(i).update(delta);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_1)) {
			isRendering = true;
			container.setTargetFrameRate(60);
		}else if(Keyboard.isKeyDown(Keyboard.KEY_2)) {
			isRendering = false;
			container.setTargetFrameRate(Integer.MAX_VALUE);
		}
		
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
	
	public static void lateUpdate() {
		
		while(true) {
		
		for(int i = 0; i < cells.size(); i++)
			cells.get(i).lateUpdate();
		
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
	}
	
	public static void secondUpdate() {
		
		while(true) {
		
		
		
		populationGraph.add(cells.size());
		
		if(populationGraph.size()>30)
			populationGraph.remove(0);
		
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
	}
	
	public static void eatFood(Food tempFood) {
		
		food.remove(tempFood);
		//food.add(new Food(new Random().nextInt(1270), new Random().nextInt(720))); 
		
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
