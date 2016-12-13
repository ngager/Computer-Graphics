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
public class QuadricDisk extends Object3D {

    GLUquadric quadric;
    int internalRadius;
    int externalRadius;
    int slices;
    int rings;
    Texture texture;

    /**-------------------------- Constructors ------------------------------**/
    public QuadricDisk(GLUquadric quad, int internal, int external, int slices, int rings){

        this.quadric = quad;
        this.internalRadius = internal;
        this.externalRadius = external;
        this.slices = slices;
        this.rings = rings;
    }

    @Override
    void setLocation() {

    }

    @Override
    void setSize() {

    }

    @Override
    protected void drawPrimitives() {
        JOGL.glu.gluDisk(quadric, internalRadius, externalRadius, slices, rings);


    }

    @Override
    void redraw() {

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

        drawPrimitives();
        JOGL.glu.gluQuadricTexture(quadric, true);
        texture.disable(JOGL.gl);

    }
}
