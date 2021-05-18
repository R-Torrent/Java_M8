package M8_Milestone3.domain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import java.time.format.DateTimeFormatter;

import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.Timer;
import java.util.TimerTask;

public class Video implements Comparable<Video> {
	
	protected String URL, titol;
	protected LocalTime durada;
	protected LocalDateTime registre;
	protected List<String> tags;
	protected UploadStatus upldS;
	
	private Set<ActionListener> listeners;
	private Timer uploadTimer;
		
	// Contructor habitual para los registros nuevos
	public Video(String URL, String titol, LocalTime durada) throws Exception {
		this(URL, titol, durada, LocalDateTime.now());
	}
	
	// Constructor para crear objetos en memoria desde una fuente externa (XML, etc.)
	public Video(String URL, String titol, int duradaHour, int duradaMin, int duradaSec,
			int stampYear, int stampMon, int stampDay, int stampHour, int stampMin, int stampSec) throws Exception {
		this(URL, titol, LocalTime.of(duradaHour, duradaMin, duradaSec),
				LocalDateTime.of(stampYear, stampMon, stampDay, stampHour, stampMin, stampSec));
	}
	
	public Video(String URL, String titol, LocalTime durada, LocalDateTime timestamp) throws Exception {
		if(URL == null || titol == null || timestamp == null || durada == null || URL.isBlank() || titol.isBlank())
			throw new Exception("Data insufficient to create a new video.");
		
		this.URL = URL;
		this.titol = titol;
		this.durada = durada;
		registre = timestamp.truncatedTo(ChronoUnit.SECONDS);
		tags = new ArrayList<>();
		upldS = UploadStatus.determine(registre);
		
		listeners = new HashSet<>();
		StatusUpdate(); // Establece la serie de temporizadores que actualizan el 'UploadStatus' del vídeo
	}
	
	// Define una ordenación natural para la clase 'Video': Alfabéticamente, por título (1º) y URL (2º)
	@Override
	public int compareTo(Video v) {
		final int c1 = titol.compareToIgnoreCase(v.titol);
		final int c2 = URL.compareToIgnoreCase(v.URL);
			
		return c1 == 0 ? c2 : c1;
	}
	
	// Este método se sobreescribe para que se mantenga la consistencia del ordenamiento definido anteriormente
	@Override
	public boolean equals(Object v) {
		return v instanceof Video && URL.equalsIgnoreCase(((Video)v).URL) && titol.equalsIgnoreCase(((Video)v).titol);
	}
	
	public String getURL() {
		return URL;
	}
	
	// Campo examinado por el buscador de vídeos
	public String getTitol() {
		return titol;
	}
	
	public LocalTime getDurada() {
		return durada;
	}
	
	public LocalDateTime getRegistry() {
		return registre;
	}
	
	public UploadStatus getUploadStatus() {
		return upldS;
	}
	
	// Descripción breve para el buscador
	@Override
	public String toString() {
		return titol + "  [ " + URL + " ]" + (upldS != UploadStatus.PUBLIC ? "  *" + upldS + "*" : "");
	}
	
	// Descripción detallada para la impresora de vídeos
	public String showVideo() {
		String textTit = "Títol:  " + titol + "\n";
		String textURL = "URL:  " + URL + "\n";
		String textDur = "Durada:  " + durada + "\n";
		
		StringJoiner textTags = new StringJoiner("\n   ", "Tag(s):\n   ", "\n");
		textTags.setEmptyValue("< no tags >\n");
		tags.forEach(t -> textTags.add(t));
		
		String textReg = "Uploaded:  " + registre.format(DateTimeFormatter.ofPattern("uuuu-MM-dd  HH:mm:ss")) + "\n";
		String textStU = "Status:  " + upldS;
		
		return textTit + textURL + textDur + textTags.toString() + textReg + textStU;
	}
	
	public List<String> getTags() {
		return tags;
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}
	
	private void StatusUpdate() {
		if(upldS != UploadStatus.PUBLIC) {
			LocalDateTime ldt_next = UploadStatus.nextStatus(registre);
			Date date_next = Date.from(ldt_next.atZone(ZoneId.systemDefault()).toInstant()); // Molesto cambio de formato
			uploadTimer = new Timer();
			uploadTimer.schedule(new StatusUpdateTask(), date_next);			
		}
	}
	
	public void killTimer() { // Cancela el 'thread' abierto por el temporizador 
		if(uploadTimer != null) uploadTimer.cancel();
	}
	
	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}
	
	private class StatusUpdateTask extends TimerTask {
				
		private StatusUpdateTask() {}
		
		@Override
		public void run() {
			upldS = upldS.next();
			
			// Actualiza las pantallas que estén mostrando el vídeo con 'toString()' o 'showVideo()'
			ActionEvent action = new ActionEvent((Object)Video.this, ActionEvent.ACTION_PERFORMED, upldS.name()); 
			listeners.removeIf(l -> l == null);
			listeners.forEach(l -> l.actionPerformed(action));
			
			StatusUpdate();
		}
		
	}
	
}
