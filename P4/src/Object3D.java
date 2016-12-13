/**
 * Object3D.java - an abstract class representing an OpenGL graphical object
 *
 * 10/16/13 rdb derived from Object3D.cpp
 * 10/28/14 rdb Revised redraw to take GL2 parameter
 *              changed drawPrimitives to drawObject( GL2 )
 *                
 */
import java.io.*;
import java.util.*;
import java.awt.Color;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;        // GL utility library

//-------------- import static ---------------------------------------
// These imports allow you to avoid prefixing static variables and methods
//    in the GL and GL2 classes with the class prefix.
// Hence  
//      GL2.gl.glMatrixMode( GL2.GL_MODELVIEW )
// can be written as:
//      gl.glMatrixMode( GL_MODELVIEW );

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;

abstract public class Object3D
{
    //------------------ class variables ------------------------------
    static final int MAX_COLORS = 20;   // arbitrary number
    
    public static int shaderProgram;  // set by SceneManager

    //------------------ instance variables ----------------------------
    protected float xLoc, yLoc, zLoc;        // location (origin) of object
    protected float xSize, ySize, zSize;     // size of the object
    protected float angle, dxRot, dyRot, dzRot; // rotation angle and axis
    
    protected ArrayList<Color> colors; 
        
    //----------- abstract methods to be overridden by children -----
    protected abstract void redrawObject( GL2 gl );
    protected abstract void redrawVAO( GL2 gl );
    protected abstract void redrawVBO( GL2 gl );
    
     //------------------ Constructors ----------------------------------
    /**
     * Create a new object3D at position 0,0,0 of size 1,1,1
     */
    Object3D()
    {
        colors = new ArrayList<Color>();
        Color color = new Color( 1.0f, 0.0f, 0.0f ); //red is default
        colors.add( color );
        
        setLocation( 0, 0, 0 );
        setSize( 1, 1, 1 );
        setRotate( 0, 0, 0, 0);
    }
    
    //------------------ public methods -------------------------------
    //------------- redraw ---------------------------
    public void redraw( GL2 gl )
    {
        //float[] rgb = colors.get( 0 ).getComponents( null );
        ///gl.glColor3f( rgb[ 0 ], rgb[ 1 ], rgb[ 2 ] ); 
        // Apply the modeling transformations for this object.
        gl.glMatrixMode( GL_MODELVIEW );
        gl.glPushMatrix();
        {
            gl.glTranslatef( xLoc, yLoc, zLoc );
            gl.glRotatef( angle, dxRot, dyRot, dzRot );
            gl.glScalef( xSize, ySize, zSize );
            
            if ( !SceneManager.glslDraw() )
                redrawObject( gl );  // invoke "old" code
            else
                redrawGLSL( gl );
        }
        gl.glPopMatrix();
    }
    //------------- redrawGLSL( GL2 ) ----------------------------------
    public void redrawGLSL( GL2 gl )
    {   
        // Color: To fully support Object3D, need to get an array of
        //    colors as a Uniform variable. This has to be have a 
        //    fixed size, so it's ok to limit the size of the colors 
        //    array to something like 8 or so. That number should be
        //    determined by the child class.
        //
        //  This code only supports 1 color.
        int colorLoc = gl.glGetUniformLocation( shaderProgram, "myColor" );

        // Use the "color" instance variable of this object to set the 
        //    myColor uniform variable in the shader.
        float[] color = new float[ 4 ];
        colors.get( 0 ).getComponents( color );
        
        gl.glUniform4fv( colorLoc, 1, color, 0 ); 
       
        // Update the transformation uniform variable
        sendXformToShader( gl );       
        
        if ( JOGL.vaoSupported ) 
            redrawVAO( gl );  
        else
            redrawVBO( gl );        
    }
        //---------------- sendXformToShader( GL2 ) ---------------------
    /**
     * Before invoking rendering of the Shape, need to download current
     *    Projection * ModelView matrix.
     */
    protected void  sendXformToShader( GL2 gl )
    {
        //---- need to get Proj * ModelView matrix into the shader for
        //  transforming all vertices.
        //
        // Do that by getting the MV matrix and multiplying it in the
        //   Proj matrix stack (after pushing)
        
        float modelview[] = new float[ 16 ];
        gl.glGetFloatv( GL_MODELVIEW_MATRIX, modelview, 0 );

        float PtimesMV[] = new float[ 16 ];  
        gl.glMatrixMode( GL_PROJECTION );
        gl.glPushMatrix();
        {
            gl.glMultMatrixf( modelview, 0 );   // P = P*MV            
            gl.glGetFloatv( GL_PROJECTION_MATRIX, PtimesMV, 0 );  
        }
        gl.glPopMatrix();   // and clean up Proj matrix stack
        
        gl.glMatrixMode( GL_MODELVIEW ); // restore to MV mode
        
        //--- now push the composite into a uniform var in vertex shader
        int matLoc = gl.glGetUniformLocation( shaderProgram, "pXmv" );
        gl.glUniformMatrix4fv( matLoc, 1, false, PtimesMV, 0 );
        JOGL.check( "Ptimes set uniform" );
    }    

