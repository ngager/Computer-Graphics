import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import java.awt.*;
import java.util.Random;

/**
 * Polygon.java
 * A Polygon object constructor can take information defining any number of points. Feel free to define
 * multiple constructors in order to simplify the task of creating objects. You should use a Point class that
 * encapsulates x and y instead have having to define separate arrays for x and y.
 * Created by nicolegager on 9/6/14.
 */
public class Polygon extends Shape {

    //---------------- instance variables ------------------------
    private boolean fillFlag = false;       //determines if we want to fill the polygon
    private boolean borderFlag = false;     //determines if we want to add a border
    private Polygon.PointF[] points;        //array of points to construct the polygon
    private String id;

    //--------------------  constructors ---------------------------
    /**
     * Constructor
     */
    public Polygon(PointF[] points, boolean fill, boolean border, String id) {
        this.points = points;
        initialize(points, fill, border);
        this.id = id;
    }



    public void initialize(PointF[] points, boolean fillFlag, boolean borderFlag) {

        this.fillFlag = fillFlag;
        this.borderFlag = borderFlag;

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

        if(borderFlag){
            setBorderColor(borderColor);
            gl.glColor3f(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue());
            gl.glBegin(GL.GL_LINE_LOOP);

            for(int index = 0; index < points.length-1; index++)
                gl.glVertex2f(xLoc + points[index].getX() * xSize, yLoc + points[index].getY() * ySize);

            gl.glEnd();
        }

        if(fillFlag){

            setFillColor(fillColor);
            gl.glColor3f(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue());
            gl.glBegin(GL2.GL_POLYGON);

            for(int index = 0; index < points.length; index++)
                gl.glVertex2f(xLoc + points[index].getX() * xSize, yLoc + points[index].getY() * ySize);

            gl.glEnd();
        }

        if(!fillFlag && !borderFlag) {

            gl.glColor3f(color.getRed(), color.getGreen(), color.getBlue());
            gl.glBegin(GL2.GL_POLYGON);

            for(int index = 0; index < points.length; index++)
                gl.glVertex2f(xLoc + points[index].getX() * xSize, yLoc + points[index].getY() * ySize);

            gl.glEnd();
        }


    }


    public static class PointF {
        private float x, y;

        //--------------------  constructors ---------------------------
        /**
         * Constructor
         */
        public PointF(float x, float y)
        {
            this.x = x;
            this.y = y;
        }

        public float getX()
        {
            return x;
        }
        public float getY()
        {
            return y;
        }


    }


}
