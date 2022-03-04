import java.util.Random;

public class GoalGenerator {

    private double goalAngle;

    public GoalGenerator(){
        goalAngle = generateRandomAngle();
    }

    public double generateRandomAngle(){
        Random random = new Random();

        goalAngle = random.nextDouble() * 360;          //generate random angle
        goalAngle = (double) Math.round(goalAngle * 1000) / 1000;

        if(goalAngle > Turret.MAX_POSITION)
            goalAngle = Turret.MAX_POSITION;
        else if(goalAngle < Turret.MIN_POSITION)
            goalAngle = Turret.MIN_POSITION;

        return goalAngle;
    }

    public double getGoalAngle(){
        return this.goalAngle;
    }


}
