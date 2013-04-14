package pl.edu.agh.cs.kraksimcitydesigner.propertiesdialogs;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//import com.sun.org.apache.xerces.internal.impl.RevalidationHandler;

import pl.edu.agh.cs.kraksimcitydesigner.MainFrame;
import pl.edu.agh.cs.kraksimcitydesigner.element.Intersection;
import pl.edu.agh.cs.kraksimcitydesigner.element.Link;
import pl.edu.agh.cs.kraksimcitydesigner.element.Node;
import pl.edu.agh.cs.kraksimcitydesigner.element.Intersection.IncomingLane;
import pl.edu.agh.cs.kraksimcitydesigner.element.Intersection.LightState;
import pl.edu.agh.cs.kraksimcitydesigner.element.Intersection.Phase;
import pl.edu.agh.cs.kraksimcitydesigner.element.Intersection.TrafficLightsSchedule;
import pl.edu.agh.cs.kraksimcitydesigner.traffic.TrafficFileParser;
//import sun.util.logging.resources.logging;

/**
 * Tutaj zostal wpiety kot wyswietlajacy dialogi do konfiguracji klas traffic
 * @author Pawel Pierzchala
 *
 */
public class IntersectionPropertiesDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private MainFrame mf;
    private Intersection copiedIntersection;
    private JTextField idTextField;
    private JLabel xLabel;
    private JLabel yLabel;
    private final ActionPropertiesDialog actionPropertiesDialog;
    private JScrollPane scrollPane;
    private JPanel mainPane;
    
    /**
     * Light States that should be changed when user click "Save"
     */
    private Map<Intersection.Phase, Map<Intersection.IncomingLane,Intersection.LightState>> newLightsStates = new HashMap<Intersection.Phase, Map<Intersection.IncomingLane,Intersection.LightState>>();

    private Intersection originalIntersection;
    
    /**
     * Instantiates a new intersection properties dialog.
     * 
     * @param owner the owner
     */
    public IntersectionPropertiesDialog(MainFrame owner) {
        
        super(owner,true);
        mf = owner;
        idTextField = new JTextField();
        xLabel = new JLabel();
        yLabel = new JLabel();
        actionPropertiesDialog = new ActionPropertiesDialog(this);
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Intersection properties");
        //this.setSize(new Dimension(400,600));
    }
    
    /**
     * Inits the.
     */
    public void init() {
        this.getContentPane().removeAll();
        
        this.idTextField.setText(copiedIntersection.getId());
        this.xLabel.setText(String.valueOf((int)copiedIntersection.getX()));
        this.yLabel.setText(String.valueOf((int)copiedIntersection.getY()));
        
        Container verticalLayout = Box.createVerticalBox(); 
        verticalLayout.add(Box.createRigidArea(new Dimension(0,10)));
   
        JPanel properitesPanel = new JPanel();     
        properitesPanel.setLayout(new GridBagLayout());
        properitesPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        GridBagConstraints c = new GridBagConstraints();
        
        int row = 0;
        
        c.gridx = 0;
        c.gridy = 0;   
        c.ipadx = 20;
        c.ipady = 10;    
        properitesPanel.add(new JLabel("Id:"),c);
       
        c.gridx = 1;
        c.gridy = row;   
        idTextField.setColumns(5);
        properitesPanel.add(idTextField,c);
        
        row++;
        
        c.gridx = 0;
        c.gridy = row;     
        properitesPanel.add(new JLabel("x:"),c);
        
        c.gridx = 1;
        c.gridy = row; 
        //c.fill = GridBagConstraints.VERTICAL;
        properitesPanel.add(xLabel,c);   
  
        row++;
        
        c.gridx = 0;
        c.gridy = row;     
        properitesPanel.add(new JLabel("y:"),c);

        c.gridx = 1;
        c.gridy = row;         
        properitesPanel.add(yLabel,c);
               
        JPanel buttonsPanel = new JPanel();
        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                originalIntersection.copyIntersectionState(copiedIntersection);
                originalIntersection.setId(idTextField.getText());
                mf.getEditorPanel().repaint();
                mf.setProjectChanged(true);
                setVisible(false);
            }
        });
        buttonsPanel.add(saveBtn);
        JButton cancelBtn = new JButton("Discard changes");
        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonsPanel.add(cancelBtn);
        
        JPanel armActionsPanel = createArmActionsPanel();
        
        JButton createPhaseButton = new JButton("Create lights phase");
        createPhaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TrafficLightsSchedule trafficLightsSchedule = copiedIntersection.getTrafficLightsSchedule();
                trafficLightsSchedule.addAllGreenPhase(trafficLightsSchedule.getPhases().size()+1);
                refresh();
            }
        });
        
        JPanel trafficLightsSchedulePanel = createTrafficLightsSchedulePanel();
        JPanel trafficSourcePanel = createTrafficSourcePanel();
        
        Container propertiesHorizontalContainer = Box.createHorizontalBox();
        //propertiesHorizontalContainer.add(new JLabel("xxxx"));
        propertiesHorizontalContainer.add(properitesPanel);
        //propertiesHorizontalContainer.add(Box.createHorizontalGlue());
        //propertiesHorizontalContainer.add(new JLabel("xxxx"));
              
        //verticalLayout.add(properitesPanel);
        verticalLayout.add(propertiesHorizontalContainer);
        verticalLayout.add(trafficSourcePanel);
        verticalLayout.add(armActionsPanel);
        verticalLayout.add(createPhaseButton);
        verticalLayout.add(trafficLightsSchedulePanel);
        verticalLayout.add(buttonsPanel);
        
        Container horizontalOuterLayout = Box.createHorizontalBox();
        horizontalOuterLayout.add(Box.createRigidArea(new Dimension(5,0)));
        horizontalOuterLayout.add(verticalLayout);
        horizontalOuterLayout.add(Box.createRigidArea(new Dimension(5,0)));
        
        mainPane = new JPanel();
        mainPane.add(horizontalOuterLayout);
        
        scrollPane = new JScrollPane(mainPane);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        this.add(scrollPane);
        this.setPreferredSize(new Dimension(430,700));
        
        this.pack();
        
    }
    
    /**
     * Creates the traffic lights schedule panel.
     * 
     * @return the j panel
     */
    private JPanel createTrafficLightsSchedulePanel() {
        JPanel resultPanel = new JPanel();
        Container resultVerticalContainer = Box.createVerticalBox();
        
        final Map<Integer,Intersection.Phase> phases = copiedIntersection.getTrafficLightsSchedule().getPhases();
        
        for ( final Integer phaseNum : phases.keySet()) {

            final Intersection.Phase phase = phases.get(phaseNum);
            
            JPanel phasePanel = new JPanel();
            Box phasePanelVerticalContainer = Box.createVerticalBox();
            phasePanel.setBorder(BorderFactory.createTitledBorder("Phase num = "+phase.getNum()+", name = "+phase.getName()+", duration = "+phase.getDuration()));
            
            Container phaseSettingsHorizontalContainer = Box.createHorizontalBox();
            final JTextArea durationTextArea = new JTextArea(""+phase.getDuration());
            durationTextArea.setPreferredSize(new Dimension(100,15));
            final JTextArea nameTextArea = new JTextArea(phase.getName());
            nameTextArea.setPreferredSize(new Dimension(100,15));
                      
            phaseSettingsHorizontalContainer.add(new JLabel("name:   "));
            phaseSettingsHorizontalContainer.add(nameTextArea);
            phaseSettingsHorizontalContainer.add(new JLabel("   duration:   "));
            phaseSettingsHorizontalContainer.add(durationTextArea);
            
            JPanel inlaneLightsStatesPanel = createInlaneLightsStatesPanel(phase);
            
            JButton deletePhaseButton = new JButton("Delete light phase");
            deletePhaseButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    assert phases.remove(phaseNum) != null;
                    for (int i=phaseNum+1; i <= phases.size()+1; i++) {
                        Phase toMovePhase = phases.remove(i);
                        toMovePhase.setNum(i-1);
                        phases.put(i-1, toMovePhase);
                    }
                    refresh();
                }
            });
            JButton saveChangeButton = new JButton("Save changes");
            saveChangeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    phase.setName(nameTextArea.getText());
                    phase.setDuration(Integer.parseInt(durationTextArea.getText()));
                }
            });
            Container horizontalButtonsContainer = Box.createHorizontalBox();
            horizontalButtonsContainer.add(saveChangeButton);
            horizontalButtonsContainer.add(deletePhaseButton);
            
            phasePanelVerticalContainer.add(phaseSettingsHorizontalContainer);
            phasePanelVerticalContainer.add(inlaneLightsStatesPanel);
            phasePanelVerticalContainer.add(horizontalButtonsContainer);
            
            phasePanel.add(phasePanelVerticalContainer);
            //phasePanel.add(inlaneLightsStatesPanel);
            
            resultVerticalContainer.add(phasePanel);
        }
        
        resultPanel.add(resultVerticalContainer);
        return resultPanel;
    }
    
    /**
     * Creates the inlane lights states panel.
     * 
     * @param aPhase the a phase
     * 
     * @return the j panel
     */
    private JPanel createInlaneLightsStatesPanel(final Intersection.Phase aPhase) {
        
        Container verticalMainBox = Box.createVerticalBox();
        
        /**
         * Will be updated by anonymous inner class, I hope
         */
        final Map<Intersection.IncomingLane,Intersection.LightState> phaseUpdatedLightsStates = new HashMap<IncomingLane, LightState>();
        newLightsStates.put(aPhase, phaseUpdatedLightsStates);
        
        Map<Intersection.IncomingLane,Intersection.LightState> lightsStates = aPhase.getLightsStates();
        LinkedList<Intersection.IncomingLane> lightsStatesSortedKeys = new LinkedList<Intersection.IncomingLane>(lightsStates.keySet());
        Collections.sort(lightsStatesSortedKeys, new Comparator<Intersection.IncomingLane>() {
            public int compare(IncomingLane o1, IncomingLane o2) {
                int compare = o1.getFromNode().getId().compareTo(o2.getFromNode().getId());
                if (compare == 0) {
                    compare = (new Integer(o1.getLaneNum())).compareTo(new Integer(o2.getLaneNum()));
                }
                return compare;
            }
        });
        for (final Intersection.IncomingLane incomingLane : lightsStatesSortedKeys) {
            
            final Intersection.LightState lightState = lightsStates.get(incomingLane);
            
            Container horizontalLightState = Box.createHorizontalBox();
            
            String lightStateStr = "inlane arm = "+incomingLane.getFromNode().getId()+" lane = "+incomingLane.getLaneNum()+"  ";
            JLabel lightStateStrPanel = new JLabel(lightStateStr);
            lightStateStrPanel.setPreferredSize(new Dimension(280,10));
            
            String[] lightStates = new String[2];
            if (lightState == Intersection.LightState.GREEN) {
                lightStates[0] = "green";
                lightStates[1] = "red";
            } else {
                lightStates[0] = "red";
                lightStates[1] = "green";
            }
            
            final JComboBox lightStateComboBox = new JComboBox(lightStates);
            lightStateComboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JComboBox cb = (JComboBox)e.getSource();
                    LightState newState = LightState.valueOf(cb.getSelectedItem().toString().toUpperCase());
                    System.err.println("Uaktualniam wartoscia "+newState.toString());
                    if (! (lightState == newState)) {
                        aPhase.updateIncomingLane(incomingLane, newState);
                    }
                }
            });
           
            horizontalLightState.add(lightStateStrPanel);
            horizontalLightState.add(lightStateComboBox);
            
            verticalMainBox.add(horizontalLightState);
        }
        JPanel result = new JPanel();
        result.add(verticalMainBox);
        return result;
    }
    
    private JPanel createTrafficDirectionsPanel() {
    	JPanel resultPanel = new JPanel();
        
        Container resultVerticalContainer = Box.createVerticalBox();
        
    	TrafficFileParser source = new TrafficFileParser(new File(Intersection.getTrafficRootDirectory(),copiedIntersection.getTrafficFile()).getAbsolutePath());
    	
    	String[] nodes = new String[copiedIntersection.getOrderedReachableNodes().size() + 1];
    	for(int i = 1; i < nodes.length; i++)
    		nodes[i] = copiedIntersection.getOrderedReachableNodes().get(i - 1).getId();
    	nodes[0] = "none";
    	
		for(final String street : source.getStreets() ) {
    		Container resultHorizontalContainer = Box.createHorizontalBox();
    		
    		resultHorizontalContainer.add(new JLabel(street));
    		final JComboBox comboBox = new JComboBox(nodes);
    		if (copiedIntersection.getDirection(street) == null)
    			comboBox.setSelectedIndex(0);
    		else
    			comboBox.setSelectedItem(copiedIntersection.getDirection(street));
    		
    		comboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	if (comboBox.getSelectedIndex() != 0)
                		copiedIntersection.setDirection(street, (String)comboBox.getSelectedItem());
                	else
                		copiedIntersection.removeDirection(street);
                }
            });
    		
    		resultHorizontalContainer.add(comboBox);
    		resultVerticalContainer.add(resultHorizontalContainer);
    	}

    	resultPanel.add(resultVerticalContainer);
    	
    	return resultPanel;
    }
    
    private JPanel createTrafficFilePanel() {
    	JPanel result = new JPanel();
    	Container resultVerticalContainer = Box.createVerticalBox();
        
    	if (Intersection.getTrafficRootDirectory() == null)
    		resultVerticalContainer.add(new JLabel("Set root directory first."));
    	else {
    		File directory = new File(Intersection.getTrafficRootDirectory());
    		String[] files = directory.list();
    		String[] options = new String[files.length + 1];
    		System.arraycopy(files, 0, options, 1, files.length);
    		options[0] = "none";
    		final JComboBox comboBox = new JComboBox(options);
    		comboBox.setSelectedItem(copiedIntersection.getTrafficFile());
    		
    		comboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	if (copiedIntersection.getTrafficFile() != null && !copiedIntersection.getTrafficFile().equals((String)comboBox.getSelectedItem()))
                		copiedIntersection.removeDirections();
                    copiedIntersection.setTrafficFile((String)comboBox.getSelectedItem());
                    updateDireciontsPanel();    
                }
            });
    		
    		resultVerticalContainer.add(comboBox);
    	}
    	
    	result.add(resultVerticalContainer);
    	return result;
    }
    
    private Container trafficDirectionsContainer;
    private void updateDireciontsPanel() {
    	trafficDirectionsContainer.removeAll();
    	if (Intersection.getTrafficRootDirectory() != null &&
    	    Intersection.getTrafficRootDirectory() != "" &&
    	    copiedIntersection.getTrafficFile() != null &&
    	    copiedIntersection.getTrafficFile() != "" &&
    	    copiedIntersection.getTrafficFile() != "none") {
    		try {
    			JPanel directionsPanel = createTrafficDirectionsPanel();
    			trafficDirectionsContainer.add(directionsPanel);
    		} catch(Exception fnfe) {
    			fnfe.printStackTrace();
    		}
    	}
    	this.pack();
    }
    
    private JPanel createTrafficSourcePanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setBorder(BorderFactory.createTitledBorder("TrafficSource"));
        
        Container resultVerticalContainer = Box.createVerticalBox();
        resultVerticalContainer.add(createTrafficFilePanel());
        resultPanel.add(resultVerticalContainer);
        
        trafficDirectionsContainer = Box.createVerticalBox();
        updateDireciontsPanel();
        resultVerticalContainer.add(trafficDirectionsContainer);
        
        return resultPanel;
    	
    }
    /**
     * Creates the arm actions panel.
     * 
     * @return the j panel
     */
    private JPanel createArmActionsPanel() {
        
        JPanel resultPanel = new JPanel();
        Container resultVerticalContainer = Box.createVerticalBox();
        
        JPanel armActionsPanel;
        Container verticalContainer;

        for (final Intersection.ArmActions armActions : copiedIntersection.getArmActionsList()) {
            
            armActionsPanel = new JPanel();
            verticalContainer = Box.createVerticalBox();
            armActionsPanel.setBorder(BorderFactory.createTitledBorder("Arm = "+armActions.getArm().getId()+", dir = "+armActions.getDir()));
            
            for (final Intersection.Action action : armActions.getActions()) {
                JLabel actionLabel = new JLabel("entrance: "+action.getExitNode().getId()+", line: "+action.getLineNum()+" ("+action.getPrivileged().size()+")");
                actionLabel.setPreferredSize(new Dimension(220,15));
                
                JButton deleteActionButton = new JButton("delete");
                
                deleteActionButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        final TrafficLightsSchedule trafficLightsSchedule = copiedIntersection.getTrafficLightsSchedule();
                        
                        armActions.delete(action);
                        trafficLightsSchedule.tryToDeleteUnusedLightStates(action.getArmActions().getArm(),action.getLineNum());
                        init();
                    }
                });
                JButton editActionButton = new JButton("edit");
                
                editActionButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        actionPropertiesDialog.setAction(action);
                        actionPropertiesDialog.init();
                        actionPropertiesDialog.showCentered();
                    }
                });
                
                Container horizontalContainer = Box.createHorizontalBox();
                horizontalContainer.add(actionLabel);
                horizontalContainer.add(Box.createHorizontalGlue());
                horizontalContainer.add(editActionButton);
                horizontalContainer.add(deleteActionButton);
                
                verticalContainer.add(horizontalContainer);
            }
            JButton addActionButton = new JButton("Add action");
            final Link incomingLink = copiedIntersection.findIncomingLinkBySourceNode(armActions.getArm());
            addActionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AddingActionDialog addingActionDialog = new AddingActionDialog(IntersectionPropertiesDialog.this,armActions,incomingLink,copiedIntersection.getOutcomingLinks());
                    addingActionDialog.init();
                    addingActionDialog.showCentered();
                }
            });
            verticalContainer.add(addActionButton);
            
            armActionsPanel.add(verticalContainer);
            resultVerticalContainer.add(armActionsPanel);
        }
        
        for (Link incomingLink : copiedIntersection.getIncomingLinks()) {
            
            assert (incomingLink.getStartNode() == originalIntersection
                    || incomingLink.getEndNode() == originalIntersection);
            
            final Node connectedNode = (incomingLink.getStartNode() == originalIntersection) ? incomingLink.getEndNode() : incomingLink.getStartNode();           
            
            if (! copiedIntersection.hasArmTo(connectedNode)) {
                armActionsPanel = new JPanel();
                
                armActionsPanel.setBorder(BorderFactory.createTitledBorder("Arm = "+connectedNode.getId()));
                verticalContainer = Box.createVerticalBox();
                
                JButton createArmActionsButton = new JButton("Create armsActions");
                createArmActionsButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        copiedIntersection.addArmActions(connectedNode, "NS");
                        refresh();
                    }
                });
                
                verticalContainer.add(createArmActionsButton);
                
                
                armActionsPanel.add(verticalContainer);
                resultVerticalContainer.add(armActionsPanel);
            }
        }
        
        resultPanel.add(resultVerticalContainer);
        return resultPanel;
    }
    
    /**
     * Sets the intersection.
     * 
     * @param intersection the new intersection
     */
    public void setIntersection(Intersection intersection) {
        this.copiedIntersection = new Intersection(intersection);
        this.originalIntersection = intersection;
    }
    
    /**
     * Sets the position.
     * 
     * @param x the x
     * @param y the y
     */
    public void setPosition(int x, int y) {
        Point topLeft = getOwner().getLocationOnScreen();
        Point position = new Point((int)topLeft.getX()+x+130,(int)topLeft.getY()+y+70);
        setLocation(position);
    }

    /**
     * Scroll to begining.
     */
    public void scrollToBegining() {
        mainPane.scrollRectToVisible(new Rectangle(10,10,10,10));
        mainPane.revalidate();
        repaint();
        //scrollPane.getViewport().setViewPosition(new Point(30,30));
        //scrollPane.getVerticalScrollBar().setValue(0);
        //scrollPane.getHorizontalScrollBar().setValue(0);
    }
    
    /**
     * Refresh.
     */
    public void refresh() {
        init();
    }

}
