import com.jogamp.opengl.util.texture.TextureIO;
import javax.media.opengl.GL2;
import java.io.IOException;
import java.io.InputStream;

/** RoundedBox.java
 *  A child class of Box that gives the illusion of having rounded edges by
 *  calculating the averages of the normals that share a vertex
 */
public class RoundedBox extends Box {


    //----------------------- instance variables -----------------------------//
    float length;
    Boolean addTexture;

    float frontXNormal = 0.0f;
    float frontYNormal = 0.0f;
    float frontZNormal = 1.0f;

    float rightXNormal = 1.0f;
    float rightYNormal = 0.0f;
    float rightZNormal = 0.0f;

    float bottomXNormal = 0.0f;
    float bottomYNormal = -1.0f;
    float bottomZNormal = 0.0f;

    float topXNormal = 0.0f;
    float topYNormal = 1.0f;
    float topZNormal = 0.0f;

    float backXNormal = 0.0f;
    float backYNormal = 0.0f;
    float backZNormal = -1.0f;

    float leftXNormal = -1.0f;
    float leftYNormal = 0.0f;
    float leftZNormal = 0.0f;

    /**------------------------- Constructor -----------------------------------
     *  Constructor for RoundedBox
     */
    public RoundedBox( float length, Boolean texture ) {
        super(length);
        this.length = length;
        this.addTexture = texture;
    }

    @Override
    public void drawPrimitives() { }


    /**------------------------- redraw ----------------------------------------
     *  Overridden redraw method to handle texture redraws
     */
    @Override
    void redraw() {
        if ( addTexture == true )
            redrawTexture();
        else
            makeBox();
    }


    /**------------------------- makeFrontFace ---------------------------------
     *  Makes front face of box
     */
    private void makeFrontFace() {

        // bottom left vertex: adjacent to left, front, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (leftXNormal + frontXNormal + bottomXNormal)/3,
                (leftYNormal + frontYNormal + bottomYNormal)/3,
                (leftZNormal + frontZNormal + bottomZNormal)/3 );
        JOGL.gl.glVertex3f(-length, -length, length);

