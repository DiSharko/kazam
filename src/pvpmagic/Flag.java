package pvpmagic;

import java.awt.Image;
import java.util.ArrayList;


public class Flag extends Unit {
	public Flag(GameData data, Vector pos, double size){
		super(data, "flag");
		_pos = pos;
		
		Image sprite = Resource._gameImages.get("flag");
		_size = new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(90);
		
		_movable = false;
		
		_shape = new Box(this, new Vector(0,0), _size);
		_restitution = 0;
	}
	
	public void setShape(){
		ArrayList<Vector> points = new ArrayList<Vector>();
		points.add(new Vector(0,_size.y*3/4));
		points.add(new Vector(_size.x/2, 0));
		points.add(new Vector(_size.x, _size.y*3/4));
		_shape = new Polygon(this, points);
	}
	
	public void draw(View v){
		v.drawImage(Resource._gameImages.get("flag"), _pos, _size);
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