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

public class FearSpell extends Spell {
	public static String TYPE = "ConfuseSpell";

	public FearSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Fear";
		_cooldown = 1000;
		_manaCost = 10;
		Image sprite = Resource.get("FearSpell");
		setProperties(new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(50), 15);
	
		_shape = new Circle(this, new Vector(-9,-9), 9);
	}
	
	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		target.changeHealth(-5);
		if (target._type.equals(Player.TYPE)) {
			Player p = (Player) target;
			p.confuse(3000);
		}
	}
	
	@Override
	public void draw(View v){
		v.rotate(_vel, _pos);
		v.drawImage(Resource.get("FearSpell"), _pos.plus(-9,-9), _size);
		v.unrotate();
	}

}
