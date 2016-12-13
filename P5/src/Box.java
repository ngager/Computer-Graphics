import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
/** Box.java
 *  Creates a 3D Box through OpenGL calls that define the Box vertices with
 *  normals for each face. If a texture is assigned to a Box object, the texture
 *  is mapped to each face of the Box.
 */

public class Box extends Object3D {

    //-------------------------- Instance Variables --------------------------//

    float length;               // Length of box's sides
    Boolean addTexture;         // True if box has texture

    /**------------------------- Constructor -----------------------------------
     *  Constructor for Box
     */
    public Box( float length ) {
        this.length = length;
        this.addTexture = false;
    }

    public Box(float length, boolean add) {
        this.length = length;
        this.addTexture = add;
    }

    @Override
    void setLocation() { }

    @Override
    void setSize() { }

    @Override
    public void drawPrimitives() { }


    /**--------------------- redraw --------------------------------------------
     * Redraws a box based on whether a texture is to be added or not
     */
    @Override
    void redraw() {
        if (addTexture == true)
            redrawTexture( );
        else
            makeBox();
    }


    /**--------------------- makeFrontFace -------------------------------------
     *  Makes front face of a basic box
     */
    private void makeFrontFace( ) {

        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(0.0f, 0.0f, 1.0f);           // First normal
        JOGL.gl.glVertex3f(-length, -length,  length);
        JOGL.gl.glVertex3f( length, -length,  length);
        JOGL.gl.glVertex3f( length,  length,  length);
        JOGL.gl.glVertex3f(-length,  length,  length);

    }


    /**--------------------- makeBackFace --------------------------------------
     *  Makes back face of a basic box
     */
    private void makeBackFace( ) {

        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(0.0f, 0.0f, -1.0f);          // Second normal
        JOGL.gl.glVertex3f( length, -length, -length);
        JOGL.gl.glVertex3f(-length, -length, -length);
        JOGL.gl.glVertex3f(-length,  length, -length);
        JOGL.gl.glVertex3f(length,  length, -length);

    }

    /**--------------------- makeTopFace --------------------------------------
     *  Makes top face of a basic box
     */
    private void makeTopFace( ) {

        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(0.0f, 1.0f, 0.0f);           // Third normal
        JOGL.gl.glVertex3f(-length,  length,  length);
        JOGL.gl.glVertex3f( length,  length,  length);
        JOGL.gl.glVertex3f( length,  length, -length);
        JOGL.gl.glVertex3f(-length,  length, -length);

    }

    /**--------------------- makeBottomFace ------------------------------------
     *  Makes bottom face of a basic box
     */
    private void makeBottomFace( ) {

        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(0.0f, -1.0f, 0.0f);          // Fourth normal
        JOGL.gl.glVertex3f( length, -length,  length);
        JOGL.gl.glVertex3f(-length, -length,  length);
        JOGL.gl.glVertex3f(-length, -length, -length);
        JOGL.gl.glVertex3f( length, -length, -length);

    }


    /**--------------------- makeRightFace ------------------------------------
     *  Makes right face of a basic box
     */
    private void makeRightFace( ) {

        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(1.0f, 0.0f, 0.0f);           // Fifth normal
        JOGL.gl.glVertex3f( length, -length,  length);
        JOGL.gl.glVertex3f( length, -length, -length);
        JOGL.gl.glVertex3f( length,  length, -length);
        JOGL.gl.glVertex3f( length,  length,  length);

    }

    /**--------------------- makeLeftFace ------------------------------------
     *  Makes left face of a basic box
     */
    private void makeLeftFace( ) {

        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(-1.0f, 0.0f, 0.0f);          // Sixth normal
        JOGL.gl.glVertex3f(-length, -length, -length);
        JOGL.gl.glVertex3f(-length, -length,  length);
        JOGL.gl.glVertex3f(-length,  length,  length);
        JOGL.gl.glVertex3f(-length,  length, -length);

    }


