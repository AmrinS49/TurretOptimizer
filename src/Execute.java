public class Execute {

    public static void main(String[] args) {
        Scenario scenario = Scenario.RANDOM;

        GoalGenerator gg = new GoalGenerator();
        double goalAngle = gg.getGoalAngle();
        double turretAngle = gg.generateRandomAngle();

        System.out.println("Generated goal angle : " + goalAngle);
        System.out.println("Generated turret angle : " + turretAngle);
        System.out.println();

        Turret t;
        PrimitiveTurret p;

        switch(scenario) {
            case RANDOM:
                //Make sure goal angle and turret angle are not the same
                while(goalAngle == turretAngle)
                    turretAngle = gg.generateRandomAngle();

                t = new Turret(goalAngle, turretAngle);
                p = new PrimitiveTurret(goalAngle, turretAngle);
                break;
            case MAX:
                t = new Turret(Turret.MIN_POSITION, Turret.MAX_POSITION);
                p = new PrimitiveTurret(Turret.MIN_POSITION, Turret.MAX_POSITION);
                break;
            case CUSTOM:
                t = new Turret(30.0, 200.0);
                p = new PrimitiveTurret(30.0, 200.0);
                break;
            default:
                t = new Turret(0,0);
                p = new PrimitiveTurret(0,0);
        }

        while(!t.locked){
            t.operate();
            System.out.println(t.toString());
        }

        System.out.println("Target locked in [" + t.moves + "] moves.");
        //System.out.println("Primitive locked in [" + p.getMovesToLock() + "] moves.");
    }

    public enum Scenario {
        RANDOM,
        MAX,
        CUSTOM;
    }
}
