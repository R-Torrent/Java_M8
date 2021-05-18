package M8_Milestone3.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.URL;
import java.net.MalformedURLException;

import java.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import M8_Milestone3.domain.Video;

import M8_Milestone3.utilities.HintTextField;

public class VideoNou {
	
	private JPanel panelVN;
	private HintTextField url, titol, fieldTg;
	private Box boxTg;
	private JButton buttonTg, buttonOK;
	private JTextArea textA;
	private List<String> tags;
	private JScrollPane scrollPanel;
	private JOptionPane pane;
	private JDialog dialog;
	private Object selectedValue;
	
	public VideoNou() {}
	
	public Video inici() {
		panelVN = new JPanel();
		panelVN.setLayout(new BoxLayout(panelVN, BoxLayout.Y_AXIS));
		
		panelVN.add(Box.createHorizontalStrut(250));
		
		url = new HintTextField("< URL -- només adreces vàlides >");
		titol = new HintTextField("< títol >");
		
		panelVN.add(url);
		panelVN.add(titol);
		
		boxTg = Box.createHorizontalBox();
		fieldTg = new HintTextField("< tag >");
		buttonTg = new JButton("Afegir");
		
		boxTg.add(fieldTg);
		boxTg.add(buttonTg);
		panelVN.add(boxTg);
		
		textA = new JTextArea("< llista de tags buida >\n\n\n");
		textA.setEditable(false);
		textA.setFocusable(false);
		
		tags = new ArrayList<>();
		
		buttonTg.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String tag = fieldTg.getText();
				if(!tag.isBlank()) {
					tags.add(tag);
					fieldTg.reset();
					
					StringJoiner textTgs = new StringJoiner("\n");
					for(String t : tags)
						textTgs.add(t);
					textA.setText(textTgs.toString());
				}
			}
				
		});
		
		scrollPanel = new JScrollPane(textA);
		panelVN.add(scrollPanel);
		
		pane = new JOptionPane(panelVN, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		
		dialog = pane.createDialog("INTRODUEXI DADES DEL VIDEO");

		buttonOK = pane.getRootPane().getDefaultButton();
		buttonOK.setEnabled(false);

		class TextsEmplenats implements DocumentListener {

			@Override
			public void insertUpdate(DocumentEvent e) {
				documentChanged(e);
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				documentChanged(e);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				documentChanged(e);
			}
			
			public void documentChanged(DocumentEvent ev) { try {
				new URL(url.getText()); // URL válida o salta la excepción
				buttonOK.setEnabled(!titol.getText().isBlank());
			} catch (MalformedURLException ex) { buttonOK.setEnabled(false); } }
			
		}
		
		url.getDocument().addDocumentListener(new TextsEmplenats());
		titol.getDocument().addDocumentListener(new TextsEmplenats());
		
		dialog.setVisible(true);
		
		selectedValue = pane.getValue();
		if(selectedValue != null && ((Integer)selectedValue).intValue() == JOptionPane.OK_OPTION) try {		
/*
 * Aquí vendría la determinación automática de la duración del vídeo a partir del material original en la dirección URL.
 * A falta de vídeos auténticos, generamos una variable 'durada' ficticia razonablemente corta:
 */
			LocalTime durada = LocalTime.of(0, 0, 25).plusSeconds(LocalTime.now().getSecond()); 
			
	    	Video video = new Video(url.getText(), titol.getText(), durada);
		    tags.forEach(t -> video.addTag(t));
		    return video;
	    } catch (Exception e) { JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE); }
	    
	    return null;
	}
	
}
