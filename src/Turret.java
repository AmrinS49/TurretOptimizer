public class Turret {

    //Limitations
    private static final double FIELD_OF_VIEW = 60.0;
    private static final double VISIBILITY_THRESHOLD = FIELD_OF_VIEW / 2.0;
    public static final double MIN_POSITION = 21.754;
    public static final double MAX_POSITION = 283.467;

    //Parameters
    private static final double LOCK_TOLERANCE = 1.000;                 //how many degrees from target is acceptable
    private static final double MAX_SEEK_THETA = VISIBILITY_THRESHOLD;  //max jog movement during seek (half of FOV as of now)

    //Status
    private double currentPosition = 0;                                 //current angle of turret
    private double currentSpeed = 0;                                    //curent speed of turret motor

    private TurretState turretState;                                    // * seeking, locking, locked
    private TurretDirection turretDirection;                            // * clockwise, counterclockwise

    //Target
    private double targetPosition = 0;                                  //the angle we want to move to the next tick
    private boolean targetIsVisible =  false;

    //Simulation
    public double goalPosition = 0;
    public boolean locked = false;

    //Statistics
    public int moves = 0;

    public Turret(double goalPosition, double turretAngle){
        turretState = TurretState.SEEKING;
        turretDirection = TurretDirection.CLOCKWISE;

        this.goalPosition = goalPosition;
        this.currentPosition = turretAngle;
    }

    // ***************** //
    // *** OPERATION *** //
    // ***************** //

    public void operate(){
        double relativeAngleToTarget = getRelativeAngleOfTarget();

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

        move();
    }

    public void move(){
        currentPosition = targetPosition;

        moves++;
    }


    // ************** //
    // *** STATES *** //
    // ************** //

    //Sets turret to move at the max rate, continuing in current direction.
    private void seek(){
        incrementTargetPosition(MAX_SEEK_THETA);
    }

    //Goal angle is known, proceed closer to goal.
    private void locking(double relativeAngleToTarget){
        if(relativeAngleToTarget > LOCK_TOLERANCE)
            setTargetPosition(getAbsoluteAngleOfTarget(relativeAngleToTarget / 2));
    }

    //Goal angle is within LOCK_TOLERANCE.
    private void locked(){
        locked = true;
    }

    // *************************** //
    // *** SET TURRET POSITION *** //
    // *************************** //

    private void setTargetPosition(double angle){
        targetPosition = getBoundedAngle(angle);
    }

    private void incrementTargetPosition(double increment){
        if(turretDirection == TurretDirection.CLOCKWISE){
            setTargetPosition(currentPosition + increment);             //Increment if CW
        }
        else{
            setTargetPosition(currentPosition - increment);             //Decrement if CCW
        }
    }

    // ************************* //
    // *** GET TARGET ANGLES *** //
    // ************************* //

    private double getAbsoluteAngleOfTarget(double relativeAngleToTarget){
        if(turretDirection == TurretDirection.CLOCKWISE)
            return currentPosition + relativeAngleToTarget;
        else
            return currentPosition - relativeAngleToTarget;
    }

    private double getRelativeAngleOfTarget(){
        double delta = goalPosition - currentPosition;

        updateTargetVisible(delta);
        updateTurretDirection(delta);
        updateTurretState(delta);
        updateSpeed(delta);

        return Math.abs(delta);
    }

    // ******************************* //
    // *** UPDATE STATES AND SPEED *** //
    // ******************************* //

    private void updateTargetVisible(double delta){
        targetIsVisible = Math.abs(delta) < VISIBILITY_THRESHOLD;
    }

    private void updateTurretDirection(double delta){
        if(!targetIsVisible){
            updateTurretDirectionAtBounds();
        }

        else {
            if (delta > 0)
                turretDirection = TurretDirection.CLOCKWISE;
            else if (delta < 0)
                turretDirection = TurretDirection.COUNTERCLOCKWISE;
        }
    }

    private void updateTurretState(double delta){
        if(!targetIsVisible)
            turretState = TurretState.SEEKING;
        else if(Math.abs(delta) > LOCK_TOLERANCE)
            turretState = TurretState.LOCKING;
        else
            turretState = TurretState.LOCKED;
    }

    private void updateSpeed(double delta){
        if(turretState == TurretState.SEEKING)
            currentSpeed = 1.0;
        else
            currentSpeed = Math.pow(Math.min(((Math.abs(delta) / VISIBILITY_THRESHOLD) + .4), 1.0), 3) / Math.pow(1, 3);
    }

    // ************************* //
    // *** BOUNDARY HANDLERS *** //
    // ************************* //

    //Returns an angle that adheres to MIN_POSITION and MAX_POSITION
    private double getBoundedAngle(double angle){
        if(angle <= MIN_POSITION)
            return MIN_POSITION;
        else if(angle >= MAX_POSITION)
            return MAX_POSITION;
        else
            return angle;
    }

    //Reverses turret direction if target angle is at lower or upper bound.
    private void updateTurretDirectionAtBounds(){
        if(targetPosition == MIN_POSITION)
            turretDirection = TurretDirection.CLOCKWISE;
        else if(targetPosition == MAX_POSITION)
            turretDirection = TurretDirection.COUNTERCLOCKWISE;
    }

    // ************* //
    // *** ENUMS *** //
    // ************* //

    public enum TurretState {
        SEEKING,
        LOCKING,
        LOCKED;
    }

    public enum TurretDirection {
        CLOCKWISE,
        COUNTERCLOCKWISE;
    }

    // ********************* //
    // *** PRINT UTILITY *** //
    // ********************* //

    public String toString(){
        sanitizePositionValues();

        String out = "";

        out += "State:\t\t" + turretState + "\n";
        out += "Visible:\t" + targetIsVisible + "\n";
        out += "Direction:\t" + turretDirection + "\n";
        out += "Current:\t" + currentPosition + "\n";
        out += "Goal:\t\t" + goalPosition + "\n";
        out += "Target:\t\t" + targetPosition + "\n";
        out += "Delta:\t\t" + roundToThreeDecimals(Math.abs(goalPosition - currentPosition)) + "\n";
        out += "Speed:\t\t" + currentSpeed + "\n";

        return out;
    }

    private void sanitizePositionValues(){
        currentPosition = roundToThreeDecimals(currentPosition);
        targetPosition = roundToThreeDecimals(targetPosition);
    }

    private double roundToThreeDecimals(double in){
        return (double) Math.round(in * 1000) / 1000;
    }
}
