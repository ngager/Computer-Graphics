import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;


/**
 * Quad.java
 * A Quad object constructor should accept an array of 4 Point.
 * Created by nicolegager on 9/6/14.
 */
public class Quad extends Shape {

    //---------------- instance variables ------------------------
    private boolean fillFlag = false;
    private boolean borderFlag = false;
    private PointF v1, v2, v3, v4;
    private String id;


    public static class PointF {
        private float x, y;

        //--------------------  constructors ---------------------------
        /**
         * Constructor
         */
        public PointF(float x, float y) {
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

    //--------------------  constructors ---------------------------
    /**
     * Constructor
     */


    public Quad(PointF[] v, boolean fill, boolean border, String id) {

        initialize(v, fill, border);
        this.id = id;
    }

    public void initialize(PointF[] v, boolean fill, boolean border) {

        this.fillFlag = fill;
        this.borderFlag = border;

        v1 = v[0];
        v2 = v[1];
        v3 = v[2];
        v4 = v[3];

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
        if(borderFlag) {

            setBorderColor(borderColor);
            gl.glColor3f(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue());
            gl.glBegin(GL.GL_LINE_LOOP);

            gl.glVertex2d(xLoc + v1.getX() * xSize, yLoc + v1.getY() * ySize);
            gl.glVertex2d(xLoc + v2.getX() * xSize, yLoc + v2.getY() * ySize);
            gl.glVertex2d(xLoc + v4.getX() * xSize, yLoc + v4.getY() * ySize);
            gl.glVertex2d(xLoc + v3.getX() * xSize, yLoc + v3.getY() * ySize);
            gl.glVertex2d(xLoc + v1.getX() * xSize, yLoc + v1.getY() * ySize);

            gl.glEnd();
        }

        //add a fill
        if(fillFlag) {

            setFillColor(fillColor);
            gl.glColor3f(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue());
            gl.glBegin(GL2GL3.GL_QUADS);

            gl.glVertex2d(xLoc + v1.getX() * xSize, yLoc + v1.getY() * ySize);
            gl.glVertex2d(xLoc + v2.getX() * xSize, yLoc + v2.getY() * ySize);
            gl.glVertex2d(xLoc + v4.getX() * xSize, yLoc + v4.getY() * ySize);
            gl.glVertex2d(xLoc + v3.getX() * xSize, yLoc + v3.getY() * ySize);
            gl.glVertex2d(xLoc + v1.getX() * xSize, yLoc + v1.getY() * ySize);
            gl.glEnd();
        }

        if(!fillFlag && !borderFlag) {

            gl.glColor3f(color.getRed(),  color.getGreen(), color.getBlue());
            gl.glBegin(GL2GL3.GL_QUADS);

            gl.glVertex2d(xLoc + v1.getX() * xSize, yLoc + v1.getY() * ySize);
            gl.glVertex2d(xLoc + v2.getX() * xSize, yLoc + v2.getY() * ySize);
            gl.glVertex2d(xLoc + v4.getX() * xSize, yLoc + v4.getY() * ySize);
            gl.glVertex2d(xLoc + v3.getX() * xSize, yLoc + v3.getY() * ySize);
            gl.glVertex2d(xLoc + v1.getX() * xSize, yLoc + v1.getY() * ySize);
            gl.glEnd();
        }




    }
}
