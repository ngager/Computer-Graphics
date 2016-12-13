/**
 * SceneManager: Manages all the scenes and initiates drawing of current scene.
 *               Implements the Singleton class pattern, but also uses a static
 *               interface to interact with ControlPanel and the Scene class
 * 
 * @author rdb
 * @date 10/17/13
 * ----------------------
 * History
 * 09/25/14: Added scene label support
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import java.awt.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;

public class SceneManager implements GLEventListener
{
    //------------------- class variables ---------------------------
    private static SceneManager me = null;
    private static boolean drawAxesFlag = true;
    private static ArrayList<Scene> allScenes; 
    private static int curSceneIndex = -1;
    private static Scene curScene = null;
    
    private static GLCanvas canvas = null;
    private static ControlPanel  cp = null;
    private JSlider  SliderX, SliderY, SliderZ;
    public JRadioButton translate, rotate, scale;
    public static ButtonGroup group;
    public static Boolean doTranslate;
    public static Boolean doRotate;
    public static Boolean doScale;
    public static float x, y, z;
    //------------------- instance variables ------------------------
    
    // For testing, keep pointers to a bunch of created objects in a vector
    //    also. This makes it easy to just select previously created objects
    //    for multiple scenes, or to copy them and change transformations.
    private ArrayList<Object3D> someObjects;
    
    //------------------ constructor  --------------------------------
    public static SceneManager getInstance()
    {
        if ( me == null )
            me = new SceneManager();
        return me;
    }
    private SceneManager()
    {
        allScenes = new ArrayList<Scene>();
        someObjects = new ArrayList<Object3D>(); 
        cp = ControlPanel.getInstance();

        doTranslate = false;
        doRotate = false;
        doScale = false;
    }
    //----------------- setCanvas -------------------------------------
    /**
     * Main program needs to tell sceneManager about the GL canvas to use
     */
    public void setCanvas( GLCanvas glCanvas )
    {
        canvas = glCanvas;
        Animator a = new Animator(canvas);
        a.start();
    }
    //------------------------- addScene -------------------------------
    /**
     * Add a new scene to the scene collection
     */
    private void addScene( Scene newScene ) 
    {
        allScenes.add( newScene );
        curScene = newScene;
        curSceneIndex = allScenes.size() - 1;
    }
    //------------------------- nextScene -------------------------------
    /**
     * update current scene to next one with wraparound
     * this is a static method to facilitate interaction with ControlPanel
     */
    public static void nextScene() 
    {
        doTranslate = false;
        doRotate = false;
        doScale = false;

        group.clearSelection();


        curSceneIndex++;
        if ( curSceneIndex >= allScenes.size() )
            curSceneIndex = 0;    // wrap around
        curScene = allScenes.get( curSceneIndex );
        cp.setSceneTitle( curScene.getTitle() );
        canvas.repaint();


    }
    //------------------------- setDrawAxes( boolean ) ------------------
    /**
     * set the status of the axes drawing; called by ControlPanel
     */
    public static void setDrawAxes( boolean onoff ) 
    {
        drawAxesFlag = onoff;
        canvas.repaint();
    }
    //------------------------- drawAxes() ------------------
    /**
     * retrieve axes drawing status; called by Scene
     */
    public static boolean drawAxes() 
    {
        return drawAxesFlag;
    }
    //+++++++++++++++  GLEventListener override methods ++++++++++++++++++++
    //-------------------- display -------------------------------------
    /**
     * Override the parent display method
     *    In this framework, the display method is responsible for setting
     *       up the projection specification, but the "render" method
     *       is responsible for the View and Model specifications.
     * 
     *    This display method is reasonably application-independent;
     *      It defines a pattern that can be reused with the exception
     *      of the specifying the actual objects to render.
     */
    @Override
    public void display( GLAutoDrawable drawable )
    {  
        //System.out.println( "Display called" );
        curScene = allScenes.get( curSceneIndex );
        if ( curScene != null )
            curScene.display( drawable );
        else
            System.err.println( "??? Trying to draw null scene" );
            /**/
    }

    //--------------------- dispose ------------------------------    
    @Override
    public void dispose( GLAutoDrawable arg0 )
    {
        // nothing to dispose of...
    }
    
    //--------------------- init ------------------------------
    @Override
    public void init( GLAutoDrawable drawable )
    {   
        JOGL.gl = drawable.getGL().getGL2();
        
        JOGL.gl.setSwapInterval( 1 );  // animation event occurs (maybe)
                                       //   only at end of frame draw.
                                       //  0 => render as fast as possible
        JOGL.glu = new GLU();
        JOGL.glut = new GLUT();


        appInit();
    }
    //--------------------- reshape ----------------------------------------
    /**
     * Window has been resized, readjust internal information
     */
    @Override
    public void reshape( GLAutoDrawable drawable, int x, int y, int w, int h )
    {
        System.out.println( "reshape" );
        JOGL.gl = drawable.getGL().getGL2();
        JOGL.gl.glViewport( 0, 0, w, h );
        System.out.println( "Viewport size: " + w + " x " + h );
    }

    //----------------- makeBox ----------------------------
    /**
     *  A convenience function to create a Sphere with a uniform scale,
     *    a specified color, and at 0,0,0.
     */
    Box makeBox( float scale, Color c )
    {
        Box box = new Box();
        box.setColor(new Color(0, 255, 255, 64));
        box.setLocation(0, 0, 0);
        box.setSize(scale, scale, scale);
        return box;
    }
    //----------------- makeBall ----------------------------
    Sphere makeBall( float scale, Color c )
    {
        Sphere sp = new Sphere();
        sp.setLocation( 0, 0, 0 );
        sp.setSize( scale, scale, scale );
        sp.setColor( c );
        return sp;
    }
    //----------------- makeTeapot ----------------------------
    Teapot makeTeapot( float scale, Color c )
    {
        Teapot t = new Teapot();
        t.setLocation(0, 0, 0);
        t.setSize(scale, scale, scale);
        t.setColor(c);
        return t;
    }
    //----------------- makeCylinder ----------------------------
    Cylinder makeCylinder( float scale, Color c )
    {
        Cylinder cy = new Cylinder();
        cy.setLocation(0, 0, 0);
        cy.setSize(scale, scale, scale);
        cy.setColor(c);
        return cy;
    }
    //----------------- makeSnowman ----------------------------
    Snowman makeSnowman( float scale, Color c )
    {
        Snowman s = new Snowman();
        s.setLocation(0, 0, 0);
        s.setSize(scale, scale, scale);
        s.setColor(c);
        return s;
    }



    //--------------------- appInit ------------------------
    void appInit()
    {
        JOGL.gl.glClearColor( 0.0f, 0.0f, 0.0f, 0.0f );//  black
        JOGL.gl.glClearDepth( 1.0 );               // sets farthest z-value
        JOGL.gl.glEnable( GL2.GL_DEPTH_TEST );     // Enable depth testing
        JOGL.gl.glShadeModel( GL2.GL_SMOOTH );     // Enable smooth shading
        JOGL.gl.glEnable( GL2.GL_NORMALIZE );      // Make all normals unit len
        JOGL.gl.glEnable( GL2.GL_COLOR_MATERIAL ); // Color used for material
        
        //lighting set up
        JOGL.gl.glEnable( GL2.GL_LIGHTING );
        JOGL.gl.glEnable( GL2.GL_LIGHT0 );
        
        //Set lighting intensity and color
        float gAmbientLight[] = { 0.2f, 0.2f, 0.2f, 1f };
        JOGL.gl.glLightModelfv( GL2.GL_LIGHT_MODEL_AMBIENT, gAmbientLight, 0 );
        float ambientLight[] = { 0.3f, 0.3f, 0.3f, 1f };
        //JOGL.gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0 );
        
        float diffuseLight[] = { 0.7f, 0.7f, 0.7f, 1f };
        JOGL.gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0 );
        
        //Set the light position
        //GLfloat lightPosition[] = {-1, 1, 1, 0};
        float lightPosition[] = {-1, 1, 1, 0};
        JOGL.gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0 );
        
        makeSimpleScenes();  // make scenes with at least 1 example of each Object3D
        makeMultiObjectScenes();
        nextScene();


    }
    
