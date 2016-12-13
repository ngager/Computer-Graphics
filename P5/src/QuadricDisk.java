import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import java.io.IOException;
import java.io.InputStream;

/** QuadricDisk.java
 *  Creates a subclass of Object3D that draws a gluQuadric Disk object and has
 *  texture functionality
 */
public class QuadricDisk extends Object3D {

    //------------------------ instance variables ----------------------------//
    GLUquadric quadric;
    float internalRadius;
    float externalRadius;
    int slices;
    int rings;
    Texture texture;
    boolean addTexture;

    /**------------------------- Constructor -----------------------------------
     *  Constructor for QuadricDisk
     */
    public QuadricDisk( GLUquadric quad, float i, float e, int slices,
                        int rings, boolean add ) {

        this.quadric = quad;
        this.internalRadius = i;
        this.externalRadius = e;
        this.slices = slices;
        this.rings = rings;
        this.addTexture = add;
    }

    @Override
    void setLocation( float x, float y, float z ) {
        super.setLocation( x, y, z );
    }

    @Override
    void setSize( float xs, float ys, float zs) {
        super.setSize( xs, ys, zs );
    }

    @Override
    void setLocation( ) {
        setLocation( 0, 0, 0 );
    }

    @Override
    void setSize( ) {
        setSize( .5f, .5f, .5f );
    }

    /**------------------------- drawPrimitives --------------------------------
     *  Overridden method for drawing a disk with gluQuadrics
     */
    @Override
    protected void drawPrimitives( ) {
        JOGL.glu.gluDisk( quadric, internalRadius, externalRadius, slices,
                          rings );
    }

    /**------------------------- redraw ----------------------------------------
     *  Overridden method for redrawing a disk, applies a texture if needed
     */
    @Override
    void redraw( ) {
        if ( addTexture == true)
            redrawTexture( );
        else
            super.redraw( );
    }


    /**------------------------- redrawTexture ---------------------------------
     *  Redraws the disk with a texture applied
     */
    void redrawTexture( ) {
        JOGL.gl.glPushMatrix( );

        InputStream stream;
        try {
            stream = getClass( ).getResourceAsStream( "texture.jpg" );
            texture = TextureIO.newTexture( stream, true, "jpg");
        }
        catch ( IOException exc) {
            exc.printStackTrace( );
            System.exit( 1);
        }


        JOGL.glu.gluQuadricDrawStyle( quadric, GLU.GLU_FILL );
        JOGL.glu.gluQuadricTexture( quadric, true );
        JOGL.glu.gluQuadricNormals( quadric, GLU.GLU_SMOOTH );

        JOGL.gl.glTranslatef( xLoc, yLoc, zLoc );
        JOGL.gl.glRotatef( angle, dxRot, dyRot, dzRot );
        JOGL.gl.glScalef( xSize, ySize, zSize );

        texture.enable( JOGL.gl );

        texture.setTexParameteri( JOGL.gl, textureParam1, textureParam2 );

        texture.bind( JOGL.gl );

        drawPrimitives(  );
        JOGL.glu.gluQuadricTexture( quadric, true );
        texture.disable( JOGL.gl );

        JOGL.gl.glPopMatrix( );
    }
}
