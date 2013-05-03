package pvpmagic.spells;

import java.awt.Image;

import pvpmagic.*;


public class RootSpell extends Spell {
	public static String TYPE = "RootSpell";
	double scale = .3;

	public RootSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
		_name = "Root";
		Image sprite = Resource._gameImagesAlpha.get("RootSpell");
		_size = new Vector(sprite.getWidth(null)*scale, sprite.getHeight(null)*scale);
		_cooldown = 1000;
		_manaCost = 10;
		_shape = new Circle(this, new Vector(0, 0), 12);
		setVelocity(10);
		setPosition();
	}
	
	@Override
	public void collide(Collision c){
		Unit target = c.other(this);
		target.root(3000);
		target.changeHealth(-5);
	}
	
	@Override
	public void draw(View v){
		v.rotate(_vel, _pos.plus(10));
		v.drawImage(Resource._gameImagesAlpha.get("RootSpell"), _pos, _size);
		v.unrotate();
	}
}
