package M8_Milestone3.persistence;

import java.io.File;
import java.io.IOException;

import java.time.LocalDateTime;

import java.time.temporal.ChronoUnit;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import javax.xml.transform.dom.DOMSource;

import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import M8_Milestone3.domain.Usuari;
import M8_Milestone3.domain.UsuariExtended;
import M8_Milestone3.domain.Video;

public class RepositoryXML_DAO implements IRepository_DAO {  // Base de dades al disc dur - format XML

/*
 * Implementa el patrón de diseño "singleton (creational)" para mantener la unicidad de la BBDD en el disco.
 * 
 * Los datos se insertan lexicográficamente, respetando las ordenaciones de las clases 'Usuari' y 'Video'.
 */
	
	private static File xmlFile = new File("./M8_Milestone3.xml");
	private static RepositoryXML_DAO instance = null;
	
	private RepositoryMem_DAO internalRepository; // Base de datos paralela en memoria
	private Map<Usuari, LocalDateTime> updates; // Registro del último 'update' en cada 'usuari'
	
	private Document doc;
	private NodeList usuaris, videos; // 'videos' se actualiza antes de cada uso; esta clase la usan todos los 'usuari'
	private Transformer transf;
	private Element elementFound, elementAfter; // Es fan servir a las funcions de cerca
		
	private RepositoryXML_DAO() {
		DocumentBuilderFactory dBF = DocumentBuilderFactory.newInstance();
		dBF.setValidating(true);
		dBF.setIgnoringElementContentWhitespace(true); // Requiere la validación anterior y el archivo .dtd
		try {
		DocumentBuilder dB = dBF.newDocumentBuilder();	
		doc = dB.parse(xmlFile);
		
		usuaris = doc.getElementsByTagName("usuari");
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transf = transformerFactory.newTransformer();
        transf.setOutputProperty(OutputKeys.INDENT, "yes");     
        DocumentType docType = doc.getDoctype();
        transf.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, docType.getSystemId());
		} catch (IOException e) { error("Unable to read the XML file or its DTD validation."); }
		catch (ParserConfigurationException | SAXException | TransformerConfigurationException e) { e.printStackTrace(); }
		
