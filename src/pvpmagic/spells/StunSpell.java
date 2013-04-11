package pvpmagic.spells;

import pvpmagic.Player;
import pvpmagic.Spell;
import pvpmagic.Unit;
import pvpmagic.Vector;
import pvpmagic.View;

public class StunSpell extends Spell {
	
	public StunSpell(Player caster, Vector pos, Vector dir){
		super(caster, pos, dir);
		_size = new Vector(30, 30);
		_cooldown = 5;
	}

	@Override
	public void hit(Unit u){
		u.stun(10);
	}
	
	@Override
	public void draw(View v){
		v.fillCircle(_pos, 10);
	}
}
