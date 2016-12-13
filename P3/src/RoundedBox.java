import javax.media.opengl.GL2;

/**
 * Created by nicolegager on 10/21/14.
 */
public class RoundedBox extends Box {

    float length;

    public RoundedBox( float length ) {
        super(length);

        this.length = length;
    }

    @Override
    protected void drawPrimitives() {

        makeBox();
    }

    @Override
    public void makeBox( ) {


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




        if(texture != null) {
            texture.enable(JOGL.gl);
            texture.bind(JOGL.gl);
        }

        JOGL.gl.glBegin(JOGL.gl.GL_QUADS);
        JOGL.gl.glColor3f(1f, 1f, 0f);

        /**------------------------- front face -----------------------------**/
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (leftXNormal + frontXNormal + bottomXNormal)/3,
                            (leftYNormal + frontYNormal + bottomYNormal)/3,
                            (leftZNormal + frontZNormal + bottomZNormal)/3 );
        JOGL.gl.glVertex3f(-length, -length, length);  //bottom left vertex: adjacent to left, front, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((rightXNormal + frontXNormal + bottomXNormal) / 3,
                (rightYNormal + frontYNormal + bottomYNormal) / 3,
                (rightZNormal + frontZNormal + bottomZNormal) / 3);
        JOGL.gl.glVertex3f(length, -length, length);  //bottom right vertex: adjacent to right, front, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (rightXNormal + frontXNormal + topXNormal)/3,
                            (rightYNormal + frontYNormal + topYNormal)/3,
                            (rightZNormal + frontZNormal + topZNormal)/3 );
        JOGL.gl.glVertex3f(length, length, length);  //top right vertex: adjacent to right, front, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + frontXNormal + topXNormal) / 3,
                (leftYNormal + frontYNormal + topYNormal) / 3,
                (leftZNormal + frontZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(-length,  length,  length);  //top left vertex: adjacent to left, front, top


        /**------------------------- back face ------------------------------**/
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f( (leftXNormal + backXNormal + bottomXNormal)/3,
                            (leftYNormal + backYNormal + bottomYNormal)/3,
                            (leftZNormal + backZNormal + bottomZNormal)/3 );
        JOGL.gl.glVertex3f(length, -length, -length);  //bottom left vertex: adjacent to left, back, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((rightXNormal + backXNormal + bottomXNormal) / 3,
                (rightYNormal + backYNormal + bottomYNormal) / 3,
                (rightZNormal + backZNormal + bottomZNormal) / 3);
        JOGL.gl.glVertex3f(-length, -length, -length);  //bottom right vertex: adjacent to right, back, bottom
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((rightXNormal + backXNormal + topXNormal) / 3,
                (rightYNormal + backYNormal + topYNormal) / 3,
                (rightZNormal + backZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(-length, length, -length);  //top right vertex: adjacent to right, back, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + backXNormal + topXNormal) / 3,
                (leftYNormal + backYNormal + topYNormal) / 3,
                (leftZNormal + backZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(length, length, -length);   //top left vertex: adjacent to left, back, top



        /**------------------------- top face -------------------------------**/
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + frontXNormal + topXNormal) / 3,
                (leftYNormal + frontYNormal + topYNormal) / 3,
                (leftZNormal + frontZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(-length, length, length);  //top front left vertex: adjacent to left, front, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((rightXNormal + frontXNormal + topXNormal) / 3,
                (rightYNormal + frontYNormal + topYNormal) / 3,
                (rightZNormal + frontZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(length, length, length);  //top front right vertex: adjacent to right, front, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((rightXNormal + backXNormal + topXNormal) / 3,
                (rightYNormal + backYNormal + topYNormal) / 3,
                (rightZNormal + backZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(length, length, -length);  //top back right vertex: adjacent to right, back, top
        JOGL.gl.glShadeModel(JOGL.gl.GL_SMOOTH);
        JOGL.gl.glNormal3f((leftXNormal + backXNormal + topXNormal) / 3,
                (leftYNormal + backYNormal + topYNormal) / 3,
                (leftZNormal + backZNormal + topZNormal) / 3);
        JOGL.gl.glVertex3f(-length, length, -length);  //top back left vertex: adjacent to left, back, top


        /**------------------------- bottom face ----------------------------**/
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


        /**------------------------- right face -----------------------------**/
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


        /**------------------------- left face ------------------------------**/
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


        JOGL.gl.glEnd();


    }



}
