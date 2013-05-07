package pvpmagic;

import java.util.HashMap;

public abstract class TeamData {
	protected final int NEEDED;
	protected HashMap<Vector,Player> spawnMapTeam1;
	protected HashMap<Vector,Player> spawnMapTeam2;
	protected int _team1Score = 0;
	protected int _team2Score = 0;
	protected String _winTeam = null;
	
	public TeamData (int winNum) {
		spawnMapTeam1 = new HashMap<Vector,Player>();
		spawnMapTeam2 = new HashMap<Vector,Player>();
		NEEDED = winNum;
	}
	
	public abstract void update();
	
	public String getWinTeam() {
		return this._winTeam;
	}
}
