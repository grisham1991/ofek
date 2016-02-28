package ofek_proj.FileAndDirectoryEncryptors;

import ofek_proj.EncryptionObserverImplLogger.EncryptionLogEventArgs;

public interface EncryptionObserver {
	public void updateEncryptionStarted(EncryptionLogEventArgs args);
	public void updateEncryptionEnded(EncryptionLogEventArgs args);
	public void updateDecryptionStarted(EncryptionLogEventArgs args);
	public void updateDecryptionEnded(EncryptionLogEventArgs args);

}
