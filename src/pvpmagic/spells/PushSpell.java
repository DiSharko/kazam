package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;


public class PushSpell extends Spell {
	public static String TYPE = "PushSpell";

	public PushSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Push";
		_size = new Vector(10, 10);
		_cooldown = 1000;
		_manaCost = 10;
		setVelocity(4);
		setPosition();
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
