package pvpmagic.spells;

import pvpmagic.*;

public class CleanseSpell extends Spell {
	public static String TYPE = "CleanseSpell";

	public CleanseSpell(GameData data, Player caster, Vector dir){
		super(data, TYPE, caster, dir);
		_name = "Cleanse";
		_size = new Vector(10, 10);
		_cooldown = 1000;
		_manaCost = 10;
		setProperties(_size, 10);
	}

	public void cleanse() {
		_caster.cleanse();
	}
}