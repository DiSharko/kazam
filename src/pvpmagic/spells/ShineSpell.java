package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;

public class ShineSpell extends Spell {

	public ShineSpell(GameData data, Player caster, Vector dir) {
		super(data, caster, dir);
		_name = "Shine";
		_size = new Vector(10, 10);
		_cooldown = 1000;
		_manaCost = 10;
		setVelocity(4);
	}
	
	@Override
	public void collide(Collision c){
		//TODO: must cast light
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.yellow);
		v.fillRect(_pos, _size);
	}
}