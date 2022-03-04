public class Execute {

    public static void main(String[] args) {
        GoalGenerator gg = new GoalGenerator();

        double goalAngle = gg.getGoalAngle();
        double turretAngle = gg.generateRandomAngle();

        System.out.println("Generated goal angle : " + goalAngle);
        System.out.println("Generated turret angle : " + turretAngle);
        System.out.println();

        Turret t = new Turret(goalAngle, turretAngle);
        //Turret t = new Turret(Turret.MIN_POSITION, Turret.MAX_POSITION);

        while(!t.locked){
            t.operate();
        }

        System.out.println("Target locked in [" + t.moves + "] moves.");
    }
}
