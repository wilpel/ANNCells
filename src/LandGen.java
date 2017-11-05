import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class LandGen {
	
	public float x,y,size;
	public double color;
	public Rectangle landPc = new Rectangle(0, 0, 0, 0);
	
	public LandGen(int x, int y, int size, double color) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.color = color;
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(0.5f, 0.5f, (float)color));
		g.fillRect(x-1, y-1, size-1, size-1);
		
		landPc.setBounds(x,y, size, size);
	}

}

