import java.awt.*;

/**
 * Created by nicolegager on 10/8/14.
 */
public class Head extends Object3D {

    //--------- instance variables -----------------
    Sphere head;
    Sphere leftEye, rightEye;

    //------------- constructor -----------------------
    public Head(){
        //build head
        head = new Sphere();
        head.setColor(new Color(233, 233, 233));
        head.setLocation(0,0,0);
        head.setSize(.5f, .5f, .5f);

        //build eyes
        makeEyes();
    }

    /*----------------makeEyes()---------------------------
     * Makes eyes for the head
     */
    private void makeEyes(){

        leftEye = new Sphere();
        leftEye.setColor(new Color(193, 136, 165));
        leftEye.setLocation(head.getX() + .05f, head.getY() + 0.05f, head.getZ() + 1f );
        leftEye.setSize(.02f, .02f, .02f);

        rightEye = new Sphere();
        rightEye.setColor(new Color(193, 136, 165));
        rightEye.setLocation( head.getX() + .01f, head.getY(), head.getZ() );
        rightEye.setSize(.02f, .02f, .02f);
    }



    /*----------------redraw()---------------------------
     * Redraw the object
     */
    @Override
    void redraw() {
       head.redraw();
       leftEye.redraw();
       rightEye.redraw();
    }

    /*----------------setSize(float, float, float)--------------------------
     * set the scale along the x, y and z axes to size this object in the caller’s coordinate frame
     */
    @Override
    void setSize(float xs, float ys, float zs){
        head.setSize(xs, ys, zs);
        float constant = 0.2f;
        leftEye.setSize(xs * constant, ys * constant, zs * constant);
        rightEye.setSize(xs * constant, ys * constant, zs * constant);
    }

    /*----------------setRotate(float, float, float, float)--------------------------
     * set the object’s orientation in the caller’s frame based on an axis and angle of rotation
     */
     void setRotate(float angle, float dx, float dy, float dz ){
         head.setRotate( angle, dx, dy, dz );
         leftEye.setRotate( angle, dx, dy, dz );
         rightEye.setRotate( angle, dx, dy, dz );
     }

    /*----------------setLocation(float, float, float)--------------------------
     * set the location of the object in 3D relative to the caller’s coordinate frame
     */
    @Override
    void setLocation(float x, float y, float z){
        head.setLocation(x, y, z);
        leftEye.setLocation(head.getX() + .1f , head.getY() + .1f , head.getZ() + .3f );
        rightEye.setLocation(head.getX() - .1f , head.getY() +.1f , head.getZ() + .3f);
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
        else if(i == 1)
            leftEye.setColor( color );
        else
            rightEye.setColor( color );
    }

    @Override
    void setLocation(){}

    @Override
    void setSize() { }

    float getX(){
        return head.getX();
    }

    float getY(){
        return head.getY();
    }

    float getZ(){
        return head.getZ();
    }


    @Override
    protected void drawPrimitives() { }

}
