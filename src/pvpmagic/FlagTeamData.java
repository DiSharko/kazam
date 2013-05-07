package pvpmagic;

public class FlagTeamData extends TeamData{
	private FlagPedestal _team1Ped;
	private FlagPedestal _team2Ped;
	 	
	public FlagTeamData(int winNum) {
		super(winNum);
	}
	
	public void setTeam1Ped (FlagPedestal ped) {
		_team1Ped = ped;
	}
	public void setTeam2Ped (FlagPedestal ped) {
		_team2Ped = ped;
	}

	@Override
	public void update() {
		//update scores
		if (_team1Ped._flag != null) {
			_team1Score += 1;
			_team1Ped._flag._pos = _team1Ped._flag._originalPos;
			_team1Ped._flag._vel = new Vector(0,0);
			_team1Ped._flag._delete = false;
			_team1Ped._flag._data._units.add(_team1Ped._flag);
			_team1Ped._flag = null;
		} else if (_team2Ped._flag != null) {
			_team2Score += 1;
			_team2Ped._flag._pos = _team2Ped._flag._originalPos;
			_team2Ped._flag._vel = new Vector(0,0);
			_team2Ped._flag._delete = false;
			_team2Ped._flag._data._units.add(_team2Ped._flag);
			_team2Ped._flag = null;
		}
		
		//check if someone has won
		if (_team1Score == NEEDED) {
			_winTeam = "Team 1";
		} else if (_team2Score == NEEDED) {
			_winTeam = "Team 2";
		}
	}
}