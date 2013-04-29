package pvpmagic;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;

import pvpmagic.spells.Spell;

public class Player extends Unit {
	String _characterName;
	String _playerName;

	Vector _destination;

	double _spellCastingTime = 0;
	Spell _spellToCast = null;

	String[] _spells;
	HashMap<String, Long> _spellCastingTimes;

	/* The time at which the most recent spell was cast by
	   the player. Used for calculation of mana cost. */
	long _timeLastCast;

	double _velocity = 3;


	public Player(GameData data, String characterName, String playerName, String[] spellNames){
		super(data, "player");
		_canBeRooted = true;
		_canBeSilenced = true;

		_health = 100;
		_mana = 100;

		_pos = new Vector(-50, -20);
		_size = new Vector(20, 20);
		
		_shape = new Box(this, new Vector(0,0), _size);

		_spells = spellNames;
		_spellCastingTimes = new HashMap<String, Long>();
		
		_characterName = characterName;
		_playerName = playerName;

	}

	@Override
	public void draw(View v){
		v.getGraphics().setColor(Color.blue);
		v.fillRect(_pos, _size);
	}

	public void stop(){
		_destination = null;
		_vel = new Vector(0,0);
	}

	@Override
	public void update(){
		super.update();
		
		//health and mana regeneration
		changeHealth(0.125);
		changeMana(0.125);
		
		Vector center = _pos.plus(_size.div(2.0));
		if (_spellCastingTime > 0) _spellCastingTime--;
		else if (_spellToCast != null){
			_data.finishCastingSpell(_spellToCast);
			_spellToCast = null;
		}
		if (_destination != null){
			if (_spellCastingTime > 0) _vel = new Vector(0, 0);
			else {
				_vel = (_destination.minus(center)).normalize().mult(_velocity);
				if (_destination.minus(center).mag() < 0.5){
					stop();
				} else if (_destination.minus(center).mag() < _velocity){
					_vel = _destination.minus(center);
				}
			}
		}
	}


	private void decrementMana(Spell spell) {
		//Change the mechanics of this to make it decrease
		//exponentially with quick successions of spells
		Long specificTimeLastCast = _spellCastingTimes.get(spell._name);
		if (specificTimeLastCast == null) specificTimeLastCast = (long) 0;
		
		double multiplier = 1;
		if (System.currentTimeMillis() - _timeLastCast < 1000) {
			multiplier += 0.25;
		}
		
		long score = (long) (System.currentTimeMillis() - specificTimeLastCast - spell._cooldown);
		if (score > 0 && score < 1000) {
			multiplier += 0.75;
		}
		
		changeMana((-1 * multiplier) * spell._manaCost);
	}

	public void castSpell(Spell spell) {
		_spellToCast = spell;
		_spellCastingTime = spell._castingTime;
		decrementMana(spell);
		_timeLastCast = System.currentTimeMillis();
		//Need some way of finding out if a spell and unit have crossed paths
		//Spell.newSpell(_spells[spellIndex], this, pos, dir).hit(target);
	}
}