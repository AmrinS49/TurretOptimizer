public class Execute {

    public static void main(String[] args) {
        GoalGenerator gg = new GoalGenerator();

        double goalAngle = gg.getGoalAngle();
        double turretAngle = gg.generateRandomAngle();

        //Make sure goal agle and turret angle are not the same
        while(goalAngle == turretAngle)
            turretAngle = gg.generateRandomAngle();

        System.out.println("Generated goal angle : " + goalAngle);
        System.out.println("Generated turret angle : " + turretAngle);
        System.out.println();

        Turret t = new Turret(goalAngle, turretAngle);
        PrimitiveTurret p = new PrimitiveTurret(goalAngle, turretAngle);

        //Turret t = new Turret(Turret.MIN_POSITION, Turret.MAX_POSITION);
        //wPrimitiveTurret p = new PrimitiveTurret(Turret.MIN_POSITION, Turret.MAX_POSITION);

        //Turret t = new Turret(30.0, 200.0);
        //PrimitiveTurret p = new PrimitiveTurret(30.0, 200.0);

        while(!t.locked){
            t.operate();
            System.out.println(t.toString());
        }

        System.out.println("Target locked in [" + t.moves + "] moves.");
        //System.out.println("Primitive locked in [" + p.getMovesToLock() + "] moves.");
    }
}
