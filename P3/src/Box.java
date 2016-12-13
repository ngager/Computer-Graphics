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
/**
 * Box.java - a class implementation representing a Box object in OpenGL
 * Oct 16, 2013
 * rdb - derived from Box.cpp
 */

public class Box extends Object3D {
    //---------------------------instance variables --------------------------//
    float length;
    Texture texture;
    
    //----------------------------constructor --------------------------------//
    public Box( float length ) {
        this.length = length;
        this.texture = null;
    }

    public Box(float length, Texture t) {
        this.length = length;
        this.texture = t;
    }
    @Override
    void setLocation() {

    }

    @Override
    void setSize() {

    }

    @Override
    protected void drawPrimitives() {
        makeBox();
    }


    public void makeBox( ) {

        if(texture != null) {
            texture.enable(JOGL.gl);
            texture.bind(JOGL.gl);
        }

        JOGL.gl.glBegin(JOGL.gl.GL_QUADS);
        JOGL.gl.glColor3f(1f, 1f, 0f);

        /**----------------------- front face -------------------------------**/
        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(0.0f, 0.0f, 1.0f);           // First normal
        JOGL.gl.glVertex3f(-length, -length,  length);
        JOGL.gl.glVertex3f( length, -length,  length);
        JOGL.gl.glVertex3f( length,  length,  length);
        JOGL.gl.glVertex3f(-length,  length,  length);

        /**----------------------- back face --------------------------------**/
        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(0.0f, 0.0f, -1.0f);          // Second normal
        JOGL.gl.glVertex3f( length, -length, -length);
        JOGL.gl.glVertex3f(-length, -length, -length);
        JOGL.gl.glVertex3f(-length,  length, -length);
        JOGL.gl.glVertex3f(length,  length, -length);

        /**----------------------- top face ---------------------------------**/
        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(0.0f, 1.0f, 0.0f);           // Third normal
        JOGL.gl.glVertex3f(-length,  length,  length);
        JOGL.gl.glVertex3f( length,  length,  length);
        JOGL.gl.glVertex3f( length,  length, -length);
        JOGL.gl.glVertex3f(-length,  length, -length);

        /**----------------------- bottom face ------------------------------**/
        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(0.0f, -1.0f, 0.0f);          // Fourth normal
        JOGL.gl.glVertex3f( length, -length,  length);
        JOGL.gl.glVertex3f(-length, -length,  length);
        JOGL.gl.glVertex3f(-length, -length, -length);
        JOGL.gl.glVertex3f( length, -length, -length);

        /**----------------------- right face -------------------------------**/
        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(1.0f, 0.0f, 0.0f);           // Fifth normal
        JOGL.gl.glVertex3f( length, -length,  length);
        JOGL.gl.glVertex3f( length, -length, -length);
        JOGL.gl.glVertex3f( length,  length, -length);
        JOGL.gl.glVertex3f( length,  length,  length);

        /**----------------------- left face --------------------------------**/
        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(-1.0f, 0.0f, 0.0f);          // Sixth normal
        JOGL.gl.glVertex3f(-length, -length, -length);
        JOGL.gl.glVertex3f(-length, -length,  length);
        JOGL.gl.glVertex3f(-length,  length,  length);
        JOGL.gl.glVertex3f(-length,  length, -length);

        JOGL.gl.glEnd();


    }


    /**---------------------------- redraw -------------------------------------
     *

    public void redraw( ) {
        if (texture != null){
            redrawTexture();
        }
        else {
            JOGL.gl.glPushMatrix();
            JOGL.gl.glTranslatef(xLoc, yLoc, zLoc);
            JOGL.gl.glRotatef(angle, dxRot, dyRot, dzRot);
            JOGL.gl.glScalef(xSize, ySize, zSize);
            //JOGL.gl.glColorMaterial(JOGL.gl.GL_FRONT_AND_BACK, JOGL.gl.GL_SPECULAR);
            makeBox();
            JOGL.gl.glPopMatrix();

        }

    }*/



