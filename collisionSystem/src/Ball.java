import edu.princeton.cs.algs4.StdDraw;

public class Ball {
    private double rx, ry;
    private double vx, vy;
    private final double radius;

    public Ball(double xpos, double ypos, double xvel, double yvel, double rd) {
        rx = xpos;
        ry = ypos;
        vx = xvel;
        vy = yvel;
        radius = rd;
    }

    public void move(double dt) {
        // todo! unrational design of wall bouncing, need further check
        double newRx = rx + vx * dt;
        double newRy = ry + vy * dt;
        if (newRx > 1.0 - radius || newRx < radius) vx *= -1;
        if (newRy > 1.0 - radius || newRy < radius) vy *= -1;
        rx += vx * dt;
        ry += vy * dt;
    }

    public void draw() {
        StdDraw.filledCircle(rx, ry, radius);
    }
}
