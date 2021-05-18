package M8_Milestone3.application;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import M8_Milestone3.api.UserMenu;

import M8_Milestone3.domain.UploadStatus;
import M8_Milestone3.domain.Usuari;
import M8_Milestone3.domain.UsuariExtended;
import M8_Milestone3.domain.Video;

import M8_Milestone3.persistence.IRepository_DAO;
import M8_Milestone3.persistence.RepositoryMem_DAO;
import M8_Milestone3.persistence.RepositoryXML_DAO;

public class Controller {
	
/*
 * Implementa el patrón de diseño "prototype (creational)" para generar clones de sí mismo: Un clon para cada cuenta abierta.
 * 
 * Implementa el patrón de diseño "strategy (behavioral)" al separar la familia de algoritmos 'RepositoryXXX_DAO' (las
 * estrategias) en clases separadas de métodos intercambiables. 'Controller' ejerce de "context" del patrón.
 * 
 * NOTA: No fue posible aprovechar la interfaz 'Cloneable' junto al método '.clone()' de la clase 'Object', ambos incluidos
 * en el paquete estándar 'java.lang', porque la clase 'Controller' contiene dos clases internas. Éstas poseen sendas
 * referencias "ocultas" 'this$0' a la clase madre que las arropa QUE TAMBIÉN SON CLONADAS. Permiten a las subclases acceder
 * a todo el material de la clase 'Controller', por ejemplo, 'contU.usuari' desde 'contV'. Aunque es posible leer el
 * contenido de estos campos utilizando métodos reflexivos (paquete 'java.lang.reflect') desde dentro de cada subclase,
 * 
 *  Field madreU = Controller.U.class.getDeclaredField("this$0");
 *  Controller madre = (Controller)madreU.get(this);
 *  
 *  no se pueden modificar con el método 'madreU.set( , )' ¡porque el campo 'this$0' es "final"!
 */
	
	public static final List<String> BBDDs = List.of("Memòria", "Disc dur - XML");
	public static List<UserMenu> activeUsers = new ArrayList<>();
	
	private IRepository_DAO repository;
	
	public String labelBBDD;
	
	public U contU;
	public V contV;
	
	public Controller() {
		contU = new U();
		contV = new V();
	}
	
	public Controller(Controller c) {
		this();
		if(c != null) {
			this.repository = c.repository; // Todos los clones mantienen el mismo 'repository'
			this.labelBBDD = c.labelBBDD;
			this.contU.usuari = c.contU.usuari;
			this.contV.video = c.contV.video;
		}
	}
	
	@Override // Método '.clone()' usando un constructor de copia
	public Controller clone() {
		return new Controller(this);
	}
	
	public void setRepository(String op) {
		switch(op) {
		case "Memòria": default:
			repository = RepositoryMem_DAO.getInstance();
			break;
		case "Disc dur - XML":
			repository = RepositoryXML_DAO.getInstance();
			break;
		}
		labelBBDD = " -- " + op;
	}
	
	public void error(Exception e) {
		error(e.getMessage());
	}
	
	public void error(String e) {
		JOptionPane.showMessageDialog(null, e, "ERROR", JOptionPane.ERROR_MESSAGE);
	}
	
	public class U { // Controlador: funcions d'usuari
		
		public Usuari usuari;
		
		public U() {}
		
		public void openUsuari() {
			if(selectUsuari() && checkUsuari()) try {
				new UserMenu(Controller.this.clone()).start();
			} catch (Exception e) { error(e); }
		}
		
		public boolean selectUsuari() {
			usuari = null;
			try { usuari = new ObrirUsuari(repository).inici();
			} catch (Exception e) { error(e); }
			
			return usuari != null;
		}
		
		public boolean checkUsuari() {
			boolean passwordChecked = false;
			if(usuari != null) try {
				passwordChecked = new VerificarPassword(((UsuariExtended)usuari).getHashCode(), usuari.toString()).inici();
			} catch (Exception e) { error(e); }
			
			return passwordChecked;
		}
		
		public void createUsuari() {
			usuari = new UsuariNou().inici();
			if(usuari != null && !repository.addUsuari(((UsuariExtended)usuari)))
				error("Unable to add the account: User already exists in the database.");
		}
		
		public void printUsuaris() { try {
			new LlistatUsuaris(repository.findUsuaris(null), labelBBDD).inici();
		} catch (Exception e) { error(e); } }
		
		public void deleteUsuari() {
			if(selectUsuari() && checkUsuari()
					&& new Confirmacio().inici("Segur que vol esborrar el compte \'" + usuari.toString() + labelBBDD + "\'?")) {
				usuari.getVideos().forEach(v -> v.killTimer());
					
				repository.removeUsuari(usuari);
				// Cierra las ventanas abiertas con este 'usuari' en la misma BBDD
				activeUsers.forEach(aU -> {
					if(aU.getController().repository == repository && aU.getController().contU.usuari.equals(usuari))
						aU.kill();
				});
				// Da de baja de la lista activa a todas las ventanas del 'usuari' en la misma BBDD
				activeUsers.removeIf(aU -> aU.getController().repository == repository &&
						aU.getController().contU.usuari.equals(usuari));
			}
		}
		
	}
	
	public class V { // Controlador: funcions de vídeo (ja tenim l'usuari des del repositori a 'contU.usuari'!)
		
		public Video video;
		
		public V() {}
		
		public void playVideo() {
			if(selectVideo())
				if(video.getUploadStatus() != UploadStatus.PUBLIC)
					error("Please wait for the video's full upload and its integrity verification.");
				else try {
					new ReproduirVideo(video).inici();
				} catch (Exception e) { error(e); }
		}
		
		public void createVideo() {
			video = new VideoNou().inici();
			if(video != null && !repository.addVideo(contU.usuari, video))
				error("Unable to add the video: Video already exists in the database.");
		}
		
		public boolean selectVideo() {
			video = null;
			try { video = new ObrirVideo(contU.usuari.getVideos()).inici();
			} catch (Exception e) { error(e); }
			
			return video != null;
		}
		
		public void printVideo() {
			if(selectVideo()) try {
				new MostrarVideo(Controller.this, video).inici();
			} catch (Exception e) { error(e); }
		}
		
		public void deleteVideo() {
			if(selectVideo() && new Confirmacio().inici("Segur que vol esborrar el vídeo \'" + video.toString() + labelBBDD + "\'?")) {
				video.killTimer();
				repository.removeVideo(contU.usuari, video);
			}
		}
		
	}
	
}
