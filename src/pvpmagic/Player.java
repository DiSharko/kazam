package pvpmagic;

import java.awt.Color;

public class Player extends Unit {
	double _health;
	double _mana;
	
	String _characterName;
	String _playerName;
	
	Vector _destination;
	
	
	public Player(String characterName, String playerName, String[] spells){
		_canBeStunned = true;
		_canBeRooted = true;
		_canBeSilenced = true;
		
		_health = 100;
		_mana = 100;
		
		_pos = new Vector(-50, -20);
		_size = new Vector(20, 20);
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
	
	public void decrementMana(int amount) {
		_mana = _mana - amount;
		if (_mana < 0) _mana = 0;
	}
}