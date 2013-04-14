/*Author: Tomasz*/
package pl.edu.agh.cs.kraksimcitydesigner.propertiesdialogs;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import pl.edu.agh.cs.kraksimcitydesigner.element.ElementManager;
import pl.edu.agh.cs.kraksimcitydesigner.traffic.GenerateTraffic;

public class GenerateTrafficDialog extends JDialog implements ActionListener{
	
	private ElementManager elementManager;
	
	private JLabel populationLabel=new JLabel("Insert the algorithm population size:");
	private JTextField populationTextField=new JTextField();
	private JLabel stepsLabel=new JLabel("Insert algorithm steps:");
	private JTextField stepsTextField=new JTextField();
	private JLabel filenameLabel=new JLabel("Insert filename:");
	private JTextField filenameTextField=new JTextField();
	
	private JButton okButton=new JButton("Generate");
	private JButton cancelButton=new JButton("Cancel");
	
	public GenerateTrafficDialog(ElementManager elementManager) {
		setTitle("Generate Traffic");
		
		this.elementManager=elementManager;
		
		
		setResizable(false);
		setSize(270, 200);
		
		populationTextField.setPreferredSize(new Dimension(260, 23));
		stepsTextField.setPreferredSize(new Dimension(260, 23));
		filenameTextField.setPreferredSize(new Dimension(260, 23));
		
		okButton.setPreferredSize(new Dimension(125, 23));
		okButton.addActionListener(this);
		
		cancelButton.setPreferredSize(new Dimension(125, 23));
		cancelButton.addActionListener(this);
		
		setLayout(new FlowLayout());
		add(populationLabel);
		add(populationTextField);
		add(stepsLabel);
		add(stepsTextField);
		add(filenameLabel);
		add(filenameTextField);
		add(okButton);
		add(cancelButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source=e.getSource();
		if(source==okButton){
			try {
				int populationSize=Integer.parseInt(populationTextField.getText());
				GenerateTraffic.algorithmPopulationSize=populationSize;
				
				int steps=Integer.parseInt(stepsTextField.getText());
				GenerateTraffic.algorithmSteps=steps;
				
				String filename=filenameTextField.getText();
				
				GenerateTraffic.Generate(elementManager.getIntersections(),elementManager.getNodes(),filename);
			}
			catch(Exception exc){
				exc.printStackTrace();
			}
			setVisible(false);
		}
		if(source==cancelButton){
			setVisible(false);
		}
		
	}
}
