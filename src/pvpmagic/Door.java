package pvpmagic;

import java.awt.Color;
import java.util.ArrayList;


public class Door extends Unit {
	private Vector _openPos;
	private Vector _lockPos;
	
	public Door(GameData data, Vector lockpos, Vector openpos, double size){
		super(data, "door");
		_lockPos = lockpos;
		_openPos = openpos;
		_pos = _lockPos;
		_size = new Vector(145, 117).normalize().mult(size);
		_movable = false;
		
		_shape = new Box(this, new Vector(0,0), _size);
		_restitution = 0.8;
	}
	
	public void setShape(){
		ArrayList<Vector> points = new ArrayList<Vector>();
		points.add(new Vector(0,_size.y));
		points.add(new Vector(_size.x, 0));
		points.add(new Vector(0, 0));
		points.add(new Vector(_size.x, _size.y));
		_shape = new Polygon(this, points);
	}
	
	public void draw(View v){
		setShape();
		v.getGraphics().setColor(new Color(139, 69, 19));
		v.fillRect(_pos, _size);
		v.getGraphics().setColor(Color.black);
		v.drawRect(_pos, _size);
	}

	@Override
	public void collide(Collision c){
		//nothing happens if something collides with this
	}	
	
	public void lock() {
		_pos = _lockPos;
	}
	public void open() {
		_pos = _openPos;
	}
}