import org.newdawn.slick.geom.Rectangle;

public class PhysicsHandeler {

	public static Food isCollidingWithFood(Cell cell) {

		for (int i = 0; i < Main.food.size(); i++) {
			try {
				if (cell.getHitbox().intersects(Main.food.get(i).getHitbox())) {
					return Main.food.get(i);
				}
			} catch (Exception e) {

			}
		}

		return null;

	}

	public static LandGen isCollidingWithTile(Rectangle rect, int id) {

		for (int x = 0; x < Main.landmass.length; x++) {
			for (int y = 0; y < Main.landmass.length; y++) {
				try {
					if (rect.intersects(Main.landmass[x][y].landPc) && Main.landmass[x][y].id == id) {
						return Main.landmass[x][y];
					}
				} catch (Exception e) {

				}
			}
		}

		return null;

	}

	public static Cell isCollidingWithOtherCell(Cell cell) {

		for (int i = 0; i < Main.cells.size(); i++) {

			try {
				if (i == Main.cells.indexOf(cell))
					continue;

				if (cell.getHitbox().intersects(Main.cells.get(i).getHitbox())) {
					return Main.cells.get(i);
				}
			} catch (Exception e) {

			}

		}

		return null;

	}

	public static float getTargetAngle(float startX, float startY, float targetX, float targetY) {
		float dx = targetX - startX;
		float dy = targetY - startY;
		return (float) Math.toDegrees(Math.atan2(dy, dx));
	}

}
