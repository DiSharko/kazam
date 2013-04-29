package pvpmagic.spells;

import java.awt.Color;

import pvpmagic.*;

public class ShineSpell extends Spell {

	public ShineSpell(GameData data, Player caster, Vector dir) {
		super(data, caster, dir);
		_size = new Vector(10, 10);
		setVelocity(4);
	}
	
	@Override
	public void collide(Collision c){
<<<<<<< HEAD
		//light area of the map that you are in
=======
		c.other(this).silence(10);
>>>>>>> 47a379b975c4c4fca136db2b75f8de2fbd657f27
	}
	
	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.yellow);
		v.fillRect(_pos, _size);
	}
}