/**
 * Scene.java - a class to represent a scene: its objects and its view
 * 
 * 10/16/13 rdb derived from Scene.cpp
 * ----------------------
 * History
 * 09/25/14: Added scene label support
 */

import java.nio.FloatBuffer;
import java.util.*;
import javax.media.opengl.*;
import javax.media.opengl.fixedfunc.*;

public class Scene {
    //------------------ class variables ---------------------------------------
    static boolean  drawAxes;    // package access
    
    //------------------ instance variables ------------------------------------
    ArrayList<Object3D> objects;

    // gluLookat parameters
    float eyeX, eyeY, eyeZ;         // gluLookat eye position
    float lookX, lookY, lookZ;      // gluLookat look position
    float upX, upY, upZ;            // up vector

    // gluPerspective parameters
    float viewAngle, aspectRatio, near, far; 
    
    // scene title
    String sceneTitle = null;

    int Xrotate, Yrotate, Zrotate;
    int Xtranslate, Ytranslate, Ztranslate;
    int Xscale, Yscale, Zscale;
    private GLAutoDrawable drawable;
    public static float redLight, greenLight, blueLight;

    float[] lightColor;


    /*------------------ Constructors ------------------------------------------
     * Initialize any values, register callbacks
     */
    public Scene( String title ) {
        objects = new ArrayList<Object3D>();
        resetView();
        drawAxes = true;
        sceneTitle = title;
        lightColor = new float[4];
    }
    
    /*-------------- getTitle() ------------------------------------------------
     *  Return the title for the scene.
     */
    public String getTitle() {
        return sceneTitle;
    }
    
    /*-------------- setTitle( String ) ----------------------------------------
     *  Return the title for the scene.
     */
    public void getTitle( String title )
    {
        sceneTitle = title;
    }


    /*-------------- resetView -------------------------------------------------
     *  restore the view to default settings
     */
    public void resetView()
    {
        setLookat( 10, 3, 10, // eye
                    0, 0, 0,   // at
                    0, 1, 0 ); // up
        
        setPerspective( 10, 1.33f, 0.1f, 100.0f ); //should calc windowWid / windowHt
    }
    
    /*------------------------------addObject( Object3D )-----------------------
     * Add object to object collection
     */
    public void addObject( Object3D newObject ) {
        objects.add( newObject );
    }


    /*------------------------------ clear -------------------------------------
     * Clears objects on scene
     */
    public void clear() {
        objects.clear();
        redraw();


    }
    
    /*---------------------------- setLookat -----------------------------------
     *  set lookat parameters
     */
    public void setLookat( float eyeX, float eyeY, float eyeZ,
                           float lookX, float lookY, float lookZ,
                           float upX, float upY, float upZ ) {
        this.eyeX = eyeX;
        this.eyeY = eyeY;
        this.eyeZ = eyeZ;
        this.lookX = lookX;
        this.lookY = lookY;
        this.lookZ = lookZ;
        this.upX = upX;
        this.upY = upY;
        this.upZ = upZ;
    }
    /*---------------setPerspective( float, float, float, float )---------------
     *  set perspective parameters
     */
    void setPerspective ( float angle, float ratio, float near, float far ) {
        this.viewAngle = angle;
        this.aspectRatio = ratio;
        this.near = near;
        this.far = far;
    }


    /*---------------- drawCoordinateAxes --------------------------------------
     * Draw the world coord axes to help orient viewer.
     */
    void drawCoordinateAxes() {
        float scale = 1.8f;  // scale factor for experimenting with size
        JOGL.gl.glPushMatrix();
        
        JOGL.gl.glDisable( GLLightingFunc.GL_LIGHTING );
        JOGL.gl.glScalef( scale, scale, scale );
        float[] origin = { 0, 0, 0 };
        
        float[] xaxis = { 1, 0, 0 };
        float[] yaxis = { 0, 1, 0 };
        float[] zaxis = { 0, 0, 1 };
                
        JOGL.gl.glLineWidth( 3 );
                
        JOGL.gl.glBegin( GL2.GL_LINES );
        {
            JOGL.gl.glColor3f( 1, 0, 0 ); // X axis is red.
            JOGL.gl.glVertex3fv( origin, 0 );
            JOGL.gl.glVertex3fv( xaxis, 0 );
            JOGL.gl.glColor3f( 0, 1, 0 ); // Y axis is green.
            JOGL.gl.glVertex3fv( origin, 0 );
            JOGL.gl.glVertex3fv( yaxis, 0 );
            JOGL.gl.glColor3f( 0, 0, 1 ); // z axis is blue.
            JOGL.gl.glVertex3fv( origin, 0 );
            JOGL.gl.glVertex3fv( zaxis, 0 );
        }
        JOGL.gl.glEnd();
        JOGL.gl.glPopMatrix();
        JOGL.gl.glEnable( GLLightingFunc.GL_LIGHTING );
    }


    /*--------------------- display( GLAutoDrawable )  -------------------------
     * Displays the drawable
     */
    public void display( GLAutoDrawable drawable ) {
        //redraw( drawable );
        redraw();
    }


