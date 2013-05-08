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

public class SummonSpell extends Spell {
	public static String TYPE = "SummonSpell";

	public SummonSpell(GameData data, Player caster, Vector target) {
		super(data, TYPE, caster, target);
		_name = "Summon";
		_cooldown = 1000;
		_manaCost = 10;

		Image sprite = Resource.get("SummonSpell");
		setProperties(new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(100), 20);
		_shape = new Circle(this, new Vector(-10, -10), 10);
		
		_appliesRestitution = false;
		
		_restitution = 0.6;
	}
	
	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		Vector dir = _caster._pos.minus(target._pos).normalize().mult(10);
		if (target._type.equals("Flag")) {
			System.out.println(dir);
			System.out.println("here");
			target.applyForce(dir);
			this.die();
		}
	}
	
	@Override
	public void draw(View v){
		v.rotate(_vel, _pos);
		v.drawImage(Resource.get("SummonSpell"), _pos.minus(10, 10), _size);
		v.unrotate();
	}

}
