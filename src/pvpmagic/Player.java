package pvpmagic;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Image;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import pvpmagic.spells.Spell;

public class Player extends Unit {
	public static String TYPE = "player";
	
	String _characterName;
	String _playerName;

	public Vector _destination;

	double _spellCastingTime = 0;
	Spell _spellToCast = null;
	double _hidden = 1.0;
	Composite _old;

	String[] _spells;
	//ArrayList<Carryable> inventory = new ArrayList<Carryable>();
	public Flag _flag = null;
	Vector _flagSize = new Vector(40,40);
	
	HashMap<String, Long> _spellCastingTimes;

	/* The time at which the most recent spell was cast by
	   the player. Used for calculation of mana cost. */
	long _timeLastCast;

	double _velocity = 6;


	public Player(GameData data, String characterName, String playerName, String[] spellNames){
		super(data, TYPE);
		_canBeRooted = true;
		_canBeSilenced = true;

		_mass = 3;
		
		_health = 100;
		_mana = 100;

		_pos = new Vector(-50, -20);
		Image sprite = Resource._gameImages.get("player1_back");
		_size = new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(80);
		
		double hitBoxScale = 0.9;
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
			v.drawImage(Resource._gameImages.get("flag"), flagPos, _flag._size.mult(0.8));
			v.unrotate();
		}
		_old = v.getGraphics().getComposite();
		if (_hidden < 1) {
			AlphaComposite ac = java.awt.AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float)_hidden);
			v.getGraphics().setComposite(ac);
			v.drawImage(Resource._gameImages.get("player1_back"), _pos, _size);
		} else if (_hidden == 1) {
			v.drawImage(Resource._gameImages.get("player1_back"), _pos, _size);
		}
		v.getGraphics().setComposite(_old);
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

	public static void fromNetInit(Integer netID, String networkString, HashMap<Integer, Unit> objectMap, GameData data) {
		/*String[] netSplit = networkString.split("\t");
		Player p;
		String[] spells = netSplit[5].split(", ");
		Vector dir = Vector.fromNet(netSplit[7]);
		Vector pos = Vector.fromNet(netSplit[9]);*/
		//Will have to figure this out, how to put it into GameData
	}
	public String toNet() {
		String lastCastTimes = "";
		for (Entry<String, Long> e : _spellCastingTimes.entrySet()) {
			lastCastTimes += e.getKey() + "," + e.getValue() + ".";
		}
		return "\tp\t" + _pos.toNet() +
				"\td\t" + _destination.toNet() +
				"\tf\t" + _flag._netID +
				"\th\t" + _health +
				"\tm\t" + _mana +
				"\tlct\t" + lastCastTimes.substring(0, lastCastTimes.length() - 1);
		//Need to figure out string for timed effects
	}
	
	public void fromNet(Integer netID, String networkString, HashMap<Integer, Unit> objectMap, GameData data) {
		String[] netSplit = networkString.split("\t");
		Player p;
		if (validNetworkString(netSplit)) {
			if ((p = (Player) objectMap.get(netID)) != null) {
				p._pos = Vector.fromNet(netSplit[1]);
				p._destination = Vector.fromNet(netSplit[3]);
				p._flag = (Flag) objectMap.get(Integer.parseInt(netSplit[5]));
				p._health = Double.parseDouble(netSplit[7]);
				p._mana = Double.parseDouble(netSplit[9]);
				
				String[] lastCastTimes = netSplit[11].split(".");
				String[] sp;
				for (String spell : lastCastTimes) {
					sp = spell.split(",");
					p._spellCastingTimes.put(sp[0], Long.parseLong(sp[1]));
				}
			}
		}		
	}
	
	public String toNetInit() {
		String spells = Arrays.toString(_spells);
		return "cn\t" + _characterName + 
				"\tpn\t" + _playerName + 
				"\ts\t" + spells.substring(1, spells.length() - 1) +
				"\tp\t" + _pos.toNet() +
				"\td\t" + _destination.toNet();
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
		if (networkData.length != 12 || !(networkData[0].equals("p")
				&& networkData[2].equals("d") && networkData[4].equals("f")
				&& networkData[6].equals("h")) && networkData[8].equals("m")
				&& networkData[10].equals("lct")) {
			System.err.println("ERROR: Invalid String from network - " + Arrays.toString(networkData));
			return false;
		} else {
			return true;
		}
	}
}