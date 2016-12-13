import java.awt.*;

/**
 * Created by nicolegager on 10/10/14.
 */
public class Clown extends Object3D {

    Head head;
    Sphere nose;

    public Clown(){
        head = new Head();
        head.setLocation(0, 0, 0);
        head.setColor(new Color(252, 252, 252));
        head.setSize(.3f, .3f, .3f);

        nose = new Sphere();
        nose.setLocation(0, 0, .2f);
        nose.setColor(new Color(205, 18, 8));
        nose.setSize(.07f, .07f, .07f);
    }


    /*----------------redraw()---------------------------
   * Redraw the object
   */
    @Override
    void redraw() {
        head.redraw();
        nose.redraw();
    }

    /*----------------setSize(float, float, float)--------------------------
   * set the scale along the x, y and z axes to size this object in the caller’s coordinate frame
   */
    void setSize(float xs, float ys, float zs){
        float c = .3f;
        head.setSize(xs, ys, zs);
        nose.setSize(xs * c, ys * c, zs * c);
    }

    /*----------------setRotate(float, float, float, float)--------------------------
     * set the object’s orientation in the caller’s frame based on an axis and angle of rotation
    */
    void setRotate(float angle, float dx, float dy, float dz ){
        head.setRotate( angle, dx, dy, dz );
        nose.setRotate( angle, dx, dy, dz );
    }

    /*----------------setLocation(float, float, float)--------------------------
    * set the location of the object in 3D relative to the caller’s coordinate frame
    */
    @Override
    void setLocation(float x, float y, float z) {
        head.setLocation(x, y, z);
        nose.setLocation(x, y, head.getZ() + .3f);
    }


    /*----------------setColor(Color)--------------------------
    * set the “default” color of the object
    */
    @Override
    void setColor(Color c){
        setColor(0, c);
    }


    /*----------------setColor(int, Color)--------------------------
     * set the i-th color of the object; this is an abstract mechanism for allowing
     * instances of the same class to have multiple objects within them each with a
     * different color. The 0th color is the same as the “default” color.
     */
    void setColor(int i, Color color) {
        if(i == 0)
            head.setColor(color);
        else
            nose.setColor( color );
    }


    @Override
    void setLocation() { }

    @Override
    void setSize() { }

    @Override
    protected void drawPrimitives() { }
}
