package pvpmagic;

public class Collision {
	private Unit e1, e2;
	public Shape s1, s2;
	private Vector mtv;
	
	public Collision(Unit _e1, Unit _e2, Shape _s1, Shape _s2, Vector _mtv){
		e1 = _e1;
		e2 = _e2;
		s1 = _s1;
		s2 = _s2;
		mtv = _mtv;
	}
	
	
	public Unit other(Unit e){
		if (e == e1) return e2;
		return e1;
	}
	
	public Vector mtv(Unit e){
		if (e == e1) return mtv;
		return mtv.mult(-1);
	}
}