		internalRepository = new RepositoryMem_DAO(); // Única instancia de la BBDD en memoria "paralela"
		updates = new TreeMap<>();
	}
	
	public static RepositoryXML_DAO getInstance() { // Unicidad de la BBDD mediante el patrón "singleton"
		if (instance == null)
			instance = new RepositoryXML_DAO();
		return instance;
	}
	
	@Override
	public boolean addUsuari(UsuariExtended usuari) {
		reviewIntRep();
		
		if(!internalRepository.addUsuari(usuari))
			return false; // El video ya existe
		
		LocalDateTime update = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		updates.put(usuari, update); // Actualiza el registro interno
				
		Element el, nuevoU = doc.createElement("usuari");
		nuevoU.setAttribute("registre", usuari.getRegistry().toString());
		nuevoU.setAttribute("update", update.toString());
		
		el = doc.createElement("password");
		el.appendChild(doc.createTextNode(usuari.getHashCode().toString()));
		nuevoU.appendChild(el);
		
		el = doc.createElement("nom");
		el.appendChild(doc.createTextNode(usuari.getNom()));
		nuevoU.appendChild(el);
		
		el = doc.createElement("cognom");
		el.appendChild(doc.createTextNode(usuari.getCognom()));
		nuevoU.appendChild(el);
		
		el = doc.createElement("videos");
		nuevoU.appendChild(el);
		
		findUsuariInXML(usuari);
		doc.getDocumentElement().insertBefore(nuevoU, elementAfter); // 'elementAfter' == 'null' equivale a '.appendChild()'
		
		saveXML();
		
		return true;
	}
	
	@Override
	public Set<Usuari> findUsuaris(String cognomParcial) {
		reviewIntRep();
		
		return internalRepository.findUsuaris(cognomParcial);
	}
	
	@Override
	public void removeUsuari(Usuari usuari) {
		reviewIntRep();
		
		internalRepository.removeUsuari(usuari);
		
		updates.remove(usuari); // Actualiza el registro interno
		
		findUsuariInXML(usuari);
		doc.getDocumentElement().removeChild(elementFound);
		
		saveXML();
	}
	
	@Override	
	public int size() {
		reviewIntRep();
		
		return internalRepository.size();
	}
	
	@Override
	public boolean addVideo(Usuari usuari, Video video) { try {
		reviewIntRep();
		
		if(!internalRepository.addVideo(usuari, video))
			return false; // El video ya existe
		
		LocalDateTime update = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		updates.put(usuari, update); // Actualiza el registro interno
		
		findUsuariInXML(usuari);
		elementFound.setAttribute("update", update.toString());
		
		Element el, elTag, nuevoV = doc.createElement("video");
		nuevoV.setAttribute("timestamp", video.getRegistry().toString());
		
		el = doc.createElement("URL");
		el.appendChild(doc.createTextNode(video.getURL()));
		nuevoV.appendChild(el);
		
		el = doc.createElement("titol");
		el.appendChild(doc.createTextNode(video.getTitol()));
		nuevoV.appendChild(el);
		
		el = doc.createElement("durada");
		el.appendChild(doc.createTextNode(video.getDurada().toString()));
		nuevoV.appendChild(el);
		
		el = doc.createElement("tags");
		for(String tag : video.getTags()) {
			elTag = doc.createElement("tag");
			elTag.appendChild(doc.createTextNode(tag));
			el.appendChild(elTag);
		}
		nuevoV.appendChild(el);
		
		videos = elementFound.getElementsByTagName("video");
		Node nodeVideos = elementFound.getElementsByTagName("videos").item(0);
		
		findVideoInXML(video);
		nodeVideos.insertBefore(nuevoV, elementAfter); // 'elementAfter' == 'null' equivale a '.appendChild()'
		
		saveXML();
	} catch (NullPointerException e) { error("Database scrambled outside the application. External editing required."); } return true; }
	
	@Override
	public void removeVideo(Usuari usuari, Video video) { try {
		reviewIntRep();
		
		internalRepository.removeVideo(usuari, video);
		
		LocalDateTime update = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		updates.put(usuari, update); // Actualiza el registro interno
		
		findUsuariInXML(usuari);
		elementFound.setAttribute("update", update.toString());
		
		videos = elementFound.getElementsByTagName("video");
		Node nodeVideos = elementFound.getElementsByTagName("videos").item(0);
		
		findVideoInXML(video);
		nodeVideos.removeChild(elementFound);
		
		saveXML();
	} catch (NullPointerException e) { error("Database scrambled outside the application. External editing required."); } }
	
	private void error(String e) {
		JOptionPane.showMessageDialog(null, e, "ERROR", JOptionPane.ERROR_MESSAGE);
	}
	
	private void reviewIntRep() { // Contrasta y actualiza el registro en memoria con la versión en el disco
		Map<Usuari, LocalDateTime> checkedUpdates = new TreeMap<>();
		
		for(int u = 0; u < usuaris.getLength(); u++) {
			Element el = (Element)usuaris.item(u);
			try {
				Scanner updateEl = new Scanner(el.getAttribute("update"));
				updateEl.useDelimiter(Pattern.compile("-|T|:"));
				int updtYr = updateEl.nextInt();
				int updtMo = updateEl.nextInt();
				int updtDy = updateEl.nextInt();
				int updtHr = updateEl.nextInt();
				int updtMi = updateEl.nextInt();
				int updtSc = updateEl.nextInt();
				updateEl.close();
				LocalDateTime ldtXML = LocalDateTime.of(updtYr, updtMo, updtDy, updtHr, updtMi, updtSc);
				
				String nomXML = el.getElementsByTagName("nom").item(0).getFirstChild().getNodeValue();
				String cognomXML = el.getElementsByTagName("cognom").item(0).getFirstChild().getNodeValue();
				
				// Mueve los registros existentes updates -> checkedUpdates
				boolean found = false;
				for(Map.Entry<Usuari, LocalDateTime> entryUpdate : updates.entrySet()) {
					Usuari entryU = entryUpdate.getKey();
					String nomEntry = entryU.getNom();
					String cognomEntry = entryU.getCognom();
					if(entryUpdate.getValue().equals(ldtXML) && nomEntry.equalsIgnoreCase(nomXML) && cognomEntry.equalsIgnoreCase(cognomXML)) {
						found = true;
						checkedUpdates.put(entryU, ldtXML);
						updates.remove(entryU);
						break;
					}
				}
				
				// Creación de objetos y registros nuevos
				if(!found) { 
					UsuariExtended usuariFromXML = MakeObject.fromXML(el);
					internalRepository.usuaris.put(usuariFromXML, usuariFromXML.getHashCode());
					checkedUpdates.put(usuariFromXML, ldtXML);
				}
			} catch (NoSuchElementException | NullPointerException e) {	error("Database format corrupted. Unable to read the required field(s)."); }
		}
		
		// Elimina los registros anticuados que ya no aparecen en el XML
		for(Map.Entry<Usuari, LocalDateTime> entryUpdate : updates.entrySet())
			internalRepository.usuaris.remove(entryUpdate.getKey());	
		
		// Guarda los cambios
		updates = checkedUpdates;
	}
	
	private void saveXML() {
		Source source = new DOMSource(doc);
		Result result = new StreamResult(xmlFile);
		try { transf.transform(source, result);
		} catch (TransformerException e) { error("Unable to write to XML file."); }		
	}
	
	private boolean findUsuariInXML(Usuari usuari) {
		Element elementU;
		int c1 = -1, c2 = -1;
		elementFound = null;
		elementAfter = null;
		for(int u = 0; u < usuaris.getLength(); u++) { try {
			elementU = (Element)usuaris.item(u);
			String cognom = elementU.getElementsByTagName("cognom").item(0).getFirstChild().getNodeValue();
			String nom = elementU.getElementsByTagName("nom").item(0).getFirstChild().getNodeValue();
			c1 = cognom.compareToIgnoreCase(usuari.getCognom());
			c2 = nom.compareToIgnoreCase(usuari.getNom());
			if(c1 < 0 || (c1 == 0 && c2 < 0))
				continue;
			if(c1 == 0 && c2 == 0) {
				elementFound = elementU;
				return true;
			}
			elementAfter = elementU;
			break;
			} catch (NullPointerException e) { error("Database format corrupted. Unable to read the required field(s)."); }
		}
		
		return false;
	}
	
	private boolean findVideoInXML(Video video) {
		Element elementV;
		int c1 = -1, c2 = -1;
		elementFound = null;
		elementAfter = null;
		for(int v = 0; v < videos.getLength(); v++) { try {
			elementV = (Element)videos.item(v);
			String url = elementV.getElementsByTagName("URL").item(0).getFirstChild().getNodeValue();
			String titol = elementV.getElementsByTagName("titol").item(0).getFirstChild().getNodeValue();
			c1 = titol.compareToIgnoreCase(video.getTitol());
			c2 = url.compareToIgnoreCase(video.getURL());
			if(c1 < 0 || (c1 == 0 && c2 < 0))
				continue;
			if(c1 == 0 && c2 == 0) {
				elementFound = elementV;
				return true;
			}
			elementAfter = elementV;
			break;
			} catch (NullPointerException e) { error("Database format corrupted. Unable to read the required field(s)."); }
		}
		
		return false;
	}
	
}
