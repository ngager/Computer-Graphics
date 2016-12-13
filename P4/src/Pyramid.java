import com.jogamp.opengl.util.GLBuffers;

import javax.media.opengl.GL2;
import java.nio.FloatBuffer;

import static javax.media.opengl.GL.*;

/**
 * Created by nicolegager on 11/5/14.
 */
public class Pyramid extends Object3D {

    //----------------------- Instance Variables -----------------------------//
    private FloatBuffer dataBuffer;
    private FloatBuffer colBuffer;

    // Vertex coordinates
    private float[] verts  = {

            // Front face
            0f, 1f, 0f, 1f,
            -1f, -1f, 1f, 1f,
            1f, -1f, 1f, 1f,

            // Right face
            0f, 1f, 0f, 1f,
            1f, -1f, 1f, 1f,
            1f, -1f, -1f, 1f,

            // Back face
            0f, 1f, 0f, 1f,
            1f, -1f, -1f, 1f,
            -1f, -1f, -1f, 1f,

            // Left face
            0f, 1f, 0f, 1f,
            -1f, -1f, -1f, 1f,
            -1f, -1f, 1f, 1f,


            // Now add vertex colors

            // Front face: blue
            0f, 0f, 1f, 1f,
            0f, 0f, 1f, 1f,
            0f, 0f, 1f, 1f,
            // back face: red
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            // left face: magenta
            1f, 0f, 1f, 1f,
            1f, 0f, 1f, 1f,
            1f, 0f, 1f, 1f,
            // right face: cyan
            0f, 1f, 1f, 1f,
            0f, 1f, 1f, 1f,
            0f, 1f, 1f, 1f
    };


    public final float normals[] = {
            -1.0f,0.0f,0.0f,  -1.0f,0.0f,0.0f,  -1.0f,0.0f,0.0f,
            -1.0f,0.0f,0.0f,  -1.0f,0.0f,0.0f,  -1.0f,0.0f,0.0f,
            0.0f,-1.0f,0.0f,  0.0f,-1.0f,0.0f,  0.0f,-1.0f,0.0f,
            0.0f,-1.0f,0.0f,  0.0f,-1.0f,0.0f,  0.0f,-1.0f,0.0f,
            1.0f,0.0f,0.0f,  1.0f,0.0f,0.0f,  1.0f,0.0f,0.0f,
            1.0f,0.0f,0.0f,  1.0f,0.0f,0.0f,  1.0f,0.0f,0.0f,
            0.0f,1.0f,0.0f,  0.0f,1.0f,0.0f,  0.0f,1.0f,0.0f,
            0.0f,1.0f,0.0f,  0.0f,1.0f,0.0f,  0.0f,1.0f,0.0f,
            0.0f,0.0f,1.0f,  0.0f,0.0f,1.0f,  0.0f,0.0f,1.0f,
            0.0f,0.0f,1.0f,  0.0f,0.0f,1.0f,  0.0f,0.0f,1.0f,
            0.0f,0.0f,-1.0f,  0.0f,0.0f,-1.0f,  0.0f,0.0f,-1.0f,
            0.0f,0.0f,-1.0f,  0.0f,0.0f,-1.0f,  0.0f,0.0f,-1.0f
    };


    /**---------------------- Constructor --------------------------------------
     * Constructor for pyramid
     */
    public Pyramid( ) {


        dataBuffer = makeDataBuffer( verts );
        int numVertice = 1;

        if(SceneManager.pyramidNormalsPrinted == false){
            for(int i = 0; i < 80; i+=4){
                System.out.print("Pyramid normal for vertice " + numVertice + ": ");

                System.out.println("(" + normals[i] + ", " + normals[i+1]
                            + ", " + normals[i+2] + ")");


                numVertice++;
            }
            SceneManager.pyramidNormalsPrinted  = true;
        }

    }


    /**---------------------- makeDataBuffer -----------------------------------
     * Builds a FloatBuffer containing primitive vertices. This code builds
     * the buffer at construction time; but does not download to the gpu until
     * draw time.
     */
    protected FloatBuffer makeDataBuffer( float[] data )
    {
        FloatBuffer fBuffer;

        fBuffer = GLBuffers.newDirectFloatBuffer( data, 0, data.length );
        fBuffer.flip();  // Prepare to read

        return fBuffer;
    }



    /**---------------------- redrawObject -------------------------------------
     */
    @Override
    protected void redrawObject( GL2 gl ) {

        gl.glBegin( GL2.GL_TRIANGLES );
        {
            for ( int i = 0; i < verts.length; i += 4 )
            {
                gl.glNormal3f( verts[i], verts[i+1], verts[i+2] );
                gl.glVertex4f( verts[i], verts[i+1], verts[i+2], verts[i+3] );
            }
        }
        gl.glEnd();
    }


    /**------------------------- redrawVAO -------------------------------------
     */
    @Override
    protected void redrawVAO(GL2 gl) { }


    /**------------------------- redrawVBO -------------------------------------
     */
    @Override
    protected void redrawVBO( GL2 gl )
    {


        int[] bufferIds = new int[ 1 ];
        bufferIds[ 0 ] = 0;
        gl.glGenBuffers( 1, bufferIds, 0 );
        JOGL.check( "redrawVBO: glGenBuffers" );

        // 2. Bind the buffer
        gl.glBindBuffer( GL_ARRAY_BUFFER, bufferIds[ 0 ] );
        JOGL.check( "redrawVBO: glBindBuffer" );

        gl.glBufferData( GL_ARRAY_BUFFER, dataBuffer.capacity() * 4,
                dataBuffer, gl.GL_STATIC_DRAW );
        JOGL.check( "redrawVBO: glBufferData: " + dataBuffer.capacity());

        int vPosition = gl.glGetAttribLocation( shaderProgram, "vPosition" );
        gl.glEnableVertexAttribArray( vPosition );
        JOGL.check( "redrawVBO: glEnableVertexAttribArray" );
        int vColor = gl.glGetAttribLocation( shaderProgram, "vColor" );
        gl.glEnableVertexAttribArray( vColor );
        JOGL.check( "redrawVBO: glEnableVertexAttribArray-color" );


        gl.glVertexAttribPointer( vPosition, 4, GL_FLOAT, false, 0, 0L );
        JOGL.check( "redrawVBO: glVertexAttribPointer" );

        gl.glVertexAttribPointer( vColor, 4, GL_FLOAT, false, 0,
                verts.length * 2 );
        JOGL.check( "redrawVBO: glVertexAttribPointer - color" );


        gl.glDrawArrays( GL_TRIANGLES, 0, verts.length / 4 );
        JOGL.check( "redrawVBO: drawArrays" );

        // unbind the buffer, so space can be re-used.
        gl.glBindBuffer( GL_ARRAY_BUFFER, 0 );
        JOGL.check( "redrawVBO: glBindBuffer" );

    }
}
