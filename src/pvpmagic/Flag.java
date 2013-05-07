package pvpmagic;

import java.awt.Image;
import java.util.ArrayList;


public class Flag extends Unit {
	public static String TYPE = "flag";
	public Vector _originalPos;
	
	public Flag(GameData data, Vector pos, double size){
		super(data, TYPE);
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
	public void collide(Collision c){
		Unit u = c.other(this);
		if (u._type.equals("player")){
			System.out.println("player has flag");
			Player player = (Player) u;
			player._flag = this;
			_delete = true;
		}
	}
}