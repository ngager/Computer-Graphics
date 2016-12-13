import java.awt.*;

/**
 * Created by nicolegager on 10/10/14.
 */
public class SnowmanWithHat extends Object3D {

    Snowman snowman;
    Hat hat;

    public SnowmanWithHat(){
        snowman = new Snowman();
        snowman.setLocation(0, 0, 0);
        snowman.setSize(.3f, .3f, .3f);
        snowman.makeNose();

        hat = new Hat();
        hat.setLocation(0, .25f, 0);
    }

    /*----------------redraw()---------------------------
     * Redraw the object
     */
    void redraw(){
        snowman.redraw();
        hat.redraw();
    }

    /*----------------setSize(float, float, float)--------------------------
     * set the scale along the x, y and z axes to size this object in the caller’s coordinate frame
     */
    void setSize(float xs, float ys, float zs){
        snowman.setSize(xs, ys, zs);
        hat.setSize(xs, ys, zs);
    }

    /*----------------setRotate(float, float, float, float)--------------------------
    * set the object’s orientation in the caller’s frame based on an axis and angle of rotation
    */
    void setRotate(float angle, float dx, float dy, float dz ){
        snowman.setRotate( angle, dx, dy, dz );
        hat.setRotate( angle, dx, dy, dz );
    }

    /*----------------setLocation(float, float, float)--------------------------
     * set the location of the object in 3D relative to the caller’s coordinate frame
     */
    @Override
    void setLocation(float x, float y, float z) {
        snowman.setLocation(x, y, z);
        hat.setLocation(x, snowman.getX() + .25f, z);
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
            snowman.setColor(color);
        else
            hat.setColor( color );
    }


    @Override
    void setLocation() {}

    @Override
    void setSize() {}

    @Override
    protected void drawPrimitives() {}
}
