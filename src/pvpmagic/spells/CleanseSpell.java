package pvpmagic.spells;

import pvpmagic.*;

/**
 * Cleanse is a self-casted spell that removes all de-buffs
 * (health burns, mana burns, silences, and roots) immediately.
 * @author Miraj
 *
 */
public class CleanseSpell extends Spell {
	public static String TYPE = "CleanseSpell";
	
	public CleanseSpell(GameData data, Player caster, Vector dir){
		super(data, TYPE, caster, dir);
		_name = "Cleanse";
		_size = new Vector(10, 10);
		_cooldown = 5000;
		_manaCost = 20;
		setProperties(_size, 10);
	}

	public void cleanse() {
		System.out.println("caster: "+_caster);
		_caster.cleanse();
	}
}