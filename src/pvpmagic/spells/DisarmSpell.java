package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;

public class DisarmSpell extends Spell {

	public DisarmSpell(GameData data, Player caster, Vector dir) {
		super(data, caster, dir);
		_size = new Vector(10, 10);
		setVelocity(4);
	}
	
	@Override
	public void collide(Collision c){
		c.other(this).silence(5000);
		//TODO: possibly make it so that your wand also gets knocked away
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.yellow);
		v.fillRect(_pos, _size);
	}
}
