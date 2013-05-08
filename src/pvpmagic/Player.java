package pvpmagic;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Image;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import pvpmagic.spells.DashSpell;
import pvpmagic.spells.HideSpell;
import pvpmagic.spells.Spell;

public class Player extends Unit {
	public static Boolean STATICOBJ = true;
	public static String TYPE = "Player";
	public Vector _spawn;
	public int _teamNum;
	public int _spawnTimer = 0;
	private int _flagGrabTimer = 0;
	public boolean _flagable = true;
	
	public String _characterName;
	String _playerName;

	public Vector _destination;
	public boolean _connected;
	double _spellCastingTime = 0;
	Spell _spellToCast = null;
	double _hidden = 1.0;
	Composite _old;

	public String[] _spells;
	//ArrayList<Carryable> inventory = new ArrayList<Carryable>();
	public Flag _flag = null;

	Vector _flagSize = new Vector(40,40);

	HashMap<String, Long> _spellCastingTimes;

	/* The time at which the most recent spell was cast by
	   the player. Used for calculation of mana cost. */
	long _timeLastCast;

	double _velocity = 8;

	public Player(GameData data, String characterName, String playerName, String[] spellNames){
		super(data, TYPE, STATICOBJ, characterName);
		_canBeRooted = true;
		_canBeSilenced = true;

		_mass = 3;

		_health = 100;
		_mana = 100;

		_characterName = characterName;
		_basicImage = _characterName;
		_playerName = playerName;
		
		_pos = new Vector(-50, -20);
		Image sprite = Resource.get(_characterName);
		_size = new Vector(sprite.getWidth(null), sprite.getHeight(null)).normalize().mult(70);

		double hitBoxScale = .7;
		_shape = new Box(this, _size.mult(0, (1-hitBoxScale)/2), _size.mult(1, hitBoxScale));

		_spells = spellNames;
		_spellCastingTimes = new HashMap<String, Long>();


		this._restitution = 0;

		_appliesFriction = true;
		
		_connected = true;

	}

