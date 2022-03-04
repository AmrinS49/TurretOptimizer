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
        else if(turretDirection == TurretDirection.COUNTERCLOCKWISE){
            targetPosition -= MAX_SEEK_THETA;
        }
    }

    private void locking(double absoluteAngleToTarget){

    }

    private void lock(double absoluteAngleToTarget){
        //verify lock
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
