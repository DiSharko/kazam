package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;


public class AbracadabraSpell extends Spell {

	public AbracadabraSpell(GameData data, Player caster, Vector dir) {
		super(data, caster, dir);
		_size = new Vector(10, 10);
		setVelocity(4);
	}
	
	@Override
	public void collide(Collision c) {
		Unit target = c.other(this);
		target.changeHealth((-2.0)*target._maxHealth);
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.black);
		v.fillRect(_pos, _size);
	}
}
