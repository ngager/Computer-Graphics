/**
 * Rectangle.java
 * A Rectangle object should be specified by a location and width, height.
 * Created by nicolegager on 9/6/14.
 */

//import java.awt.geom.*;
//import java.awt.*;

import javax.media.opengl.*;
import javax.swing.*;


public class Rectangle extends Shape {

    //---------------- instance variables ------------------------
    private boolean fillFlag = false;       //determines if we want to fill the polygon
    private boolean borderFlag = false;     //determines if we want to add a border
    private float[] v1, v2, v3, v4;         //vertices
    private String id;


    //--------------------  constructors ---------------------------
    public Rectangle(float[] x1, float[] y1, boolean fill, boolean border, String s){

        //initialize(x,y,w,h,fill,border);
        this.id = s;
        this.fillFlag = fill;
        this.borderFlag = border;
        v1 = new float[] {x1[0], y1[0]};
        v2 = new float[] {x1[1], y1[1]};
        v3 = new float[] {x1[2], y1[2]};
        v4 = new float[] {x1[3], y1[3]};

    }

    public Rectangle(float x, float y, float w, float h, boolean fill, boolean border, String s){

        initialize(x,y,w,h,fill,border);
        this.id = s;


    }

    //------------------------  methods ---------------------------
    public void initialize(float x, float y, float w, float h, boolean fill, boolean border) {

        this.fillFlag = fill;
        this.borderFlag = border;

        v1 = new float[]{x,y};
        v2 = new float[]{x+w,y};
        v3 = new float[]{x+w,y+h};
        v4 = new float[]{x,y+h};

        // access global information about JOGL components
        gl   = JOGL.gl;
        glu  = JOGL.glu;
        glut = JOGL.glut;

    }

    //------------- redraw ---------------------------
    /**
     * Need to actually do the drawing here
     */
    @Override
    public void redraw() {


        //add a border
        if (borderFlag) {

            setBorderColor(borderColor);
            gl.glColor3f(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue());
            gl.glBegin(GL2.GL_LINE_LOOP);
            gl.glVertex2f(xLoc + v1[0] * xSize, yLoc + v1[1] + ySize);
            gl.glVertex2f(xLoc + v2[0] * xSize, yLoc + v2[1] + ySize);
            gl.glVertex2f(xLoc + v3[0] * xSize, yLoc + v3[1] + ySize);
            gl.glVertex2f(xLoc + v4[0] * xSize, yLoc + v4[1] + ySize);
            gl.glVertex2f(xLoc + v1[0] * xSize, yLoc + v1[1] + ySize);

            gl.glEnd();
        }

        //add a fill
        if (fillFlag) {

            setFillColor(fillColor);
            gl.glColor3f(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue());
            gl.glBegin(GL2.GL_QUADS);

            gl.glVertex2f(xLoc + v1[0] * xSize, yLoc + v1[1] + ySize);
            gl.glVertex2f(xLoc + v2[0] * xSize, yLoc + v2[1] + ySize);
            gl.glVertex2f(xLoc + v3[0] * xSize, yLoc + v3[1] + ySize);
            gl.glVertex2f(xLoc + v4[0] * xSize, yLoc + v4[1] + ySize);
            gl.glVertex2f(xLoc + v1[0] * xSize, yLoc + v1[1] + ySize);

            gl.glEnd();
        }


        if(!fillFlag && !borderFlag){

            gl.glColor3f(color.getRed(), color.getGreen(), color.getBlue());
            gl.glBegin(GL2.GL_QUADS);

            gl.glVertex2f(xLoc + v1[0] * xSize, yLoc + v1[1] + ySize);
            gl.glVertex2f(xLoc + v2[0] * xSize, yLoc + v2[1] + ySize);
            gl.glVertex2f(xLoc + v3[0] * xSize, yLoc + v3[1] + ySize);
            gl.glVertex2f(xLoc + v4[0] * xSize, yLoc + v4[1] + ySize);
            gl.glVertex2f(xLoc + v1[0] * xSize, yLoc + v1[1] + ySize);

            gl.glEnd();

        }
    }

}
