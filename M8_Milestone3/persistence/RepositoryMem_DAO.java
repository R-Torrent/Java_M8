package M8_Milestone3.persistence;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import M8_Milestone3.domain.Usuari;
import M8_Milestone3.domain.UsuariExtended;
import M8_Milestone3.domain.Video;

public final class RepositoryMem_DAO implements IRepository_DAO { // Base de dades a la memòria
	
/*
 * Implementa el patrón de diseño "singleton (creational)" para mantener la unicidad de la BBDD en memoria.
 * 
 * NOTA acerca de la tabla 'usuaris', TreeMap<KEY, VALUE>
 *   KEY: es el objeto 'usuari', pues debe ser un conjunto único
 *   VALUE: el la contraseña del usuario/a, transformado a 'int' vía el método '.hashCode()' de la clase 'Object'
 * 
 * Este 'TreeMap' utiliza la ordenación definida en la clase 'Usuari'.
 */
	
	private static RepositoryMem_DAO instance = null;
	
	protected Map<Usuari, Integer> usuaris;
	
/*
 * Este constructor generalmente se declara 'private'. Pero en este caso queremos que los otros 'RepositoryXXX_DAO'
 * operen con una base de datos paralela en memoria para minimizar los accesos al disco duro/servidor remoto, según
 * el caso. Por el mismo motivo, el mapa 'usuaris' tampoco es 'private'.
 */
	protected RepositoryMem_DAO() {
		usuaris = new TreeMap<>();
	}
	
	public static RepositoryMem_DAO getInstance() { // Unicidad de la BBDD mediante el patrón "singleton"
		if (instance == null)
			instance = new RepositoryMem_DAO();
		return instance;
	}
	
	@Override
	public boolean addUsuari(UsuariExtended usuari) {
		return !usuaris.containsKey(usuari) && usuaris.put(usuari, usuari.getHashCode()) == null;
	} // La comprobación inicial evita que se machaque la contraseña con una entrada repetida
		
	@Override
	public Set<Usuari> findUsuaris(String cognomParcial) {
		Set<Usuari> trobats = new TreeSet<>(usuaris.keySet());
		if(cognomParcial == null || cognomParcial.isBlank()) return trobats;
		
		trobats.removeIf(u -> !u.getCognom().toUpperCase().contains(cognomParcial.toUpperCase()));
		
		return trobats;
	}
	
	@Override
	public void removeUsuari(Usuari usuari) {
		usuaris.remove(usuari);
	}
	
	@Override
	public int size() {
		return usuaris.size();
	}
	
	@Override
	public boolean addVideo(Usuari usuari, Video video) {
		return !usuari.getVideos().contains(video) && usuari.addVideo(video);
	} // La comprobación inicial evita que se machaquen los 'tags' del vídeo con una entrada repetida
	
	@Override
	public void removeVideo(Usuari usuari, Video video){
		usuari.removeVideo(video);
	}
	
}
