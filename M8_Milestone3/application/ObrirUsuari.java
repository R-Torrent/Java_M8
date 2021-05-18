package M8_Milestone3.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import M8_Milestone3.domain.Usuari;

import M8_Milestone3.persistence.IRepository_DAO;

import M8_Milestone3.utilities.HintTextField;

public class ObrirUsuari {
	
	private IRepository_DAO repository;
	
	private JPanel panelOU;
	private Box boxCg;
	private HintTextField fieldCg;
	private JButton buttonCg, buttonOK;
	private DefaultListModel<Usuari> listModel;
	private JList<Usuari> jList;
	private JScrollPane scrollPane;
	private JOptionPane pane;
	private JDialog dialog;
	private Object selectedValue;
	
	public ObrirUsuari(IRepository_DAO repository) throws Exception {
		if(repository.size() == 0) throw new Exception("User database empty.");
		
		this.repository = repository;
	}
	
	public Usuari inici() {
		panelOU = new JPanel();
		panelOU.setLayout(new BoxLayout(panelOU, BoxLayout.Y_AXIS));
		
		panelOU.add(Box.createHorizontalStrut(250));
		
		boxCg = Box.createHorizontalBox();
		fieldCg = new HintTextField("< cognom -- complet o parcial >");
		buttonCg = new JButton("Cerca");
		boxCg.add(fieldCg);
		boxCg.add(buttonCg);
		panelOU.add(boxCg);
		
		listModel = new DefaultListModel<>();
	    listModel.ensureCapacity(repository.size());
		
		buttonCg.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				listModel.clear();
				for(Usuari u : repository.findUsuaris(fieldCg.getText()))
					listModel.addElement(u);
			}
			
		});
		buttonCg.doClick(); // Carga la lista de usuarios completa: repository.findUsuari(null)
		
		jList = new JList<>(listModel);
		scrollPane = new JScrollPane(jList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    panelOU.add(scrollPane);
	    
	    pane = new JOptionPane(panelOU, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
	    
		dialog = pane.createDialog("SELECCIONAR COMPTE DE L'USUARI");
		
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

}
