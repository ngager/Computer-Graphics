/**
 * Triangle.java: version 1a 
 *
 * @author rdb
 * @version 0.1 08/27/13
 *
 *     This class defines a triangle object and is responsible for drawing it.
 *
 *     The class simplifies the access to the JOGL class variables, 
 *     by making copies of them as Triangle class variables at construction time;
 *          this means we use references such as 
 *              gl.glBegin();
 *          instead of 
 *              JOGL.gl.glBegin();
 *     The possible disadvantage of this approach might occur if one or more of
 *     the JOGL class variables needs to be changed, we'd need to provide an
 *     opportunity to update the local variables.
 */

//import java.awt.geom.*;
//import java.awt.*;

import javax.media.opengl.*;


public class Triangle extends Shape {

    //---------------- instance variables ------------------------
    private boolean fillFlag = false;       //determines if we want to fill the polygon
    private boolean borderFlag = false;     //determines if we want to add a border
    private float[] dx;
    private float[] dy;
    private String id;


    //--------------------  constructors ---------------------------
    /**
     * Constructor
     */
    public Triangle() {
        // default triangle is equilateral triangle with center at origin
        //    and base and height of size 1.
        float dxDefault[] = { -0.5f, 0.0f,  0.5f };
        float dyDefault[] = { -0.5f, 0.5f, -0.5f };

        initialize( dxDefault, dyDefault, false, true );
    }

    public Triangle( float[] x, float[] y, boolean fill, boolean border, String s ) {
        initialize(x, y, fill, border);
        this.id = s;


    }

    //-------------------------- initialize ------------------------
    public void initialize( float[] x, float[] y, boolean fill, boolean border ) {

        this.fillFlag = fill;
        this.borderFlag = border;

        dx = new float[ 3 ];
        dy = new float[ 3 ];
        for ( int i = 0; i < x.length; i++ ) {
            dx[ i ] = x[ i ];
            dy[ i ] = y[ i ];
        }
        // access global information about JOGL components
        gl   = JOGL.gl;
        glu  = JOGL.glu;
        glut = JOGL.glut;
    }


    //------------- redraw ---------------------------
    /**
     * Need to actually do the drawing here
     */
    public void redraw() {

        if(fillFlag) {

            //set fill color
            setFillColor(fillColor);
            gl.glColor3f(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue());
            gl.glBegin(GL2.GL_POLYGON);
            // The triangle is defined by positions relative to its location stored
            //   in the dx and dy arrays
            // The scale factor applies to the relative offset of each coordinate from the
            //    origin (which is xLoc, yLoc )

            for (int i = 0; i < dx.length; i++) {
                gl.glVertex2f(xLoc + dx[i] * xSize, yLoc + dy[i] * ySize);
            }

            gl.glEnd();


        }



        if(borderFlag) {
            //set the border color
            setBorderColor(borderColor);
            gl.glColor3f(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue());
            gl.glBegin(GL2.GL_LINE_LOOP);
            for (int i = 0; i < dx.length; i++) {
                gl.glVertex2f(xLoc + dx[i] * xSize,
                        yLoc + dy[i] * ySize);
            }

            gl.glEnd();
        }

        if(!fillFlag && !borderFlag) {


            gl.glColor3f(color.getRed(), color.getGreen(), color.getBlue());
            gl.glBegin(GL2.GL_POLYGON);
            for (int i = 0; i < dx.length; i++) {
                gl.glVertex2f(xLoc + dx[i] * xSize,
                        yLoc + dy[i] * ySize);
            }

            gl.glEnd();
        }

    }



}
