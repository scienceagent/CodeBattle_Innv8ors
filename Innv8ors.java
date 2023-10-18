package Innv8ors;

import robocode.*;

public class Innv8ors extends Robot {
    private static final double RANGE_FAR = 400; // Distanța la care robotul va trage proiectilele
    private static final double BEARING_PREDICT = 5; // Prezicerea unghiului pentru a ataca un roboțel în mișcare

    @Override
    public void run() {
        // Inițializare a roboțelului
        setAdjustRadarForGunTurn(true); // Asigură că radarul se rotește independent de turele armei
        setAdjustGunForRobotTurn(true); // Asigură că arma se rotește independent de turele roboțelului
        while (true) {
            turnRadarRight(360); // Rotirea radarului cu 360 de grade
            ahead(100); // Deplasare înainte cu 100 de unități
            turnRight(45); // Rotirea la dreapta cu 45 de grade
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        // Manipularea evenimentului de scanare a unui roboțel
        double gunAdjust = getHeading() - getGunHeading() + e.getBearing();
        gunAdjust = normalizeBearing(gunAdjust); // Normalizarea unghiului pentru a trage precis

        if (e.getVelocity() > 2) {
            // Dacă roboțelul țintă se mișcă, ajustează unghiul de tragere pentru a ținti în fața lui
            gunAdjust = gunAdjust > 0 ? gunAdjust + BEARING_PREDICT : gunAdjust - BEARING_PREDICT;
        }

        turnGunRight(gunAdjust); // Rotirea armei pentru a ținti roboțelul
        fire(Math.min(RANGE_FAR / e.getDistance(), 3)); // Trage cu puterea potrivită în funcție de distanța față de țintă
    }

    /** Normalizează un unghi între +180 și -180 */
    double normalizeBearing(double angle) {
        while (angle >  180) angle -= 360; // Reducere la unghi pozitiv mai mic de 180
        while (angle < -180) angle += 360; // Reducere la unghi negativ mai mare de -180
        return angle;
    }
}
