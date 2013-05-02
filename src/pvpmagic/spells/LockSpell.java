package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;


public class LockSpell extends Spell {
	public static String TYPE = "LockSpell";

	public LockSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Lock";
		_size = new Vector(10, 10);
		_cooldown = 1000;
		_manaCost = 10;
		setVelocity(4);
	}
	
	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		if (target.type().equals("door")) {
			Door door = (Door) target;
			door.lock();
		}
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.blue);
		v.fillRect(_pos, _size);
	}
}
