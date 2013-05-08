package pvpmagic.spells;

import pvpmagic.GameData;
import pvpmagic.Player;
import pvpmagic.Vector;

public class FlashSpell extends Spell {
	public static String TYPE = "FlashSpell";

	public FlashSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Flash";
		_size = new Vector(10, 10);
		_cooldown = 4000;
		_manaCost = 10;
		setProperties(_size, 1);
	}
	
	public void flash() {
		_caster._vel = _dir.normalize().mult(50);
		_caster._destination = null;
	}
}