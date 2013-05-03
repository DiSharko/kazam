package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;

public class DisarmSpell extends Spell {
	public static String TYPE = "DisarmSpell";

	public DisarmSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Disarm";
		_size = new Vector(10, 10);
		_cooldown = 1000;
		_manaCost = 10;
		setVelocity(4);
		setPosition();
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
