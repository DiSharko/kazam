package pvpmagic;

import java.util.ArrayList;


public class Wall extends Unit {
	public static String TYPE = "Wall";
	public static String VWALL = "vwall";
	public static String HWALL = "hwall";
	private final boolean VERTICAL;
	
	public Wall(GameData data, Vector pos, double size, boolean orientation){
		super(data, TYPE);
		_pos = pos;
		_size = new Vector(145, 117).normalize().mult(size);
		_movable = false;
		
		_shape = new Box(this, new Vector(0,0), _size);
		_restitution = 0.8;
		VERTICAL = orientation; //true is vertical, false is horizontal
	}
	
	public void setShape(){
		ArrayList<Vector> points = new ArrayList<Vector>();
		points.add(new Vector(0,_size.y*3/4));
		points.add(new Vector(_size.x/2, 0));
		points.add(new Vector(_size.x, _size.y*3/4));
		_shape = new Polygon(this, points);
	}
	
	public void draw(View v){
		setShape();
//		v.getGraphics().setColor(Color.gray);
//		v.fillRect(_pos, _size);
//		v.getGraphics().setColor(Color.DARK_GRAY);
//		v.drawRect(_pos, _size);
		if (VERTICAL)
			v.drawImage(Resource._gameImagesAlpha.get(VWALL), _pos, _size);
		else
			v.drawImage(Resource._gameImagesAlpha.get(HWALL), _pos, _size);
	}

	@Override
	public void collide(Collision c){
		//nothing happens when something collides with it
	}	
}