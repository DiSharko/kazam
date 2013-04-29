package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;


public class CloneSpell extends Spell {

	public CloneSpell(GameData data, Player caster, Vector dir) {
		super(data, caster, dir);
		_name = "Clone";
		_size = new Vector(10, 10);
		_cooldown = 1000;
		_manaCost = 10;
		setVelocity(4);
	}
	
	@Override
	public void collide(Collision c){
		//do nothing if it hits something, just disappear
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.blue);
		v.fillRect(_pos, _size);
	}
}
