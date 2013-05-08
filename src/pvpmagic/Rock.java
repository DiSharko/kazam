package pvpmagic;

import java.awt.Image;
import java.util.ArrayList;


public class Rock extends Unit {
	public static String TYPE = "Rock";
	private boolean _dead = false;
	
	public Rock(GameData data, Vector pos, double size){
		super(data, TYPE);
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
		if (!_dead) v.drawImage(Resource.get("rock"), _pos, _size);
		else v.drawImage(Resource.get("deadrock"), _pos, _size);
	}

	@Override
	public void changeHealth(double amount){
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
		if (u._type.equals("spell")){
			u._health -= 10;
		}
	}
	
}