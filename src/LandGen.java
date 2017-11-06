import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class LandGen {

	public float x, y, size;
	public double noise;
	public Rectangle landPc = new Rectangle(0, 0, 0, 0);
	public static Color green = new Color(0x71BC78);
	public static Color yellow = new Color(0xFCE883);
	public static Color blue = new Color(0x7CB9E8);
	public static Color white = new Color(1f, 1f, 1f);
	public Color this_color;

	public int id;

	public static int WATER = 0, GRASS = 1, SAND = 2;

	public LandGen(int x, int y, int size, double noise) {
		this.x = x;
		this.y = y;
		this.size = size + 1;
		this.noise = noise;

		if (noise < -0.2) {
			id = WATER;
		} else if (noise < 0.2) {
			id = SAND;
		} else if (noise < 0.8) {
			id = GRASS;
		} else {
			// this_color = white;
		}

	}

	public void eat() {

		if (id == GRASS && noise - 0.05 > -0.2) {

			noise -= 0.05f;

		}

	}

	public void render(Graphics g) {

		// System.out.println(x+" "+Main.cameraX+" | "+y+" "+Main.cameraY);
		if (x + 10 < -Main.cameraX || x > -Main.cameraX + Main.width || y - 1 < -Main.cameraY
				|| y > -Main.cameraY + Main.height) {
			// System.out.println("outside!");
			return;
		}


		g.setColor(this_color);

		g.fillRect(x - 1, y - 1, size - 1, size - 1);

	}

	
	public void update() {
		
		
		if (noise < -0.2) {
			this_color = blue;
			id = WATER;
		} else if (noise < 0.2) {
			this_color = yellow;
			id = SAND;
		} else if (noise < 0.8) {
			this_color = green;
			id = GRASS;
		} else {
			this_color = white;
		}
		
		if (id == GRASS) {
			if (new Random().nextInt(1000) < 5) {

				int x1 = (int) (x / Main.size + new Random().nextInt(2) - 1);
				int y1 = (int) (y / Main.size + new Random().nextInt(2) - 1);
				try {
					if (Main.landmass[x1][y1].id == SAND) {

						Main.landmass[x1][y1].noise = 0.79;

					}
				} catch (Exception e) {

				}
			}
		}
		landPc.setBounds(x, y, size, size);
	}
	
}
