import com.jogamp.opengl.util.texture.TextureIO;

import javax.media.opengl.GL;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by nicolegager on 12/2/14.
 */
public class SweepSurface extends Object3D {

    float [][] points;
    float [][] sweepPoints;
    float [][] scaleFactor;

    public SweepSurface( float[][] points ){
        this.points = points;

        InputStream stream;
        try {
            stream = getClass( ).getResourceAsStream( "christmas2.jpg" );
            texture = TextureIO.newTexture(stream, true, "jpg");
        }
        catch ( IOException exc) {
            exc.printStackTrace( );
            System.exit( 1);
        }
    }

    @Override
    void setLocation() {

    }

    @Override
    void setSize() {

    }

    private boolean isWired;
    void setWired( boolean isWired ){
        this.isWired = isWired;
    }

    @Override
    protected void drawPrimitives() {
        JOGL.gl.glPushMatrix();
            if(showPath){
                JOGL.gl.glBegin( GL.GL_LINE_STRIP );
                    JOGL.gl.glColor3f(1.0f, 1.0f, 1.0f);
                    for(int i = 0; i < sweepPoints.length; i++){
                        JOGL.gl.glVertex3fv(sweepPoints[i], 0);
                    }
                JOGL.gl.glEnd();
            }

            if(!hasDrawPrepped){
                prepDraw();
                hasDrawPrepped = true;

            }

            setupTextures();

        JOGL.gl.glColor3f(1.0f, 1.0f, 1.0f);
        for(int i = 0; i < sweepPoints.length - 1; i++) {
            JOGL.gl.glBegin(isWired ? GL.GL_LINE_STRIP : GL.GL_TRIANGLE_STRIP);

            for(int j = 0; j < orderedStripPoints[i].length; j++) {
                    JOGL.gl.glNormal3fv(normalVectors[i][j], 0);
                    JOGL.gl.glTexCoord2fv(texturePoints[i][j], 0);
                    JOGL.gl.glVertex3fv(orderedStripPoints[i][j], 0);
                }

            JOGL.gl.glEnd();
        }

            takeDownTexture();
        JOGL.gl.glPopMatrix();

    }


    private boolean hasDrawPrepped;

    private float[][][] trsShapes;
    private float[][][] texturePoints;
    private float[][][] normalVectors;
    private float[][][] orderedStripPoints;

    private void prepDraw(){
        trsShapes = getTrsShapes();
        float[][][] perSweepTexturePoints = createTexturePoints(trsShapes);
        texturePoints = createOrderedTexture(perSweepTexturePoints);

        orderedStripPoints = createOrderedPoints(trsShapes);
        float[][][] triangleNormals = getNormals(orderedStripPoints);
        normalVectors = getSmoothNormals(triangleNormals);
    }

    private void setupTextures(){
        texture.setTexParameteri(JOGL.gl, JOGL.gl.GL_TEXTURE_WRAP_S, textureMode);
        texture.setTexParameteri(JOGL.gl, JOGL.gl.GL_TEXTURE_WRAP_T, textureMode);

        texture.enable(JOGL.gl);
        texture.bind(JOGL.gl);
    }

    private void takeDownTexture(){
        texture.disable(JOGL.gl);
    }

    private float uScale = 1.0f, vScale = 1.0f;
    public void setTextureScale(float u, float v){
        uScale = u;
        vScale = v;
    }

    private int textureMode;
    public void clampTexture(){
        this.textureMode = JOGL.gl.GL_CLAMP;
    }

    public void repeatTexture(){
        this.textureMode = JOGL.gl.GL_REPEAT;
    }

    public void repeatTextureMirrored(){
        this.textureMode = JOGL.gl.GL_MIRRORED_REPEAT;
    }

    private float[][][] getTrsShapes(){
        float[][][] trsShapes = new float[sweepPoints.length][points.length][];
        for (int i = 0; i < sweepPoints.length; i++) {
            for (int j = 0; j < points.length; j++) {
                trsShapes[i][j] = trsPoints(j, i);
            }
        }

        return trsShapes;
    }

    private float getPathLength( float[][] path, boolean wrap){
        float distance = 0;

        if(wrap) {
            for (int i = 0; i < path.length; i++) {
                distance += getDistance(path[i], path[(path.length - 1 == i) ? 0 : i + 1]);
            }
        } else{
            for (int i = 0; i < path.length - 1; i++) {
                distance += getDistance(path[i], path[i + 1]);
            }
        }

        return distance;
    }

    private double getDistance(float[] p1, float[] p2){
        float xDif = p1[0] - p2[0];
        float yDif = p1[1] - p2[1];
        float zDif = p1[2] - p2[2];

        return Math.sqrt(xDif*xDif + yDif*yDif + zDif*zDif);
    }