    /**---------------------------- makeBox ------------------------------------
     *  Makes a basic box with 6 faces without texture
     */
    private void makeBox( ) {

        JOGL.gl.glBegin(JOGL.gl.GL_QUADS);
        JOGL.gl.glColor3f(0f, 1f, 1f);


        // Draw the box
        makeFrontFace();
        makeBackFace();
        makeTopFace();
        makeBottomFace();
        makeRightFace();
        makeLeftFace();

        JOGL.gl.glEnd();
    }




    //************************ TEXTURE FUNCTIONALITY *************************//
    //************************************************************************//

    /**--------------------- makeFrontFaceTexture ------------------------------
     *  Makes front face of a box with a texture mapped to it
     */
    private void makeFrontFaceTexture( ) {

        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(0.0f, 0.0f, 1.0f);           // First normal
        JOGL.gl.glTexCoord2f(0.0f, 0.0f); JOGL.gl.glVertex3f(-length, -length, length);
        JOGL.gl.glTexCoord2f(1.0f, 0.0f); JOGL.gl.glVertex3f( length, -length, length);
        JOGL.gl.glTexCoord2f(1.0f, 1.0f); JOGL.gl.glVertex3f( length,  length, length);
        JOGL.gl.glTexCoord2f(0.0f, 1.0f); JOGL.gl.glVertex3f(-length,  length, length);

    }


    /**--------------------- makeBackFaceTexture -------------------------------
     *  Makes back face of a box with a texture mapped to it
     */
    private void makeBackFaceTexture( ) {

        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(0.0f, 0.0f, -1.0f);          // Second normal
        JOGL.gl.glTexCoord2f(0.0f, 0.0f); JOGL.gl.glVertex3f( length, -length, -length);
        JOGL.gl.glTexCoord2f(1.0f, 0.0f); JOGL.gl.glVertex3f(-length, -length, -length);
        JOGL.gl.glTexCoord2f(1.0f, 1.0f); JOGL.gl.glVertex3f(-length,  length, -length);
        JOGL.gl.glTexCoord2f(0.0f, 1.0f); JOGL.gl.glVertex3f(length,  length, -length);

    }



    /**--------------------- makeTopFaceTexture --------------------------------
     *  Makes top face of a box with a texture mapped to it
     */
    private void makeTopFaceTexture( ) {

        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(0.0f, 1.0f, 0.0f);           // Third normal
        JOGL.gl.glTexCoord2f(0.0f, 0.0f); JOGL.gl.glVertex3f(-length,  length,  length);
        JOGL.gl.glTexCoord2f(1.0f, 0.0f); JOGL.gl.glVertex3f( length,  length,  length);
        JOGL.gl.glTexCoord2f(1.0f, 1.0f); JOGL.gl.glVertex3f( length,  length, -length);
        JOGL.gl.glTexCoord2f(0.0f, 1.0f); JOGL.gl.glVertex3f(-length,  length, -length);

    }


    /**--------------------- makeBottomFaceTexture -----------------------------
     *  Makes bottom face of a box with a texture mapped to it
     */
    private void makeBottomFaceTexture( ) {

        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(0.0f, -1.0f, 0.0f);          // Fourth normal
        JOGL.gl.glTexCoord2f(0.0f, 0.0f); JOGL.gl.glVertex3f( length, -length,  length);
        JOGL.gl.glTexCoord2f(1.0f, 0.0f); JOGL.gl.glVertex3f(-length, -length,  length);
        JOGL.gl.glTexCoord2f(1.0f, 1.0f); JOGL.gl.glVertex3f(-length, -length, -length);
        JOGL.gl.glTexCoord2f(0.0f, 1.0f); JOGL.gl.glVertex3f( length, -length, -length);

    }



