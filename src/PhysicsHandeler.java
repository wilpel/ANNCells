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
	
	public static boolean isCollidingWithTile(Rectangle rect, int id) {

		for (int i = 0; i < Main.landmass.size(); i++) {
			try {
				if (rect.intersects(Main.landmass.get(i).landPc)&&Main.landmass.get(i).id == id) {
					return true;
				}
			} catch (Exception e) {

			}
		}

		return false;

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

}
