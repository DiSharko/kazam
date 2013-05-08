package pvpmagic.spells;

import pvpmagic.*;

public class ShieldSpell extends Spell {
	public static String TYPE = "HideSpell";

	public ShieldSpell(GameData data, Player caster, Vector dir){
		super(data, TYPE, caster, dir);
		_name = "Hide";
		_cooldown = 10000;
		_manaCost = 20;
	}
	
	public void hide() {
		_data._units.add(new Shield());
		this.die();
	}
}