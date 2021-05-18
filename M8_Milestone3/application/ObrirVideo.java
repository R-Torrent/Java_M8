package M8_Milestone3.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Set;
import java.util.TreeSet;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import M8_Milestone3.domain.Video;

import M8_Milestone3.utilities.HintTextField;

public class ObrirVideo implements ActionListener {
	
	private Set<Video> setV;
	
	private JPanel panelOV;
	private Box boxTi;
	private HintTextField fieldTi;
	private JButton buttonTi, buttonOK;
	private DefaultListModel<Video> listModel;
	private JList<Video> jList;
	private JScrollPane scrollPane;
	private JOptionPane pane;
	private JDialog dialog;
	private Object selectedValue;

	public ObrirVideo(Set<Video> setV) throws Exception {
		if(setV.size() == 0) throw new Exception("Video list empty.");
		
		this.setV = setV;
		
		setV.forEach(v -> v.addActionListener(this)); // Permite que el 'UploadStatus' se pueda actualizar
	}
	
	@Override
	public void actionPerformed(ActionEvent e) { // Actualización en directo
		panelOV.repaint();
	}
	
	public Video inici() {
		panelOV = new JPanel();
		panelOV.setLayout(new BoxLayout(panelOV, BoxLayout.Y_AXIS));
		
		panelOV.add(Box.createHorizontalStrut(250));
		
		boxTi = Box.createHorizontalBox();
		fieldTi = new HintTextField("< títol -- complet o parcial >");
		buttonTi = new JButton("Cerca");
		boxTi.add(fieldTi);
		boxTi.add(buttonTi);
		panelOV.add(boxTi);
		
		listModel = new DefaultListModel<>();
	    listModel.ensureCapacity(setV.size());
		
		buttonTi.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				listModel.clear();
				for(Video v : findVideo(fieldTi.getText()))
					listModel.addElement(v);
			}
			
		});
		buttonTi.doClick(); // Carga la lista de vídeos completa: findVideo(null)
		
		jList = new JList<>(listModel);
		scrollPane = new JScrollPane(jList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    panelOV.add(scrollPane);
	    
	    pane = new JOptionPane(panelOV, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
	    
		dialog = pane.createDialog("SELECCIONAR VÍDEO DE L'USUARI");
		
		buttonOK = pane.getRootPane().getDefaultButton();
		buttonOK.setEnabled(false);
		
		jList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				buttonOK.setEnabled(!e.getValueIsAdjusting() && jList.getSelectedValue() != null);
			}
			
		});
		
		dialog.setVisible(true);
		
		selectedValue = pane.getValue();
		if(selectedValue == null || ((Integer)selectedValue).intValue() != JOptionPane.OK_OPTION)
			return null;
	    
		return jList.getSelectedValue();
	}
	
	private Set<Video> findVideo(String titolParcial) {
		Set<Video> trobats = new TreeSet<>(setV); 
		if(titolParcial == null || titolParcial.isBlank()) return trobats;
		
		trobats.removeIf(v -> !v.getTitol().toUpperCase().contains(titolParcial.toUpperCase()));
		
		return trobats;
	}
}
