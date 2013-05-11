package pvpmagic.spells;

import pvpmagic.*;
/**
 * Hide is a self-casted spell, causing the caster
 * to fade out (disappear) and fade back into the world
 * over the course of the specified time period.
 * @author Miraj
 *
 */
public class HideSpell extends Spell {
	public static String TYPE = "HideSpell";

	public HideSpell(GameData data, Player caster, Vector dir){
		super(data, TYPE, caster, dir);
		_name = "Hide";
		_cooldown = 6000;
		_manaCost = 20;
	}
	
	public void hide() {
		if (_caster != null) _caster.hide(3500, _caster);
		this.die();
	}
}