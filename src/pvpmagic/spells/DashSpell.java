package pvpmagic.spells;

import pvpmagic.GameData;
import pvpmagic.Player;
import pvpmagic.Vector;
/**
 * Dash is a self-casted spell that rapidly propels the
 * caster a certain distance in the direction of the
 * cursor.
 * @author Miraj
 *
 */
public class DashSpell extends Spell {
	public static String TYPE = "DashSpell";

	public DashSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Dash";
		_size = new Vector(10, 10);
		_cooldown = 4000;
		_manaCost = 10;
		setProperties(_size, 1);
	}
	
	public void dash() {
		_caster._pos = _caster._pos.plus(_dir.normalize().mult(30));
		this.die();
		_caster._vel = _dir.normalize().mult(50);
		_caster._destination = null;
	}
}