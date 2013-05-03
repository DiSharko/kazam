package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.Collision;
import pvpmagic.GameData;
import pvpmagic.Player;
import pvpmagic.Unit;
import pvpmagic.Vector;
import pvpmagic.View;

public class SummonSpell extends Spell {
	public static String TYPE = "SummonSpell";

	public SummonSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Disarm";
		_size = new Vector(10, 10);
		_cooldown = 1000;
		_manaCost = 10;
	}
	
	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		Vector dir = _caster._pos.minus(target._pos).normalize().mult(3);
		if (target._type.equals("flag")) {
			target._vel = dir;
		}
		//TODO: possibly make it so that your wand also gets knocked away
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.yellow);
		v.fillRect(_pos, _size);
	}

}
