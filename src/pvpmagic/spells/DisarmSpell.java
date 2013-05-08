package pvpmagic.spells;

import java.awt.Image;

import pvpmagic.*;

public class DisarmSpell extends Spell {
	public static String TYPE = "DisarmSpell";

	public DisarmSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Disarm";

		Image sprite = Resource.get("DisarmSpell");
		setProperties(new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(80), 25);

		_shape = new Circle(this, new Vector(-8, -8), 8);
		_cooldown = 1000;
		_manaCost = 10;
	}

	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		target.changeHealth(-5, _caster);
		if(target instanceof Player) {
			target.silence(5000, _caster);
			this.die();
		}
	}

	@Override
	public void draw(View v){
		v.rotate(_vel, _pos);
		v.drawImage(Resource.get("DisarmSpell"), _pos.minus(8,8), _size);
		v.unrotate();
	}
}
