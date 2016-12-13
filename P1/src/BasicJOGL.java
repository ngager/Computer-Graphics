/**
 * JOGLDemo -- A class to create a very simple 2D scene with JOGL
 *
 * @author rdb
 *
 * Derived loosely from code written by Derek Dupuis, Fall 2012.
 *   The overall framework for Derek's code came from OpenGL demo code
 *   he found on the web, but did not identify.
 * A few framework modifications were based on the tutorial by Justin
 *   Stoecker at
 *   https://sites.google.com/site/justinscsstuff/jogl-tutorials
 * It looks like Derek's code may have come from the Stoecker tutorial,
 *   but there are a few differences.
 * The version available to me was dated 2011.
 *
 * The main class is responsible for creating the needed JOGL objects:
 *     gl:   the openGL Drawable reference
 *     glut: the GLUT reference
 *     glu:  the GL Utility reference
 *  and assigning them to the JOGL "holder" class.

 *
 *  Once this class has created these objects, it does not maintain a local
 *  copy, but always references the objects through the JOGL variable.
 *
 * @version 0.1 08/23/2013
 * @version 0.2 09/30/2013
 *     - discovered an error in display method; was initializing ModelView
 *       stack AFTER calling render.
 *     - added Animator creation, but don't use it????
 *     - added some comments
 */
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.*;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;
import javax.swing.ButtonGroup;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class BasicJOGL extends JFrame implements GLEventListener, MouseListener, MouseMotionListener{

    //-------------------- instance variables --------------------------
    private int width, height, xLocation, yLocation;
    private int triangleCount, rectangleCount, quadCount, polygonCount;
    private float mouseX, mouseY;

    private GLCanvas canvas;
    private boolean drawTriangle, drawRectangle, drawQuad, drawPolygon; //indicates which shape is to be next drawn
    final JFrame interaction_window;

    private	JList fillList, borderList;
    String[] listColorNames = { "blue", "green", "yellow", "white" };
    Color[] listColorValues = { Color.BLUE, Color.GREEN, Color.YELLOW, Color.WHITE };
    Color fillColor, borderColor;
    Boolean fillFlag, borderFlag, slid;
    JSlider xSlider, ySlider;

    private ArrayList<Shape> sceneObjs;
    private ArrayList<Polygon.PointF> coordinates;
    private ArrayList<Quad.PointF> quad_coordinates;



    //------------------ constructors ----------------------------------
    public BasicJOGL( int w, int h ) {
        super( "BasicJOGL" );
        width = w;
        height = h;
        fillColor = Color.BLUE;
        borderColor = Color.BLUE;
        fillFlag = false;
        borderFlag = false;
        slid = false;
        xLocation = 0;
        yLocation = 0;

        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities( glp );

        canvas = new GLCanvas( caps );
        canvas.addGLEventListener( this );
        canvas.addMouseListener(this);

        this.setSize( width, height );
        this.add( canvas );
        this.setVisible( true );
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        // terminate the program when the window is asked to close
        this.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );


        //start the animator
        Animator animator = new Animator(canvas);
        animator.start();

        //create interaction window
        interaction_window = new JFrame();
        interaction_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //create panel
        JPanel panel = new JPanel();
        panel.setPreferredSize (new Dimension(500,500));
        panel.setBackground(Color.CYAN);



        //create buttons
        addButtons(panel);


        JLabel fillLabel = new JLabel("Fill Color:  ");
        JLabel borderLabel = new JLabel("Border Color:                                                                    ");

        panel.add(fillLabel);
        panel.add(borderLabel);

        //add JLists
        addJLists(panel);

        //add checkboxes
        addCheckBoxes(panel);

        JLabel myLabel = new JLabel("Delete Shape: ");

        panel.add(myLabel);

        //text field
        addDeleteTextField(panel);

        //create sliders
        addSliders(panel);

        //display interaction window
        interaction_window.getContentPane().add(panel);
        interaction_window.pack();
        interaction_window.setVisible(true);
    }



    //+++++++++++++++  GLEventListener override methods ++++++++++++++++++++
    //-------------------- display -------------------------------------
    /**
     * Override the parent display method
     *    In this framework, the display method is responsible for setting
     *       up the projection specification, but the "render" method
     *       is responsible for the View and Model specifications.
     *
     *    This display method is reasonably application-independent;
     *      It defines a pattern that can be reused with the exception
     *      of the specifying the actual objects to render.
     */
    @Override
    public void display( GLAutoDrawable drawable )
    {
        // set op projection matrix
        JOGL.gl.glMatrixMode( GL2.GL_PROJECTION );
        JOGL.gl.glLoadIdentity();

        // Set up a projection specification that will "see" objects
        //    defined in a coordinate system plane and "window" that
        //    approximates the size of the window.
        JOGL.glu.gluOrtho2D( 0.0, width, 0.0, height );
        //JOGL.glu.gluOrtho2D( -1, 1, -1, 1 );


        // Change back to model view matrix and initialize it
        JOGL.gl.glMatrixMode( GL2.GL_MODELVIEW );
        JOGL.gl.glLoadIdentity();

        render( drawable );
    }

    //--------------------- dispose ------------------------------
    @Override
    public void dispose( GLAutoDrawable arg0 ){
        // nothing to dispose of...
    }

    //--------------------- init ------------------------------
    @Override
    public void init( GLAutoDrawable drawable ){

        System.err.println( "init" );

        //initialize variables
        this.drawPolygon = false;
        this.drawRectangle = false;
        this.drawQuad = false;
        this.drawTriangle = false;

        this.sceneObjs = new ArrayList<Shape>();
        this.coordinates = new ArrayList<Polygon.PointF>();
        this.quad_coordinates = new ArrayList<Quad.PointF>();


        //reset count
        quadCount = 0;
        polygonCount = 0;
        rectangleCount = 0;
        triangleCount = 0;

        JOGL.gl = drawable.getGL().getGL2();

        JOGL.gl.setSwapInterval( 0 );  // animation event occurs (maybe)
        //   only at end of frame draw.
        //  0 => render as fast as possible
        JOGL.glu = new GLU();
        JOGL.glut = new GLUT();

    }
    //--------------------- reshape ----------------------------------------
    /**
     * Window has been resized, readjust internal information
     */
    @Override
    public void reshape( GLAutoDrawable drawable, int x, int y, int w, int h )
    {
        JOGL.gl = drawable.getGL().getGL2();
        JOGL.gl.glViewport( 0, 0, w, h );
        System.out.println( "Viewport size: " + w + " x " + h );
    }


    //---------------------- render ---------------------------------------
    /**
     * Do the actual drawing
     */
    private void render( GLAutoDrawable drawable ) {
        //System.err.println( "render" );
        JOGL.gl.glClear( GL2.GL_COLOR_BUFFER_BIT );

        for ( Shape s: sceneObjs ) { s.redraw( ); }
        //makeScene();
    }

    //++++++++++++++++++++++++++++ main ++++++++++++++++++++++++++++++++++++++
    public static void main( String[] args ) {
        int winW = 900, winH = 800;
        BasicJOGL scene = new BasicJOGL( winW, winH );
        scene.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseX = (float)e.getPoint().getX();
        mouseY = this.getHeight() - (float)e.getPoint().getY();
        coordinates.clear();
        quad_coordinates.clear();

        float[] x1 = { 0, 0.4f, 0.2f };
        float[] y1 = { 0, 0, 0.7f };

        Quad.PointF[] quadPoints = new Quad.PointF[4];
        quadPoints[0] = new Quad.PointF(.0f, .0f);
        quadPoints[1] = new Quad.PointF(.3f, .0f);
        quadPoints[2] = new Quad.PointF(.0f, .3f);
        quadPoints[3] = new Quad.PointF(.3f, .2f);


        Polygon.PointF[] polyPoints = new Polygon.PointF[5];
        polyPoints[0] = new Polygon.PointF(0.0f, 0.0f);
        polyPoints[1] = new Polygon.PointF(0.0f, .2f);
        polyPoints[2] = new Polygon.PointF(.2f, .3f);
        polyPoints[3] = new Polygon.PointF(0.3f, .1f);
        polyPoints[4] = new Polygon.PointF(.2f, .0f);

        if(drawTriangle){
            triangleCount++;
            Triangle tri = new Triangle(x1, y1, fillFlag, borderFlag, new String("t" + triangleCount));
            tri.setLocation(mouseX, mouseY);
            tri.setID(new String("t" + triangleCount));
            tri.setSize(200f, 200f);
            if(fillFlag)tri.setFillColor(fillColor);
            if(borderFlag)tri.setBorderColor(borderColor);
            if(slid) setLocation(xLocation, yLocation);
            sceneObjs.add(tri);

        }


        if(drawRectangle){
            rectangleCount++;
            Rectangle rect = new Rectangle(0, 0, 50f, 50f, fillFlag, borderFlag, new String("r" + rectangleCount));
            rect.setLocation(mouseX, mouseY );
            rect.setID(new String("r" + rectangleCount));
            rect.setSize(1f, 1f);
            if(fillFlag)rect.setFillColor(fillColor);
            if(borderFlag)rect.setBorderColor(borderColor);
            sceneObjs.add(rect);
        }

        if(drawQuad) {
            quadCount++;
            Quad quad = new Quad(quadPoints, fillFlag, borderFlag, new String("q" + quadCount));
            quad.setLocation(mouseX, mouseY);
            quad.setSize(200f, 200f);
            quad.setID(new String("q" + quadCount));
            if(fillFlag)quad.setFillColor(fillColor);
            if(borderFlag) quad.setBorderColor(borderColor);
            sceneObjs.add(quad);
        }

        if(drawPolygon) {
            polygonCount++;
            Polygon poly = new Polygon(polyPoints, fillFlag, borderFlag, new String("p" + polygonCount));
            poly.setLocation(mouseX, mouseY);
            poly.setSize(500f, 500f);
            poly.setID(new String("p" + polygonCount));
            if(fillFlag)poly.setFillColor(fillColor);
            if(borderFlag) poly.setBorderColor(borderColor);
            sceneObjs.add(poly);
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

        Polygon.PointF p = new Polygon.PointF(e.getX(), this.getHeight() - e.getY());
        Quad.PointF qp = new Quad.PointF(e.getX(), this.getHeight() - e.getY());
        coordinates.add(p);
        quad_coordinates.add(qp);


        if(drawTriangle){
            float[] x1 = new float[3];
            float[] y1 = new float[3];
            int i = 0;
            for(Polygon.PointF point: coordinates) {
                x1[i] = point.getX();
                y1[i] = this.getHeight() - point.getY();
                i++;
            }
            if(coordinates.size() == 3) {
                triangleCount++;
                Triangle tri = new Triangle(x1, y1, fillFlag, borderFlag, new String("t" + triangleCount));
                tri.setID(new String("t" + triangleCount));
                if (fillFlag) tri.setFillColor(fillColor);
                if (borderFlag) tri.setBorderColor(borderColor);
                if (slid) tri.setLocation(xLocation, yLocation);
                sceneObjs.add(tri);
                coordinates.clear();
            }
        }


        if(drawRectangle){

            if(coordinates.size() == 4) {

                float[] x1 = new float[4];
                float[] y1 = new float[4];

                Quad.PointF[] qpoints = new Quad.PointF[4];
                x1[0] = quad_coordinates.get(0).getX();
                y1[0] = quad_coordinates.get(0).getY();
                x1[1] = quad_coordinates.get(1).getX();
                y1[1] = quad_coordinates.get(1).getY();
                x1[2] = quad_coordinates.get(2).getX();
                y1[2] = quad_coordinates.get(2).getY();
                x1[3] = quad_coordinates.get(3).getX();
                y1[3] = quad_coordinates.get(3).getY();


                Rectangle rect = new Rectangle(x1, y1, fillFlag, borderFlag, new String("R" + rectangleCount));

                if (fillFlag) rect.setFillColor(fillColor);
                if (borderFlag) rect.setBorderColor(borderColor);
                if (slid) rect.setLocation(xLocation, yLocation);
                rect.setID(new String("r" + rectangleCount));
                sceneObjs.add(rect);
                coordinates.clear();
            }
        }

        if(drawQuad) {

            if(quad_coordinates.size()==4) {
                Quad.PointF[] qpoints = new Quad.PointF[4];
                qpoints[0] = quad_coordinates.get(0);
                qpoints[1] = quad_coordinates.get(1);
                qpoints[2] = quad_coordinates.get(3);
                qpoints[3] = quad_coordinates.get(2);

                Quad quad = new Quad(qpoints, fillFlag, borderFlag, new String("q" + quadCount));

                if (fillFlag) quad.setFillColor(fillColor);
                if (borderFlag) quad.setBorderColor(borderColor);
                if (slid) quad.setLocation(xLocation, yLocation);
                quad.setID(new String("q" + quadCount));
                sceneObjs.add(quad);
                quad_coordinates.clear();
            }
        }

        if(drawPolygon) {

            if(coordinates.size() == 5) {
                Polygon.PointF[] polyPoints = new Polygon.PointF[5];
                polyPoints[0] = coordinates.get(0);
                polyPoints[1] = coordinates.get(1);
                polyPoints[2] = coordinates.get(2);
                polyPoints[3] = coordinates.get(3);
                polyPoints[4] = coordinates.get(4);

                Polygon poly = new Polygon(polyPoints, fillFlag, borderFlag, new String("p" + polygonCount));

                if (fillFlag) poly.setFillColor(fillColor);
                if (borderFlag) poly.setBorderColor(borderColor);
                if (slid) poly.setLocation(xLocation, yLocation);
                poly.setID(new String("p" + polygonCount));
                sceneObjs.add(poly);

                coordinates.clear();
            }
        }
        repaint();

    }




    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private class SliderListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider slider = (JSlider) e.getSource();
            slid = true;
            if (slider == xSlider) {
               xLocation = slider.getValue();
            } else if (slider == ySlider) {
               yLocation = slider.getValue();
            }

            if(sceneObjs.size() >= 1) {
                Shape s = sceneObjs.get(sceneObjs.size() - 1);
                s.setLocation(xLocation, yLocation);
            }
        }
    }


    /*--------------------------addRadioButtons( JPanel )----------------------------------
    * Adds JRadioButtons to a ButtonGroup.
    */
    private void addRadioButtons(JPanel panel) {

        ButtonGroup buttonGroup = new ButtonGroup( );
        JRadioButton TriangleButton = new JRadioButton("Triangle");
        JRadioButton RectangleButton = new JRadioButton("Rectangle");
        JRadioButton QuadButton = new JRadioButton("Quad");
        JRadioButton PolygonButton = new JRadioButton("Polygon");

        buttonGroup.add(TriangleButton);
        buttonGroup.add(RectangleButton);
        buttonGroup.add(QuadButton);
        buttonGroup.add(PolygonButton);

        handleRadioButtons(panel, TriangleButton, RectangleButton, QuadButton, PolygonButton);
    }


    /*---------handleRadioButtons( JPanel, JRadioButton, JRadioButton, JRadioButton, JRadioButton )---------------
     * Handles functionality of the JRadioButtons and determines which shape is to be drawn next.
     * Adds the buttons to the JPanel.
     */
    private void handleRadioButtons(JPanel panel, JRadioButton T, JRadioButton R, JRadioButton Q, JRadioButton P){

        T.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawTriangle = true;
                drawQuad = false;
                drawRectangle = false;
                drawPolygon = false;
            }
        });

        R.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawRectangle = true;
                drawTriangle = false;
                drawQuad = false;
                drawPolygon = false;
            }
        });

        Q.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawQuad = true;
                drawTriangle = false;
                drawRectangle = false;
                drawPolygon = false;
            }
        });

        P.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawPolygon = true;
                drawTriangle = false;
                drawRectangle = false;
                drawQuad = false;
            }
        });

        panel.add(T);
        panel.add(R);
        panel.add(Q);
        panel.add(P);
    }



    /*-------------------------addButtons( JPanel )-----------------------------
     * Handles functionality of the erase-all button and the adds it to the
     * JPanel along with the radio buttons.
     */
    private void addButtons(JPanel panel) {
        JButton erase_all_button = new JButton("Erase all");
        erase_all_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                sceneObjs.clear();
                coordinates.clear();
            }
        });
        panel.add(erase_all_button);
        addRadioButtons(panel);
    }


    /*-------------------------addJLists( JPanel )-----------------------------
     * Handles functionality of the border JList and fill JList the adds them
     * to the JPanel.
     */
    private void addJLists(JPanel panel) {

        fillList = new JList(listColorNames);
        fillList.setSelectedIndex(0);
        fillList.setPreferredSize(new Dimension(70, 20));
        fillList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(fillList));
        fillList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                fillColor = listColorValues[fillList.getSelectedIndex()];
            }
        });

        borderList = new JList(listColorNames);
        borderList.setSelectedIndex(0);
        borderList.setPreferredSize(new Dimension(70, 20));
        borderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(borderList));
        borderList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                borderColor = listColorValues[borderList.getSelectedIndex()];
            }
        });
    }


    /*-------------------------addDeleteTextField( JPanel )-----------------------------
     * Handles functionality of the sliders and adds them to the JPanel.
     */
    private void addDeleteTextField(JPanel panel){


        JTextField text = new JTextField();
        text.setPreferredSize(new Dimension(50,20));
        text.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    JTextField textField = (JTextField) e.getSource();
                    String shape = textField.getText();
                    deleteShape(shape);
                }
            }
        });

        panel.add(text);
    }


    /*-----------------------------addSliders( JPanel )--------------------------------
     * Handles functionality of the sliders and adds them to the JPanel.
     */
    private void addSliders(JPanel panel){

        JLabel xSlide = new JLabel(" Translate Over X-Axis: ");
        xSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, (int)mouseX);
        xSlider.setPaintTicks(true);
        xSlider.setMajorTickSpacing(200);
        xSlider.setPreferredSize(new Dimension(300, 50));
        xSlider.setPaintLabels(true);
        xSlider.addChangeListener(new SliderListener());
        panel.add(xSlide);
        panel.add(xSlider);


        JLabel ySlide = new JLabel("Translate Over Y-Axis: ");
        ySlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, (int)mouseY);
        ySlider.setPaintTicks(true);
        ySlider.setMajorTickSpacing(200);
        ySlider.setPreferredSize(new Dimension(300, 50));
        ySlider.setPaintLabels(true);
        ySlider.addChangeListener(new SliderListener());
        panel.add(ySlide);
        panel.add(ySlider);
    }


    /*-----------------------------addCheckBoxes( JPanel )--------------------------------
     * Handles functionality of the checkboxes and adds them to the JPanel.
     */
    private void addCheckBoxes(JPanel panel){

        final Checkbox border = new Checkbox("Border");
        final Checkbox fill = new Checkbox("Fill");

        border.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e) {
                Object source = e.getSource();

                if(source == border) borderFlag = true;
                else borderFlag = false;

                if(e.getStateChange() == ItemEvent.DESELECTED) borderFlag = false;
            }
        });

        fill.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e) {
                Object source = e.getSource();
                if (source == fill) fillFlag = true;
                else fillFlag = false;

                if (e.getStateChange() == ItemEvent.DESELECTED) fillFlag = false;
            }
        });

        panel.add(border);
        panel.add(fill);
    }


    /*-----------------------------deleteShape( String )--------------------------------
     * Handles functionality of the shape deletion based on the JTextField.
     */
    private void deleteShape(String shape){


        //remove all polygons
        ArrayList<Integer> p_indices = new ArrayList<Integer>();
        String polygons = "p*";
        CharSequence p = "p";
        if(shape.equals(polygons)) {

            int iterate = 0;
            for (int i = 0; i < sceneObjs.size(); i++) {

                if (sceneObjs.get(i).getID().contains(p)) {

                    p_indices.add(iterate, i);
                    iterate++;
                }
            }

            for (int i = p_indices.size()-1; i >= 0; i--) {
                int ind = p_indices.get(i);
                sceneObjs.remove(ind);
            }
            polygonCount = 0;
        }
        //remove all quads
        ArrayList<Integer> q_indices = new ArrayList<Integer>();
        String quad = "q*";
        CharSequence q = "q";
        if(shape.equals(quad)) {

            int iterate = 0;
            for (int i = 0; i < sceneObjs.size(); i++) {
                if (sceneObjs.get(i).id.contains(q)) {
                    q_indices.add(iterate, i);
                    iterate++;
                }
            }

            for (int i = q_indices.size()-1; i >= 0; i--) {
                int ind = q_indices.get(i);
                sceneObjs.remove(ind);
            }
            quadCount = 0;
        }

        //remove all triangles
        ArrayList<Integer> t_indices = new ArrayList<Integer>();
        String tri = "t*";
        CharSequence t = "t";
        if(shape.equals(tri)) {

            int iterate = 0;
            for (int i = 0; i < sceneObjs.size(); i++) {
                if (sceneObjs.get(i).id.contains(t)) {
                    t_indices.add(iterate, i);
                    iterate++;
                }
            }

            for (int i = t_indices.size()-1; i >= 0; i--) {
                int ind = t_indices.get(i);
                sceneObjs.remove(ind);
            }
            triangleCount = 0;
        }
        //remove add rectangles
        ArrayList<Integer> r_indices = new ArrayList<Integer>();
        String rect = "r*";
        CharSequence r = "r";
        if(shape.equals(rect)) {

            int iterate = 0;
            for (int i = 0; i < sceneObjs.size(); i++) {
                if (sceneObjs.get(i).id.contains(r)) {
                    r_indices.add(iterate, i);
                    iterate++;
                }
            }

            for (int i = r_indices.size()-1; i >= 0; i--) {
                int ind = r_indices.get(i);
                sceneObjs.remove(ind);
            }
            rectangleCount = 0;
        }


        //remove random matches
        int index = 0;
        boolean match=false;
        for(int i=0; i<sceneObjs.size(); i++){

            if (shape.equals(sceneObjs.get(i).getID())) {
                index = i;
                match =true;
            }
        }
        if(match) {
            sceneObjs.remove(index);

        }

    }

}
