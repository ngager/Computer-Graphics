import java.awt.*;

/**
 * Created by nicolegager on 10/9/14.
 */
public class Snowfriends extends Object3D{

    //--------- instance variables -----------------
    Snowman guy;
    Snowman girl;

    //------------- constructor -----------------------
    public Snowfriends(){
        guy = new Snowman();
        guy.setLocation(.4f, 0f, .2f);
        guy.setSize(.3f, .3f, .3f);
        guy.makeNose();

        girl = new Snowman();
        girl.setLocation(-.4f, 0f, .2f);
        girl.setSize(.3f, .3f, .3f);
        girl.makeNose();

    }


    /*----------------redraw()---------------------------
     * Redraw the object
     */
    @Override
    void redraw() {
        girl.redraw();
        guy.redraw();
    }


    /*----------------setSize(float, float, float)--------------------------
     * set the scale along the x, y and z axes to size this object in the caller’s coordinate frame
     */
    void setSize(float xs, float ys, float zs){
        float c = .3f;
        guy.setSize(xs, ys, zs);
        guy.nose.setSize(xs * c, ys * c, zs * c);
        girl.setSize(xs, ys, zs);
        girl.nose.setSize(xs * c, ys * c, zs * c);
    }

    /*----------------setRotate(float, float, float, float)--------------------------
    * set the object’s orientation in the caller’s frame based on an axis and angle of rotation
    */
    void setRotate(float angle, float dx, float dy, float dz ){
        guy.setRotate( angle, dx, dy, dz );
        guy.nose.setRotate( angle, dx, dy, dz );
        girl.setRotate( angle, dx, dy, dz );
        girl.nose.setRotate( angle, dx, dy, dz );
    }

    /*----------------setLocation(float, float, float)--------------------------
    * set the location of the object in 3D relative to the caller’s coordinate frame
    */
    void setLocation(float x, float y, float z){
        guy.setLocation(x, y, z);
        girl.setLocation(-girl.getX(), girl.getY(), girl.getZ());
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
            guy.setColor(color);
        else
            girl.setColor( color );
    }






    @Override
    void setLocation() { }

    @Override
    void setSize() { }

    @Override
    protected void drawPrimitives() { }
}
