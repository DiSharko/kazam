package pvpmagic;

import java.awt.Image;
import java.util.HashMap;


public class Wall extends Unit {
	public static Boolean STATICOBJ = true;
	public static String TYPE = "Wall";
	public static String VWALL = "vwall";
	public static String HWALL = "hwall";
	private final boolean VERTICAL;
	
	public Wall(GameData data, Vector pos, double size, boolean vertical){
		super(data, TYPE, STATICOBJ);
		_pos = pos;
		VERTICAL = vertical; //true is vertical, false is horizontal
		Image sprite = Resource.get(VERTICAL ? VWALL : HWALL);
		_size = new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(size);
		
		if (!VERTICAL){
			_size = new Vector(size, size/_size.x*_size.y);
		} else {
			_size = new Vector(size/_size.y*_size.x, size);

		}
		
		_movable = false;
		
		_shape = new Box(this, new Vector(VERTICAL ? 7 : 0, VERTICAL ? 0 : 5), _size.minus(VERTICAL ? 14 : 0, VERTICAL ? 0 : 10));
		_restitution = 0.8;
	}
	
	public void draw(View v){
		v.drawImage(Resource.get(VERTICAL ? VWALL : HWALL), _pos, _size);
	}

	@Override
	public void collide(Collision c){
		//nothing happens when something collides with it
	}
	
	@Override
	public void changeHealth(double health, Player caster){}
	
	@Override
	public String toNet() {
		return "";
	}
	
	@Override
	public void fromNet(String[] networkString, HashMap<Integer, Unit> objectMap) {}

}