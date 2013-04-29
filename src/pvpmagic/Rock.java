package pvpmagic;

import java.util.ArrayList;


public class Rock extends Unit {
	public Rock(GameData data, Vector pos, Vector size){
		super(data, "rock");
		_pos = pos;
		_size = new Vector(145, 117).normalize().mult(size.mag());
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
		setShape();
//		v.getGraphics().setColor(Color.gray);
//		v.fillRect(_pos, _size);
//		v.getGraphics().setColor(Color.DARK_GRAY);
//		v.drawRect(_pos, _size);
		v.drawImage(Resource._gameImagesAlpha.get("rock"), _pos, _size);
	}

	@Override
	public void collide(Collision c){
		Unit u = c.other(this);
		if (u.type().equals("spell")){
			u._health -= 10;
//			u._delete = true;
//			System.out.println("I am " + _pos +", "+_size);
//			System.out.println("Spell is "+ u._pos + ", "+u._size);
		}
	}
	
}