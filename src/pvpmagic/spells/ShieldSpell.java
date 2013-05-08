package pvpmagic.spells;

import java.awt.Image;

import pvpmagic.*;

public class ShieldSpell extends Spell {
	public static String TYPE = "ShieldSpell";

	public ShieldSpell(GameData data, Player caster, Vector target){
		super(data, TYPE, caster, target);
		_name = "Shield";
		_dir = target;
		

		Image sprite = Resource.get("ShieldSpell");
		setProperties(new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(50), 15);
	
		_shape = new Circle(this, new Vector(-20, -20), 20);
		_vel = new Vector(0,0);
		_movable = false;
		
		if (caster != null){
			_pos = _pos.plus(_dir.mult(30));
		}
		
		_castingTime = 0;
		_cooldown = 8000;
		_manaCost = 20;
	}
	
	@Override
	public void update(){
		super.update();
		changeHealth(-1.5, _caster);
	}

	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		if (target instanceof Spell) {
			target.die();
		}
	}
	
	@Override
	public boolean canCollideWith(Unit u){
		if (u == _caster || u._type.equals("ShieldSpell")){
			return false;
		}
		return true;
	}
	
	@Override
	public void draw(View v){
		v.rotate(_dir, _pos);
		v.drawImage(Resource.get("ShieldSpell"), _pos.minus(22, 22), _size);
		v.unrotate();
	}
}