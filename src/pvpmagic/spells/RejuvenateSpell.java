package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;

public class RejuvenateSpell extends Spell {

	public RejuvenateSpell(GameData data, Player caster, Vector dir) {
		super(data, caster, dir);
		_name = "Rejuvenate";
		_size = new Vector(10, 10);
		_cooldown = 1000;
		_manaCost = 10;
		setVelocity(4);
	}
	
	@Override
	public void collide(Collision c){
		c.other(this).changeHealth(30, 2000);
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.yellow);
		v.fillRect(_pos, _size);
	}
}