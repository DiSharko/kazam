package pvpmagic;

import java.awt.Color;


public class Door extends Unit {
	private Vector _openPos;
	private Vector _lockPos;
	
	private double _sizeScalar;
	
	public Door(GameData data, Vector lockpos, Vector openpos, double size){
		super(data, "door");
		_lockPos = lockpos;
		_openPos = openpos;
		_pos = _lockPos;
		_sizeScalar = size;
		_size = new Vector(10, 60).normalize().mult(_sizeScalar);
		
		_movable = false;
		
		_shape = new Box(this, new Vector(0,0), _size);
		_restitution = 0.8;
	}
	
	public void draw(View v){
		if (_pos.equals(_openPos)) {
			v.getGraphics().setColor(new Color(139, 69, 19));
			v.fillRect(_pos, _size);
		} else {
			v.getGraphics().setColor(new Color(139, 69, 19));
			v.fillRect(_pos, _size);
		}
//		v.getGraphics().setColor(Color.black);
//		v.drawRect(_pos, _size);
	}

	@Override
	public void collide(Collision c){
		//nothing happens if something collides with this
	}	
	
	public void lock() {
		_pos = _lockPos;
		_size = new Vector(10, 60).normalize().mult(_sizeScalar);
		_shape = new Box(this, new Vector(0,0), _size);
	}
	public void open() {
		_pos = _openPos;
		_size = new Vector(60, 10).normalize().mult(_sizeScalar);
		_shape = new Box(this, new Vector(0,0), _size);
	}
}