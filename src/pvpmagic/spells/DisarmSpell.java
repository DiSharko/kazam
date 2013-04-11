package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.Player;
import pvpmagic.Spell;
import pvpmagic.Unit;
import pvpmagic.Vector;
import pvpmagic.View;

public class DisarmSpell extends Spell {

	public DisarmSpell(Player caster, Vector dir) {
		super(caster, dir);
		_size = new Vector(10, 10);
		setVelocity(4);
	}
	
	@Override
	public void hit(Unit u){
		u.silence(10);
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.yellow);
		v.fillRect(_pos, _size);
	}
}
