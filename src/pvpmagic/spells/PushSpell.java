package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;


public class PushSpell extends Spell {

	public PushSpell(GameData data, Player caster, Vector dir) {
		super(data, caster, dir);
		_size = new Vector(10, 10);
		setVelocity(4);
	}
	
	@Override
	public void collide(Collision c){
		//u.push(10);
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.pink);
		v.fillRect(_pos, _size);
	}
}
