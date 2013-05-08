package pvpmagic.spells;

import java.awt.Image;

import pvpmagic.*;


public class OpenSpell extends Spell {
	public static String TYPE = "OpenSpell";

	public OpenSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Open";
		_cooldown = 1000;
		_manaCost = 10;

		Image sprite = Resource.get("OpenSpell");
		setProperties(new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(80), 18);
		
		_shape = new Circle(this, new Vector(-8, -8), 8);
		
	}
	
	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		if (target._type.equals(Door.TYPE)) {
			Door door = (Door) target;
			door.open();
			_health = 0;
		}
	}
	
	@Override
	public void draw(View v){
		v.rotate(_vel, _pos);
		v.drawImage(Resource.get("OpenSpell"), _pos.plus(-8-(_vel.x < 0 ? -_size.x: 0), -8), _size.mult(Math.signum(_vel.x), 1));
		v.unrotate();
	}
}
