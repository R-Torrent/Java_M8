package M8_Milestone3.domain;

import java.time.LocalDate;

import java.util.Set;
import java.util.TreeSet;

public class Usuari implements Comparable<Usuari> {
	
	protected String nom, cognom;
	protected LocalDate registry;
	protected Set<Video> videos;
	
	public Usuari(String nom, String cognom, LocalDate registry) throws Exception {
		if(nom == null || cognom == null || nom.isBlank() || cognom.isBlank())
			throw new Exception("Data insufficient to create a new user.");
		
		this.nom = nom;
		this.cognom = cognom;
		this.registry = registry;
		videos = new TreeSet<>();
	}
	
	// Define una ordenación natural para la clase 'Usuari': Alfabéticamente por apellido (1º) y nombre (2º)
	@Override
	public int compareTo(Usuari u) {
		final int c1 = cognom.compareToIgnoreCase(u.cognom);
		final int c2 = nom.compareToIgnoreCase(u.nom);
			
		return c1 == 0 ? c2 : c1;
	}
	
	// Este método se sobreescribe para que se mantenga la consistencia del ordenamiento definido anteriormente
	@Override
	public boolean equals(Object u) {
		return u instanceof Usuari && cognom.equalsIgnoreCase(((Usuari)u).cognom) && nom.equalsIgnoreCase(((Usuari)u).nom);
	}
	
	public String getNom() {
		return nom;
	}
	
	// Campo examinado por el buscador de usuarios
	public String getCognom() {
		return cognom;
	}
	
	public LocalDate getRegistry() {
		return registry;
	}
	
	// Descripción breve para el buscador
	@Override
	public String toString() {
		return cognom + ", " + nom;
	}
	
	// Descripción detallada para la impresora de usuarios
	public String showUsuaris() {
		return this.toString() + " (" + registry + ") " + videos.size() + " vídeo(s)";
	}
	
	public Set<Video> getVideos(){
		return videos;
	}
	
	public boolean addVideo(Video video) {
		return videos.add(video);
	}
	
	public boolean removeVideo(Video video) {
		return videos.remove(video);
	}
	
}
