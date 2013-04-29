package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;


public class BlindSpell extends Spell {

	public BlindSpell(GameData data, Player caster, Vector dir) {
		super(data, caster, dir);
		_size = new Vector(10, 10);
		setVelocity(4);
	}
	
	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		target.changeHealth(-5);
		//TODO: reduce targets vision
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.cyan);
		v.fillRect(_pos, _size);
	}
}
