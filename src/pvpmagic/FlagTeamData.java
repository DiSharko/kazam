package pvpmagic;

public class FlagTeamData extends TeamData{
	private FlagPedestal _pedestal;
	 	
	public FlagTeamData(int teamNum) {
		super(teamNum);
	}
	
	public void setPed (FlagPedestal ped) {
		_pedestal = ped;
	}

	@Override
	public void update() {
		//update scores
		if (_pedestal._flag != null) {
			_teamScore += 1;
			System.out.println("Flagdata flag orig pos = "+_pedestal._flag._originalPos);
			_pedestal._flag._pos = _pedestal._flag._originalPos;
			System.out.println("Flagdata flag pos = "+_pedestal._flag._pos);
			_pedestal._flag._delete = false;
			_pedestal._flag._data._units.add(_pedestal._flag);
			System.out.println("Flagdata flag pos = "+_pedestal._flag._pos);
			_pedestal._flag = null;
		}
	}
}