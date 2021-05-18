package M8_Milestone3.application;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import M8_Milestone3.domain.UsuariExtended;

import M8_Milestone3.utilities.HintTextField;

public class UsuariNou {
	
	private JPanel panelUN;
	private HintTextField nom, cognom, password;
	private JOptionPane pane;
	private JDialog dialog;
	private JButton buttonOK;
	private Object selectedValue;
	
	public UsuariNou() {}
	
	public UsuariExtended inici() {
		panelUN = new JPanel();
		panelUN.setLayout(new BoxLayout(panelUN, BoxLayout.Y_AXIS));
		
		panelUN.add(Box.createHorizontalStrut(250));
		
		nom = new HintTextField("< nom >");
		cognom = new HintTextField("< cognom >");
		password = new HintTextField("< password -- opcional >");
		
		panelUN.add(nom);
		panelUN.add(cognom);
		panelUN.add(password);
		
		pane = new JOptionPane(panelUN, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
	    
		dialog = pane.createDialog("INTRODUEXI DADES DE L'USUARI");
		
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
			
			public void documentChanged(DocumentEvent e) {
				buttonOK.setEnabled(!nom.getText().isBlank() && !cognom.getText().isBlank());
			}
			
		}
		
		nom.getDocument().addDocumentListener(new TextsEmplenats());
		cognom.getDocument().addDocumentListener(new TextsEmplenats());
		
		dialog.setVisible(true);
		
		selectedValue = pane.getValue();
		if(selectedValue != null && ((Integer)selectedValue).intValue() == JOptionPane.OK_OPTION) try {
			return new UsuariExtended(nom.getText(), cognom.getText(), password.getText().hashCode());
	    } catch (Exception e) { JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE); }
	    
	    return null;
	}
	
}

/*
 * Nota: Además de ser '.hashCode()' una encriptación inaceptable, la contraseña se almacena sin encriptar y en formato String
 * en algún rincón de la JVM. Los String no se pueden editar. Debería usarse un array de char y sobrescribir/borrar cada letra
 * de la contraseña al terminar con ella.
 */ 