    /*--------------------- redraw( drawable )  --------------------------------
     * Redraws the scene
     */
    public void redraw( ) {

        JOGL.gl.glMatrixMode(GL2.GL_PROJECTION);
        JOGL.gl.glLoadIdentity();      // Reset The Projection Matrix
        
        // Only do perspective for now
        JOGL.glu.gluPerspective( viewAngle, aspectRatio, near, far );
        JOGL.gl.glMatrixMode( GL2.GL_MODELVIEW );
        JOGL.gl.glLoadIdentity();                // Reset The Projection Matrix
        
        JOGL.glu.gluLookAt( eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ );



        JOGL.gl.glRotatef(Xrotate, 1, 0, 0);
        JOGL.gl.glRotatef(Yrotate, 0, 1, 0);
        JOGL.gl.glRotatef(Zrotate, 0, 0, 1);

        JOGL.gl.glTranslatef((float)Xtranslate/100, 0, 0);
        JOGL.gl.glTranslatef(0, (float)Ytranslate/100, 0);
        JOGL.gl.glTranslatef(0, 0, (float)Ztranslate/100);

        if(SceneManager.doScale)
            JOGL.gl.glScalef((float)Xscale/100, (float)Yscale/100, (float)Zscale/100);


        JOGL.gl.glClear( GL2.GL_DEPTH_BUFFER_BIT | GL2.GL_COLOR_BUFFER_BIT );


        lightColor = new float[] {redLight, greenLight, blueLight, 1f};

        light();


        if( SceneManager.drawAxes() )
            drawCoordinateAxes();
        
        // create a vector iterator to access and draw the objects
        for ( Object3D obj: objects )
            obj.redraw();
        JOGL.gl.glFlush();                         // send all output to display 
    }



    public void light(){

        float lightPosition0[] = {-1, 1, 1, 0};
        float lightPosition1[] = {1, 1, 1, 0};
        float lightDirection[] = { -2.0f, -2.0f, -3.0f };


        if(SceneManager.doPointLight == true && SceneManager.doDirectional == false){

            JOGL.gl.glEnable( JOGL.gl.GL_LIGHT0 );
            JOGL.gl.glDisable( JOGL.gl.GL_LIGHT1 );

            //JOGL.gl.glEnable(JOGL.gl.GL_AMBIENT_AND_DIFFUSE);
            JOGL.gl.glLightfv(JOGL.gl.GL_LIGHT0, JOGL.gl.GL_AMBIENT, FloatBuffer.wrap(lightColor));
            JOGL.gl.glLightfv(JOGL.gl.GL_LIGHT0, JOGL.gl.GL_DIFFUSE, FloatBuffer.wrap(lightColor));
            JOGL.gl.glLightfv(JOGL.gl.GL_LIGHT0, JOGL.gl.GL_SPECULAR, FloatBuffer.wrap(lightColor));
            JOGL.gl.glLightfv(JOGL.gl.GL_LIGHT0, JOGL.gl.GL_POSITION, FloatBuffer.wrap(lightPosition0));


        }
        else if(SceneManager.doPointLight == false  && SceneManager.doDirectional == false){
            JOGL.gl.glDisable( JOGL.gl.GL_LIGHT0 );
            JOGL.gl.glDisable( JOGL.gl.GL_LIGHT1 );
        }
        else if(SceneManager.doDirectional == true  && SceneManager.doPointLight== false){

            JOGL.gl.glDisable( JOGL.gl.GL_LIGHT0 );
            JOGL.gl.glEnable( JOGL.gl.GL_LIGHT1 );

            //JOGL.gl.glEnable(JOGL.gl.GL_AMBIENT_AND_DIFFUSE);
            JOGL.gl.glLightfv(JOGL.gl.GL_LIGHT1, JOGL.gl.GL_AMBIENT, FloatBuffer.wrap(lightColor));
            JOGL.gl.glLightfv(JOGL.gl.GL_LIGHT1, JOGL.gl.GL_DIFFUSE, FloatBuffer.wrap(lightColor));
            //JOGL.gl.glLightfv(JOGL.gl.GL_LIGHT1, JOGL.gl.GL_SPECULAR, FloatBuffer.wrap(lightColor));
            JOGL.gl.glLightfv(JOGL.gl.GL_LIGHT1, JOGL.gl.GL_SPOT_DIRECTION, FloatBuffer.wrap(lightDirection));

            JOGL.gl.glLightf(JOGL.gl.GL_LIGHT1, JOGL.gl.GL_SPOT_EXPONENT, 20.0f);


        }
        else if(SceneManager.doDirectional == true  && SceneManager.doPointLight == true){
            JOGL.gl.glEnable( JOGL.gl.GL_LIGHT0 );
            JOGL.gl.glEnable( JOGL.gl.GL_LIGHT1 );

            JOGL.gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition0, 0 );
            JOGL.gl.glLightfv(JOGL.gl.GL_LIGHT0, JOGL.gl.GL_DIFFUSE, FloatBuffer.wrap(lightColor));
            JOGL.gl.glLightfv(JOGL.gl.GL_LIGHT1, JOGL.gl.GL_DIFFUSE, FloatBuffer.wrap(lightColor));


        }

    }




    /*--------------------- setDrawAxes( int )  --------------------------------
     * Draws the axes if yesno  is non-zero
     */
    public void setDrawAxes( boolean yesno ) {
        drawAxes = yesno;
    }
}