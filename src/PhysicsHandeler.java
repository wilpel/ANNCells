
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
