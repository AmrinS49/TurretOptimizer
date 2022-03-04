public class Turret {

    //Limitations
    private static final double FIELD_OF_VIEW = 60;
    private static final double MIN_POSITION = 21.754;
    private static final double MAX_POSITION = 283.467;

    //Parameters
    private static final double LOCK_TOLERANCE = 2.000;
    private static final double MAX_SEEK_THETA = 15.000;

    //Status
    private double currentPosition = 0;
    private double currentSpeed = 0;
    private double currentSampleRate = 0;

    private TurretState turretState;
    private TurretDirection turretDirection;

    //Target
    private double targetPosition = 0;
    private double targetSpeed = 0;
    private double targetSampleRate = 0;

    public Turret(){
        turretState = TurretState.SEEKING;
    }

    public void operate(){
        targetPosition = currentPosition; //synchronize target and current positions
        double absoluteAngleToTarget = -1;

        switch(turretState){
            case SEEKING:
                seek();
                break;
            case LOCKING:
                locking(absoluteAngleToTarget);
                break;
            case LOCKED:
                lock(absoluteAngleToTarget);
                break;
        }
    }

    public void move(){

    }

    private void seek(){
        if(turretDirection == TurretDirection.CLOCKWISE){
            targetPosition += MAX_SEEK_THETA;
        }
        else {
            targetPosition -= MAX_SEEK_THETA;
        }
    }

    private void locking(double absoluteAngleToTarget){

    }

    private void lock(double absoluteAngleToTarget){
        //verify lock
    }

    private void incrementTargetPosition(double increment){
        if(turretDirection == TurretDirection.CLOCKWISE){
            if(currentPosition + increment > MAX_POSITION) {            //Attempt to move past max
                targetPosition = MAX_POSITION;                          //Set target position to max
                turretDirection = TurretDirection.COUNTERCLOCKWISE;     //Reverse movement direction for next move
            }
            else
                targetPosition += increment;                            //Increment normally
        }
        else{
            if(currentPosition - increment < MIN_POSITION){             //Attempt to move past min
                targetPosition = MIN_POSITION;                          //Set target position to min
                turretDirection = TurretDirection.CLOCKWISE;            //Reverse movement direction for next move
            }
            else
                targetPosition -= increment;                            //Decrement Normally
        }
    }

    private void setTargetPosition(){

    }

    public enum TurretState {
        SEEKING,
        LOCKING,
        LOCKED;
    }

    public enum TurretDirection {
        CLOCKWISE,
        COUNTERCLOCKWISE;
    }
}
