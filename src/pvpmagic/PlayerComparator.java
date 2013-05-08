package pvpmagic;

import java.util.Comparator;

public class PlayerComparator implements Comparator<Player> {
	
	public PlayerComparator() {}

	@Override
	public int compare(Player o1, Player o2) {
		Integer net1 = o1._netID;
		Integer net2 = o2._netID;
		if (net1 != null && net2 != null) {
			if (net1 > net2) {
				return -1;
			} else
			if (net1 < net2) {
				return 1;
			} else {
				return 0;
			}
			
		}
		return 0;
	}
	
	

}
