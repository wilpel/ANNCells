import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class LandGen {
	
	public float x,y,size;
	public double noise;
	public Rectangle landPc = new Rectangle(0, 0, 0, 0);
	public Color green = new Color(0f, 1f, 0f);
	public Color blue = new Color(0f, 0f, 1f);
	public Color white = new Color(1f, 1f, 1f);
	public Color this_color;
	
	public LandGen(int x, int y, int size, double noise) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.noise = noise;
	}
	
	public void render(Graphics g) {
		if (noise < -0.2) {
			this_color = blue;
		} else if(noise < 0.7) {
			this_color = green;
		} else {
			this_color = white;
		}
		g.setColor(this_color);
		
		g.fillRect(x-1, y-1, size-1, size-1);
		
		landPc.setBounds(x,y, size, size);
	}

}