//------------------------- makeSimpleScenes --------------------------
    /**
     *  make all one object scenes
     */
    void makeSimpleScenes()
    {
        Box box1 = makeBox( 1, new Color( 1f, 0f, 1f ));  //unit magenta box
        someObjects.add( box1 );  // save it for future use

        
        Scene box1Scene = new Scene( "Small cyan box" );
        box1Scene.addObject( box1 );
        addScene( box1Scene );
        
        Box box2 = makeBox( 0.5f, new Color( 0f, 1f, 1f )); // smaller cyan box
        box2.setRotate( 45, 0f, 0f, 1f );
        someObjects.add( box2 );  // save it for future use
        
        Scene box2Scene = new Scene( "Bigger box" );
        box2Scene.addObject( box2 );
        addScene( box2Scene );
        
        Sphere sp = makeBall( 0.45f, new Color( 0.8f, 0.8f, 0f )); // yellow ball
        someObjects.add( sp );  // save it for future use
        
        Scene ballScene = new Scene( "Yellow ball" );
        ballScene.addObject( sp );
        addScene( ballScene );

        Teapot t = makeTeapot(.2f, new Color(228, 183, 109));
        someObjects.add(t);

        Scene teapotScene = new Scene( "Teapot" );
        teapotScene.addObject( t );
        addScene( teapotScene );


        Cylinder cylinder = makeCylinder(.2f, Color.RED);
        someObjects.add(cylinder);

        Scene cylinderScene = new Scene( "Cylinder" );
        cylinderScene.addObject( cylinder );
        addScene( cylinderScene );



        Snowman snowman = new Snowman();
        someObjects.add(snowman);

        Scene snowmanScene2 = new Scene("Snowman");
        snowmanScene2.addObject(snowman);
        addScene(snowmanScene2);

        Snowfriends friends = new Snowfriends();
        someObjects.add(friends);

        Scene snowFriends = new Scene("Snowfriends");
        snowFriends.addObject(friends);
        addScene(snowFriends);


        Hat h = new Hat();
        h.setColor(0, new Color(229, 209, 21));
        h.setColor(1, new Color(111, 107, 128));
        someObjects.add(h);
        Scene hat = new Scene("Hat");
        hat.addObject(h);
        addScene(hat);


        Scene sillySnowmanScene =  new Scene("New Friends");
        SnowmanWithHat snowmanHat = new SnowmanWithHat();
        Clown c = new Clown();
        c.setLocation(.8f, 0, 0);
        sillySnowmanScene.addObject(c);
        sillySnowmanScene.addObject(snowmanHat);
        addScene(sillySnowmanScene);


        JFrame controlPanel = new JFrame("Control Panel");
        controlPanel.setVisible(true);
        controlPanel.setSize(500, 900);
        controlPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel();
        controlPanel.add(p);

        makeRadioButtons(p);

        SliderX = getSlider(-180, 180, 0, 180, 10);
        SliderY = getSlider(-180, 180, 0, 180, 10);
        SliderZ = getSlider(-180, 180, 0, 180, 10);

        p.add(SliderX);
        p.add(SliderY);
        p.add(SliderZ);


        controlPanel.pack();


    }


    void makeRadioButtons(JPanel p){
        translate = new JRadioButton("Translate");
        rotate = new JRadioButton("Rotate");
        scale = new JRadioButton("Scale");

        group = new ButtonGroup();
        group.add(translate);
        group.add(rotate);
        group.add(scale);

        handleRadioButtons(p, translate, rotate, scale);

    }

    private void handleRadioButtons(JPanel panel, JRadioButton T, JRadioButton R, JRadioButton S){

        T.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doTranslate = true;
                doRotate = false;
                doScale = false;


            }
        });

        R.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doTranslate = false;
                doRotate = true;
                doScale = false;

            }
        });

        S.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doTranslate = false;
                doRotate = false;
                doScale = true;

                SliderX.setValue(100);
                SliderY.setValue(100);
                SliderZ.setValue(100);
            }
        });


        panel.add(T);
        panel.add(R);
        panel.add(S);
    }