    private float[][][] createTexturePoints(float[][][] trsShapes){
        float totalSweepLength = getPathLength(sweepPoints, false);

        float[][][] texturePoints = new float[sweepPoints.length][points.length][];

        float sweepTraveledDistance = 0;
        for(int i = 0; i < sweepPoints.length; i++){
            float u;
            if(i == sweepPoints.length - 1){
                u = 1.0f * uScale;
            }
            else{
                if(i != 0){
                    sweepTraveledDistance += getDistance(sweepPoints[i], sweepPoints[i - 1]);
                }
                u = (sweepTraveledDistance / totalSweepLength) * uScale;
            }

            float totalPathLength = getPathLength(trsShapes[i], true);
            float pathTraveledDistance = 0;
            for(int j = 0; j < points.length; j++){
                float v;
                if(j == points.length - 1){
                    v = 1.0f * vScale;
                }
                else{
                    if(j != 0){
                        pathTraveledDistance += getDistance(points[j], points[j - 1]);
                    }
                    v = (pathTraveledDistance / totalPathLength) * vScale;
                }

                texturePoints[i][j] = new float[]{u, v};
            }
        }

        return texturePoints;
    }

    //numstrips|numPoints|points
    private float[][][] createOrderedPoints(float[][][] trsPoints){
        float[][][] orderedPoints = new float[sweepPoints.length - 1][points.length*2][];
        for (int i = 0; i < sweepPoints.length - 1; i++) {
            int orderedIndex = 0;

            for (int j = 0; j < points.length; j++) {
                orderedPoints[i][orderedIndex] = trsPoints[i][j];
                orderedIndex++;
                orderedPoints[i][orderedIndex] = trsPoints[i + 1][j];
                orderedIndex++;
            }
        }

        return orderedPoints;
    }

    private float[][][] createOrderedTexture(float[][][] texturePoints){
        float[][][] orderedPoints = new float[sweepPoints.length - 1][points.length*2][];
        for (int i = 0; i < sweepPoints.length - 1; i++) {
            int pIndex = 0;
            for (int j = 0; j < points.length; j++) {
                orderedPoints[i][pIndex++] = texturePoints[i][j];
                orderedPoints[i][pIndex++] = texturePoints[i + 1][j];
            }
        }

        return orderedPoints;
    }


    //[numStrips][pointsForStripInOrder][x,y,z]
    private float[][][] getNormals(float[][][] stripPoints){
        float[][] currentTrianglePoints = new float[3][];

        //float[numStrips][numTrianglesPerStrip][normalForThatTriangle]
        float[][][] triangleNormals = new float[sweepPoints.length - 1][(points.length - 1) * 2][];

        for(int i = 0; i < sweepPoints.length - 1; i++) {
            int triPointIndex = 0;
            int triIndex = 0;

            for(int j = 0; j < stripPoints[i].length; j++){
                currentTrianglePoints[triPointIndex] = stripPoints[i][j];
                triPointIndex++;
                triPointIndex %= 3;

                if(j > 1) {
                    //v1 = t2 - t1
                    float vx1 = currentTrianglePoints[1][0] - currentTrianglePoints[0][0];
                    float vy1 = currentTrianglePoints[1][1] - currentTrianglePoints[0][1];
                    float vz1 = currentTrianglePoints[1][2] - currentTrianglePoints[0][2];

                    //v2 = t3 - t1
                    float vx2 = currentTrianglePoints[2][0] - currentTrianglePoints[0][0];
                    float vy2 = currentTrianglePoints[2][1] - currentTrianglePoints[0][1];
                    float vz2 = currentTrianglePoints[2][2] - currentTrianglePoints[0][2];

                    float vx, vz, vy;
                    if(triIndex % 2 == 0) {
                        vx = vy1 * vz2 - vz1 * vy2;
                        vy = vz1 * vx2 - vx1 * vz2;
                        vz = vx1 * vy2 - vy1 * vx2;
                    }
                    else{
                        vx = -vy1 * vz2 + vz1 * vy2;
                        vy = -vz1 * vx2 + vx1 * vz2;
                        vz = -vx1 * vy2 + vy1 * vx2;
                    }

                    triangleNormals[i][triIndex] = (new float[]{vx, vy, vz});
                    triIndex++;
                }
            }
        }

        return triangleNormals;
    }

