package pvpmagic.spells;

import java.awt.Color;
import pvpmagic.*;

public class HideSpell extends Spell {
	public static String TYPE = "HideSpell";

	public HideSpell(GameData data, Player caster, Vector dir){
		super(data, TYPE, caster, dir);
		_name = "Hide";
		_size = new Vector(10, 10);
		_cooldown = 1000;
		_manaCost = 10;
		setVelocity(10);
	}

	@Override
	public void collide(Collision c){
		//we may or may not have a hide method that sets up a hide debuff
		//c.other(this).hide(6000);
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.green);
		v.fillRect(_pos, _size);
	}
}