    /**--------------------- makeRightFaceTexture ------------------------------
     *  Makes right face of a box with a texture mapped to it
     */
    private void makeRightFaceTexture( ) {

        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(1.0f, 0.0f, 0.0f);           // Fifth normal
        JOGL.gl.glTexCoord2f(0.0f, 0.0f); JOGL.gl.glVertex3f( length, -length,  length);
        JOGL.gl.glTexCoord2f(1.0f, 0.0f); JOGL.gl.glVertex3f( length, -length, -length);
        JOGL.gl.glTexCoord2f(1.0f, 1.0f); JOGL.gl.glVertex3f( length,  length, -length);
        JOGL.gl.glTexCoord2f(0.0f, 1.0f); JOGL.gl.glVertex3f( length,  length,  length);

    }

    /**--------------------- makeLeftFaceTexture -------------------------------
     *  Makes left face of a box with a texture mapped to it
     */
    private void makeLeftFaceTexture( ) {

        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(-1.0f, 0.0f, 0.0f);          // Sixth normal
        JOGL.gl.glTexCoord2f(0.0f, 0.0f); JOGL.gl.glVertex3f(-length, -length, -length);
        JOGL.gl.glTexCoord2f(1.0f, 0.0f); JOGL.gl.glVertex3f(-length, -length,  length);
        JOGL.gl.glTexCoord2f(1.0f, 1.0f); JOGL.gl.glVertex3f(-length,  length,  length);
        JOGL.gl.glTexCoord2f(0.0f, 1.0f); JOGL.gl.glVertex3f(-length,  length, -length);

    }


    /**---------------------------- drawTextureBox -----------------------------
     * Draws a textured box with 6 faces
     */
    void drawTextureBox(){
        makeFrontFaceTexture();
        makeBackFaceTexture();
        makeTopFaceTexture();
        makeBottomFaceTexture();
        makeRightFaceTexture();
        makeLeftFaceTexture();
    }


    /**---------------------------- redrawTexture ------------------------------
     * Redraws a box with a texture
     */
    private void redrawTexture( ) {

        JOGL.gl.glPushMatrix();

        try {
            InputStream stream = getClass().getResourceAsStream( "texture.jpg" );
            texture = TextureIO.newTexture(stream, true, "jpg");
        }
        catch (IOException exc) {
            exc.printStackTrace();
            System.exit(1);
        }
        JOGL.gl.glMatrixMode(JOGL.gl.GL_MODELVIEW);
        JOGL.gl.glTranslatef(xLoc, yLoc, zLoc);
        JOGL.gl.glRotatef(angle, dxRot, dyRot, dzRot);
        JOGL.gl.glScalef( (1.75f * ((float)texture.getWidth()/texture.getHeight()) * xSize), 1.75f * ySize, 1.0f );

        float[] rgba = {1f, 1f, 1f};
        JOGL.gl.glMaterialfv(JOGL.gl.GL_FRONT, JOGL.gl.GL_AMBIENT, rgba, 0);
        JOGL.gl.glMaterialfv(JOGL.gl.GL_FRONT, JOGL.gl.GL_SPECULAR, rgba, 0);
        JOGL.gl.glMaterialf(JOGL.gl.GL_FRONT, JOGL.gl.GL_SHININESS, 0.5f);

        JOGL.gl.glTranslatef(xLoc, yLoc, zLoc);
        JOGL.gl.glRotatef(angle, dxRot, dyRot, dzRot);
        JOGL.gl.glScalef(xSize, ySize, zSize);

        texture.setTexParameteri( JOGL.gl, this.textureParam1 , this.textureParam2 );
        texture.bind(JOGL.gl);

        JOGL.gl.glEnable( JOGL.gl.GL_TEXTURE_2D);
        JOGL.gl.glBegin(JOGL.gl.GL_QUADS);
        JOGL.gl.glColor3f(1f, 1f, 1f);

        drawTextureBox();                               // Draw the box

        JOGL.gl.glEnd();
        JOGL.gl.glDisable( JOGL.gl.GL_TEXTURE_2D);
        JOGL.gl.glPopMatrix();
    }







}
