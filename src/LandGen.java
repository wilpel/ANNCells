import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class LandGen {
	
	public float x,y,size;
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
		this.size = size+1;
		this.noise = noise;
		
		
		if (noise < -0.2) {
			id = WATER;
		} else if(noise < 0.2) {
			id = SAND;
		} else if(noise < 0.8) {
			id = GRASS;
		} else {
			//this_color = white;
		}
		
	}
	
	public void render(Graphics g) {
		if (noise < -0.2) {
			this_color = blue;
		} else if(noise < 0.2) {
			this_color = yellow;
		} else if(noise < 0.8) {
			this_color = green;
		} else {
			this_color = white;
		}
		g.setColor(this_color);
		
		g.fillRect(x-1, y-1, size-1, size-1);
		
		landPc.setBounds(x,y, size, size);
	}

}

