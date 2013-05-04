package pvpmagic;

import java.awt.Color;
import java.util.ArrayList;

public abstract class Shape {

	protected Unit _parent;
	
	public boolean colliding = false;
	Vector _relPos;
	
	static Color _debugColor = new Color(1, 0.1f, 0.1f, 0.5f);
	
	public static Double intervalMTV(Vector a, Vector b){
		double aRight = b.y - a.x;
		double aLeft = a.y - b.x;
		if (aLeft < 0 || aRight < 0) return null;
		if (aRight < aLeft) return aRight;
		return -aLeft;
	}
	
	public static Vector shapeMTV(Shape a, Shape b){
		Double minMagnitude = Double.POSITIVE_INFINITY;
		
		Vector mtv = null;
		
		ArrayList<Vector> axes = b.getSeparatingAxes(a);
		axes.addAll(a.getSeparatingAxes(b));
		
		for (int i = 0; i < axes.size(); i++){
			Vector axis = axes.get(i);
			Double mtv1d = intervalMTV(a.proj(axis), b.proj(axis));
			if (mtv1d == null) return null;
			if (Math.abs(mtv1d) < minMagnitude){
				minMagnitude = Math.abs(mtv1d);
				mtv = axis.normalize().mult(mtv1d);
			}
		}
		
		return mtv;
	}
	

	public static Collision collide(Shape a, Shape b){
		Vector mtv = shapeMTV(a, b);
		if (mtv != null){
			return new Collision(a._parent, b._parent, a, b, mtv);
		}
		
		return null;
	}


	public abstract Vector proj(Vector axis);
	

	public abstract void draw(View v, boolean fill);

	public Vector getPosition(){
		if (_parent != null) return (_relPos.plus(_parent._pos));
		return _relPos;
	}

	public abstract Box getBounds();
	public abstract ArrayList<Vector> getSeparatingAxes(Shape other);
	public abstract void setSize(Vector newSize);
	
}
