package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;


public class PushSpell extends Spell {

	public PushSpell(GameData data, Player caster, Vector dir) {
		super(data, caster, dir);
		_size = new Vector(10, 10);
		setVelocity(4);
	}
	
	@Override
	public void collide(Collision c){
		//TODO: gotta do displacement effects
		Unit target = c.other(this);
		target.changeHealth(-5);
		//c.other(this).applyForce(new Vector());
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.pink);
		v.fillRect(_pos, _size);
	}
}
