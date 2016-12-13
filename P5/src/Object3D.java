/**
 * Object3D.java - an abstract class representing an OpenGL graphical object
 *
 * 10/16/13 rdb derived from Object3D.cpp
 */
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.awt.Color;

/** Object3D.java
 *
 */

abstract public class Object3D {

    //------------------ class variables -------------------------------------//
    static final int MAX_COLORS = 20;

    //------------------ instance variables ----------------------------------//
    protected float xLoc, yLoc, zLoc;               // Origin of the object
    protected float xSize, ySize, zSize;            // Size of the object
    protected float angle, dxRot, dyRot, dzRot;     // Rotation angle and axis
    protected Material material;
    protected Texture texture;
    protected int textureParam1;
    protected int textureParam2;
    protected ArrayList<Color> colors;

    //-------------------- abstract methods ----------------------------------//
    abstract void setLocation();
    abstract void setSize();
    protected abstract void drawPrimitives();

    /**------------------------- Constructor -----------------------------------
     *  Constructor for Object3D
     */
    Object3D() {
        colors = new ArrayList<Color>();
        material = new Material();
        Color color = new Color( 1.0f, 0.0f, 0.0f ); //red is default
        colors.add( color );
        try {
            InputStream stream = getClass().getResourceAsStream( "texture.jpg" );
            texture = TextureIO.newTexture(stream, true, "jpg");
        }
        catch (IOException exc) {
            exc.printStackTrace();
            System.exit(1);
        }

    }

    /**------------------------- setTextureParameter ---------------------------
     *  Sets parameters for texture
     */
    void setTextureParameters(int parameter1, int parameter2){
        textureParam1 = parameter1;
        textureParam2 = parameter2;
    }


    /**------------------------- redraw ----------------------------------------
     *  Redraws the object
     */
    void redraw() {
        JOGL.gl.glPushMatrix();
        float[] rgb = colors.get( 0 ).getComponents( null );
        JOGL.gl.glColor3f( rgb[ 0 ], rgb[ 1 ], rgb[ 2 ] );
        //testMaterial();
        JOGL.gl.glTranslatef(xLoc, yLoc, zLoc);
        JOGL.gl.glRotatef(angle, dxRot, dyRot, dzRot);
        JOGL.gl.glScalef(xSize, ySize, zSize);
        material.redraw();
        drawPrimitives();
        JOGL.gl.glPopMatrix();
    }

    private void testMaterial() {
        float no_mat[] = { 0.0f, 0.0f, 0.0f, 1.0f };
        float no_shininess[] = { 0.0f };
        float mat_red[] = {1.0f, 0.0f, 0.0f, 1.0f};
        JOGL.gl.glPushMatrix();
        JOGL.gl.glColor3f(1.0f,1.0f, 1.0f);
        JOGL.gl.glMaterialfv(JOGL.gl.GL_FRONT, JOGL.gl.GL_AMBIENT, mat_red, 0);
        JOGL.gl.glMaterialfv(JOGL.gl.GL_FRONT, JOGL.gl.GL_DIFFUSE, no_mat, 0);
        JOGL.gl.glMaterialfv(JOGL.gl.GL_FRONT, JOGL.gl.GL_SPECULAR, no_mat, 0);
        JOGL.gl.glMaterialfv(JOGL.gl.GL_FRONT, JOGL.gl.GL_SHININESS, no_shininess, 0);
        JOGL.gl.glMaterialfv(JOGL.gl.GL_FRONT, JOGL.gl.GL_EMISSION, no_mat, 0);
        JOGL.gl.glDisable( JOGL.gl.GL_COLOR_MATERIAL );
    }

    /**------------------------- setLocation -----------------------------------
     * Sets the location of the object to the x,y,z position defined by the args
     */
    void setLocation( float x, float y, float z ) {
        xLoc = x;
        yLoc = y;
        zLoc = z;
    }


    /**---------------------------- getX ---------------------------------------
     * Returns the value of the x origin of the shape
     */
    float getX() { return xLoc; }

    /**---------------------------- getY ---------------------------------------
     * Returns the value of the Y origin of the shape
     */
    float getY() { return yLoc; }

    /**---------------------------- getZ ---------------------------------------
     * Returns the value of the z origin of the shape
     */
    float getZ() { return zLoc; }

    /**----------------------- getLocation -------------------------------------
     * Returns the location as a Point3 object
     */
    Point3 getLocation() { return new Point3( xLoc, yLoc, zLoc ); }




    //************************ setColor methods ******************************//
    //************************************************************************//

    /**-------------------------- setColor -------------------------------------
     * Sets the "nominal" color of the object to the specified color; this
     * does not require that ALL components of the object must be the same
     * color. Typically, the largest component will take on this color, but the
     * decision is made by the child class.
     */
    void setColor( Color c ) { setColor( 0, c ); }

    
    /**------------------------- setColor --------------------------------------
     * Sets the nominal color (index 0) to the specified color with floats
     */
    void setColor( float r, float g, float b ) { setColor( 0, new Color( r, g, b )); }


    /**------------------------- setColor --------------------------------------
     * Sets the index color entry to the specified color with floats
     */
    void setColor( int i, float r, float g, float b ) { setColor( i, new Color( r, g, b )); }


    /**------------------------- setColor --------------------------------------
     * Sets the i-th color entry to the specified color with Color
     */
    void setColor( int i, Color c ) {

        if( i < 0 || i > MAX_COLORS ) {
            System.err.println( "*** ERROR *** Object3D.setColor: bad index: " 
                                   + i + "\n" );
            return;
        }

        float[] rgb = c.getComponents( null );
        Color newColor = new Color( rgb[ 0 ], rgb[ 1 ], rgb[ 2 ] );
        if ( i >= colors.size() ) {             // Need to add entries to vector
            for( int n = colors.size(); n < i; n++ ) // Fill w/ black if needed
                colors.add( Color.BLACK );
            colors.add( newColor );        // Put desired color at desired index
        }
        else {
            colors.set( i, newColor );     // Now replace old entry
        }
    }


    /**--------------------------- setSize -------------------------------------
     * Sets the size of the shape to be scaled by xs, ys, zs. That is, the shape
     * has an internal fixed size, the shape parameters scale that internal size.
     */
    void setSize( float xs, float ys, float zs ) {
        xSize = xs;
        ySize = ys;
        zSize = zs;
    }


    /**--------------------------- setRotate -----------------------------------
     * Sets the rotation parameters: angle, and axis specification
     */
    void setRotate( float a, float dx, float dy, float dz ) {
        angle = a;
        dxRot = dx;
        dyRot = dy;
        dzRot = dz;
    }
}
