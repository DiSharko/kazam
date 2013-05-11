package pvpmagic.spells;

import java.awt.Image;

import pvpmagic.*;

/**
 * Felify will turn a target object, including flags,
 * doors, and rocks, into cats.
 * @author Miraj
 *
 */
public class FelifySpell extends Spell {
	public static String TYPE = "FelifySpell";

	public FelifySpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Felify";

		Image sprite = Resource.get("FelifySpell");
		setProperties(new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(30), 15);
		
		_shape = new Circle(this, new Vector(-8, -8), 8);
		
		_cooldown = 3000;
		_manaCost = 10;
	}
	
	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		target.changeHealth(-5, _caster);
		if (!(target instanceof Spell) && !(target instanceof Wall)) {
			target.kitty(3000, _caster);
			this.die();
		}
	}
	
	@Override
	public void draw(View v){
		v.rotate(_vel, _pos);
		v.drawImage(Resource.get("FelifySpell"), _pos.minus(8,8), _size);
		v.unrotate();
	}
}