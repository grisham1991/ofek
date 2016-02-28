package ofek_proj.EncryptionAlgorithms;

import java.util.Comparator;
//compare algorithms
public class AlgorithmComparator<T> implements Comparator<EncryptionAlgorithm<T>> {

	public int compare(EncryptionAlgorithm<T> algorithm1, EncryptionAlgorithm<T> algorithm2) {
		return algorithm1.getKeyStrength()-algorithm2.getKeyStrength();
	}

}
