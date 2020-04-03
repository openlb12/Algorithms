import edu.princeton.cs.algs4.StdDraw;

public class BouncingBalls {


    public static void main(String[] args) {
        int ballNum = Integer.parseInt(args[0]);
        Ball[] balls = new Ball[ballNum];
        for (int i = 0; i < ballNum; i++) {
            balls[i] = new Ball();
        }
        while (true) {
            StdDraw.clear();
            for (int i = 0; i < ballNum; i++) {
                balls[i].move(0.5);
                balls[i].draw();
            }
        }
        StdDraw.show();
    }
}
