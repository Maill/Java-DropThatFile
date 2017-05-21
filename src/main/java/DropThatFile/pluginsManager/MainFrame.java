package DropThatFile.pluginsManager;

import DropThatFile.pluginsManager.plugins.IntPlugins;
import DropThatFile.pluginsManager.plugins.PluginsLoader;
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
	//private static final long serialVersionUID = 4932662545205980307L;
	
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
    private JTextArea ihmTextArea;
	
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
        this.ihmTextArea = new JTextArea();
		
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
		this.stringTextArea.setBorder(new LineBorder(Color.black));
		this.stringTextArea.setText("String zone");
		
		//intTextArea
		this.intTextArea.setBorder(new LineBorder(Color.black));
		this.intTextArea.setText("Int zone");

        //ihmTextArea
        this.ihmTextArea.setBorder(new LineBorder(Color.black));
        this.ihmTextArea.setText("Interface zone");
		
		//this
		this.setSize(800,600);
		this.setJMenuBar(this.menuBar);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(2,1));
		this.getContentPane().add(this.stringTextArea);
		this.getContentPane().add(this.intTextArea);
        this.getContentPane().add(this.ihmTextArea);
	}

	/*public static void main(String[] args) {
		new MainFrame().setVisible(true);
	}*/
	
	
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == this.exitMenuItem ){
			this.setVisible(false);
		}
		else {
			if( arg0.getSource() == this.loadMenuItem ){
				JFileChooser f = new JFileChooser();
				
				if(f.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
					this.files.add(f.getSelectedFile().getAbsolutePath());
				}
			}
			else {
				if( this.runPluginsMenuItem == arg0.getSource() ){
					this.pluginsLoader.setFiles(this.convertArrayListToArrayString(this.files));
					
					try {
						this.fillStringPlugins(this.pluginsLoader.loadAllStringPlugins());
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				}
				else {
					this.ActionFromPlugins(arg0);
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
		JMenuItem menuItem ;
	
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
	
	private void ActionFromPlugins(ActionEvent e){
		for(int index = 0 ; index < this.stringPlugins.size(); index ++ )
		{
			if(e.getActionCommand().equals( ((StringPlugins)this.stringPlugins.get(index)).getLibelle() )){
				this.stringTextArea.setText(((StringPlugins)this.stringPlugins.get(index)).actionOnString(this.stringTextArea.getText()));
				return;
			}
		}
		
		for(int index = 0 ; index < this.intPlugins.size(); index ++ ){
			if(e.getActionCommand().equals( ((IntPlugins)this.intPlugins.get(index)).getLibelle() )){
				int res = ((IntPlugins)this.intPlugins.get(index)).actionOnInt( Integer.parseInt(this.stringTextArea.getText()) );
				this.stringTextArea.setText( new Integer(res).toString() );
				
				return;
			}
		}
	}
}