    //---------------- setLocation( float, float, float ) ----------------
    /**
     * set the location of object to x,y,z position defined by args
     */
    public void setLocation( float x, float y, float z )
    {
        xLoc = x;
        yLoc = y;
        zLoc = z;
    }
    /**
     * return the value of the x origin of the shape
     */
    public float getX()
    {
        return xLoc;
    }
    /**
     * return the value of the y origin of the shape
     */
    public float getY()
    {
        return yLoc;
    }
    /**
     * return the value of the z origin of the shape
     */
    public float getZ()
    {
        return zLoc;
    }
    /**
     * return the location as a Coord object
     */
    public Coord getLocation()             // return location as a Coord
    {
        return new Coord( xLoc, yLoc, zLoc );
    }
    //----------------------- setColor methods ---------------------------
    /**
     * set the "nominal" color of the object to the specified color; this
     *   does not require that ALL components of the object be the same
     *   color. Typically, the largest component will take on this color,
     *   but the decision is made by the child class.
     */
    public void setColor( Color c )
    {
        setColor( 0, c );
    }
    
    //----------- setColor( float r, float g, float b ) ----------------
    /**
     * set the nominal color (index 0) to the specified color with floats
     */
    public void setColor( float r, float g, float b )
    {
        setColor( 0, new Color( r, g, b ));
    }
    
    //------------ setColor( int, float,f loat, float ) ----------------
    /**
     * set the index color entry to the specified color with floats
     */
    public void setColor( int i, float r, float g, float b )
    {
        setColor( i, new Color( r, g, b ));
    }
    //------------------- setColor( int, Color ) -----------------------
    /**
     * set the i-th color entry to the specified color with Color
     */
    public void setColor( int i, Color c )
    {
        if ( i < 0 || i > MAX_COLORS ) // should throw an exception!
        {
            System.err.println( "*** ERROR *** Object3D.setColor: bad index: " 
                                   + i + "\n" );
            return;
        }
        float[] rgb = c.getComponents( null );
        Color newColor = new Color( rgb[ 0 ], rgb[ 1 ], rgb[ 2 ] );
        if ( i >= colors.size() )  // need to add entries to vector
        {
            for ( int n = colors.size(); n < i; n++ ) // fill w/ black
                colors.add( Color.BLACK );
            colors.add( newColor );  // put desired color at desired index
        }
        else
        {
            // now replace old entry 
            colors.set( i, newColor );
        }
    }
    //------------------ setSize ----------------------------------------
    /**
     * set the size of the shape to be scaled by xs, ys, zs
     *    I.e, the shape has an internal fixed size, the size parameters
     *    scale that internal size.
     */
    public void setSize( float xs, float ys, float zs )
    {
        xSize = xs;
        ySize = ys;
        zSize = zs;
    }
    //------------------ setRotate --------------------------------------
    
    /**
     * set the rotation parameters: angle, and axis specification
     */
    public void setRotate( float a, float dx, float dy, float dz )
    {
        angle = a;
        dxRot = dx;
        dyRot = dy;
        dzRot = dz;
    }
    //--------------------- main -----------------------------------
    /**
     * Convenience main to invoke the app
     */
    public static void main( String[] args )
    {
        GLSL3D.main( args );
    }
}
