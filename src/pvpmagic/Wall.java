package pvpmagic;

import java.awt.Image;


public class Wall extends Unit {
	public static String TYPE = "Wall";
	public static String VWALL = "vwall";
	public static String HWALL = "hwall";
	private final boolean VERTICAL;
	
	public Wall(GameData data, Vector pos, double size, boolean vertical){
		super(data, TYPE);
		_pos = pos;
		VERTICAL = vertical; //true is vertical, false is horizontal
		Image sprite = Resource._gameImages.get(VERTICAL ? VWALL : HWALL);
		_size = new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(size);
		
		_movable = false;
		
		_shape = new Box(this, new Vector(VERTICAL ? 7 : 0, VERTICAL ? 0 : 5), _size.minus(VERTICAL ? 14 : 0, VERTICAL ? 0 : 10));
		_restitution = 0.8;
	}
	
	public void draw(View v){
		v.drawImage(Resource._gameImages.get(VERTICAL ? VWALL : HWALL), _pos, _size);
	}

	@Override
	public void collide(Collision c){
		//nothing happens when something collides with it
	}	
}