/**
 * SceneManager: Manages all the scenes and initiates drawing of current scene.
 *               Implements the Singleton class pattern, but also uses a static
 *               interface to interact with ControlPanel and the Scene class
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.FloatBuffer;
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


public class SceneManager implements GLEventListener {

    //------------------- class variables ------------------------------------//
    private static SceneManager me = null;
    private static boolean drawAxesFlag = true;
    private static ArrayList<Scene> allScenes; 
    private static int curSceneIndex = -1;
    private static Scene curScene = null;
    
    private static GLCanvas canvas = null;
    private static ControlPanel  cp = null;
    public static ButtonGroup group;
    public static JRadioButton pointLight;
    public static JRadioButton directionalLight;

    public static Boolean doTranslate;
    public static Boolean doRotate;
    public static Boolean doScale;
    public static Boolean doPointLight;
    public static Boolean doDirectional;


    //------------------- instance variables ---------------------------------//
    private ArrayList<Object3D> someObjects;
    private GLUquadric quad;
    private Boolean addTexture;
    private JSlider SliderX, SliderY, SliderZ;
    private JSlider redSlider, greenSlider, blueSlider;
    private JRadioButton translate, rotate, scale;

    float lightDirection[] = { -2.0f, -2.0f, -3.0f };
    float lightPosition0[] = {-1, 1, 1, 1};
    float lightPosition1[] = {1, 1, 1, 0};
    float lightAmbient[] = { .2f, 0.0f, 0.0f, 1.0f };
    float lightDiffuse[] = { .5f, 1.0f, 0.0f, 1.0f };
    float lightSpecular[] = { 1.0f, 0.0f, 1.0f, 1.0f };



    /**------------------------- Constructor -----------------------------------
     *  Constructor for SceneManager
     */
    private SceneManager() {

        allScenes = new ArrayList<Scene>();
        someObjects = new ArrayList<Object3D>(); 
        cp = ControlPanel.getInstance();

        doTranslate = false;
        doRotate = false;
        doScale = false;

        addTexture = true;
    }


    /**------------------------- getInstance -----------------------------------
     *  Returns an instance of SceneManager
     */
    public static SceneManager getInstance() {
        if ( me == null )
            me = new SceneManager();
        return me;
    }


    /**----------------- setCanvas ---------------------------------------------
     * Main program needs to tell sceneManager about the GL canvas to use
     */
    public void setCanvas( GLCanvas glCanvas ) {
        canvas = glCanvas;
        Animator a = new Animator(canvas);
        a.start();
    }


    /**------------------------- addScene --------------------------------------
     * Add a new scene to the scene collection
     */
    private void addScene( Scene newScene ) {
        allScenes.add( newScene );
        curScene = newScene;
        curSceneIndex = allScenes.size() - 1;
    }


    /**------------------------- nextScene -------------------------------------
     * Update current scene to next one with wraparound. This is a static method
     * to facilitate interaction with ControlPanel
     */
    public static void nextScene() {

        // Clear radio button logic
        doTranslate = false;
        doRotate = false;
        doScale = false;
        doPointLight = false;
        doDirectional = false;

        // Clear radio buttons
        group.clearSelection();
        pointLight.setSelected(false);
        directionalLight.setSelected(false);


        curSceneIndex++;
        if ( curSceneIndex >= allScenes.size() )
            curSceneIndex = 0;    // Wrap around
        curScene = allScenes.get( curSceneIndex );
        cp.setSceneTitle( curScene.getTitle() );
        canvas.repaint();
    }


    /**------------------------- setDrawAxes -----------------------------------
     * Sets the status of the axes drawing; called by ControlPanel
     */
    public static void setDrawAxes( boolean onoff ) {
        drawAxesFlag = onoff;
        canvas.repaint();
    }


    /**------------------------- drawAxes --------------------------------------
     * Retrieve axes drawing status; called by Scene
     */
    public static boolean drawAxes() 
    {
        return drawAxesFlag;
    }






    //**************** GLEventListener override methods **********************//
    //************************************************************************//


    /**--------------------------- display -------------------------------------
     * Override the parent display method. In this framework, the display
     * method is responsible for setting up the projection specification, but
     * the "render" method is responsible for the View and Model specifications.
     * 
     * This display method is reasonably application-independent; It defines a
     * pattern that can be reused with the exception of the specifying the
     * actual objects to render.
     */
    @Override
    public void display( GLAutoDrawable drawable ) {
        curScene = allScenes.get( curSceneIndex );
        if ( curScene != null )
            curScene.display( drawable );
        else
            System.err.println( "??? Trying to draw null scene" );
    }


    /**-------------------------- dispose --------------------------------------
     * Overridden dispose method
     */
    @Override
    public void dispose( GLAutoDrawable arg0 ) { }


    /**--------------------------- init ----------------------------------------
     * Initializes JOGL variables
     */
    @Override
    public void init( GLAutoDrawable drawable ) {

        JOGL.gl = drawable.getGL().getGL2();
        JOGL.gl.setSwapInterval( 1 );  // if set to 0, renders quickly
        JOGL.glu = new GLU();
        JOGL.glut = new GLUT();
        quad = JOGL.glu.gluNewQuadric();

        appInit();
    }


    /**--------------------- reshape -------------------------------------------
     * Window has been resized, readjust internal information
     */
    @Override
    public void reshape( GLAutoDrawable drawable, int x, int y, int w, int h ){
        System.out.println( "reshape" );
        JOGL.gl = drawable.getGL().getGL2();
        JOGL.gl.glViewport(0, 0, w, h);
    }


    /**----------------- makeBox -----------------------------------------------
     * Returns a Box with the location, color, and size properties set
     */
    Box makeBox(float scale, Color c, boolean b, float x, float y, float z){
        Box box = new Box(0.5f, b);
        box.setColor(c);
        box.setLocation(x, y, z);
        box.setSize(scale, scale, scale);
        return box;
    }


    /**------------------------- makeQuadricSphere -----------------------------
     * Returns a QuadricSphere with the location, color, and size properties set
     */
    QuadricSphere makeQuadricSphere( float scale, Color c, Boolean add, float x, float y, float z ){
        QuadricSphere sphere = new QuadricSphere(quad, .4f, 100, 100, add);
        sphere.setLocation(x, y, z);
        sphere.setSize(scale, scale, scale);
        sphere.setColor( c );
        return sphere;
    }


    /**----------------- appInit -----------------------------------------------
     * Initializes JOGL properties and calls methods to make the scenes
     */
    void appInit() {
        JOGL.gl.glClearColor( 0.0f, 0.0f, 0.0f, 0.0f );//  black
        JOGL.gl.glClearDepth( 1.0 );               // Sets farthest z-value
        JOGL.gl.glEnable( GL2.GL_DEPTH_TEST );     // Enable depth testing
        JOGL.gl.glShadeModel( GL2.GL_SMOOTH );     // Enable smooth shading
        JOGL.gl.glEnable( GL2.GL_NORMALIZE );      // Make all normals unit len
        JOGL.gl.glEnable(GL2.GL_COLOR_MATERIAL);   // Color used for material
        
        // Lighting set up
        JOGL.gl.glEnable( GL2.GL_LIGHTING );       // Enable lighting in general
        JOGL.gl.glEnable( GL2.GL_LIGHT0 );         // Enable point lighting
        JOGL.gl.glEnable( GL2.GL_LIGHT1 );         // Enable directional


        // Properties for point lighting
        JOGL.gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition0, 0 );
        JOGL.gl.glLightfv(JOGL.gl.GL_LIGHT0, JOGL.gl.GL_SPECULAR, FloatBuffer.wrap(lightSpecular));


        // Properties for directional lighting
        JOGL.gl.glLightfv(JOGL.gl.GL_LIGHT1, JOGL.gl.GL_AMBIENT, FloatBuffer.wrap(lightAmbient));
        JOGL.gl.glLightfv(JOGL.gl.GL_LIGHT1, JOGL.gl.GL_DIFFUSE, FloatBuffer.wrap(lightDiffuse));
        JOGL.gl.glLightfv(JOGL.gl.GL_LIGHT1, JOGL.gl.GL_SPECULAR, FloatBuffer.wrap(lightSpecular));
        JOGL.gl.glLightfv(JOGL.gl.GL_LIGHT1, JOGL.gl.GL_POSITION, FloatBuffer.wrap(lightPosition1));
        JOGL.gl.glLightfv(JOGL.gl.GL_LIGHT1, JOGL.gl.GL_SPOT_DIRECTION, FloatBuffer.wrap(lightDirection));

        JOGL.gl.glLightf(JOGL.gl.GL_LIGHT1, JOGL.gl.GL_SPOT_EXPONENT, 20.0f);
        
        makeSimpleScenes();  // make scenes with at least 1 example of each Object3D
        makeMultiObjectScenes();
        nextScene();
    }


    /**----------------- makeNewScene ------------------------------------------
     * Makes a new scene given a string and object, then adds the new scene to
     * the collection of all scenes
     */
    void makeNewScene(String sceneName, Object3D object ){
        Scene newScene = new Scene( sceneName );
        newScene.addObject(object);
        addScene( newScene );
        someObjects.add(object);
    }


    /**----------------- makeNewScene ------------------------------------------
     * Makes a new scene given a string and 2 objects, then adds the new scene
     * to the collection of all scenes
     */
    void makeNewScene(String sceneName, Object3D object, Object3D object2 ){
        Scene newScene = new Scene( sceneName );
        newScene.addObject(object);
        newScene.addObject((object2));
        addScene( newScene );
        someObjects.add(object);
        someObjects.add(object2);
    }


    /**----------------- makeQuadricSpheresScene -------------------------------
     * Creates all the scenes involving the QuadricSphere class and each to the
     * collection of all scenes. Demonstrates material properties.
     */
    void makeQuadricSpheresScenes(){

        //------- No Texture Quadric Sphere vs Texture Quadric Sphere --------//
        QuadricSphere sphere1 = makeQuadricSphere(1.0f, new Color(245, 236, 255), false, .8f, 0f, -.5f);
        QuadricSphere sphere2 = makeQuadricSphere(1.0f, new Color(1, 1, 1, 2), true, -1.0f, 0f, .9f);
        sphere2.setTextureParameters(GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
        makeNewScene("No Texture Quadric Sphere vs Texture Quadric Sphere", sphere1, sphere2);


        //--- Diffused Quadric Sphere vs Specular and Shiny Quadric Sphere ---//
        QuadricSphere sphere3 = makeQuadricSphere(1.0f, new Color(100, 30, 70), false, .8f, 0, -.8f);
        QuadricSphere sphere4 = makeQuadricSphere(1.0f, new Color(100, 30, 70), false, 0f, 0f, -.8f);
        sphere3.material.diffuse(new float[]{10.0f, 5.0f, 20.0f, 1.0f});
        sphere4.material.specular(new float[]{.6f, 0.0f, 5.0f, 1.0f});
        sphere4.material.shine(4.0f);
        makeNewScene("Diffused Quadric Sphere vs Specular and Shiny Quadric Sphere", sphere3, sphere4);


        //---------- Shiny Quadric Sphere vs Specular Quadric Sphere ---------//
        QuadricSphere sphere5 = makeQuadricSphere(1.0f, new Color(100, 30, 70), false, .9f, 0, 0);
        QuadricSphere sphere6 = makeQuadricSphere(1.0f, new Color(40, 100, 90), false, -.7f, 0, 0);
        sphere5.material.shine(10.0f);
        sphere5.material.emission(new float[]{10f, 7.0f, 10f, 1f});
        sphere6.material.specular(new float[]{10f, 5.0f, 2f, 0f});
        sphere6.material.ambient(new float[]{10f, 5.0f, 2f, 0f});
        makeNewScene("Shiny Quadric Sphere vs Specular Quadric Sphere", sphere5, sphere6);
    }


    /**----------------- makeBoxScenes -----------------------------------------
     * Creates all the scenes involving the Box class and adds each to the
     * larger succession of all scenes
     */
    void makeBoxScenes(){

        //-------------------- Box without Texture ---------------------------//
        Box box = makeBox( 1, new Color(212, 121, 213), false, 0, 0, 0);
        box.material.diffuse(new float[]{1.0f, 5.0f, 20.0f, 1.0f});
        box.material.shine(4.0f);
        makeNewScene("Box without Texture", box);


        //------------------------ Box with Texture --------------------------//
        Box box2 = makeBox( .5f, new Color(0.9098039f, 1.0f, 0.9529412f), true, 0, 0, 0);
        box2.setTextureParameters(GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
        makeNewScene("Box with Texture", box2);


        //----------------------- Rounded Box  -------------------------------//
        RoundedBox rb = new RoundedBox(.75f, false);
        rb.setLocation(0, 0, 0);
        rb.setSize(.5f, .5f, .5f);
        makeNewScene("Rounded Box without Texture", rb);


        //-------------------- Rounded Box with Texture ----------------------//
        RoundedBox rb2 = new RoundedBox(.75f, true);
        rb2.setTextureParameters(GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        rb2.setLocation(0, 0, 0);
        rb2.setSize(.5f, .5f, .5f);
        makeNewScene("Rounded Box with Texture", rb2);

        makeTextureScenes();
    }


    /**----------------- makeTextureScenes ---------------------------------
     * Creates the scenes showing texture properties
     */
    void makeTextureScenes(){

        //----- MAG_FILTER with GL_NEAREST vs MAG_FILTER with GL_LINEAR ------//
        QuadricSphere sphere1 = makeQuadricSphere(2.f, new Color(232, 219, 0), true, .9f, 0, 0f);
        QuadricSphere sphere2 = makeQuadricSphere(2.0f, new Color(232, 219, 0), true, 0f, 0f, .9f);
        sphere1.setTextureParameters(GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
        sphere2.setTextureParameters(GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        makeNewScene("MAG_FILTER with GL_NEAREST vs MAG_FILTER with GL_LINEAR", sphere1, sphere2);


        //---- MIN_FILTER with GL_NEAREST vs MIN_FILTER with GL_LINEAR -------//
        Box box1 = makeBox( .7f, new Color( 0f, 0.101960786f, 1f ), true, .5f, 0, 0);
        Box box2 = makeBox( .7f, new Color(1.0f, 0.13333334f, 0.96862745f), true, 0, 0, .5f);
        //box1.setTextureParameters(GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
        //box2.setTextureParameters(GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        //box1.setRotate( 45, 0f, 0f, 1f );
        makeNewScene("MIN_FILTER with GL_NEAREST vs MIN_FILTER with GL_LINEAR", box1, box2);


        //---- WRAP_S with GL_REPEAT vs WRAP_T with GL_REPEAT -------//
        QuadricDisk disk1 = new QuadricDisk(quad, .5f, 1, 20, 20, true);
        QuadricDisk disk2 = new QuadricDisk(quad, .5f, 1, 20, 20, true);
        disk1.setTextureParameters(GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
        disk2.setTextureParameters(GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
        disk1.setLocation(.9f, 0, 0);
        disk2.setLocation( 0f, 0, .9f);
        disk1.setSize(.5f, .5f, .5f);
        disk2.setSize(.5f, .5f, .5f);
        makeNewScene("WRAP_S with GL_REPEAT vs WRAP_T with GL_REPEAT", disk1, disk2);
    }


    /**----------------- makeMaterialScenes ---------------------------------
     * Creates the scenes showing texture properties
     */
    void makeMaterialScenes(){

        //----- MAG_FILTER with GL_NEAREST vs MAG_FILTER with GL_LINEAR ------//
        QuadricSphere sphere1 = makeQuadricSphere(2.f, new Color(232, 219, 0), true, .9f, 0, 0f);
        QuadricSphere sphere2 = makeQuadricSphere(2.0f, new Color(232, 219, 0), true, 0f, 0f, .9f);
        sphere1.setTextureParameters(GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
        sphere2.setTextureParameters(GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        makeNewScene("MAG_FILTER with GL_NEAREST vs MAG_FILTER with GL_LINEAR", sphere1, sphere2);


        //---- MIN_FILTER with GL_NEAREST vs MIN_FILTER with GL_LINEAR -------//
        Box box1 = makeBox( .7f, new Color( 0f, 0.101960786f, 1f ), true, .5f, 0, 0);
        Box box2 = makeBox( .7f, new Color(1.0f, 0.13333334f, 0.96862745f), true, 0, 0, .5f);
        //box1.setTextureParameters(GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
        //box2.setTextureParameters(GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        //box1.setRotate( 45, 0f, 0f, 1f );
        makeNewScene("MIN_FILTER with GL_NEAREST vs MIN_FILTER with GL_LINEAR", box1, box2);


        //---- WRAP_S with GL_REPEAT vs WRAP_T with GL_REPEAT -------//
        Box box3 = makeBox( 0.3f, new Color( 0f, 0.101960786f, 1f ), true, .9f, 0, 0);
        Box box4 = makeBox( 0.3f, new Color(1.0f, 0.13333334f, 0.96862745f), true, 0, 0, .6f);
        box3.setTextureParameters(GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
        box4.setTextureParameters(GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
        makeNewScene("WRAP_S with GL_REPEAT vs WRAP_T with GL_REPEAT", box3, box4);
    }



    /**----------------- makeQuadricDiskScenes ---------------------------------
     * Creates all the scenes involving the QuadricDisk class and adds each to
     * the larger succession of all scenes
     */
    void makeQuadricDiskScenes(){

        //------------- Quadric Disk with Texture ----------------------------//
        QuadricDisk disk1 = new QuadricDisk(quad, .5f, 1, 20, 20, true);
        disk1.setTextureParameters(GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        disk1.setLocation(0, 0, 0);
        disk1.setSize(.8f, .8f, .8f);
        someObjects.add(disk1);

        makeNewScene("Quadric Disk with Texture", disk1);

        //------------- Quadric Disk without Texture  ------------------------//
        QuadricDisk disk2 = new QuadricDisk(quad, .75f, 1f, 40, 40, false);
        disk2.setLocation(0, 0, 0);
        disk2.setSize(.8f, .8f, .8f);
        disk2.setColor(new Color(85, 128, 70));
        someObjects.add(disk2);

        makeNewScene("Quadric Disk without Texture", disk2);
    }


    /**----------------- makeHatScenes -----------------------------------------
     * Creates all the scenes involving the Hat class and adds each to the
     * larger succession of all scenes
     */
    void makeHatScenes(){
        Hat h = new Hat();
        h.setColor(0, new Color(229, 209, 21));
        h.setColor(1, new Color(111, 107, 128));
        someObjects.add(h);
        makeNewScene("Hat", h);
    }


    /**----------------- makeSnowmanScenes -------------------------------------
     * Creates all the scenes involving the Snowman class and adds each to the
     * larger succession of all scenes
     */
    void makeSnowmanScenes(){

        //------------------- Single Snowman ---------------------------------//
        Snowman snowman = new Snowman();
        someObjects.add(snowman);
        makeNewScene("Snowman", snowman);

        //------------------- Two Snowman Friends ----------------------------//
        Snowfriends friends = new Snowfriends();
        someObjects.add(friends);
        makeNewScene("Snowfriends", friends);

        //------------------Snowman with Hat and Clown -----------------------//
        SnowmanWithHat snowman2 = new SnowmanWithHat();
        Clown clown = new Clown();
        clown.setLocation(.8f, 0, 0);
        makeNewScene("New Friends", snowman2, clown);
    }


    /**----------------- makeSliders -------------------------------------------
     * Creates JSliders for rotation, translation, scale, red lighting, green
     * lighting, and blue lighting
     */
    void makeSliders(JPanel p){

        // Create labels
        JLabel space = new JLabel("                                      ");
        JLabel sliderx = new JLabel("X Coordinate:");
        JLabel slidery = new JLabel("Y Coordinate:");
        JLabel sliderz = new JLabel("Z Coordinate:");
        JLabel sliderr = new JLabel("Red Light Component:");
        JLabel sliderg = new JLabel("Green Light Component:");
        JLabel sliderb = new JLabel("Blue Light Component:");

        // Create transformation sliders
        SliderX = getSlider(-180, 180, 0, 180, 10);
        SliderY = getSlider(-180, 180, 0, 180, 10);
        SliderZ = getSlider(-180, 180, 0, 180, 10);

        // Create light sliders
        redSlider = getSlider(0, 100, 0, 100, 10);
        greenSlider = getSlider(0, 100, 0, 100, 10);
        blueSlider = getSlider(0, 100, 0, 100, 10);

        p.add(space);
        p.add(sliderx);
        p.add(SliderX);
        p.add(slidery);
        p.add(SliderY);
        p.add(sliderz);
        p.add(SliderZ);
        p.add(sliderr);
        p.add(redSlider);
        p.add(sliderg);
        p.add(greenSlider);
        p.add(sliderb);
        p.add(blueSlider);
    }

    
    /**------------------------- makeSimpleScenes ------------------------------
     *  Make all one object scenes
     */
    void makeSimpleScenes() {

        makeBoxScenes();                // Boxes
        makeQuadricSpheresScenes();     // Quadric Spheres
        makeQuadricDiskScenes();        // Quadric Disks
        makeHatScenes();                // Hat
        makeSnowmanScenes();            // Snowman


        JFrame controlPanel = new JFrame("Control Panel");    // Create JFrame
        controlPanel.setVisible(true);
        controlPanel.setPreferredSize(new Dimension(200,800));
        controlPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel p = new JPanel();        // Create a new JPanel
        controlPanel.add(p);            // Add the panel to the JFrame

        makeRadioButtons(p);            // Create the transformation buttons
        makeSliders(p);                 // Create all sliders
        makeLightButtons(p);            // Create light buttons

        controlPanel.pack();
    }


    /**------------------------- makeRadioButtons ----------------------------
     *  Creates new translate, rotate, and scale radio buttons, and adds them
     *  to a button group
     */
    void makeRadioButtons(JPanel p) {
        translate = new JRadioButton("Translate");
        rotate = new JRadioButton("Rotate");
        scale = new JRadioButton("Scale");

        group = new ButtonGroup();
        group.add(translate);
        group.add(rotate);
        group.add(scale);

        handleRadioButtons(p, translate, rotate, scale);

    }


    /**------------------------- makeLightButtons ------------------------------
     *  Creates new pointLight and directionalLight radio buttons
     */
    void makeLightButtons(JPanel p) {
        pointLight = new JRadioButton("Point Source Light");
        directionalLight = new JRadioButton("Directional Light");

        handleLightButtons(p, pointLight, directionalLight);
    }


    /**------------------------- handleLightButtons ----------------------------
     *  Determines when pointLight and directionalLight buttons have been
     *  selected
     */
    private void handleLightButtons(JPanel panel, JRadioButton P, JRadioButton D) {
        P.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {        // Translate button
               if(doPointLight == true)
                   doPointLight = false;
               else
                   doPointLight = true;
            }
        });

        D.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {        // Rotate button
                if(doDirectional == true)
                    doDirectional = false;
                else
                    doDirectional = true;
            }
        });

        panel.add(P);
        panel.add(D);
    }


    /**------------------------- handleRadioButtons ----------------------------
     *  Determines when rotate, translate, and scale buttons have been selected
     */
    private void handleRadioButtons(JPanel panel, JRadioButton T, JRadioButton R, JRadioButton S) {

        T.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {        // Translate button
                doTranslate = true;
                doRotate = false;
                doScale = false;
            }
        });

        R.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {        // Rotate button
                doTranslate = false;
                doRotate = true;
                doScale = false;
            }
        });

        S.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {        // Scale button
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


    /**------------------------- makeMultiObjectScenes -------------------------
     *  Makes all multi object scenes
     */
    void makeMultiObjectScenes() {

        Scene multi1 = new Scene( "2 box scene" );
        Object3D box = makeBox( 1, new Color( 1f, 0f, 1f ), false, 1f, 0f, 0f); // magenta
        box.setSize( 0.4f, 0.4f, 0.4f );
        multi1.addObject( box );
        
        Object3D box2 = makeBox( 0.5f, new Color( 0f, 1f, 1f ), true, 0f, 0f, 1f); // cyan
        multi1.addObject( box2 );
        
        addScene( multi1 );        
    }


    /**------------------------- getSlider -------------------------------------
     *  Returns a newly constructed JSlider
     */
    public JSlider getSlider(int min, int max, int init, int mjrTkSp, int mnrTkSp){
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, init);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(mjrTkSp);
        slider.setMinorTickSpacing(mnrTkSp);
        slider.setPaintLabels(true);
        slider.addChangeListener(new SliderListener());
        return slider;
    }





    /**---------------------- onTransformationDetection ------------------------
     *  Detects if any of the transformation sliders have been altered and save
     *  the value to a static variable to later be used in Scene
     */
    void onTransformationDetection(ChangeEvent e){
        JSlider slider = (JSlider) e.getSource();
        if(slider == SliderX){
            if (doTranslate)
                curScene.Xtranslate = SliderX.getValue();
            else if (doRotate)
                curScene.Xrotate = SliderX.getValue();
            else
                curScene.Xscale = SliderX.getValue();
        }
        if(slider == SliderY){

            if (doTranslate)
                curScene.Ytranslate = SliderY.getValue();
            else if (doRotate)
                curScene.Yrotate = SliderY.getValue();
            else
                curScene.Yscale = SliderY.getValue();
        }
        if(slider == SliderZ){

            if (doTranslate)
                curScene.Ztranslate = SliderZ.getValue();
            else if (doRotate)
                curScene.Zrotate = SliderZ.getValue();
            else
                curScene.Zscale = SliderZ.getValue();
        }
    }


    /**------------------------- onLightSliderDetection ------------------------
     *  Detects if the state of the redSlider, greenSlider, or blueSlider has
     *  changed, then saves the value to a static variable to later be used in
     *  Scene
     */
    void onLightSliderDetection(ChangeEvent e){
        JSlider slider = (JSlider) e.getSource();
        if(slider == redSlider){

            curScene.redLight = (float)(redSlider.getValue())/100;
        }
        if(slider == greenSlider){

            curScene.greenLight = (float)(greenSlider.getValue())/100;
        }
        if(slider == blueSlider){

            curScene.blueLight = (float)(blueSlider.getValue())/100;
        }
    }


    private class SliderListener implements ChangeListener {

        /**------------------------- stateChanged ------------------------------
         *  Detects if a change has occurred in any of the sliders by calling
         *  onTransformationDetection() and onLightSliderDetection() on
         *  ChangeEvent e
         */
        @Override
        public void stateChanged(ChangeEvent e) {
            onTransformationDetection(e);
            onLightSliderDetection(e);
        }
    }







}
