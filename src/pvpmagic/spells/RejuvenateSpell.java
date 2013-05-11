package pvpmagic.spells;

import java.awt.Image;

import pvpmagic.*;
/**
 * Heals an ally (or enemy) by a certain amount of health
 * over time.
 * @author Miraj
 *
 */
public class RejuvenateSpell extends Spell {
	public static String TYPE = "RejuvenateSpell";

	public RejuvenateSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Rejuvenate";
		Image sprite = Resource.get("RejuvenateSpell");
		setProperties(new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(70), 12);
		_cooldown = 2000;
		_manaCost = 10;
		
		_shape = new Circle(this, new Vector(-9,-9), 9);
	}
	
	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		if (target._type.equals(Player.TYPE)){
			target.changeHealth(30, 2000, _caster);
			this.die();
		}
	}
	
	@Override
	public void draw(View v){
		v.rotate(_vel, _pos);
		v.drawImage(Resource.get("RejuvenateSpell"), _pos.plus(-18,-18), _size);
		v.unrotate();
	}
}