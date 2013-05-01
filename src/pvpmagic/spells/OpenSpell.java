package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;


public class OpenSpell extends Spell {
	public static String TYPE = "OpenSpell";

	public OpenSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_size = new Vector(10, 10);
		setVelocity(4);
	}
	
	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		//TODO: make door object and do shit with that 
		//if (target instanceof door)
			//u.unlock();
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.red);
		v.fillRect(_pos, _size);
	}
}
