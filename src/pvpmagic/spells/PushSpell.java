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
		setProperties(_size, 4);
	}
	
	@Override
	public void collide(Collision c){
		this.die();
		Unit target = c.other(this);
		target.changeHealth(-5);
		if (target._type.equals("player") && !target.equals(_caster)) {
			Player p = (Player) target;
			Vector f = p._pos.minus(_caster._pos).normalize();
			p._destination = null;//p._pos.plus(f.mult(100));
			//p.applyForce(f.mult(30));
			p._vel = f.mult(15);
		}
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.pink);
		v.fillRect(_pos, _size);
	}
}
