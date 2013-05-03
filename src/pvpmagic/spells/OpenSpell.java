package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;


public class OpenSpell extends Spell {
	public static String TYPE = "OpenSpell";

	public OpenSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Open";
		_cooldown = 1000;
		_manaCost = 10;
		_size = new Vector(10, 10);
		setProperties(_size, 4);
	}
	
	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		if (target._type.equals("door")) {
			Door door = (Door) target;
			door.open();
		}
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.red);
		v.fillRect(_pos, _size);
	}
}
