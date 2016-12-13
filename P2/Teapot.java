/**
 * Box.java - a class implementation representing a Box object
 *           in OpenGL
 * Oct 16, 2013
 * rdb - derived from Box.cpp
 */

public class Teapot extends Object3D
{

    //--------- instance variables -----------------
    float size;

    //------------- constructor -----------------------
    public Teapot()
    {
        size = 1;
    }

    @Override
    void setSize() {

    }

    //------------- drawPrimitives ---------------------------
    public void drawPrimitives()
    {
        JOGL.glut.glutSolidTeapot( size );
    }

    @Override
    void setLocation() {

    }
}
