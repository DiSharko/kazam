package pvpmagic.spells;

import java.awt.Image;

import pvpmagic.*;
/**
 * Stun roots (prevents movement) and silences
 * (prevents casting) a target for a set amount of time.
 * @author Miraj
 *
 */
public class StunSpell extends Spell {
	public static String TYPE = "StunSpell";
	
	public StunSpell(GameData data, Player caster, Vector dir){
		super(data, TYPE, caster, dir);
		_cooldown = 1000;
		Image sprite = Resource.get("StunSpell");
		setProperties(new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(50), 15);
		_name = "Stun";
		_cooldown = 3000;
		_manaCost = 25;
		_shape = new Circle(this, new Vector(-7,-7), 7);
	}

	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		target.changeHealth(-10, _caster);
		if (target._type.equals(Player.TYPE)){
			target.root(2000, _caster);
			target.silence(2000, _caster);
			this.die();
		}
	}
	
	@Override
	public void draw(View v){
		v.rotate(_vel, _pos);
		v.drawImage(Resource.get("StunSpell"), _pos.plus(-7,-7), _size);
		v.unrotate();
	}
}