package pvpmagic;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;


public class Flag extends Unit {
	public static Boolean STATICOBJ = true;
	public static String TYPE = "Flag";
	public Vector _originalPos;
	
	public Flag(GameData data, Vector pos, double size){
		super(data, TYPE, STATICOBJ);
		_pos = pos;
		_originalPos = pos;
		
		Image sprite = Resource.get("flag");
		_size = new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(90);
		
		_shape = new Box(this, new Vector(0,0), _size);
		_restitution = 0.5;
		_appliesFriction = true;
	}
	
	public void setShape(){
		ArrayList<Vector> points = new ArrayList<Vector>();
		points.add(new Vector(0,_size.y*3/4));
		points.add(new Vector(_size.x/2, 0));
		points.add(new Vector(_size.x, _size.y*3/4));
		_shape = new Polygon(this, points);
	}
	
	public void draw(View v){
		v.drawImage(Resource.get("flag"), _pos, _size);
	}

	@Override
	public void changeHealth(double h){};
	
	@Override
	public void collide(Collision c){
		Unit u = c.other(this);
		if (u._type.equals(Player.TYPE)){
			System.out.println("player has flag");
			Player player = (Player) u;
			player._flag = this;
			_delete = true;
		}
	}
	@Override
	public String toNet() {
		return _netID +
				"\t" + (_staticObj ? "static" : _type) + 
				"\t" + _pos + 
				"\t" + _delete;
	}
	
	@Override
	public void fromNet(String[] networkString, HashMap<Integer, Unit> objectMap) {
		if (networkString[1].equals(_staticObj ? "static" : _type) 
				&& _netID == Integer.parseInt(networkString[0])
				&& networkString.length == 4) {
			_pos = Vector.fromNet(networkString[2]);
			_delete = Boolean.parseBoolean(networkString[3]);
		}
	}
}