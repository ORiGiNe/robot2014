package simulator;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simulator.ArduinoServeur.FullPinState;
import drivers.base.IoMode;

/**
 * Classe d'affichage de l'état d'un FullPinState
 * @author Jeremy
 *
 */
public class PinWidget {
	
	protected FullPinState pinState;
	protected JPanel drawPanel;
	
	protected JLabel outputDisplay;
	
	protected boolean guiCreated;
	
	/**
	 * Créé un pinWidget et l'ajoute dans un gridBagLayout<br />
	 * Utilise 3 colonnes
	 * @param pinState le pin state à monitorer
	 * @param content le widget container parent, possédant un gridBagLayout
	 * @param row la ligne d'affichage de ce widget
	 * @param col la colonne de début d'affichage (en occupera 3)
	 */
	public PinWidget(FullPinState pinState, Container content, int row, int col) {
		this.pinState = pinState;
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.VERTICAL;
		c.gridy = row;
		c.insets = new Insets(5,5,5,5);

		String label = "Pin "+this.pinState.getId();
		c.gridx = col + 0;
		c.weightx = 0;
		content.add(new JLabel(label), c);
		
		label = "";
		if(this.pinState.isAnalogicReadable()) {
			label += " I~";
		}
		if(this.pinState.isAnalogicWritable()) {
			label += " O~";
		}
		c.gridx = col + 1;
		c.weightx = 0;
		content.add(new JLabel(label), c);
		
		
		drawPanel = new JPanel();
		c.gridx = col + 2;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		content.add(drawPanel, c);
		guiCreated = false;
	}
	
	/**
	 * Met à jour l'affichage
	 */
	protected void update() {
		if(this.pinState.getMode()!=null) {
			if(this.pinState.getMode()==IoMode.OUTPUT) {
				//Création du gui si il n'existe pas encore
				if(!guiCreated) {
					this.drawPanel.setLayout(new BorderLayout());
					outputDisplay = new JLabel();
					drawPanel.add(outputDisplay);
					guiCreated = true;
				}
				//Mise à jour du texte
				outputDisplay.setText("Output : "+this.pinState.getValue());
			} else {
				//En Input : création du gui de changement de la valeur
				
				if(!guiCreated) {
					guiCreated = true;
					this.drawPanel.setLayout(new BoxLayout(drawPanel, BoxLayout.LINE_AXIS));
					
					if(this.pinState.isAnalogicReadable()) {
						//slider
						
						final JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 255, this.pinState.getValue());
						slider.setMajorTickSpacing(50);
						slider.setMinorTickSpacing(10);
						slider.setPaintTicks(true);
						slider.setPaintLabels(true);
						slider.addChangeListener(new ChangeListener() {
							@Override
							public void stateChanged(ChangeEvent e) {
								pinState.changeValue(slider.getValue());
							}
						});
						drawPanel.add(slider);
						
					} else {
						//radio buttons
						
						JRadioButton lowButton = new JRadioButton("LOW");
						lowButton.setSelected(this.pinState.getValue()==0);
						lowButton.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								pinState.changeValue(0);
							}
						});
						JRadioButton highButton = new JRadioButton("HIGH");
						highButton.setSelected(this.pinState.getValue()!=0);
						highButton.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								pinState.changeValue(255);
							}
						});
						
						ButtonGroup group = new ButtonGroup();
						group.add(lowButton);
						group.add(highButton);
						
						drawPanel.add(lowButton);
						drawPanel.add(new JLabel(" / "));
						drawPanel.add(highButton);
					}
				}
			}
		}
	}
}
