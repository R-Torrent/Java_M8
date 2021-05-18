package M8_Milestone3.api;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import M8_Milestone3.application.Controller;

import M8_Milestone3.utilities.BoxField;

public class MainMenu {
	
	private Controller m8;
	
	private JPanel panel, panelButtons;
	private JButton button1, button2, button3, button4, button5;
	private JFrame frame;
	private Box boxDB;
	private List<JRadioButton> dataBaseListJRB;
	
	public MainMenu() {
		m8 = new Controller();
	}
	
	public void start() { // Menú principal - usuarios
		panel = new JPanel(new GridLayout(0, 2));
		
		boxDB = Box.createVerticalBox();
		
		dataBaseListJRB = new ArrayList<>();
		final ButtonGroup opDBGroup = new ButtonGroup();
		for(String dB : Controller.BBDDs) {
			JRadioButton opDB = new JRadioButton(dB);
			dataBaseListJRB.add(opDB);
			opDBGroup.add(opDB);
			boxDB.add(opDB);
		}
		
		final Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		final TitledBorder titleDB = BorderFactory.createTitledBorder(loweredetched, "BASE DE DADES", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
		boxDB.setBorder(titleDB);
		
		panel.add(boxDB);
		
		panelButtons = new JPanel();
		panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.Y_AXIS));
		
		button1 = new BoxField(panelButtons, "Obrir compte");
		button2 = new BoxField(panelButtons, "Compte nou");
		button3 = new BoxField(panelButtons, "Llistat de comptes");
		button4 = new BoxField(panelButtons, "Esborrar compte");
		button5 = new BoxField(panelButtons, "Sortida");
		
		button1.setEnabled(false);
		button2.setEnabled(false);
		button3.setEnabled(false);
		button4.setEnabled(false);
		
		panel.add(panelButtons);
		
		frame = new JFrame("MÒDUL 8 - MILESTONE 3");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		Enumeration<AbstractButton> buttons = opDBGroup.getElements(); // Activació del botons del menú
		while(buttons.hasMoreElements())
			buttons.nextElement().addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					button1.setEnabled(true);
					button2.setEnabled(true);
					button3.setEnabled(true);
					button4.setEnabled(true);
					
					m8.setRepository(e.getActionCommand()); // Elecció de la base de dades
				}				
				
			});
		
		button1.addActionListener(new ActionListener() { // Obrir usuari
			
			@Override
			public void actionPerformed(ActionEvent e) {
				m8.contU.openUsuari();
			}
			
		});
		
		button2.addActionListener(new ActionListener() { // Nou usuari
			
			@Override
			public void actionPerformed(ActionEvent e) {
				m8.contU.createUsuari();
			}
			
		});
		
		button3.addActionListener(new ActionListener() { // Llistat de usuaris
			
			@Override
			public void actionPerformed(ActionEvent e) {
				m8.contU.printUsuaris();
			}
			
		});
		
		button4.addActionListener(new ActionListener() { // Esborrar usuari
			
			@Override
			public void actionPerformed(ActionEvent e) {
				m8.contU.deleteUsuari();
			}
			
		});
		
		button5.addActionListener(new ActionListener() { // Sortir
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Adéu!");
				System.exit(0);
			}
			
		});
	}

}
