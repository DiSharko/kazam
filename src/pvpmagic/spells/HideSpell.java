package pvpmagic.spells;

import pvpmagic.*;

public class HideSpell extends Spell {
	public static String TYPE = "HideSpell";

	public HideSpell(GameData data, Player caster, Vector dir){
		super(data, TYPE, caster, dir);
		_name = "Hide";
		_cooldown = 10000;
		_manaCost = 20;
		_caster.hide(3500);
		this._delete = true;
	}
	
	@Override
	public void draw(View v){
		//nothing gets drawn
	}
}