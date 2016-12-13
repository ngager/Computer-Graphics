/**
 * Scene.java - a class to represent a scene: its objects and its view
 * 
 * 10/16/13 rdb derived from Scene.cpp
 * ----------------------
 * History
 * 09/25/14: Added scene label support
 */

import java.util.*;
import java.io.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.*;
import javax.media.opengl.fixedfunc.*;

import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.GLUT;

public class Scene
{
    //------------------ class variables ------------------------------
    //---- draw Axes flag ------------------------
    static boolean  drawAxes;    // package access
    
    //------------------ instance variables ------------------------------
    //---- objects collection -------
    ArrayList<Object3D> objects;

    //----gluLookat parameters -------
    float eyeX, eyeY, eyeZ; // gluLookat eye position
    float lookX, lookY, lookZ; // gluLookat look position
    float upX, upY, upZ; // up vector

    //----gluPerspective parameters ----
    float viewAngle, aspectRatio, near, far; 
    
    //---- scene title ---------------------
    String sceneTitle = null;

    //--------------------------------
    int Xrotate, Yrotate, Zrotate;
    int Xtranslate, Ytranslate, Ztranslate;
    int Xscale, Yscale, Zscale;

    //------------------ Constructors ------------------------------------
    /** Initialize any values, register callbacks
      */
    public Scene( String title )
    {
        objects = new ArrayList<Object3D>();
        resetView();
        drawAxes = true;
        sceneTitle = title;

    }
    
    //-------------- getTitle() -----------------------------------------
    /**
     *  Return the title for the scene.
     */
    public String getTitle()
    {
        return sceneTitle;
    }
    
    //-------------- setTitle( String ) --------------------------------
    /**
     *  Return the title for the scene.
     */
    public void getTitle( String title )
    {
        sceneTitle = title;
    }
    
    //-------------- resetView -----------------------------------------
    /**
     *  restore the view to default settings
     */
    public void resetView()
    {
        setLookat( 10, 3, 10, // eye
                  0, 0, 0,   // at
                  0, 1, 0 ); // up
        
        setPerspective( 10, 1.33f, 0.1f, 100.0f ); //should calc windowWid / windowHt
    }
    
    //--------------------------------------------------------------------
    public void addObject( Object3D newObject )
    {
        objects.add( newObject );
    }
    //--------------------------------------------------------------------
    public void clear()
    {
        objects.clear();
        redraw();


    }
    
   //---------------------------------------------------------------------
    /**
     *  set lookat parameters
     */
    public void setLookat( float eyeX, float eyeY, float eyeZ,
                   float lookX, float lookY, float lookZ,
                   float upX, float upY, float upZ )
    {
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
    //---------------------------------------------------------------------
    /**
     *  set perspective parameters
     */
    void setPerspective ( float angle, float ratio, float near, float far )
    {
        this.viewAngle = angle;
        this.aspectRatio = ratio;
        this.near = near;
        this.far = far;
    }
    
    //---------------- drawing coordinate axes -----------------------
    /**
     * Draw the world coord axes to help orient viewer.
     */
    void drawCoordinateAxes()
    {
        float scale = 1.8f;  // convenient scale factor for experimenting with size
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
    //---------------------------------------------------------------------
    public void display( GLAutoDrawable drawable )
    {
        //redraw( drawable ); 
        redraw();
    }
    public void redraw() 
    {   
        // Should I use the passed drawable or JOGL.gl???
        JOGL.gl.glMatrixMode( GL2.GL_PROJECTION );
        JOGL.gl.glLoadIdentity();                // Reset The Projection Matrix
        
        // Only do perspective for now
        JOGL.glu.gluPerspective( viewAngle, aspectRatio, near, far );
        JOGL.gl.glMatrixMode( GL2.GL_MODELVIEW );
        JOGL.gl.glLoadIdentity();                // Reset The Projection Matrix
        
        JOGL.glu.gluLookAt( eyeX, eyeY, eyeZ, 
                  lookX, lookY, lookZ, 
                  upX, upY, upZ );


        JOGL.gl.glRotatef(Xrotate, 1, 0, 0);
        JOGL.gl.glRotatef(Yrotate, 0, 1, 0);
        JOGL.gl.glRotatef(Zrotate, 0, 0, 1);

        JOGL.gl.glTranslatef((float)Xtranslate/100, 0, 0);
        JOGL.gl.glTranslatef(0, (float)Ytranslate/100, 0);
        JOGL.gl.glTranslatef(0, 0, (float)Ztranslate/100);

        if(SceneManager.doScale)
            JOGL.gl.glScalef((float)Xscale/100, (float)Yscale/100, (float)Zscale/100);





        JOGL.gl.glClear( GL2.GL_DEPTH_BUFFER_BIT 
                            | GL2.GL_COLOR_BUFFER_BIT );    
        
        if( SceneManager.drawAxes() )
            drawCoordinateAxes();
        
        // create a vector iterator to access and draw the objects
        for ( Object3D obj: objects )
            obj.redraw();
        JOGL.gl.glFlush();                         // send all output to display 
    }
    //---------------- setDrawAxes( int )  -----------------------
    /**
     *  0 means don't draw the axes
     * non-zero means draw them
     */
    //-------------------------------------------
    public void setDrawAxes( boolean yesno )
    {
        drawAxes = yesno;
    }
}