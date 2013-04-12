package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;


public class OpenSpell extends Spell {

	public OpenSpell(GameData data, Player caster, Vector dir) {
		super(data, caster, dir);
		_size = new Vector(10, 10);
		setVelocity(4);
	}
	
	@Override
	public void hit(Unit u){
		//if u.instanceof(door)?
			//u.open(10);
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.red);
		v.fillRect(_pos, _size);
	}
}
