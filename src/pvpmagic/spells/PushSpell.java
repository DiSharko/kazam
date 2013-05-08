package pvpmagic.spells;

import java.awt.Image;

import pvpmagic.*;

public class PushSpell extends Spell {
	public static String TYPE = "PushSpell";

	public PushSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Push";

		Image sprite = Resource.get("PushSpell");
		setProperties(new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(30), 15);
	
		_shape = new Circle(this, new Vector(-8, -8), 8);

		_cooldown = 1000;
		_manaCost = 10;
	}
	
	@Override
	public void collide(Collision c){
		this.die();
		Unit target = c.other(this);
		target.changeHealth(-5);
		if (target._type.equals(Player.TYPE) && !target.equals(_caster)) {
			Player p = (Player) target;
			Vector f = p._pos.minus(_caster._pos).normalize();
			p._destination = null;//p._pos.plus(f.mult(100));
			p.root(500)._display = false;

			//p.applyForce(f.mult(30));
			p._vel = f.mult(15);
		}
	}
	
	@Override
	public void draw(View v){
		v.rotate(_vel, _pos);
		v.drawImage(Resource.get("PushSpell"), _pos.minus(12, 12), _size);
		v.unrotate();
	}
}
