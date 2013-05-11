package pvpmagic.spells;

import java.awt.Image;

import pvpmagic.*;
/**
 * Abracadabra is the killing spell. Upon collision with another
 * player, it immediately causes them to die, and registers a kill
 * for the caster.
 * @author Miraj
 *
 */
public class AbracadabraSpell extends Spell {
	public static String TYPE = "AbracadabraSpell";

	public AbracadabraSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Abracadabra";
		_size = new Vector(10, 10);
		_cooldown = 10000;
		_manaCost = 10;
		
		Image sprite = Resource.get("AbracadabraSpell");
		setProperties(new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(70), 14);
		
		_shape = new Circle(this, new Vector(-8, -8), 8);
	}
	
	@Override
	public void collide(Collision c) {
		Unit target = c.other(this);
		if (target._type.equals(Player.TYPE)){
			target.changeHealth((-1.0)*target._health, _caster);
			this.die();
		}
	}
	
	@Override
	public void draw(View v){
		v.rotate(_vel, _pos);
		v.drawImage(Resource.get("AbracadabraSpell"), _pos.plus(-8,-8), _size);
		v.unrotate();
	}
}
