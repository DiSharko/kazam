package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.Collision;
import pvpmagic.GameData;
import pvpmagic.Player;
import pvpmagic.Unit;
import pvpmagic.Vector;
import pvpmagic.View;

public class FearSpell extends Spell {
	public static String TYPE = "FearSpell";

	public FearSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Fear";
		_size = new Vector(10, 10);
		_cooldown = 1000;
		_manaCost = 10;
		setVelocity(4);
	}
	
	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		if (target instanceof Player) {
			Player p = (Player) target;
			p.fear(3000);
		}
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.pink);
		v.fillRect(_pos, _size);
	}

}
