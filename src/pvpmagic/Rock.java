package pvpmagic;

import java.awt.Color;

public class Rock extends Unit {
	public Rock(Vector pos, Vector size){
		super("rock");
		_pos = pos;
		_size = size;
		
		
	}
	
	public void draw(View v){
		v.g.setColor(Color.gray);
		v.fillRect(_pos, _size);
		v.g.setColor(Color.DARK_GRAY);
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