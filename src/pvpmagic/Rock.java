package pvpmagic;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

import pvpmagic.spells.Spell;


public class Rock extends Unit {
	public static Boolean STATICOBJ = true;
	public static String TYPE = "Rock";
	private boolean _dead = false;
	
	public Rock(GameData data, Vector pos, double size, String basic){
		super(data, TYPE, STATICOBJ, basic);
		_basicImage = "rock";
		_pos = pos;
		Image sprite = Resource.get("rock");
		_size = new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(size);
		_movable = false;
		
		_shape = new Box(this, new Vector(0,0), _size);
		_restitution = 0.8;
	}
	
	public void setShape(){
		ArrayList<Vector> points = new ArrayList<Vector>();
		points.add(new Vector(0,_size.y*3/4));
		points.add(new Vector(_size.x/2, 0));
		points.add(new Vector(_size.x, _size.y*3/4));
		_shape = new Polygon(this, points);
	}
	
	public void draw(View v){
		super.draw(v);
		if (!_dead) v.drawImage(Resource.get(_basicImage), _pos, _size);
		else v.drawImage(Resource.get("deadrock"), _pos, _size);
	}

	@Override
	public void changeHealth(double amount, Player caster){
		System.out.println("rock hit");
		_health -= 20;
	}
	
	@Override
	public void die(){
		System.out.println("rock died");
		_dead = true;
		_collidable = false;
		_drawUnder = true;
	}
	
	@Override
	public void collide(Collision c){
		Unit u = c.other(this);
		if (u instanceof Spell){
			u._health -= 10;
		}
	}
	
	@Override
	public String toNet() {
		return _netID +
				"\t" + (_staticObj ? "static" : _type) + 
				"\t" + _health +
				"\t" + _basicImage;
	}
	
	@Override
	public void fromNet(String[] networkString, HashMap<Integer, Unit> objectMap) {
		if (networkString[1].equals(_staticObj ? "static" : _type) 
				&& _netID == Integer.parseInt(networkString[0])
				&& networkString.length == 4) {
			_health = Integer.parseInt(networkString[2]);
			_basicImage = networkString[3];
		}
	}
	
}