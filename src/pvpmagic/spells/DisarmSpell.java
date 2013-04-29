package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;

public class DisarmSpell extends Spell {

	public DisarmSpell(GameData data, Player caster, Vector dir) {
		super(data, caster, dir);
		_size = new Vector(10, 10);
		setVelocity(4);
	}
	
	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		target.silence(5000);
		target.changeHealth(-5);
		//TODO: possibly make it so that your wand also gets knocked away
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.yellow);
		v.fillRect(_pos, _size);
	}
}
