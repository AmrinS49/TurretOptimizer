public class Turret {

    //Limitations
    private static final double FIELD_OF_VIEW = 60;                     //limelight camera max fov
    public static final double MIN_POSITION = 21.754;                   //turret min angle
    public static final double MAX_POSITION = 283.467;                  //turret max angle

    //Parameters
    private static final double LOCK_TOLERANCE = 2.000;                 //how many degrees from target is acceptable
    private static final double MAX_SEEK_THETA = FIELD_OF_VIEW / 2;     //max jog movement during seek (half of FOV as of now)

    //States
    private static final double TARGET_NOT_FOUND = -1.000;              //value of absoluteAngleToTarget if target isn't found

    //Status
    private double currentPosition = 0;                                 //current angle of turret
    private double currentSpeed = 0;                                    //current motor speed
    private double currentSampleRate = 0;                               //current frame sample rate

    private TurretState turretState;                                    // * seeking, locking, locked
    private TurretDirection turretDirection;                            // * clockwise, counterclockwise

    //Target
    private double targetPosition = 0;                                  //the angle we want to move to the next tick
    private double targetSpeed = 0;                                     //the speed of the motor during next movement
    private double targetSampleRate = 0;                                //the sample rate to use next movement

    //Simulation
    public double goalPosition = 0;
    public boolean locked = false;

    //Statistics
    public int moves = 0;

    public Turret(double goalPosition, double turretAngle){
        turretState = TurretState.SEEKING;

        this.goalPosition = goalPosition;
        this.currentPosition = turretAngle;
    }

    public void operate(){
        double relativeAngleToTarget = getRelativeAngleToTarget();

        switch(turretState){
            case SEEKING:
                seek();
                break;
            case LOCKING:
                locking(relativeAngleToTarget);
                break;
            case LOCKED:
                locked();
                break;
        }

        printStatus();
        move();
        updateTurretDirectionAtBounds();
    }

    private void sanitizePositionValues(){
        currentPosition = roundToThreeDecimals(currentPosition);
        targetPosition = roundToThreeDecimals(targetPosition);
    }

    private double roundToThreeDecimals(double in){
        return (double) Math.round(in * 1000) / 1000;
    }

    /*
        Simulates turret movement executed after calculations are complete.
     */
    public void move(){
        currentPosition = targetPosition;

        moves++;
    }

    public void printStatus(){
        sanitizePositionValues();

        System.out.println("State:\t\t" + turretState);
        System.out.println("Direction:\t" + turretDirection);
        System.out.println("Current:\t" + currentPosition);
        System.out.println("Goal:\t\t" + goalPosition);
        System.out.println("Target:\t\t" + targetPosition);
        System.out.println("Delta:\t\t" + roundToThreeDecimals(Math.abs(goalPosition - currentPosition)));
        System.out.println();
    }

    /*
        Sets turret to move at the max rate, continuing in current direction.
     */
    private void seek(){
        incrementTargetPosition(MAX_SEEK_THETA);
    }

    /*
        Goal angle is known, proceed closer to goal.
     */
    private void locking(double relativeAngleToTarget){
        if(relativeAngleToTarget < LOCK_TOLERANCE)                                      //Do not move if within lock tolerance
            return;

        setTargetPosition(getAbsoluteAngleToTarget(relativeAngleToTarget / 2));         //Otherwise, move halfway towards goal
    }

    /*
        Goal angle is within LOCK_TOLERANCE.
     */
    private void locked(){
        locked = true;
    }

    /*
        Relative target position adjustment.
     */
    private void incrementTargetPosition(double increment){
        if(turretDirection == TurretDirection.CLOCKWISE){
            setTargetPosition(currentPosition + increment);             //Increment if CW
        }
        else{
            setTargetPosition(currentPosition - increment);             //Decrement if CCW
        }
    }

    /*
        Absolute target position adjustment.
     */
    private void setTargetPosition(double angle){
        targetPosition = getBoundedAngle(angle);
    }

    /*

     */
    private double getRelativeAngleToTarget(){
        double delta = goalPosition - currentPosition;

        updateTurretDirection(delta);
        updateTurretState(delta);

        return Math.abs(delta);
    }

    private void updateTurretState(double delta){
        if(Math.abs(delta) > MAX_SEEK_THETA)
            turretState = TurretState.SEEKING;
        else if(Math.abs(delta) > LOCK_TOLERANCE)
            turretState = TurretState.LOCKING;
        else
            turretState = TurretState.LOCKED;
    }

    private void updateTurretDirection(double delta){
        if(delta > 0)
            turretDirection = TurretDirection.CLOCKWISE;
        else if(delta < 0)
            turretDirection = TurretDirection.COUNTERCLOCKWISE;
    }

    /*
        Returns absolute angle to target based on current direction of turret
     */
    private double getAbsoluteAngleToTarget(double relativeAngleToTarget){
        if(turretDirection == TurretDirection.CLOCKWISE)
            return currentPosition + relativeAngleToTarget;
        else
            return currentPosition - relativeAngleToTarget;
    }

    /*
        Returns an angle that adheres to MIN_POSITION and MAX_POSITION
     */
    private double getBoundedAngle(double angle){
        if(angle <= MIN_POSITION)
            return MIN_POSITION;
        else if(angle >= MAX_POSITION)
            return MAX_POSITION;
        else
            return angle;
    }

    /*
        Reverses turret direction if target angle is at lower or upper bound.
     */
    private void updateTurretDirectionAtBounds(){
        if(targetPosition == MIN_POSITION)
            turretDirection  = TurretDirection.CLOCKWISE;
        else if(targetPosition == MAX_POSITION)
            turretDirection = TurretDirection.COUNTERCLOCKWISE;
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
