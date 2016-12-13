/**
 * Sphere.java - a class implementation representing a Sphere object
 *           in OpenGL
 * Oct 16, 2013
 * rdb - derived from Box.cpp
 * 
 * 10/28/14 rdb - drawPrimitives -> drawObject( GL2 )
 */

import com.jogamp.opengl.util.GLBuffers;

import javax.media.opengl.GL2;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Sphere extends Object3D
{
    //----------------------- Instance Variables -----------------------------//
    float[][][] spherePoints;
    float[][] arcPoints;
    float[] verts;
    private FloatBuffer dataBuffer;



    //----------------------- Constructor ------------------------------------//
    public Sphere()
    {

        // 1. Make a point at radius length and rotate the resulting line around
        //    the axis to create a semi-circle

        int numArcPoints = 30;
        arcPoints = new float[numArcPoints][3];     // Points used to form arc

        float startX = 0f;
        float startY = 0f;
        float startZ = 0f;
        float radius = 1f;
        float angle = 0f;

        for( int i= 0; i < numArcPoints; i++ )
        {
            float X = startX + (float) (radius * Math.sin(Math.toRadians(angle)));
            float Y = startY + (float) (radius * Math.cos(Math.toRadians(angle)));
            float Z = startZ;
            arcPoints[i][0] = X;
            arcPoints[i][1] = Y;
            arcPoints[i][2] = Z;

            angle += 180f / (numArcPoints - 1);
        }



        // 3. Rotate semi-circle around y axis to generate a sphere
        // 180 corresponds to original number of points

        int numSweepPoints = 30;
        spherePoints = new float[numArcPoints][numSweepPoints][4];

        for( int i = 0; i < numArcPoints; i++ )
        {
            float angles = 0f;
            for( int j = 0; j < numSweepPoints; j++ )
            {
                spherePoints[i][j][0] = (float) (arcPoints[i][0] * Math.cos(Math.toRadians(angles)));
                spherePoints[i][j][1] = arcPoints[i][1];
                spherePoints[i][j][2] = (float) (arcPoints[i][0] * Math.sin(Math.toRadians(angles)));
                spherePoints[i][j][3] = 1;

                angles += 360f / (numSweepPoints - 1);
            }
        }


        // 4. Convert to 1d array that will be passed to makeDataBuffer
        List<Float> floaties = new LinkedList<Float>();

        for( int i = 0; i < (numArcPoints-1); i ++ )
        {
            addArrayToList(floaties, spherePoints[i][0]);
            addArrayToList(floaties, spherePoints[i+1][0]);

            for( int j = 0; j < numSweepPoints; j ++ )
            {
                addArrayToList(floaties, spherePoints[i][j]);
                addArrayToList(floaties, spherePoints[i + 1][j]);
            }

            addArrayToList(floaties, spherePoints[i][0]);
        }


        verts = getArrayWithRoom(floaties);

        int halfVerts = verts.length/2;
        for(int i = halfVerts; i < verts.length; i += 4){
            if(i % 8 == 0) {
                verts[i] = 0.0f;
                verts[i + 1] = 0.0f;
                verts[i + 2] = 1.0f;
            }
            else{
                verts[i] = 1.0f;
                verts[i + 1] = 0.0f;
                verts[i + 2] = 0.0f;
            }
            verts[i+3] = 1.0f;
        }

    if(SceneManager.sphereNormalsPrinted == false) {
        List<Float> norms = new ArrayList<Float>();
        norms = calculateNormals(arcPoints);
        int index=0;
        for (int i = 0; i < norms.size(); i += 3) {
            if(index < 21) {
                System.out.println("Sphere vertex normal " + index + ": " +
                        norms.get(i) + ", " + norms.get(i + 1) + ", "
                        + norms.get(i + 2));
                index++;
            }
       }

        System.out.println("");

        SceneManager.sphereNormalsPrinted = true;
    }

        dataBuffer = makeDataBuffer( verts );
    }





    protected List<Float> calculateNormals(float[][] curSphereVerts) {
        float Qx, Qy, Qz, Px, Py, Pz;
        float nX = 0, nY = 0, nZ = 0;
        List<Float> normals = new ArrayList<Float>();

        for(int i = 0; i < 30; i++) {
            if((i + 2) < 30) {
                Qx = Math.abs(curSphereVerts[i + 1][0] - curSphereVerts[i][0]);
                Qy = Math.abs(curSphereVerts[i + 1][1] - curSphereVerts[i][1]);
                Qz = Math.abs(curSphereVerts[i + 1][2] - curSphereVerts[i][2]);
                Px = Math.abs(curSphereVerts[i + 2][0] - curSphereVerts[i][0]);
                Py = Math.abs(curSphereVerts[i + 2][1] - curSphereVerts[i][1]);
                Pz = Math.abs(curSphereVerts[i + 2][2] - curSphereVerts[i][2]);

                nX = Py*Qz - Pz*Qy;
                nY = Pz*Qx - Px*Qz;
                nZ = Px*Qy - Py*Qx;
                normals.add(nX);
                normals.add(nY);
                normals.add(nZ);
            }

        }

        return normals;
    }


    /**------------------------ getArrayWithRoom -------------------------------
     */
    private float[] getArrayWithRoom(List<Float> floats)
    {

        float[] vertices = new float[floats.size()*2];

        int i = 0;
        for(Float floaty : floats){
            vertices[i] = floaty;
            i++;
        }

        return vertices;
    }



    /**-------------------------- addArrayToList -------------------------------
     * Fill a given list with values from a given array
     */
    private void addArrayToList(List<Float> floaties, float[] floats){
        floaties.add(floats[0]);
        floaties.add(floats[1]);
        floaties.add(floats[2]);
        floaties.add(floats[3]);
    }




    /** ------------------------ redrawObject ----------------------------------
     */
    @Override
    public void redrawObject(GL2 gl) {

        gl.glBegin( GL2.GL_POINTS);
        {
            for ( int i = 0; i < spherePoints.length-1; i ++ )
            {
                gl.glVertex3fv(spherePoints[i][0], 0);
                gl.glVertex3fv(spherePoints[i+1][0], 0);
                for ( int j = 0; j < spherePoints[0].length; j ++ ) {

                    gl.glVertex3fv(spherePoints[i][j], 0);
                    gl.glVertex3fv(spherePoints[i+1][j], 0);
                }
            }
        }
        gl.glEnd();
    }

    /**-------------------------- makeDataBuffer -------------------------------
     * Build a FloatBuffer containing primitive vertices. This code builds
     *    the buffer at construction time; but does not download to the
     *    gpu until draw time.
     */
    protected FloatBuffer makeDataBuffer( float[] data ) {
        //
        // IMPORTANT: you should let OpenGL create the FloatBuffer; don't
        //      use the plain Java tools for that. That cost me many, many
        //      hours. There might be a way to make it work.
        FloatBuffer fBuffer
                = GLBuffers.newDirectFloatBuffer(data, 0, data.length);

        fBuffer.flip();  // prepare to read
        return fBuffer;
    }



    /** ------------------------ redrawVBO -------------------------------------
     */
    public void redrawVAO( GL2 gl )
    {

    }




    /** ------------------------ redrawVBO -------------------------------------
     */
    public void redrawVBO( GL2 gl )
    {
        int[] bufferIds = new int[ 1 ];
        bufferIds[ 0 ] = 0;
        gl.glGenBuffers( 1, bufferIds, 0 );
        JOGL.check( "redrawVBO: glGenBuffers" );

        gl.glBindBuffer( gl.GL_ARRAY_BUFFER, bufferIds[ 0 ] );
        JOGL.check( "redrawVBO: glBindBuffer" );

        gl.glBufferData( gl.GL_ARRAY_BUFFER, dataBuffer.capacity() * 4,
                dataBuffer, gl.GL_STATIC_DRAW );
        JOGL.check( "redrawVBO: glBufferData: " + dataBuffer.capacity());

        int vPosition = gl.glGetAttribLocation( shaderProgram, "vPosition" );
        gl.glEnableVertexAttribArray( vPosition );
        JOGL.check( "redrawVBO: glEnableVertexAttribArray" );
        int vColor = gl.glGetAttribLocation( shaderProgram, "vColor" );
        gl.glEnableVertexAttribArray( vColor );
        JOGL.check( "redrawVBO: glEnableVertexAttribArray-color" );


        gl.glVertexAttribPointer( vPosition, 4, gl.GL_FLOAT, false, 0, 0L );
        JOGL.check( "redrawVBO: glVertexAttribPointer" );

        gl.glVertexAttribPointer( vColor, 4, gl.GL_FLOAT, false, 0,
                verts.length * 2 );
        JOGL.check( "redrawVBO: glVertexAttribPointer - color" );

        gl.glDrawArrays( gl.GL_TRIANGLE_STRIP, 0, verts.length / 4 );
        JOGL.check( "redrawVBO: drawArrays" );

        // unbind the buffer, so space can be re-used.
        gl.glBindBuffer( gl.GL_ARRAY_BUFFER, 0 );
        JOGL.check( "redrawVBO: glBindBuffer" );

    }
}
