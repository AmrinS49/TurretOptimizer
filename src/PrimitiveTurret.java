public class PrimitiveTurret {

    //Limitations
    public static final double MIN_POSITION = 21.754;
    public static final double MAX_POSITION = 283.467;

    //Status
    private double currentPosition = 0;

    //Simulation
    public double goalPosition = 0;

    public PrimitiveTurret(double goalPosition, double turretAngle){
        this.goalPosition = goalPosition;
        this.currentPosition = turretAngle;
    }

    public int getMovesToLock(){
        if(goalPosition > currentPosition){
            return (int) (goalPosition - currentPosition);
        }
        else if(goalPosition < currentPosition){
            return (int) (MAX_POSITION - currentPosition) + (int) (MAX_POSITION - goalPosition);
        }

        return 0;
    }
}
