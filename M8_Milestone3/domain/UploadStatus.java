package M8_Milestone3.domain;

import java.time.LocalDateTime;

public enum UploadStatus {
	
	PUBLIC(180)   { public UploadStatus next() { return PUBLIC;    } },
	VERIFYING(60) { public UploadStatus next() { return PUBLIC;    } },
	UPLOADING(0)  { public UploadStatus next() { return VERIFYING; } };
	
	private final int secLapse;
	
	UploadStatus(int secLapse) {
		this.secLapse = secLapse;
	}
	
	public static UploadStatus determine(LocalDateTime timeStamp) {
		for(UploadStatus upldS : UploadStatus.values())
			if(LocalDateTime.now().compareTo(timeStamp.plusSeconds(upldS.secLapse)) >= 0)
				return upldS;
		
		return UPLOADING; // Este comando s�lo se ejecutar� en caso de tener un 'timeStamp' err�neo (�instante futuro!)
	}
	
	public static LocalDateTime nextStatus(LocalDateTime timeStamp) {
		// Devuelve el pr�ximo cambio de 'UploadStatus', o 'null' si el v�deo ya es 'PUBLIC' 
		LocalDateTime statusChange, nextSt = null;
		for(UploadStatus upldS : UploadStatus.values()) {
			statusChange = timeStamp.plusSeconds(upldS.secLapse);
			if(LocalDateTime.now().compareTo(statusChange) >= 0)
				break;
			nextSt = statusChange;
		}
		
		return nextSt;
	}
	
	public abstract UploadStatus next(); // Cada constante de enumeraci�n implementa su propia �promoci�n� de categor�a
	
}
