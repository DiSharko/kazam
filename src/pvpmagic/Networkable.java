package pvpmagic;

import java.util.HashMap;

public abstract class Networkable {
	
	public Integer _netID;
	
	public abstract void setNetID(Integer netID);
	
	public abstract String toNet();
	
	public abstract void fromNet(String s, HashMap<Integer, Unit> objectMap);
	
	public abstract Boolean validNetworkString(String[] networkData);
}
