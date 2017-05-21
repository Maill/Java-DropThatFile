package DropThatFile.pluginsManager;

import DropThatFile.pluginsManager.plugins.IhmPlugins;
import DropThatFile.pluginsManager.plugins.PluginsLoader;
import DropThatFile.pluginsManager.plugins.IntPlugins;
import DropThatFile.pluginsManager.plugins.StringPlugins;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

public class MainFrame extends JFrame implements ActionListener{
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu stringPluginsMenu;
	private JMenu intPluginsMenu;
	private JMenu ihmPluginsMenu;
	
	private JMenuItem exitMenuItem;
	private JMenuItem loadMenuItem;
	private JMenuItem runPluginsMenuItem;
	private JTextArea stringTextArea;
	private JTextArea intTextArea;
	
	private PluginsLoader pluginsLoader;
	private ArrayList files;
	private ArrayList stringPlugins;
	private ArrayList intPlugins;
    private ArrayList ihmPlugins;
	
	public MainFrame(){
		this.pluginsLoader = new PluginsLoader();
		this.files = new ArrayList();
		this.stringPlugins = new ArrayList();
		this.intPlugins = new ArrayList();
        this.ihmPlugins = new ArrayList();
		
		this.initialize();
	}
	
	private void initialize(){
		this.menuBar = new JMenuBar();
		this.fileMenu = new JMenu();
		this.stringPluginsMenu = new JMenu();
		this.intPluginsMenu = new JMenu();
        this.ihmPluginsMenu = new JMenu();
		this.exitMenuItem = new JMenuItem();
		this.loadMenuItem = new JMenuItem();
		this.runPluginsMenuItem = new JMenuItem();
		this.stringTextArea = new JTextArea();
		this.intTextArea = new JTextArea();
		
		//menuBar
		this.menuBar.add(this.fileMenu);
		this.menuBar.add(this.stringPluginsMenu);
		this.menuBar.add(this.intPluginsMenu);
        this.menuBar.add(this.ihmPluginsMenu);
		
		//fileMenu
		this.fileMenu.setText("File");
		this.fileMenu.add(this.loadMenuItem);
		this.fileMenu.add(this.runPluginsMenuItem);
		this.fileMenu.addSeparator();
		this.fileMenu.add(this.exitMenuItem);
		
		//stringPluginsMenu
		this.stringPluginsMenu.setText("String manipulation");
		
		//intPluginsMenu
		this.intPluginsMenu.setText("Int manipulation");

        //ihmPluginsMenu
        this.ihmPluginsMenu.setText("Interface manipulation");
		
		//exitMenuItem
		this.exitMenuItem.setText("Close");
		this.exitMenuItem.addActionListener(this);
		
		//loadMenuItem
		this.loadMenuItem.setText("Load a plugin");
		this.loadMenuItem.addActionListener(this);
		
		//runPluginsMenuItem
		this.runPluginsMenuItem.setText("Activate loaded plugins");
		this.runPluginsMenuItem.addActionListener(this);
		
		//stringTextArea
		this.stringTextArea.setBorder(new LineBorder(Color.blue));
		this.stringTextArea.setText("String zone");
		
		//intTextArea
		this.intTextArea.setBorder(new LineBorder(Color.blue));
		this.intTextArea.setText("Int zone");
		
		//this
		this.setSize(800,600);
		this.setJMenuBar(this.menuBar);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLayout(new GridLayout(2,1));
		this.getContentPane().add(this.stringTextArea);
		this.getContentPane().add(this.intTextArea);
	}
	
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == this.exitMenuItem ){
			this.setVisible(false);
		} else {
			if( event.getSource() == this.loadMenuItem ){
				JFileChooser f = new JFileChooser();
				
				if(f.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
					this.files.add(f.getSelectedFile().getAbsolutePath());
				}
			} else {
				if( this.runPluginsMenuItem == event.getSource() ){
					this.pluginsLoader.setFiles(this.convertArrayListToArrayString(this.files));
					
					try {
						this.fillStringPlugins(this.pluginsLoader.loadAllStringPlugins());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else {
					this.ActionFromPlugins(event);
				}
			}
		}
	}
	
	private String[] convertArrayListToArrayString(ArrayList list){
		String[] tmp = new String[list.size()];
		
		for(int index = 0 ; index < tmp.length ; index++ ){
			tmp[index] = (String)list.get(index);
		}
		return tmp;
	}

	private void fillStringPlugins(StringPlugins[] plugins){
		JMenuItem menuItem;
	
		for(int index = 0 ; index < plugins.length; index++ ){
			this.stringPlugins.add(plugins[index]);
			
			menuItem = new JMenuItem();
			menuItem.setText(plugins[index].getLibelle() );
			menuItem.addActionListener(this);
			//Add into the JMenuItem collection for click detection of this.stringPluginsMenuItem.add(menuItem);
			//Add into the menu
			this.stringPluginsMenu.add(menuItem);
		}
	}
	
	private void ActionFromPlugins(ActionEvent event){
		for(int index = 0 ; index < this.stringPlugins.size(); index++) {
			if(event.getActionCommand().equals(((StringPlugins)this.stringPlugins.get(index)).getLibelle())){
				this.stringTextArea.setText(((StringPlugins)this.stringPlugins.get(index)).actionOnString(this.stringTextArea.getText()));
				return;
			}
		}
		
		for(int index = 0 ; index < this.intPlugins.size(); index++) {
			if(event.getActionCommand().equals(((IntPlugins)this.intPlugins.get(index)).getLibelle())){
				int res = ((IntPlugins)this.intPlugins.get(index)).actionOnInt(Integer.parseInt(this.stringTextArea.getText()));
				this.stringTextArea.setText( new Integer(res).toString() );
				return;
			}
		}

		for(int index = 0 ; index < this.ihmPlugins.size(); index++) {
			if(event.getActionCommand().equals(((IhmPlugins)this.ihmPlugins.get(index)).getLibelle())){
				int res = ((IhmPlugins)this.ihmPlugins.get(index)).actionOnIhm(this.ihmPluginsMenu);
				this.stringTextArea.setText( new Integer(res).toString() );
				return;
			}
		}
	}
}

