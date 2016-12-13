import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by nicolegager on 10/21/14.
 */
public class QuadricSphere extends Object3D {

    GLUquadric quadric;
    double radius;
    int slices;
    int stacks;
    Texture texture;

    /**-------------------------- Constructors ------------------------------**/
    public QuadricSphere(GLUquadric quadric, double radius, int slices, int stacks){
        this.quadric = quadric;
        this.radius = radius;
        this.slices = slices;
        this.stacks = stacks;
        this.texture = null;

    }
    @Override
    void setLocation() {

    }

    @Override
    void setSize() {

    }

    @Override
    protected void drawPrimitives() {
        JOGL.glu.gluSphere(quadric, radius, slices, stacks);
    }

    @Override
    void redraw() {
        if (texture != null){
            redrawTexture();
        }
        else {
            JOGL.gl.glPushMatrix();
            JOGL.gl.glTranslatef(xLoc, yLoc, zLoc);
            JOGL.gl.glRotatef(angle, dxRot, dyRot, dzRot);
            JOGL.gl.glScalef(xSize, ySize, zSize);
            drawPrimitives();
            JOGL.gl.glPopMatrix();
        }


    }

    void redrawTexture() {

        try {
            InputStream stream = getClass().getResourceAsStream( "texture.jpg" );
            texture = TextureIO.newTexture(stream, true, "jpg");
        }
        catch (IOException exc) {
            exc.printStackTrace();
            System.exit(1);
        }

        float[] rgba = {1f, 1f, 1f};
        JOGL.gl.glMaterialfv(JOGL.gl.GL_FRONT, JOGL.gl.GL_AMBIENT, rgba, 0);
        JOGL.gl.glMaterialfv(JOGL.gl.GL_FRONT, JOGL.gl.GL_SPECULAR, rgba, 0);
        JOGL.gl.glMaterialf(JOGL.gl.GL_FRONT, JOGL.gl.GL_SHININESS, 0.5f);

        JOGL.glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
        JOGL.glu.gluQuadricTexture(quadric, true);
        JOGL.glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);

        texture.enable(JOGL.gl);

        texture.setTexParameteri( JOGL.gl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR );
        texture.setTexParameteri( JOGL.gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR );
        texture.setTexParameteri( JOGL.gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT );
        texture.setTexParameteri( JOGL.gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT );

        texture.bind(JOGL.gl);


        JOGL.glu.gluQuadricTexture(quadric, true);
        texture.disable(JOGL.gl);

    }

}
