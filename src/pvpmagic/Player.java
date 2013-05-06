package pvpmagic;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Image;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import pvpmagic.spells.FlashSpell;
import pvpmagic.spells.Spell;

public class Player extends Unit {
	public static String TYPE = "player";
	
	String _characterName;
	String _playerName;

	public Vector _destination;

	double _spellCastingTime = 0;
	Spell _spellToCast = null;
	double _hidden = 1.0;

	String[] _spells;
	//ArrayList<Carryable> inventory = new ArrayList<Carryable>();
	public Flag _flag = null;

	Vector _flagSize = new Vector(40,40);
	
	HashMap<String, Long> _spellCastingTimes;

	/* The time at which the most recent spell was cast by
	   the player. Used for calculation of mana cost. */
	long _timeLastCast;

	double _velocity = 8;

	public Player(GameData data, String characterName, String playerName, String[] spellNames){
		super(data, TYPE);
		_canBeRooted = true;
		_canBeSilenced = true;

		_mass = 3;
		
		_health = 100;
		_mana = 100;

		_pos = new Vector(-50, -20);
		Image sprite = Resource.get("player1_back");
		_size = new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(70);
	
		double hitBoxScale = 1;
		_shape = new Box(this, _size.mult(1-hitBoxScale).div(2), _size.mult(hitBoxScale));

		_spells = spellNames;
		_spellCastingTimes = new HashMap<String, Long>();
		
		_characterName = characterName;
		_playerName = playerName;
		
		this._restitution = 0;
		
		_appliesFriction = true;

	}

	@Override
	public void draw(View v){
		if (_flag != null) {
			Vector flagPos = _pos.plus(40, 0);
			v.rotate(new Vector(1.5, -1), flagPos);
			v.drawImage(Resource.get("flag"), flagPos, _flag._size.mult(0.8));
			v.unrotate();
		}
		if (_hidden < 1) {
			Composite old = v.getGraphics().getComposite();
			
			v.getGraphics().setComposite(java.awt.AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float)_hidden));
			v.drawImage(Resource.get("player1_back"), _pos, _size);
			
			v.getGraphics().setComposite(old);
		} else if (_hidden == 1) {
			v.drawImage(Resource.get("player1_back"), _pos, _size);
		}
	}

	public void stop(){
		_destination = null;
		_vel = new Vector(0,0);
	}

	@Override
	public void update(){
		super.update();
		
		//flag-stun checking
		if(_isRooted && _isSilenced)
			dropFlag();
		
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
			multiplier += 2;
		}
		
		changeMana((-1 * multiplier) * spell._manaCost);
	}

	public void castSpell(Spell spell) {
		if (spell._name.equals("Flash")) {
			FlashSpell f = (FlashSpell) spell;
			f.flash();
		}
		_spellToCast = spell;
		_spellCastingTime = spell._castingTime;
		decrementMana(spell);
		_timeLastCast = System.currentTimeMillis();
		//Need some way of finding out if a spell and unit have crossed paths
		//Spell.newSpell(_spells[spellIndex], this, pos, dir).hit(target);
	}

	
	public void dropFlag() {
		if (_flag == null) {
			return; //nothing happens
		} else {
			double newx = 50 - Math.random()*101;
			double newy = 50 - Math.random()*101;
			Vector newpos = new Vector(this._pos.x + newx,this._pos.y + newy);
			_flag._pos = newpos;
			_flag._delete = false;
			_data._units.add(_flag);
			_flag = null;
		}
	}

	public void fear(long time) {
		timedEffects.add(new FearEffect(numberOfIntervals(time), this));		
	}
	
	public void hide(long time) {
		timedEffects.add(new HideEffect(numberOfIntervals(time), this));
	}
	
	public String toNet() {
		String lastCastTimes = "";
		for (Entry<String, Long> e : _spellCastingTimes.entrySet()) {
			lastCastTimes += e.getKey() + "," + e.getValue() + ".";
		}
		return _netID + 
				"\t" + _type + 
				"\t" + _pos.toNet() +
				"\t" + _destination.toNet() +
				"\t" + _flag._netID +
				"\t" + _health +
				"\t" + _mana +
				"\t" + lastCastTimes.substring(0, lastCastTimes.length() - 1);
		//Need to figure out string for timed effects
		//Timed effects to and fromNet helpers for each effect must be written,
		//when fromNet is called, throw away previous timed effects
		//list, and instantiate new ones with (this) as target
	}
	
	public void fromNet(String[] networkString) {
		if (validNetworkString(networkString)) {
			this._pos = Vector.fromNet(networkString[2]);
			this._destination = Vector.fromNet(networkString[3]);
			//p._flag = (Flag) objectMap.get(Integer.parseInt(networkString[5]));
			this._health = Double.parseDouble(networkString[5]);
			this._mana = Double.parseDouble(networkString[6]);

			String[] lastCastTimes = networkString[7].split(".");
			String[] sp;
			for (String spell : lastCastTimes) {
				sp = spell.split(",");
				this._spellCastingTimes.put(sp[0], Long.parseLong(sp[1]));
			}
		}
	}		
	
	public String toNetInit() {
		String spells = "";
		for (int i = 0; i < _spells.length; i++) {
			spells += _spells[i] + " ";
		}
		return _netID +
				"\t" + _type +
				"\t" + _characterName + 
				"\t" + _playerName + 
				"\t" + spells.substring(0, spells.length() - 1) +
				"\t" + _pos.toNet() +
				"\t" + _destination.toNet();
	}
	
	//networkString format = [id, type, <any data from toNetInit, split on tabs>...]
	public static Player fromNetInit(String[] networkString) {
		if (networkString[1].equals("player")) {
			String[] spells = networkString[4].split(" ");
			Player p = new Player(null, networkString[2], networkString[3], spells);
			p._destination = Vector.fromNet(networkString[6]);
			p._pos = Vector.fromNet(networkString[5]);
			p._netID = Integer.parseInt(networkString[0]);
			return p;
		}
		return null;
	}
	
	public Boolean validNetworkStringInit(String[] networkData) {
		if (networkData.length != 10 || !(networkData[0].equals("n") 
				&& networkData[2].equals("pn") && networkData[4].equals("s")
				&& networkData[6].equals("p")) && networkData[8].equals("d")) {
			System.err.println("ERROR: Invalid String from network - " + Arrays.toString(networkData));
			return false;
		} else {
			return true;
		}
	}
	
	public Boolean validNetworkString(String[] networkData) {
		if (networkData.length != 7) {
			System.err.println("ERROR: Invalid String from network - " + Arrays.toString(networkData));
			return false;
		} else {
			return true;
		}
	}
}