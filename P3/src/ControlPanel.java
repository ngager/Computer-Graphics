/**
 * ControlPanel -- GUI Control panel with GLCanvas inside it
 * 
 * 10/01/13 rdb, derived from RadioSlider lab of CS416
 * ----------------------
 * History
 * 09/25/14: Added scene label support
 * 
 */
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class ControlPanel extends JPanel 
{
    //---------------- class variables ------------------------------  
    private static ControlPanel instance = null;
    
    //--------------- instance variables ----------------------------
    JPanel drawPanel = null; // this will be used for the rendering area
    JLabel sceneLabel = null; // Label the scene with information
    
    //------------------- constructor -------------------------------
     /**
     * return singleton instance of ControlPanel
     */
    public static ControlPanel getInstance()
    {
        if ( instance == null )
            instance = new ControlPanel();
        return instance;
    }
   /**
     * Constructor is private so can implement the Singleton pattern
     */
    private ControlPanel()     
    {
        this.setLayout( new BorderLayout() );
        buildGUI();
    }
    //--------------- addDrawPanel() -----------------------------
    /**
     * add component to draw panel
     */
    public void addDrawPanel( Component drawArea )
    {
        this.add( drawArea, BorderLayout.CENTER );
    }
    //--------------- getDrawPanel() -----------------------------
    /**
     * return reference to the drawing area
     */
    public JPanel getDrawPanel()
    {
        return drawPanel;
    }
    //--------------- setSceneTitle( String ) ----------------------
    /**
     * Update the scene title label.
     */
    public void setSceneTitle( String title )
    {
        sceneLabel.setText( title );
    }
    //--------------- build GUI components --------------------
    /**
     *  Create all the components
     */
    private void buildGUI()
    {        
        // build the button menu
        buildButtons();
    }        
      
    //--------------------- buildButtons ------------------------------------
    /**
     * build a button panel: a Next button and an DrawAxis CheckBox
     */
    private void buildButtons()
    { 
        JPanel bPanel = new JPanel();
        bPanel.setBorder( new LineBorder( Color.BLACK, 2 ));
        
        sceneLabel = new JLabel( "Scene label" );
        bPanel.add( sceneLabel );
        
        JButton nextButton = new JButton( "Next Scene" );
        nextButton.addActionListener(
             new ActionListener()
             {
                  public void actionPerformed( ActionEvent ae ) 
                  { 
                      SceneManager.nextScene(); 
                  } 
             }
         );
        bPanel.add( nextButton );
        
        JCheckBox axisChecker = new JCheckBox( "Show Axis" );
        axisChecker.setSelected( true );
        axisChecker.addActionListener(
             new ActionListener()
             {
                  public void actionPerformed( ActionEvent ae ) 
                  { 
                      SceneManager.setDrawAxes  
                               ( ((JCheckBox) ae.getSource() ).isSelected() ); 
                  } 
             }
         );
        bPanel.add( axisChecker );
        this.add( bPanel, BorderLayout.NORTH );
    }
}
