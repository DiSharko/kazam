package pvpmagic;

import java.util.ArrayList;


public class Circle extends Shape {
	public double _radius;


	public Circle(Unit parent, float radius){
		_parent = parent;
		_relPos = new Vector(0,0);
		_radius = radius;
	}

	@Override
	public void draw(View v, boolean fill) {
		if (fill) v.fillCircle(getPosition(),  _radius);
		else v.drawCircle(getPosition(), _radius);
	}


	@Override
	public Box getBounds(){
		Box b = new Box(null, _relPos, new Vector(2*_radius, 2*_radius));
		b._parent = _parent;
		return b;
	}

	public Vector getCenter(){
		return getPosition().plus(_radius, _radius);
	}

	@Override
	public void setSize(Vector newSize){
		_radius = newSize.x/2;
	}


	@Override
	public Vector proj(Vector axis){
		double projectedPoint = getPosition().plus(_radius, _radius).dot(axis.normalize());
		return new Vector(projectedPoint-_radius, projectedPoint+_radius);
	}
	
	@Override
	public ArrayList<Vector> getSeparatingAxes(Shape other){
		if (other instanceof Polygon) return getSeparatingAxes((Polygon) other);
		if (other instanceof Circle) return getSeparatingAxes((Circle) other);
		return null;
	}
	
	public ArrayList<Vector> getSeparatingAxes(Polygon p) {
		Vector closePoint = null;
		for (int i = 0; i < p.points.size(); i++){
			if (closePoint == null) closePoint = p.points.get(i);
			else if (p.points.get(i).plus(p.getPosition()).dist(getCenter()) < closePoint.dist(getCenter())) {
				closePoint = p.points.get(i).plus(p.getPosition());
			}
		}
		ArrayList<Vector> axes = new ArrayList<Vector>();
		axes.add(getCenter().minus(closePoint));

		return axes;
	}
	
	public ArrayList<Vector> getSeparatingAxes(Circle c) {
		ArrayList<Vector> axes = new ArrayList<Vector>();
		axes.add(getCenter().minus(c.getCenter()));

		return axes;
	}
	


}
