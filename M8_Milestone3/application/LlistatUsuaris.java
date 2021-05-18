package M8_Milestone3.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Set;
import java.util.StringJoiner;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import M8_Milestone3.domain.Usuari;
import M8_Milestone3.domain.UsuariExtended;

public class LlistatUsuaris {
	
	private Set<Usuari> setRepository;
	private String labelBBDD;
	
	private JPanel panelLUs;
	private JCheckBox checkPassword; // Mostrar contraseña almacenada con '.hashCode()' («mal encriptada»)
	private JTextArea textA;
	private JScrollPane scrollPanel;

	public LlistatUsuaris(Set<Usuari> setRepository, String labelBBDD) throws Exception {
		if(setRepository.size() == 0) throw new Exception("User database empty.");
		
		this.setRepository = setRepository;
		this.labelBBDD = labelBBDD;
	}
	
	public void inici() {
		panelLUs = new JPanel();
		panelLUs.setLayout(new BoxLayout(panelLUs, BoxLayout.Y_AXIS));
		
		checkPassword = new JCheckBox("Mostrar les contrasenyes emmagatzemades (encriptades)");
		panelLUs.add(checkPassword);
		
		textA = new JTextArea();
		textA.setEditable(false);
		textA.setFocusable(false);
		
		checkPassword.setSelected(true);
		checkPassword.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				StringJoiner textSJ = new StringJoiner("\n");

				for(Usuari u : setRepository)
					textSJ.add(u.showUsuaris() + (checkPassword.isSelected() ?
						" < " + Integer.toHexString(((UsuariExtended)u).getHashCode().intValue()).toUpperCase() + " >" : ""));
				textA.setText(textSJ.toString());
			}
				
		});
		checkPassword.doClick(); // Por defecto, las contraseñas no se mostrarán
		
		scrollPanel = new JScrollPane(textA);
		panelLUs.add(scrollPanel);
		
		JOptionPane.showMessageDialog(null, panelLUs, "LLISTAT DE USUARIS" + labelBBDD, JOptionPane.INFORMATION_MESSAGE);
	}

}