	@Override
	public void draw(View v){
		TimedEffect t = _timedEffects.get(RootEffect.TYPE);
		if (t != null && t._display){
			v.drawImage(Resource.get("rootEffect"), _pos.plus(-18, _size.y-23), 90);
		}

		if (_flag != null) {
			Vector flagPos = _pos.plus(40, 0);
			v.rotate(new Vector(1.5, -1), flagPos);
			
			v.drawImage(Resource.get(_flag._basicImage), flagPos, _flag._size.mult(0.8));
			v.unrotate();
		}

		_old = v.getGraphics().getComposite();
		if (_hidden < 1) {
			AlphaComposite ac = java.awt.AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float)_hidden);
			v.getGraphics().setComposite(ac);
			v.drawImage(Resource.get(_basicImage), _pos, _size);
		} else if (_hidden == 1) {
			v.drawImage(Resource.get(_basicImage), _pos, _size);
		}
		v.getGraphics().setComposite(_old);
		
		t = _timedEffects.get(ConfuseEffect.TYPE);
		if (t != null && t._display){
			v.drawImage(Resource.get("confuseEffect"), _pos.plus(10, -30), 30);
		} 
		t = _timedEffects.get(SilenceEffect.TYPE);
		if (t != null && t._display){
			v.drawImage(Resource.get("silenceEffect"), _pos.plus(30, -10), 30);
		} 
		t = _timedEffects.get(HealthBurnEffect.TYPE);
		if (t != null && t._display){
			v.drawImage(Resource.get("burnEffect"), _pos.plus(3, _size.y-35), 50);
		}

	}

	public void stop(){
		_destination = null;
		_vel = new Vector(0,0);
	}

	@Override
	public void die() {
		System.out.println("player died");
		super.die();
		dropFlag();
		_spawnTimer = 100;
		_data._spawning.add(this);
	}
	
	@Override
	public void update(){
		super.update();
		if(_flag != null) _flag.update();
		if (_flagGrabTimer == 0) {
			_flagable = true;
		} else if (_flagGrabTimer > 0) {
			_flagable = false;
			_flagGrabTimer -= 1;
		}
		
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
		_spellCastingTimes.put(spell._name, System.currentTimeMillis());
		if (spell._name.equals("Dash")) {
			DashSpell s = (DashSpell) spell;
			s.dash();
		} else if (spell._name.equals("Hide")) {
			HideSpell s = (HideSpell) spell;
			s.hide();
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
			_flag._pos = this._pos;
			double x = Math.random()*11 - 5;
			double y = Math.random()*11 - 5;
			while((x==0) && (y==0)) {
				x = Math.random()*11 - 5;
				y = Math.random()*11 - 5;
			}
			Vector newforce = new Vector(x,y).normalize().mult(5);
			_flag.applyForce(newforce); 
			_flag._delete = false;
			_data._units.add(_flag);
			_flagable = false;
			_flagGrabTimer = 50;
			_flag = null;
		}
	}

	public void confuse(long time) {
		TimedEffect t = new ConfuseEffect(numberOfIntervals(time), this);
		_timedEffects.put(t._type, t);		
	}

	public void hide(long time) {
		TimedEffect t = new HideEffect(numberOfIntervals(time), this);
		_timedEffects.put(t._type, t);
	}
	@Override
	public String toNet() {
		String lastCastTimes = "";
		for (Entry<String, Long> e : _spellCastingTimes.entrySet()) {
			lastCastTimes += e.getKey() + "," + e.getValue() + ".";
		}
		String timedEffectsStr = "";
		for (Entry<String,TimedEffect> e : _timedEffects.entrySet()) {
			if (e.getValue() != null) {	
				timedEffectsStr += e.getValue().toNet() + ".";
			}
		}
		return _netID +              						//index: 0
				"\t" + (_staticObj ? "static" : _type) +  	//index: 1
				"\t" + _pos.toNet() +      					//index: 2
				"\t" + _destination.toNet() +  				//index: 3
				"\t" + _flag._netID +      					//index: 4
				"\t" + _health +        					//index: 5
				"\t" + _mana +          					//index: 6
				"\t" + _vel.toNet() +      					//index: 7
				"\t" + _force.toNet() +      				//index: 8
				"\t" + _isRooted +        					//index: 9
				"\t" + _isSilenced +      					//index: 10
				"\t" + lastCastTimes.substring(0, lastCastTimes.length() - 1) +
				"\t" + timedEffectsStr.substring(0, timedEffectsStr.length() - 1) +
				"\t" + _basicImage +						//index: 13
				"\t" + _connected;							//index: 14
		

		//when fromNet is called, throw away previous timed effects
		//list, and instantiate new ones with (this) as target
	}
	@Override
	public void fromNet(String[] networkString, HashMap<Integer, Unit> objectMap) {
		if (networkString[1].equals(_staticObj ? "static" : _type)) {
			this._pos = Vector.fromNet(networkString[2]);
			this._destination = Vector.fromNet(networkString[3]);
			this._flag = (Flag) objectMap.get(Integer.parseInt(networkString[4]));
			this._health = Double.parseDouble(networkString[5]);
			this._mana = Double.parseDouble(networkString[6]);
			this._vel = Vector.fromNet(networkString[7]);
			this._force = Vector.fromNet(networkString[8]);
			this._isRooted = Boolean.parseBoolean(networkString[9]);
			this._isSilenced = Boolean.parseBoolean(networkString[10]);
			this._basicImage = networkString[13];
			this._connected = Boolean.parseBoolean(networkString[14]);

			String[] lastCastTimes, sp;
			lastCastTimes = networkString[11].split(".");
			for (String spell : lastCastTimes) {
				sp = spell.split(",");
				this._spellCastingTimes.put(sp[0], Long.parseLong(sp[1]));
			}
			String[] tEffects; TimedEffect ef;
			tEffects = networkString[12].split(".");
			_timedEffects = new HashMap<String, TimedEffect>();
			for (String effect : tEffects) {
				ef = TimedEffect.fromNet(effect, this);
				if (ef != null) _timedEffects.put(ef._type, ef);
			}

		}
	}		

	public String toNetInit() {
		String spells = "";
		for (int i = 0; i < _spells.length; i++) {
			spells += _spells[i] + " ";
		}
		//Note that NetID is prepended by Coder
		return  "static" +
				"\t" + _characterName + 
				"\t" + _playerName + 
				"\t" + spells.substring(0, spells.length() - 1) +
				"\t" + _pos.toNet() +
				"\t" + _destination.toNet();
	}

	//networkString format = [id, type, <any data from toNetInit split on tabs>...]
	public static Player fromNetInit(String[] networkString) {
		if (networkString[1].equals("static")) {
			String[] spells = networkString[4].split(" ");
			Player p = new Player(null, networkString[2], networkString[3], spells);
			p._destination = Vector.fromNet(networkString[6]);
			p._pos = Vector.fromNet(networkString[5]);
			p._netID = Integer.parseInt(networkString[0]);
			return p;
		}
		throw new RuntimeException("Called Player.fromNetInit on string: " 
				+ Arrays.toString(networkString));
	}
}