//------------------------- makeMultiObjectScenes --------------------------
    /**
     *  make all one object scenes
     */
    void makeMultiObjectScenes()
    {
        Scene multi1 = new Scene( "2 box scene" );
        Object3D box = makeBox( 1, new Color( 1f, 0f, 1f )); // magenta
        box.setLocation( 1f, 0f, 0f );
        box.setSize( 0.4f, 0.4f, 0.4f );
        multi1.addObject( box );
        
        Object3D box2 = makeBox( 0.5f, new Color( 0f, 1f, 1f )); // cyan
        box2.setLocation( 0f, 0f, 1f );
        multi1.addObject( box2 );
        
        addScene( multi1 );        
    }


    public JSlider getSlider(int min, int max, int init, int mjrTkSp, int mnrTkSp) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, init);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(mjrTkSp);
        slider.setMinorTickSpacing(mnrTkSp);
        slider.setPaintLabels(true);
        slider.addChangeListener(new SliderListener());
        return slider;
    }


    private class SliderListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider slider = (JSlider) e.getSource();

            if(slider == SliderX){
                if (doTranslate)
                    curScene.Xtranslate = SliderX.getValue();
                else if (doRotate)
                    curScene.Xrotate = SliderX.getValue();
                else
                    curScene.Xscale = SliderX.getValue();


            }
            if(slider == SliderY) {

                if (doTranslate)
                    curScene.Ytranslate = SliderY.getValue();
                else if (doRotate)
                    curScene.Yrotate = SliderY.getValue();
                else
                    curScene.Yscale = SliderY.getValue();

            }
            if (slider == SliderZ) {
                if (doTranslate)
                    curScene.Ztranslate = SliderZ.getValue();
                else if (doRotate)
                    curScene.Zrotate = SliderZ.getValue();
                else
                    curScene.Zscale = SliderZ.getValue();

            }

        }
    }
}
