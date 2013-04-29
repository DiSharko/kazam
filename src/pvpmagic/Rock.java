package pvpmagic;

import java.awt.Color;

public class Rock extends Unit {
	public Rock(GameData data, Vector pos, Vector size){
		super(data, "rock");
		_pos = pos;
		_size = size;
		_movable = false;
		_shape = new Box(this, new Vector(0,0), size);
	}
	
	public void draw(View v){
		v.getGraphics().setColor(Color.gray);
		v.fillRect(_pos, _size);
		v.getGraphics().setColor(Color.DARK_GRAY);
		v.drawRect(_pos, _size);
	}

	@Override
	public void hit(Unit u){
		if (u.type().equals("spell")){
			u._delete = true;
//			System.out.println("I am " + _pos +", "+_size);
//			System.out.println("Spell is "+ u._pos + ", "+u._size);
		}
	}
	
}