    /**---------------------------- redrawTexture ------------------------------
     *
     */
    private void redrawTexture() {

       /* JOGL.gl.glMatrixMode(JOGL.gl.GL_MODELVIEW);
        texture.enable(JOGL.gl);
        JOGL.gl.glTexCoord2f(0.0f, 0.0f);
        JOGL.gl.glTexCoord2f(1.0f, 0.0f);
        JOGL.gl.glTexCoord2f(1.0f, 1.0f);
        JOGL.gl.glTexCoord2f(0.0f, 1.0f);
        JOGL.gl.glEnd();

        JOGL.gl.glTranslatef(xLoc, yLoc, zLoc);
        JOGL.gl.glRotatef(angle, dxRot, dyRot, dzRot);
        JOGL.gl. glScalef( (1.75f * ((float)texture.getWidth()/texture.getHeight()) * xSize), 1.75f * ySize, 1.0f );
        //makeBox(JOGL.gl);
        texture.disable(JOGL.gl);*/
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

        texture.setTexParameteri( JOGL.gl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR );
        texture.setTexParameteri( JOGL.gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR );
        texture.setTexParameteri( JOGL.gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT );
        texture.setTexParameteri( JOGL.gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT );
        texture.bind(JOGL.gl);

        JOGL.gl.glEnable( JOGL.gl.GL_TEXTURE_2D);

        JOGL.gl.glBegin(JOGL.gl.GL_QUADS);
        JOGL.gl.glColor3f(1f, 1f, 0f);

        /**----------------------- front face -------------------------------**/
        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(0.0f, 0.0f, 1.0f);           // First normal
        JOGL.gl.glTexCoord2f(0.0f, 0.0f); JOGL.gl.glVertex3f(-length, -length,  length);
        JOGL.gl.glTexCoord2f(1.0f, 0.0f); JOGL.gl.glVertex3f( length, -length,  length);
        JOGL.gl.glTexCoord2f(1.0f, 1.0f); JOGL.gl.glVertex3f( length,  length,  length);
        JOGL.gl.glTexCoord2f(0.0f, 1.0f); JOGL.gl.glVertex3f(-length,  length,  length);

        /**----------------------- back face --------------------------------**/
        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(0.0f, 0.0f, -1.0f);          // Second normal
        JOGL.gl.glTexCoord2f(0.0f, 0.0f); JOGL.gl.glVertex3f( length, -length, -length);
        JOGL.gl.glTexCoord2f(1.0f, 0.0f); JOGL.gl.glVertex3f(-length, -length, -length);
        JOGL.gl.glTexCoord2f(1.0f, 1.0f); JOGL.gl.glVertex3f(-length,  length, -length);
        JOGL.gl.glTexCoord2f(0.0f, 1.0f); JOGL.gl.glVertex3f(length,  length, -length);

        /**----------------------- top face ---------------------------------**/
        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(0.0f, 1.0f, 0.0f);           // Third normal
        JOGL.gl.glTexCoord2f(0.0f, 0.0f); JOGL.gl.glVertex3f(-length,  length,  length);
        JOGL.gl.glTexCoord2f(1.0f, 0.0f); JOGL.gl.glVertex3f( length,  length,  length);
        JOGL.gl.glTexCoord2f(1.0f, 1.0f); JOGL.gl.glVertex3f( length,  length, -length);
        JOGL.gl.glTexCoord2f(0.0f, 1.0f); JOGL.gl.glVertex3f(-length,  length, -length);

        /**----------------------- bottom face ------------------------------**/
        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(0.0f, -1.0f, 0.0f);          // Fourth normal
        JOGL.gl.glTexCoord2f(0.0f, 0.0f); JOGL.gl.glVertex3f( length, -length,  length);
        JOGL.gl.glTexCoord2f(1.0f, 0.0f); JOGL.gl.glVertex3f(-length, -length,  length);
        JOGL.gl.glTexCoord2f(1.0f, 1.0f); JOGL.gl.glVertex3f(-length, -length, -length);
        JOGL.gl.glTexCoord2f(0.0f, 1.0f); JOGL.gl.glVertex3f( length, -length, -length);

        /**----------------------- right face -------------------------------**/
        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(1.0f, 0.0f, 0.0f);           // Fifth normal
        JOGL.gl.glTexCoord2f(0.0f, 0.0f); JOGL.gl.glVertex3f( length, -length,  length);
        JOGL.gl.glTexCoord2f(1.0f, 0.0f); JOGL.gl.glVertex3f( length, -length, -length);
        JOGL.gl.glTexCoord2f(1.0f, 1.0f); JOGL.gl.glVertex3f( length,  length, -length);
        JOGL.gl.glTexCoord2f(0.0f, 1.0f); JOGL.gl.glVertex3f( length,  length,  length);

        /**----------------------- left face --------------------------------**/
        JOGL.gl.glShadeModel(JOGL.gl.GL_FLAT);
        JOGL.gl.glNormal3f(-1.0f, 0.0f, 0.0f);          // Sixth normal
        JOGL.gl.glTexCoord2f(0.0f, 0.0f); JOGL.gl.glVertex3f(-length, -length, -length);
        JOGL.gl.glTexCoord2f(1.0f, 0.0f); JOGL.gl.glVertex3f(-length, -length,  length);
        JOGL.gl.glTexCoord2f(1.0f, 1.0f); JOGL.gl.glVertex3f(-length,  length,  length);
        JOGL.gl.glTexCoord2f(0.0f, 1.0f); JOGL.gl.glVertex3f(-length,  length, -length);

        JOGL.gl.glEnd();
        JOGL.gl.glDisable( JOGL.gl.GL_TEXTURE_2D);

    }




    @Override
    void redraw() {

        redrawTexture();
        /*try {
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
        texture.enable(JOGL.gl);
        JOGL.gl.glTexCoord2f(0.0f, 0.0f);
        JOGL.gl.glTexCoord2f(1.0f, 0.0f);
        JOGL.gl.glTexCoord2f(1.0f, 1.0f);
        JOGL.gl.glTexCoord2f(0.0f, 1.0f);



        texture.setTexParameteri( JOGL.gl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR );
        texture.setTexParameteri( JOGL.gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR );
        texture.setTexParameteri( JOGL.gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT );
        texture.setTexParameteri( JOGL.gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT );

        texture.bind(JOGL.gl);

        drawPrimitives();
        texture.disable(JOGL.gl);
        */

    }


}
