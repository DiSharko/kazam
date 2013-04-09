package pvpmagic;

public abstract class Unit {
	Vector _pos, _size, _vel;
	
	protected boolean _canBeStunned = false;
	protected double _stunnedTime = 0;
	public void stun(double time){ if (_canBeStunned) _stunnedTime = time; }
	
	boolean _canBeRooted = false;
	double _rootedTime = 0;
	public void root(double time){ if (_canBeRooted) _rootedTime = time; }

	boolean _canBeSilenced = false;
	double _silencedTime = 0;
	public void silence(double time){ if (_canBeSilenced) _silencedTime = time; }

	boolean _delete = false;
	
	public void hit(Unit u){
		// I should do things TO the object I hit (and they will take care of doing things to me).
	}
	
	public void update(){
		_pos = _pos.plus(_vel);
	}
	
	public void draw(View v){}
}
