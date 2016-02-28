package ofek_proj.Utils;

import java.util.List;

public class utilArgs {
	public static String DoublePrefix="Double Encryption using ";//prefix for double encryption name
	
	public static int smear(int hashCode) {
	    hashCode ^= (hashCode >>> 20) ^ (hashCode >>> 12);
	    return hashCode ^ (hashCode >>> 7) ^ (hashCode >>> 4);
	}
	public static int getHashCode(List<Integer> list) {
		int hash = 0, multiplier = 1,hashCode;
		for (int i = 0; i <list.size(); i++) {
			hash += list.get(i) * multiplier;
			int shifted = multiplier << 5;
			multiplier = shifted - multiplier;
		}
		hashCode = hash;
		return hashCode;
	}
}
