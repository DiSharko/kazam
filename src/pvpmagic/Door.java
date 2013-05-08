package pvpmagic;


import java.awt.Image;
import java.util.HashMap;

import pvpmagic.spells.LockSpell;


public class Door extends Unit {
	Vector _openSize;
	Vector _lockedSize;
	public static Boolean STATICOBJ = true;
	public static String TYPE = "Door";
	
	boolean _locked = true;
	
	private double _sizeScalar;
	
	public Door(GameData data, Vector pos, double size, String basicImage){
		super(data, TYPE, STATICOBJ, basicImage);
		_pos = pos;
		_sizeScalar = size;
		
		Image sprite = Resource.get("door_closed");
		_lockedSize = new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(_sizeScalar);
		
		sprite = Resource.get("door_open");
		_openSize = new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(_sizeScalar);
	
		_size = _lockedSize;
		
		_movable = false;
		
		_shape = new Box(this, new Vector(0,0), _size);
		_restitution = 0.8;
	}
	
	public void draw(View v){
		super.draw(v);
		v.drawImage(Resource.get(_basicImage), _pos, _size);
	}

	@Override
	public boolean canCollideWith(Unit u){
		if (_basicImage.equals("door_open")){
			if (u._type.equals(LockSpell.TYPE)) return true;
			return false;
		}
		return true;
	}
	
	@Override
	public void changeHealth(double health){}
	
	@Override
	public void collide(Collision c){
		//nothing happens if something collides with this
	}	
	
	public void lock() {
		_basicImage = "door_closed";
		_size = _lockedSize;
	}
	public void open() {
		_basicImage = "door_open";
		_size = _openSize;
	}
	
	@Override
	public String toNet() {
		return _netID + 
				"\t" + (_staticObj ? "static" : _type) + 
				"\t" + _locked;
	}
	
	@Override
	public void fromNet(String[] networkString, HashMap<Integer, Unit> objectMap) {
		if (networkString[1].equals(_staticObj ? "static" : _type) 
				&& _netID == Integer.parseInt(networkString[0])
				&& networkString.length == 3) {
			_locked = Boolean.parseBoolean(networkString[2]);
		}
	}
}