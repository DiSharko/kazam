package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;


public class LockSpell extends Spell {

	public LockSpell(GameData data, Player caster, Vector dir) {
		super(data, caster, dir);
		_size = new Vector(10, 10);
		setVelocity(4);
	}
	
	@Override
	public void hit(Unit u){
		//if u.instanceof(door)?
			//u.lock(10);
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.blue);
		v.fillRect(_pos, _size);
	}
}
