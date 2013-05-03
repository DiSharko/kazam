package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;


public class AbracadabraSpell extends Spell {
	public static String TYPE = "AbracadabraSpell";

	public AbracadabraSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Abracadabra";
		_size = new Vector(10, 10);
		_cooldown = 5000;
		_manaCost = 10;
		setProperties(_size, 4);
	}
	
	@Override
	public void collide(Collision c) {
		Unit target = c.other(this);
		target.changeHealth((-2.0)*target._maxHealth);
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.black);
		v.fillRect(_pos, _size);
	}
}
