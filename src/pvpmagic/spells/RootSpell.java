package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;


public class RootSpell extends Spell {
	public static String TYPE = "RootSpell";

	public RootSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Root";
		_size = new Vector(10, 10);
		_cooldown = 1000;
		_manaCost = 10;
		setVelocity(4);
	}
	
	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		target.root(3000);
		target.changeHealth(-5);
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.cyan);
		v.fillRect(_pos, _size);
	}
}
