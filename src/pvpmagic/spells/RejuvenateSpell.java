package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;

public class RejuvenateSpell extends Spell {

	public RejuvenateSpell(GameData data, Player caster, Vector dir) {
		super(data, caster, dir);
		_size = new Vector(10, 10);
		setVelocity(4);
	}
	
	@Override
	public void collide(Collision c){
<<<<<<< HEAD
		c.other(this).changeHealth(30, 2000);
=======
		//u.heal(10);
>>>>>>> 47a379b975c4c4fca136db2b75f8de2fbd657f27
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.yellow);
		v.fillRect(_pos, _size);
	}
}