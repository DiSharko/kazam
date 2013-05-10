package pvpmagic;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;


public class Flag extends Unit {
	public static Boolean STATICOBJ = true;
	public static String TYPE = "Flag";
	public Vector _originalPos;
	
	public Flag(GameData data, Vector pos, double size, String basic){
		super(data, TYPE, STATICOBJ, basic);
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
		super.draw(v);
		if (_collidable) 
			v.drawImage(Resource.get(_basicImage), _pos, _size);
	}

	@Override
	public void changeHealth(double h, Player caster){};
	
	@Override
	public void collide(Collision c){
		Unit u = c.other(this);
		if (u._type.equals("Player")){
			Player player = (Player) u;
			if(player._flagable) {
				player._flag = this;
				this._collidable = false;
				this._drawUnder = true;
				_vel = new Vector(0,0);
				_force = new Vector(0,0);
			}
		}
	}
	@Override
	public String toNet() {
		String pos = (_pos == null) ? null : _pos.toNet();
		String vel = (_vel == null) ? null : _vel.toNet();
		String force = (_force == null) ? null : _force.toNet();
		return _netID +
				"\t" + (_staticObj ? "static" : _type) + 
				"\t" + pos + 
				"\t" + _delete +
				"\t" + _basicImage + 
				"\t" + _collidable +
				"\t" + _drawUnder + 
				"\t" + vel +
				"\t" + force;
	}
	
	@Override
	public void fromNet(String[] networkString, HashMap<Integer, Unit> objectMap) {
		if (networkString[1].equals(_staticObj ? "static" : _type) 
				&& _netID == Integer.parseInt(networkString[0])
				&& networkString.length == 4) {
			_pos = Vector.fromNet(networkString[2]);
			_delete = Boolean.parseBoolean(networkString[3]);
			_basicImage = networkString[4];
			_collidable = Boolean.parseBoolean(networkString[5]);
			_drawUnder = Boolean.parseBoolean(networkString[6]);
			_vel = Vector.fromNet(networkString[6]);
			_force = Vector.fromNet(networkString[6]);
		}
	}
}