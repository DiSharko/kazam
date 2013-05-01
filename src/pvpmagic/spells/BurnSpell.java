package pvpmagic.spells;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

import pvpmagic.*;


public class BurnSpell extends Spell {
	public static String TYPE = "BurnSpell";

	public BurnSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Burn";
		_size = new Vector(10, 10);
		_cooldown = 1000;
		_manaCost = 10;
		setVelocity(4);
	}
	

	public void collide(Collision c){
		Unit target = c.other(this);
		System.out.println("Spell hit "+target.type());
		target.changeHealth(-30,5000);
		target.changeMana(-30,5000);
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.red);
		v.fillRect(_pos, _size);
	}
}
