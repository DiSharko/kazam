package pvpmagic;

public abstract class Function {
	
	public static boolean collides(Vector p1, Vector s1, Vector p2, Vector s2){
		if (p1.x + s1.x < p2.x) return false;
		if (p1.x > p2.x+s2.x) return false;
		if (p1.y + s1.y < p2.y) return false;
		if (p1.y > p2.y+s2.y) return false;
		
		return true;
	}
}
