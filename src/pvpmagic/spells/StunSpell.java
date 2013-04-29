package pvpmagic.spells;

import java.awt.Color;
import pvpmagic.*;

public class StunSpell extends Spell {
	
	public StunSpell(GameData data, Player caster, Vector dir){
		super(data, caster, dir);
		_size = new Vector(10, 10);
		setVelocity(10);
	}

	@Override
	public void collide(Collision c){
		c.other(this).stun(10);
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.green);
		v.fillRect(_pos, _size);
	}
}
