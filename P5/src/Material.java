/** Material.java
 *  Supports Material properties for Object3D objects
 */
public class Material  {

    //----------------------- instance variables -----------------------------//
    float[] diffuse;
    float[] spec;
    float[] ambient;
    float[] emiss;
    float shine;


    /**------------------------- Constructor -----------------------------------
     *  Constructor for Material
     */
    public Material(){
        diffuse = new float[]{ 0.0f, .0f, 0.0f };
        spec = new float[]{ .0f, 0.0f, 0.0f };
        ambient = new float[]{ .0f, 0.0f, 0.0f };
        emiss = new float[]{ .0f, 0.0f, 0.0f };
    }


    /**------------------------- diffuse ---------------------------------------
     *  Applies diffuse property to object
     */
    public void diffuse( float[] values ) { diffuse = values; }


    /**------------------------- specular --------------------------------------
     *  Applies specular property to object
     */
    public void specular( float[] values ) { spec = values; }


    /**------------------------- shine -----------------------------------------
     *  Applies shine property to object
     */
    public void shine( float value ) { shine = value; }

    public void ambient( float[] values ){
        ambient = values;
    }


    /**------------------------- emission --------------------------------------
     *  Applies emission property to object
     */
    public void emission( float[] values ) { emiss = values; }


    /**------------------------- redraw ----------------------------------------
     *  Redraws the Material based on the Material's properties
     */
    void redraw() {

        JOGL.gl.glMaterialf( JOGL.gl.GL_FRONT, JOGL.gl.GL_DIFFUSE, diffuse[0] );
        JOGL.gl.glMaterialf( JOGL.gl.GL_FRONT, JOGL.gl.GL_SPECULAR, spec[0] );
        JOGL.gl.glMaterialf( JOGL.gl.GL_FRONT, JOGL.gl.GL_SHININESS, shine );
        JOGL.gl.glMaterialf( JOGL.gl.GL_FRONT, JOGL.gl.GL_AMBIENT, ambient[0] );
        JOGL.gl.glMaterialf( JOGL.gl.GL_FRONT, JOGL.gl.GL_EMISSION, emiss[0] );

    }


}