    //[numStrips][numTriangles][normals]
    private float[][][] getSmoothNormals(float[][][] triangleNormals){
        //[numStrips][numPointsToMakeStrip][normalVector]
        float[][][] smoothNormals = new float[sweepPoints.length - 1][points.length*2][];
        for(int i = 0; i < sweepPoints.length - 1; i++){
            for(int j = 0; j < (points.length*2); j++){
                int pointOnShape = j / 2;
                int sweepPosition = i + j % 2;
                float[][] surroundingFaceNormals = getSurroundingFaceNormals(sweepPosition, pointOnShape, triangleNormals);
                smoothNormals[i][j] = averageNormals(surroundingFaceNormals);
            }
        }

        return smoothNormals;
    }

    private float[] averageNormals(float[][] surroundFaceNormals){
        float[] avgs = new float[3];
        for(float[] normal : surroundFaceNormals){
            avgs[0] += normal[0];
            avgs[1] += normal[1];
            avgs[2] += normal[2];
        }

        int numNormals = surroundFaceNormals.length;
        avgs[0] /= numNormals;
        avgs[1] /= numNormals;
        avgs[2] /= numNormals;

        return avgs;
    }

    private float[][] getSurroundingFaceNormals(int sweepIndex, int pointIndex, float[][][] triangleNormals){
        float[][][] surroundingStrips;

        if(sweepIndex == 0){
            surroundingStrips = new float[1][][];
            surroundingStrips[0] = triangleNormals[0];
        }
        else if(sweepIndex == (sweepPoints.length - 1)){
            surroundingStrips = new float[1][][];
            surroundingStrips[0] = triangleNormals[triangleNormals.length - 1];
        }
        else{
            surroundingStrips = new float[2][][];
            surroundingStrips[1] = triangleNormals[sweepIndex - 1];
            surroundingStrips[0] = triangleNormals[sweepIndex];
        }

        float[][] surroundingNormals = new float[surroundingStrips.length*3][];
        int normalCounter = 0;

        {
            surroundingNormals[normalCounter++] = surroundingStrips[0][pointIndex];
            int beforeTri = pointIndex == 0 ? points.length - 1 : pointIndex - 1;
            surroundingNormals[normalCounter++] = surroundingStrips[0][beforeTri];
            int afterTri = pointIndex == points.length - 1 ? 0 : pointIndex + 1;
            surroundingNormals[normalCounter++] = surroundingStrips[0][afterTri];
        }

        if(surroundingStrips.length > 1){
            surroundingNormals[normalCounter++] = surroundingStrips[1][pointIndex];
            int beforeTri2 = pointIndex == 0 ? points.length - 1 : pointIndex - 1;
            surroundingNormals[normalCounter++] = surroundingStrips[1][beforeTri2];
            int afterTri2 = pointIndex == points.length - 1 ? 0 : pointIndex + 1;
            surroundingNormals[normalCounter++] = surroundingStrips[1][afterTri2];
        }


        return surroundingNormals;
    }

    private final static int X = 0, Y = 1, Z = 2;

    private boolean showPath;

    public void showPath( boolean showPath ){
        this.showPath = showPath;
    }

    private float[] trsPoints( int pointIndex, int pathIndex ){
        float[] trsPoints = new float[3];
        System.arraycopy(points[pointIndex], 0, trsPoints, 0, trsPoints.length);

        //apply scale
        if(scaleFactor != null) {
            float[] xyScale = scaleFactor[pathIndex];
            trsPoints[X] *= xyScale[X];
            trsPoints[Y] *= xyScale[Y];
        }

        //apply rotation
        if(angles != null){
            if(angles[pathIndex] != 0) {
                float curAngle = (float) Math.atan(trsPoints[Y] / trsPoints[X]);
                float diameter = trsPoints[Y] / (float) Math.sin(curAngle);

                float endAngle = curAngle + angles[pathIndex];
                trsPoints[X] = (float) Math.cos(endAngle) * diameter;
                trsPoints[Y] = (float) Math.sin(endAngle) * diameter;
            }
        }

        //apply translate
        if(sweepPoints != null){
            float[] sweepLocation = sweepPoints[pathIndex];
            trsPoints[X] += sweepLocation[X];
            trsPoints[Y] += sweepLocation[Y];
            trsPoints[Z] += sweepLocation[Z];
        }

        //colors, doesn't matter where they are applied though
        if(colors != null) {
            JOGL.gl.glColor3fv(colors[pathIndex], 0);
        }

        return trsPoints;
    }


    public void setSweep(float[][] sweepPoints){
        this.sweepPoints = sweepPoints;
    }

    private float[] angles;
    public void setRotation(float[] angles){
        this.angles = angles;
    }

    public void setScaleFactors(float[][] scaleFactor){
        this.scaleFactor = scaleFactor;
    }


    private float[][] colors;
    public void setColors(float[][] colors){
        this.colors = colors;
    }
}
