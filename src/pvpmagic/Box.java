package pvpmagic;

import java.util.ArrayList;

public class Box extends Polygon {

	public Vector _size;
	
	public Box(Unit parent, Vector relPos, Vector size){
		super(parent, null);
		_relPos = relPos;
		_size = size;
		createShape();
	}
	
	public void createShape(){
		points = new ArrayList<Vector>();
		points.add(new Vector(0,0));
		points.add(new Vector(_size.x, 0));
		points.add(new Vector(_size.x, _size.y));
		points.add(new Vector(0, _size.y));
	}
	
	@Override
	public void draw(View v, boolean fill) {
		if (fill) v.fillRect(getPosition(), _size);
		else v.drawRect(getPosition(), _size);
	}

	@Override
	public Box getBounds() {
		return this;
	}
	
	@Override
	public void setSize(Vector newSize){
		_size = newSize;
	}
	
	@Override
	public ArrayList<Vector> getSeparatingAxes(Shape other){
		ArrayList<Vector> axes = new ArrayList<Vector>();
		axes.add(new Vector(0, 1));
		axes.add(new Vector(1, 0));
		
		return axes;
	}
}
