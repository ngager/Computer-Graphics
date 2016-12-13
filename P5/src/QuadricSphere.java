import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import java.io.IOException;
import java.io.InputStream;

/** QuadricSphere.java
 *  Creates a subclass of Object3D that draws a gluQuadric Sphere object and has
 *  texture functionality
 */
public class QuadricSphere extends Object3D {

    //-------------------------- instance variables --------------------------//
    GLUquadric quadric;
    double radius;
    int slices;
    int stacks;
    Texture texture;
    Boolean addTexture;

    /**------------------------- Constructor -----------------------------------
     *  Constructor for QuadricSphere
     */
    public QuadricSphere(GLUquadric quad, double r, int slices, int stacks, Boolean addTexture){
        this.quadric = quad;
        this.radius = r;
        this.slices = slices;
        this.stacks = stacks;
        this.texture = null;
        this.addTexture = addTexture;
    }


    @Override
    void setLocation() { }

    @Override
    void setSize() { }


    /**------------------------- drawPrimitives --------------------------------
     *  Overridden method for drawing a sphere with gluQuadrics
     */
    @Override
    protected void drawPrimitives() {
        JOGL.glu.gluSphere(quadric, radius, slices, stacks);
    }


    /**------------------------- redraw ----------------------------------------
     *  Overridden method for redrawing sphere based on whether a texture is
     *  used or not
     */
    @Override
    void redraw() {

        if (addTexture == true)
            redrawTexture();
        else
            super.redraw();

    }


    /**------------------------- redrawTexture ---------------------------------
     *  Method for redrawing the QuadricSphere if a texture is used
     */
    void redrawTexture() {

        JOGL.gl.glPushMatrix();

        try {
            InputStream stream = getClass().getResourceAsStream("orange.jpg");
            texture = TextureIO.newTexture(stream, true, "jpg");
        }
        catch (IOException exc) {
            exc.printStackTrace();
            System.exit(1);
        }

        JOGL.glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
        JOGL.glu.gluQuadricTexture(quadric, true);
        JOGL.glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);

        JOGL.gl.glTranslatef(xLoc, yLoc, zLoc);
        JOGL.gl.glRotatef(angle, dxRot, dyRot, dzRot);
        JOGL.gl.glScalef(xSize, ySize, zSize);

        texture.enable(JOGL.gl);

        texture.setTexParameteri(JOGL.gl, textureParam1, textureParam2);

        texture.bind(JOGL.gl);

        drawPrimitives();
        JOGL.glu.gluQuadricTexture(quadric, true);
        texture.disable(JOGL.gl);

        JOGL.gl.glPopMatrix();


    }

}
