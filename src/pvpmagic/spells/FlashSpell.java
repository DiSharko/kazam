package pvpmagic.spells;

<<<<<<< HEAD
import pvpmagic.*;

=======
import java.awt.Color;

import pvpmagic.GameData;
import pvpmagic.Player;
import pvpmagic.Vector;
import pvpmagic.View;
>>>>>>> c0a853cc6a39ecf03a8032c010998c5df635c396

public class FlashSpell extends Spell {
	public static String TYPE = "FlashSpell";

	public FlashSpell(GameData data, Player caster, Vector dir) {
		super(data, TYPE, caster, dir);
<<<<<<< HEAD
		_name = "Clone";
		_size = new Vector(10, 10);
		_cooldown = 1000;
		_manaCost = 30;
		setProperties(_size, 4);
	}
	
	@Override
	public void collide(Collision c){
		//do nothing if it hits something, just disappear
		this._delete = true;
	}
	
	@Override
	public void draw(View v){		
		if (_caster._flag != null) {
			Vector flagPos = _pos.plus(40, 0);
			v.rotate(new Vector(1.5, -1), flagPos);
			v.drawImage(Resource._gameImages.get("flag"), flagPos, _caster._flag._size.mult(0.8));
			v.unrotate();
		}
		v.drawImage(Resource._gameImages.get("player1_back"), _pos, _caster._size);
	}
}
=======
		_name = "Flash";
		_size = new Vector(10, 10);
		_cooldown = 4000;
		_manaCost = 10;
		setProperties(_size, 1);
	}
	
	public void flash() {
		_caster._pos = _caster._pos.plus(_dir.normalize().mult(30));
	}

}
>>>>>>> c0a853cc6a39ecf03a8032c010998c5df635c396
