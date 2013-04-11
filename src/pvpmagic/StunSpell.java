package pvpmagic;

import java.awt.Color;

public class StunSpell extends Spell {
	
	public StunSpell(Player caster, Vector dir){
		super(caster, dir);
		_size = new Vector(10, 10);
		setVelocity(4);
	}

	@Override
	public void hit(Unit u){
		//u.stun(10);
	}
	
	@Override
	public void draw(View v){
		v.g.setColor(Color.red);
		v.fillRect(_pos, _size);
	}
}
