/**
 * Created by nicolegager on 10/7/14.
 */
public class RhombicDodecahedron extends Object3D{
    //--------- instance variables -----------------

    //------------- constructor -----------------------
    public RhombicDodecahedron()
    {

    }

    @Override
    void setLocation() {

    }

    @Override
    void setSize() {

    }

    //------------- drawPrimitives ---------------------------
    public void drawPrimitives()
    {
        JOGL.glut.glutSolidRhombicDodecahedron(  );
    }
}
