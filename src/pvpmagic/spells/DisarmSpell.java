package pvpmagic.spells;

import pvpmagic.Player;
import pvpmagic.Spell;
import pvpmagic.Unit;
import pvpmagic.Vector;
import pvpmagic.View;

public class DisarmSpell extends Spell {

	public DisarmSpell(Player caster, Vector pos, Vector dir) {
		super(caster, pos, dir);
		_size = new Vector(30, 30);
	}
	
	@Override
	public void hit(Unit u){
		_caster.decrementMana(10);
		u.silence(10);
	}
	
	@Override
	public void draw(View v){
		v.fillCircle(_pos, 10);
	}
}
