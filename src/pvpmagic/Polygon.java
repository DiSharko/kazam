package pvpmagic;

import java.util.ArrayList;

public class Polygon extends Shape {
	
	public ArrayList<Vector> points;
	
	public Polygon(Unit parent, ArrayList<Vector> _points){
		_parent = parent;
		_relPos = new Vector(0,0);
		points = _points;
	}
	
	@Override
	public ArrayList<Vector> getSeparatingAxes(Shape other){
		ArrayList<Vector> axes = new ArrayList<Vector>();
		for (int i = 0; i < points.size()-1; i++){
			axes.add(new Vector(-(points.get(i).y-points.get(i+1).y), points.get(i).x-points.get(i+1).x));
		}
		if (points.size() > 1){
			axes.add(new Vector(-(points.get(points.size()-1).y-points.get(0).y), points.get(points.size()-1).x-points.get(0).x));
		}
		
		return axes;
	}

	@Override
	public void draw(View v, boolean fill) {
		if (fill) v.fillPolygon(this);
		else v.drawPolygon(this);
	}

	@Override
	public Box getBounds() {
		Vector _size = new Vector(0,0);
		for (int i = 0; i < points.size(); i++){
			Vector p = points.get(i);
			if (p.x > _size.x) _size = new Vector(p.x, _size.y);
			if (p.y > _size.y) _size = new Vector(_size.x, p.y);
		}
		Box b = new Box(_parent,_relPos, _size);
		return b;
	}

	@Override
	public void setSize(Vector newSize) {}
	
	
	@Override
	public Vector proj(Vector axis) {
		Vector projection = null;
		for (int j = 0; j < points.size(); j++){
			Vector p = points.get(j).plus(getPosition());
			double projectedPoint = p.dot(axis.normalize());
			if (projection == null) projection = new Vector(projectedPoint, projectedPoint);
			else {
				if (projection.x > projectedPoint) projection = new Vector(projectedPoint, projection.y);
				if (projection.y < projectedPoint) projection = new Vector(projection.x, projectedPoint);
			}
		}
		return projection;
	}
	
}
