/**
 * Created by nicolegager on 10/8/14.
 */
public class Cone extends Object3D {

    //--------- instance variables -----------------
    float base;
    float height;
    int slices;
    int stacks;

    //------------- constructor -----------------------
    public Cone()
    {

        base = .2f;
        height = 10f;
        slices = 10;
        stacks = 2;
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
            JOGL.glut.glutSolidCone( base, height, slices, stacks );
        }


}