        // bottom right vertex: adjacent to right, front, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((rightXNormal + frontXNormal + bottomXNormal) / 3,
                (rightYNormal + frontYNormal + bottomYNormal) / 3,
                (rightZNormal + frontZNormal + bottomZNormal) / 3);
        JOGL.gl.glVertex3f(length, -length, length);

        // top right vertex: adjacent to right, front, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (rightXNormal + frontXNormal + topXNormal)/3,
                (rightYNormal + frontYNormal + topYNormal)/3,
                (rightZNormal + frontZNormal + topZNormal)/3 );
        JOGL.gl.glVertex3f(length, length, length);

        // top left vertex: adjacent to left, front, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + frontXNormal + topXNormal) / 3,
                (leftYNormal + frontYNormal + topYNormal) / 3,
                (leftZNormal + frontZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(-length,  length,  length);
    }



    /**------------------------- makeBackFace ----------------------------------
     *  Makes back face of box
     */
    private void makeBackFace() {

        // bottom left vertex: adjacent to left, back, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (leftXNormal + backXNormal + bottomXNormal)/3,
                (leftYNormal + backYNormal + bottomYNormal)/3,
                (leftZNormal + backZNormal + bottomZNormal)/3 );
        JOGL.gl.glVertex3f(length, -length, -length);

        // bottom right vertex: adjacent to right, back, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((rightXNormal + backXNormal + bottomXNormal) / 3,
                (rightYNormal + backYNormal + bottomYNormal) / 3,
                (rightZNormal + backZNormal + bottomZNormal) / 3);
        JOGL.gl.glVertex3f(-length, -length, -length);

        //top right vertex: adjacent to right, back, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((rightXNormal + backXNormal + topXNormal) / 3,
                (rightYNormal + backYNormal + topYNormal) / 3,
                (rightZNormal + backZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(-length, length, -length);

        //top left vertex: adjacent to left, back, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + backXNormal + topXNormal) / 3,
                (leftYNormal + backYNormal + topYNormal) / 3,
                (leftZNormal + backZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(length, length, -length);
    }


    /**------------------------- makeTopFace -----------------------------------
     *  Makes top face of box
     */
    private void makeTopFace() {

        // top front left vertex: adjacent to left, front, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + frontXNormal + topXNormal) / 3,
                (leftYNormal + frontYNormal + topYNormal) / 3,
                (leftZNormal + frontZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(-length, length, length);

        // top front right vertex: adjacent to right, front, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((rightXNormal + frontXNormal + topXNormal) / 3,
                (rightYNormal + frontYNormal + topYNormal) / 3,
                (rightZNormal + frontZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(length, length, length);

        //top back right vertex: adjacent to right, back, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((rightXNormal + backXNormal + topXNormal) / 3,
                (rightYNormal + backYNormal + topYNormal) / 3,
                (rightZNormal + backZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(length, length, -length);
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + backXNormal + topXNormal) / 3,
                (leftYNormal + backYNormal + topYNormal) / 3,
                (leftZNormal + backZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(-length, length, -length);  //top back left vertex: adjacent to left, back, top
    }



    /**------------------------- makeBottomFace --------------------------------
     *  Makes bottom face of box
     */
    private void makeBottomFace() {

        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (rightXNormal + frontXNormal + bottomXNormal)/3,
                (rightYNormal + frontYNormal + bottomYNormal)/3,
                (rightZNormal + frontZNormal + bottomZNormal)/3 );
        JOGL.gl.glVertex3f( length, -length,  length);  //bottom front right vertex: adjacent to right, front, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (leftXNormal + frontXNormal + bottomXNormal)/3,
                (leftYNormal + frontYNormal + bottomYNormal)/3,
                (leftZNormal + frontZNormal + bottomZNormal)/3 );
        JOGL.gl.glVertex3f(-length, -length,  length);  //bottom front left vertex: adjacent to left, front, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (leftXNormal + backXNormal + bottomXNormal)/3,
                (leftYNormal + backYNormal + bottomYNormal)/3,
                (leftZNormal + backZNormal + bottomZNormal)/3 );
        JOGL.gl.glVertex3f(-length, -length, -length);  //bottom back left vertex: adjacent to left, back, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (rightXNormal + backXNormal + bottomXNormal)/3,
                (rightYNormal + backYNormal + bottomYNormal)/3,
                (rightZNormal + backZNormal + bottomZNormal)/3 );
        JOGL.gl.glVertex3f( length, -length, -length);  //bottom back right vertex: adjacent to right, back, bottom
    }


    /**------------------------- makeRightFace ---------------------------------
     *  Makes right face of box
     */
    private void makeRightFace() {

        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (rightXNormal + frontXNormal + bottomXNormal)/3,
                (rightYNormal + frontYNormal + bottomYNormal)/3,
                (rightZNormal + frontZNormal + bottomZNormal)/3 );
        JOGL.gl.glVertex3f( length, -length,  length);  //bottom front right vertex: adjacent to right, front, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (rightXNormal + backXNormal + bottomXNormal)/3,
                (rightYNormal + backYNormal + bottomYNormal)/3,
                (rightZNormal + backZNormal + bottomZNormal)/3 );
        JOGL.gl.glVertex3f( length, -length, -length);  //bottom back right vertex: adjacent to right back bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (rightXNormal + backXNormal + topXNormal)/3,
                (rightYNormal + backYNormal + topYNormal)/3,
                (rightZNormal + backZNormal + topZNormal)/3 );
        JOGL.gl.glVertex3f( length,  length, -length);  //top back right vertex: adjacent to right, back, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (rightXNormal + frontXNormal + topXNormal)/3,
                (rightYNormal + frontYNormal + topYNormal)/3,
                (rightZNormal + frontZNormal + topZNormal)/3 );
        JOGL.gl.glVertex3f( length,  length,  length);  //top front right vertex: adjacent to right, front, top
    }



    /**------------------------- makeLeftFace ----------------------------------
     *  Makes left face of box
     */
    private void makeLeftFace() {

        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + backXNormal + bottomXNormal) / 3,
                (leftYNormal + backYNormal + bottomYNormal) / 3,
                (leftZNormal + backZNormal + bottomZNormal) / 3);
        JOGL.gl.glVertex3f(-length, -length, -length);  //bottom back left vertex: adjacent to left, back, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + frontXNormal + topXNormal) / 3,
                (leftYNormal + frontYNormal + topYNormal) / 3,
                (leftZNormal + frontZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(-length, -length, length);  //bottom front left vertex: adjacent to left, front, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + frontXNormal + topXNormal) / 3,
                (leftYNormal + frontYNormal + topYNormal) / 3,
                (leftZNormal + frontZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(-length, length, length);  //top front left vertex: adjacent to left, front, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + backXNormal + topXNormal) / 3,
                (leftYNormal + backYNormal + topYNormal) / 3,
                (leftZNormal + backZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(-length, length, -length);  //top back left vertex: adjacent to left, back, top
    }



    /**------------------------- makeBox ---------------------------------------
     *  Constructs a basic box with 6 faces
     */
    public void makeBox( ) {
        JOGL.gl.glBegin(JOGL.gl.GL_QUADS);
        JOGL.gl.glColor3f(1f, 1f, 1f);

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


    /**------------------------- redrawTexture ---------------------------------
     *  Redraws the texture
     */
    private void redrawTexture() {
        try {
            InputStream stream = getClass().getResourceAsStream("orange.jpg");
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
        makeBoxTexture();
        JOGL.gl.glDisable( JOGL.gl.GL_TEXTURE_2D);
    }


    /**------------------------- makeFrontFaceTexture --------------------------
     *  Makes front face of box
     */
    private void makeFrontFaceTexture() {

        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (leftXNormal + frontXNormal + bottomXNormal)/3,
                (leftYNormal + frontYNormal + bottomYNormal)/3,
                (leftZNormal + frontZNormal + bottomZNormal)/3 );
        JOGL.gl.glVertex3f(-length, -length, length); JOGL.gl.glTexCoord2f(0.0f, 0.0f);  //bottom left vertex: adjacent to left, front, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((rightXNormal + frontXNormal + bottomXNormal) / 3,
                (rightYNormal + frontYNormal + bottomYNormal) / 3,
                (rightZNormal + frontZNormal + bottomZNormal) / 3);
        JOGL.gl.glVertex3f(length, -length, length); JOGL.gl.glTexCoord2f(1.0f, 0.0f);  //bottom right vertex: adjacent to right, front, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (rightXNormal + frontXNormal + topXNormal)/3,
                (rightYNormal + frontYNormal + topYNormal)/3,
                (rightZNormal + frontZNormal + topZNormal)/3 );
        JOGL.gl.glVertex3f(length, length, length); JOGL.gl.glTexCoord2f(1.0f, 1.0f);  //top right vertex: adjacent to right, front, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + frontXNormal + topXNormal) / 3,
                (leftYNormal + frontYNormal + topYNormal) / 3,
                (leftZNormal + frontZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(-length,  length,  length); JOGL.gl.glTexCoord2f(0.0f, 1.0f);  //top left vertex: adjacent to left, front, top
    }

    /**------------------------- makeBackFaceTexture ---------------------------
     *  Makes back face of box
     */
    private void makeBackFaceTexture() {

        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (leftXNormal + backXNormal + bottomXNormal)/3,
                (leftYNormal + backYNormal + bottomYNormal)/3,
                (leftZNormal + backZNormal + bottomZNormal)/3 );
        JOGL.gl.glVertex3f(length, -length, -length); JOGL.gl.glTexCoord2f(0.0f, 0.0f);   //bottom left vertex: adjacent to left, back, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((rightXNormal + backXNormal + bottomXNormal) / 3,
                (rightYNormal + backYNormal + bottomYNormal) / 3,
                (rightZNormal + backZNormal + bottomZNormal) / 3);
        JOGL.gl.glVertex3f(-length, -length, -length); JOGL.gl.glTexCoord2f(1.0f, 0.0f);  //bottom right vertex: adjacent to right, back, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((rightXNormal + backXNormal + topXNormal) / 3,
                (rightYNormal + backYNormal + topYNormal) / 3,
                (rightZNormal + backZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(-length, length, -length); JOGL.gl.glTexCoord2f(1.0f, 1.0f);  //top right vertex: adjacent to right, back, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + backXNormal + topXNormal) / 3,
                (leftYNormal + backYNormal + topYNormal) / 3,
                (leftZNormal + backZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(length, length, -length); JOGL.gl.glTexCoord2f(0.0f, 1.0f);  //top left vertex: adjacent to left, back, top
    }

    /**------------------------- makeTopFaceTexture ----------------------------
     *  Makes top face of box
     */
    private void makeTopFaceTexture() {

        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + frontXNormal + topXNormal) / 3,
                (leftYNormal + frontYNormal + topYNormal) / 3,
                (leftZNormal + frontZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(-length, length, length); JOGL.gl.glTexCoord2f(0.0f, 0.0f);   //top front left vertex: adjacent to left, front, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((rightXNormal + frontXNormal + topXNormal) / 3,
                (rightYNormal + frontYNormal + topYNormal) / 3,
                (rightZNormal + frontZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(length, length, length); JOGL.gl.glTexCoord2f(1.0f, 0.0f);   //top front right vertex: adjacent to right, front, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((rightXNormal + backXNormal + topXNormal) / 3,
                (rightYNormal + backYNormal + topYNormal) / 3,
                (rightZNormal + backZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(length, length, -length); JOGL.gl.glTexCoord2f(1.0f, 1.0f);  //top back right vertex: adjacent to right, back, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + backXNormal + topXNormal) / 3,
                (leftYNormal + backYNormal + topYNormal) / 3,
                (leftZNormal + backZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(-length, length, -length); JOGL.gl.glTexCoord2f(0.0f, 1.0f);  //top back left vertex: adjacent to left, back, top
    }


    /**------------------------- makeBottomFaceTexture -------------------------
     *  Makes bottom face of box
     */
    private void makeBottomFaceTexture() {

        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (rightXNormal + frontXNormal + bottomXNormal)/3,
                (rightYNormal + frontYNormal + bottomYNormal)/3,
                (rightZNormal + frontZNormal + bottomZNormal)/3 );
        JOGL.gl.glVertex3f( length, -length,  length); JOGL.gl.glTexCoord2f(0.0f, 0.0f);   //bottom front right vertex: adjacent to right, front, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (leftXNormal + frontXNormal + bottomXNormal)/3,
                (leftYNormal + frontYNormal + bottomYNormal)/3,
                (leftZNormal + frontZNormal + bottomZNormal)/3 );
        JOGL.gl.glVertex3f(-length, -length,  length); JOGL.gl.glTexCoord2f(1.0f, 0.0f);  //bottom front left vertex: adjacent to left, front, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (leftXNormal + backXNormal + bottomXNormal)/3,
                (leftYNormal + backYNormal + bottomYNormal)/3,
                (leftZNormal + backZNormal + bottomZNormal)/3 );
        JOGL.gl.glVertex3f(-length, -length, -length); JOGL.gl.glTexCoord2f(1.0f, 1.0f);  //bottom back left vertex: adjacent to left, back, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (rightXNormal + backXNormal + bottomXNormal)/3,
                (rightYNormal + backYNormal + bottomYNormal)/3,
                (rightZNormal + backZNormal + bottomZNormal)/3 );
        JOGL.gl.glVertex3f( length, -length, -length); JOGL.gl.glTexCoord2f(0.0f, 1.0f);   //bottom back right vertex: adjacent to right, back, bottom
    }



    /**------------------------- makeRightFaceTexture --------------------------
     *  Makes right face of box
     */
    private void makeRightFaceTexture() {

        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (rightXNormal + frontXNormal + bottomXNormal)/3,
                (rightYNormal + frontYNormal + bottomYNormal)/3,
                (rightZNormal + frontZNormal + bottomZNormal)/3 );
        JOGL.gl.glVertex3f( length, -length,  length); JOGL.gl.glTexCoord2f(0.0f, 0.0f);  //bottom front right vertex: adjacent to right, front, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (rightXNormal + backXNormal + bottomXNormal)/3,
                (rightYNormal + backYNormal + bottomYNormal)/3,
                (rightZNormal + backZNormal + bottomZNormal)/3 );
        JOGL.gl.glVertex3f( length, -length, -length); JOGL.gl.glTexCoord2f(1.0f, 0.0f);  //bottom back right vertex: adjacent to right back bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (rightXNormal + backXNormal + topXNormal)/3,
                (rightYNormal + backYNormal + topYNormal)/3,
                (rightZNormal + backZNormal + topZNormal)/3 );
        JOGL.gl.glVertex3f( length,  length, -length); JOGL.gl.glTexCoord2f(1.0f, 1.0f);  //top back right vertex: adjacent to right, back, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (rightXNormal + frontXNormal + topXNormal)/3,
                (rightYNormal + frontYNormal + topYNormal)/3,
                (rightZNormal + frontZNormal + topZNormal)/3 );
        JOGL.gl.glVertex3f( length,  length,  length); JOGL.gl.glTexCoord2f(0.0f, 1.0f);  //top front right vertex: adjacent to right, front, top
    }


    /**------------------------- makeLeftFaceTexture ---------------------------
     *  Makes left face of box
     */
    private void makeLeftFaceTexture() {

        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + backXNormal + bottomXNormal) / 3,
                (leftYNormal + backYNormal + bottomYNormal) / 3,
                (leftZNormal + backZNormal + bottomZNormal) / 3);
        JOGL.gl.glVertex3f(-length, -length, -length); JOGL.gl.glTexCoord2f(0.0f, 0.0f);  //bottom back left vertex: adjacent to left, back, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + frontXNormal + topXNormal) / 3,
                (leftYNormal + frontYNormal + topYNormal) / 3,
                (leftZNormal + frontZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(-length, -length, length); JOGL.gl.glTexCoord2f(1.0f, 0.0f);  //bottom front left vertex: adjacent to left, front, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + frontXNormal + topXNormal) / 3,
                (leftYNormal + frontYNormal + topYNormal) / 3,
                (leftZNormal + frontZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(-length, length, length); JOGL.gl.glTexCoord2f(1.0f, 1.0f);  //top front left vertex: adjacent to left, front, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + backXNormal + topXNormal) / 3,
                (leftYNormal + backYNormal + topYNormal) / 3,
                (leftZNormal + backZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(-length, length, -length); JOGL.gl.glTexCoord2f(0.0f, 1.0f);  //top back left vertex: adjacent to left, back, top
    }


    /**------------------------- makeBoxTexture --------------------------------
     *  Makes a box with texture and 6 faces
     */
    public void makeBoxTexture( ) {

        JOGL.gl.glBegin(JOGL.gl.GL_QUADS);
        JOGL.gl.glColor3f(1f, 1f, 0f);

        makeFrontFaceTexture();
        makeBackFaceTexture();
        makeTopFaceTexture();
        makeBottomFaceTexture();
        makeRightFaceTexture();
        makeLeftFaceTexture();

        JOGL.gl.glEnd();

    }



}
