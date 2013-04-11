package pvpmagic;

import java.awt.Color;

public class Player extends Unit {
	double _health;
	double _mana;
	
	String _characterName;
	String _playerName;
	
	Vector _destination;
	
	String[] _spells;

	/* The time at which the most recent spell was cast by
	   the player. Used for calculation of mana cost. */
	long _timeLastCast;
	
	public Player(String characterName, String playerName, String[] spellNames){
		_canBeStunned = true;
		_canBeRooted = true;
		_canBeSilenced = true;
		
		_health = 100;
		_mana = 100;
		
		_pos = new Vector(-50, -20);
		_size = new Vector(20, 20);
		
		_spells = spellNames;
	}
	
	@Override
	public void draw(View v){
		v.g.setColor(Color.red);
		v.fillRect(_pos, _size);
	}
	
	
	@Override
	public void update(){
		if (_destination != null){
			
		}
	}
	
	
	private void decrementMana() {
		//Change the mechanics of this to make it decrease
		//exponentially with quick successions of spells
		_mana = _mana - 10;
		if (_mana < 0) _mana = 0;
	}
	
	public void cast(int spellIndex, Vector pos, Vector dir) {
		decrementMana();
		_timeLastCast = System.currentTimeMillis();
		//Need some way of finding out if a spell and unit have crossed paths
		//Spell.newSpell(_spells[spellIndex], this, pos, dir).hit(target);
	}
}