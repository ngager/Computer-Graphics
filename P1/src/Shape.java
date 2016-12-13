

import java.awt.Color;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.swing.*;

import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Shape.java - a simple abstract class to represent graphical objects in a 
 *              JOGL environment
 *
 * @author rdb
 * Derived from Shape.cpp/h
 * 27 August 2013
 * ------- edit history -----------------------------------------
 * 07-22-14: Moved the static variables, GL2, GLUT, GLU from
 *               children classes to here; abstract parent.
 * 08-10-14: Made instance variables protected instead of package.
 *
 */

public abstract class Shape
{
    //---------------- class variables ------------------------
    public static GL2  gl = null;   // GL2 encapsulation of the openGL state

    public static GLUT glut = null; // the GLUT state
    public static GLU  glu  = null; // the GLU  state

    //------------------------- instance variables -----------------------
    protected float xLoc, yLoc;       // location (origin) of the object
    protected float xSize, ySize;     // size of the object
    protected Color color;            // color of object
    protected Color borderColor;
    protected Color fillColor;
    protected Boolean fillFlag, borderFlag;
    protected String id;
    //---------------------------- Constructor ---------------------------
    public Shape()
    {
        setColor( Color.RED ); // default color is red 
        setLocation( 0, 0 );
        setSize( 1, 1 );
    }

    //-------------------------- public methods --------------------------
    //----------------------------- redraw -------------------------------
    /**
     * Abstract redraw method must be defined by subclasses.
     */
    abstract public void redraw();

    //------------------ setLocation( float, float ) ------------------------
    /**
     * Set the location of object to the x,y position defined by the args. 
     */
    public void setLocation( float x, float y )
    {
        xLoc = x;
        yLoc = y;
    }
    //------------------ getX() ------------------------
    /**
     * Return the value of the x origin of the shape.
     */
    public float getX()
    {
        return xLoc;
    }
    //------------------ getY() ------------------------
    /**
     * Return the value of the y origin of the shape. 
     */
    public float getY()
    {
        return yLoc;
    }
    //------------------ setColor( Color ) -------------------
    /**
     * Set the "nominal" color of the object to the specified color. This 
     *   does not require that ALL components of the object must be the same 
     *   color. Typically, the largest component will take on this color, 
     *   but the decision is made by the child class. 
     */
    public void setColor( Color col )
    {
        color = col;
    }
    //------------------ setSize( float, float ) ------------------------    
    /**
     * Set the size of the shape to be scaled by xs, ys. 
     *    That is, the shape has an internal fixed size, the shape parameters 
     *    scale that internal size. 
     */
    public void setSize( float xs, float ys )
    {
        xSize = xs;
        ySize = ys;

    }
    //------------------ setBorderColor( Color ) ------------------------
    /**
     * Sets the color of the border.
     */
    public void setBorderColor( Color col )
    {
        this.borderColor = col;
    }
    //------------------ setFillColor( Color ) ------------------------
    /**
     * Sets the color of the fill.
     */
    public void setFillColor( Color col )
    {
        this.fillColor = col;
    }

    public void setBorderFlag( Boolean flag ){ this.borderFlag = flag; }
    public void setFillFlag( Boolean flag ){ this.fillFlag = flag; }
    public void setID( String id){this.id = id;}
    public String getID( ){ return id;}


}
