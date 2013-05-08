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
		if (target._type.equals(Player.TYPE)){
			target.silence(3000);
			target.changeHealth(-5);
			_health = 0;
			//TODO: possibly make it so that your wand also gets knocked away
		}
	}

	@Override
	public void draw(View v){
		v.rotate(_vel, _pos);
		v.drawImage(Resource.get("DisarmSpell"), _pos.minus(8,8), _size);
		v.unrotate();
	}
}
