import java.awt.*;

/**
 * Created by nicolegager on 10/9/14.
 */
public class Hat extends Object3D {

    Cylinder brim;
    Cone top;

    public Hat(){
        brim = new Cylinder();
        brim.setLocation(0,0,0);
        brim.setColor(new Color(186, 42, 112));
        brim.setSize(.3f, .3f, .3f);
        brim.setRotate(90f, 1f, 0, 0);

        top = new Cone();
        top.setLocation(0,0,0);
        top.setColor(new Color(186, 42, 112));
        top.setSize(.3f, .3f, .3f);
        top.setRotate(270f, 1f, 0, 0);
        top.height = 2;
        top.base = .7f;
    }

    /*----------------redraw()---------------------------
     * Redraw the object
     */
    @Override
    void redraw() {
        brim.redraw();
        top.redraw();
    }

    /*----------------setSize(float, float, float)--------------------------
     * set the scale along the x, y and z axes to size this object in the caller’s coordinate frame
     */
    void setSize(float xs, float ys, float zs){
        brim.setSize(xs, ys, zs);
        top.setSize(xs, ys, zs);
    }

    /*----------------setRotate(float, float, float, float)--------------------------
    * set the object’s orientation in the caller’s frame based on an axis and angle of rotation
    */
    void setRotate(float angle, float dx, float dy, float dz ){
        brim.setRotate( angle, dx, dy, dz );
        top.setRotate( angle, dx, dy, dz );
    }

    /*----------------setLocation(float, float, float)--------------------------
    * set the location of the object in 3D relative to the caller’s coordinate frame
    */
    void setLocation(float x, float y, float z){
        brim.setLocation(x, y, z);
        top.setLocation(brim.getX(), brim.getY(), brim.getZ());
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
            brim.setColor(color);
        else
            top.setColor( color );
    }


    @Override
    void setLocation() {}

    @Override
    void setSize() {}

    @Override
    protected void drawPrimitives() {}
}
