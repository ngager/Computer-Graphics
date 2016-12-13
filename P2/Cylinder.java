/**
 * Box.java - a class implementation representing a Box object
 *           in OpenGL
 * Oct 16, 2013
 * rdb - derived from Box.cpp
 */

public class Cylinder extends Object3D
{
    //--------- instance variables -----------------
    float radius;
    float height;
    int slices;
    int stacks;

    //------------- constructor -----------------------
    public Cylinder()
    {

        radius = 1;
        height = .1f;
        slices = 100;
        stacks = 1;
    }

    @Override
    void setLocation() {

    }

    @Override
    void setSize() {

    }



    //------------- drawPrimitives ---------------------------
    public void drawPrimitives()
    {
        JOGL.glut.glutSolidCylinder( radius, height, slices, stacks );
    }
}
