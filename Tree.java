package recursion_exercise;

import processing.core.PApplet;

public class Tree extends PApplet {
    
    public static final int maxAngle = 360;
    public static final int startX = 600;
    public static final int startY = 800;
    public static final int numOfRecursions = 9;
    public static final int startAngle = 0;
    public static final double treeSize = 2;
    public static final int Detail = 3;
    public static final int randFact = 30;
    public static final int[] constFact = {-60, 05, -50, 45};
     
    public static int[] red =   {0, 0, 0, 0, 7, 15, 23, 31, 39, 47, 55, 43};
    public static int[] green = {171, 159, 147, 135, 123, 111, 99, 87, 75, 63, 51, 43};
    public static int[] blue =  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};   
     
    public static double degToRad(int deg) {
        return deg * Math.PI / 180;
    }
     
    public void drawFractal(int x, int y, int n, int angle) {
        if (n == Detail) return;
        int len = (int) Math.round(Math.pow(treeSize, n - 1));
         
        int xn1 = (int) Math.round(x - (2 * len * Math.sin(degToRad(angle))));
        int yn1 = (int) Math.round(y - (2 * len * Math.cos(degToRad(angle))));
        int mid1x = (x + xn1) / 2;
        int mid1y = (y + yn1) / 2;
        int mid2x = (mid1x + xn1) / 2;
        int mid2y = (mid1y + yn1) / 2;
        int mid3x = (x + mid1x) / 2;
        int mid3y = (y + mid1y) / 2;
        int mid4x = (mid3x + mid1x) / 2;
        int mid4y = (mid3y + mid1y) / 2;
         
        java.util.Random randy = new java.util.Random();
        drawFractal(mid1x, mid1y, n - 1, (angle + randy.nextInt(randFact) + constFact[0]) % maxAngle);
        drawFractal(mid2x, mid2y, n - 1, (angle + randy.nextInt(randFact) + constFact[1]) % maxAngle);
        drawFractal(mid3x, mid3y, n - 1, (angle + randy.nextInt(randFact) + constFact[2]) % maxAngle);
        drawFractal(mid4x, mid4y, n - 1, (angle + randy.nextInt(randFact) + constFact[3]) % maxAngle);
         
        float r = Tree.red[(randy.nextInt() % 3) + n];
        float g = Tree.green[(randy.nextInt() % 3) + n];
        float b = Tree.blue[(randy.nextInt() % 3) + n];
        
        this.stroke(r, g, b);
        this.line(x, y, xn1, yn1);
    }

    public void settings() {
        this.size(1200, 1000);
    }

    public void setup() {
        background(0, 0, 0);
        drawFractal(startX, startY, numOfRecursions, startAngle);
    }
     
    public void draw() {
        if (PApplet.second() % 5 == 0) {
            background(0, 0, 0);
            drawFractal(startX, startY, numOfRecursions, startAngle);
        }
    }
     
    public static void main(String args[]) {
        PApplet.main("recursion_exercise.Tree");
    }
}