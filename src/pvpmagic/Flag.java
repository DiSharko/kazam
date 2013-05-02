package pvpmagic;

import java.awt.Color;
import java.util.ArrayList;


public class Flag extends Unit {
	public Flag(GameData data, Vector pos, double size){
		super(data, "flag");
		_pos = pos;
		_size = new Vector(145, 117).normalize().mult(size);
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
		v.getGraphics().setColor(Color.yellow);
		v.fillRect(_pos, _size);
		v.getGraphics().setColor(Color.black);
		v.drawRect(_pos, _size);
	}

	@Override
	public void collide(Collision c){
		Unit u = c.other(this);
		if (u.type().equals("player")){
			System.out.println("player has flag");
			Player player = (Player) u;
			player._flag = this;
			_delete = true;
		}
	}
}