package pvpmagic.spells;

import java.awt.Color;
import pvpmagic.*;

public class CleanseSpell extends Spell {
	public static String TYPE = "CleanseSpell";

	public CleanseSpell(GameData data, Player caster, Vector dir){
		super(data, TYPE, caster, dir);
		_name = "Cleanse";
		_size = new Vector(10, 10);
		_cooldown = 1000;
		_manaCost = 10;
		setVelocity(10);
	}

	@Override
	public void collide(Collision c){
//		c.other(this).cleanse();
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.green);
		v.fillRect(_pos, _size);
	}
}