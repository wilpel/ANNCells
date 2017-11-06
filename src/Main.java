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
import org.newdawn.slick.geom.Rectangle;

public class Main extends BasicGame {

	public static List<Cell> cells = new ArrayList<Cell>();
	public static List<Food> food = new ArrayList<Food>();

	public static int FOOD_AMOUNT = 100;
	public static int CELL_AMOUNT = 100;

	public static float yearsPassed = 0;
	public int seed;
	public double noise;
	public static int size = 20;
	public static int width = 1270;
	public static int height = 720;
	public static LandGen[][] landmass = new LandGen[width*2/size][height*2/size];

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
			container.setDisplayMode(width, height, false);
			container.setAlwaysRender(true);
			container.setTargetFrameRate(100);
			// container.setVSync(true);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

	public static float cameraX, cameraY;

	public void render(GameContainer arg0, Graphics g) throws SlickException {

		if (isRendering) {
			g.translate(cameraX, cameraY);

			for (int x = 0; x < landmass.length; x++) {
				for (int y = 0; y < landmass[0].length; y++) {
					landmass[x][y].render(g);
				}
			}

			for (int i = 0; i < food.size(); i++)
				food.get(i).render(g);

			for (int i = 0; i < cells.size(); i++)
				cells.get(i).render(g);

			g.resetTransform();

			renderStatistics(g);
		} else {
			g.setColor(Color.white);
			g.drawString("Year:" + yearsPassed / 10, 10, 40);
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
		for (int i = 0; i < populationGraph.size(); i++) {
			try {
				g.drawLine((i - 1) * 10, 250 - populationGraph.get(i - 1) / 5, i * 10,
						250 - populationGraph.get(i - 1) / 5);
			} catch (Exception e) {
			}
		}

	}

	public void init(GameContainer arg0) throws SlickException {

		Names.init();
		seed = new Random().nextInt(10000);

		for (int i = 0; i < CELL_AMOUNT; i++) {
			cells.add(new Cell(new Random().nextInt(1270), new Random().nextInt(720), Names.getLastName()));
		}
		// Needs seed for new Maps every time
		for (int i = 0; i < landmass.length; i++) {
			for (int j = 0; j < landmass[0].length; j++) {
				noise = SimplexNoiseLib.noise(((i * size) * 0.002) - seed, ((j * size) * 0.002) - seed);
				landmass[i][j] = (new LandGen((i * size)-(width/2), (j * size)-(height/2), size, noise));
			}
		}

		System.out.println(width*height);
		
		// for(int i = 0; i < FOOD_AMOUNT; i++) {
		//
		// int randomMass = new Random().nextInt(landmass.size());
		//
		// if(landmass.get(randomMass).id == LandGen.GRASS)
		// food.add(new Food(landmass.get(randomMass).x, landmass.get(randomMass).y));
		// }

	}

	public void update(GameContainer arg0, int delta) throws SlickException {

		container.setTitle("Neural Simulator | Leading Family: " + getTopFamily() + " | Total: " + cells.size()
				+ " | Highest gen: " + HIGHEST_GEN);

		
		for (int x = 0; x < landmass.length; x++) {
			for (int y = 0; y < landmass[0].length; y++) {
				landmass[x][y].update();
			}
		}
		
		
		for (int i = 0; i < cells.size(); i++)
			cells.get(i).update(delta);

		if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
			isRendering = true;
			container.setTargetFrameRate(60);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
			isRendering = false;
			container.setTargetFrameRate(Integer.MAX_VALUE);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			cameraY += 10f;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			cameraY -= 10f;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			cameraX += 10f;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			cameraX -= 10f;
		}

	}

	public static void lateUpdate() {

		while (true) {

			for (int i = 0; i < cells.size(); i++) {
				cells.get(i).lateUpdate();
			
			}

			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static void secondUpdate() {

		while (true) {

			yearsPassed++;

			populationGraph.add(cells.size());

			if (populationGraph.size() > 30)
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
		// food.add(new Food(new Random().nextInt(1270), new Random().nextInt(720)));

	}

	public static String getTopFamily() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();

		for (int i = 0; i < cells.size(); i++) {

			try {
				map.put((cells.get(i).NAME.split(" ")[1]), map.get(cells.get(i).NAME.split(" ")[1]) + 1);
			} catch (Exception e) {
				map.put((cells.get(i).NAME.split(" ")[1]), 0);
			}
		}

		String name = null;
		int largest = 0;
		for (String key : map.keySet()) {
			if (map.get(key) > largest) {
				largest = map.get(key);
				name = key + " | " + largest;
			}
		}

		return name;
	}

}
