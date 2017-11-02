
public class PhysicsHandeler {

	public static Food isCollidingWithFood(Cell cell) {
		
		for(int i = 0; i < Main.food.size() ; i++) {
			
			if(cell.getHitbox().intersects(Main.food.get(i).getHitbox())) {
				return Main.food.get(i);
			}
			
		}
		
		return null;
		
	}
	
}
