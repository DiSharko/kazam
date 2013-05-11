package pvpmagic.spells;

import java.awt.Image;

import pvpmagic.Circle;
import pvpmagic.Collision;
import pvpmagic.GameData;
import pvpmagic.Player;
import pvpmagic.Resource;
import pvpmagic.Unit;
import pvpmagic.Vector;
import pvpmagic.View;

/**
 * Confuse will silence a target, and proceed
 * to send them moving uncontrollably in random
 * directions for the duration of the effect.
 * @author Miraj
 *
 */
public class ConfuseSpell extends Spell {
	public static String TYPE = "ConfuseSpell";

	public ConfuseSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Confuse";
		_cooldown = 3000;
		_manaCost = 30;
		Image sprite = Resource.get("ConfuseSpell");
		setProperties(new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(50), 15);
	
		_shape = new Circle(this, new Vector(-9,-9), 9);
	}
	
	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		target.changeHealth(-5, _caster);
		if (target._type.equals(Player.TYPE)) {
			Player p = (Player) target;
			p.confuse(3000, _caster);
			this.die();
		}
	}
	
	@Override
	public void draw(View v){
		v.rotate(_vel, _pos);
		v.drawImage(Resource.get("ConfuseSpell"), _pos.plus(-9,-9), _size);
		v.unrotate();
	}

}
