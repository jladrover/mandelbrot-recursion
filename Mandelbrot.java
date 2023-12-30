import processing.core.PApplet;
import processing.core.PConstants;

/**
 * A processing implementation of the Mandelbrot set (a fractal) of complex numbers that displays recursive detail through magnification of the visualized set.
 * @author Foo Barstein, with comments by jladrover 05/08/2022
 * @version 0.1
 */

public final class Mandelbrot extends PApplet { //extends the PApplet class' methods/variables
	private int max = 64; 
	private float[][] colors = new float[48][3]; 
	private double viewX = 0.0;
	private double viewY = 0.0;
	private double zoom = 1.0;
	private int mousePressedX; //X coordinate placeholders used in mousePressed function
	private int mousePressedY; // Y coordinate placeholder used in mousePressed function
	private boolean renderNew = true; // flag to check if a render of the visualtion has occurred
	private boolean drawBox = false; //flag to check if a box has been drawn through the mousePressed function

	/**
 	* Overriden PApplet method that sets up the size of the window of the application.
 	*/
	public void settings() {
		this.size(600,400); //sets the window size to 600 pixels wide by 400 pixels long
	}

	/**
	* Overriden PApplet method that is called once before draw. 
	* This method's code intends to fill the 2d float colors array with 48 sets of color values 
 	*/
	public void setup() {
		//this loop goes through an empty 2d array by row and sets color values by index (lists of 3 floats) to each of the 48 rows
		for (int i = 0; i < colors.length; i++) {
			int c = 2 * i * 256 / colors.length;
			if (c > 255) //makes sure that c (rgb value) doesn't pass 256 limit
				c = 511 - c;
			float[] color = {c, c, c}; //initializes lists of rgb values
			this.colors[i] = color; //sets row of empty 2d array initialized previously to a float list
		}
	}

	/**
	 * Overidden PApplet method that is called automatically by Processing every 1/60th of a second by default.
	 * This method draws the visualization of the Mandelbrot set and the outline of the region the user clicks and holds to be zoomed into
	 */
	public void draw() {

		if (!renderNew && !this.drawBox) return; //if the render of the set isn't new and no drawbox has been made, then the background is black.
		this.background(0, 0, 0); //PApplet method to set background

		if (this.drawBox) { //if a drawbox has been instantiated (through mousepressed method) then create an unfilled red box outline of region where mouse is pressed and held
			this.noFill(); //inherited method that doesn't fill the box with color
			this.stroke(255, 0, 0); //inherited method that sets red color for box
			rect(this.mousePressedX, this.mousePressedY, this.mouseX - this.mousePressedX, this.mouseY - this.mousePressedY); //PApplet method where the location of rectangle are x and y position of where mouse is clicked and its width/height is determined by where the mouse is dragged to subtracted by the previously mentioned positions
		}
		//loops through the amount of pixels of the height of the screen so that a pixel with a desired color can be drawn to match the screen's height
		for (int y = 0; y < this.height; y++) { 
			//loops through the amount of pixels of the width of the screen so a pixel with a specific color (to eventually visualize the mandelbrot set) can be drawn row by row of the screen's width, through the nested for loop
			for (int x = 0; x < this.width; x++) {
				double r = zoom / Math.min(this.width, this.height);
				double dx = 2.5 * (x * r + this.viewX) - 2.0;
				double dy = 1.25 - 2.5 * (y * r + this.viewY);
				int value = this.mandel(dx, dy); //calculates the set with the given doubles
				float[] color = this.colors[value % this.colors.length]; //float color array is created with a certain colors 2d array row indice specified by the remainder of the given value variable divided length of the 2d array
				this.stroke(color[0], color[1], color[2]); //inherited method to set the color of a drawn line/shape (rgb)
				this.line(x, y, x, y); //inherited method to draw a color between two points
			}
		}
		
		this.textAlign(PConstants.CENTER); //PApplet method that centers text
		this.text("Click and drag to draw an area to zoom into.", this.width / 2, this.height-20); //PApplet method that generates text on the screen

		this.renderNew = false; //New drawing render set to false
	}

	/**
	* Method that returns a value using the mandelbrot equation at a specfied point of complex numbers (c in the equation) while the value is being incremented by 1 (z in the equation) which eventually creates a set when called recursively.
	* @param double px: X coordinate of a point that is a complex number 
	* @param double py: Y coordinate of a point that is a complex number 
	* @return int value or 0: Value of the given complex point within the equation of the set
 	*/
	private int mandel(double px, double py) {
		double zx = 0.0, zy = 0.0; 
		double zx2 = 0.0, zy2 = 0.0;
		int value = 0;
		//loops as long as int value is less than the instance of max and the zx2 & zy2 doubles have a value less than 4.0 which represents the equation/bounds of the mandelbrot set
		while (value < this.max && zx2 + zy2 < 4.0) {
			zy = 2.0 * zx * zy + py;
			zx = zx2 - zy2 + px;
			zx2 = zx * zx;
			zy2 = zy * zy;
			value++; //increments value by 1 so that it can be performed a desired amount of times up until the instance max number of iterations
		}
		return value == this.max ? 0 : value; //ternary operator where value is set to 0 if it is equal to max and is unchanged if not.
	}

	/**
	* PApplet method that is called when the user clicks (and/or holds) their mouse on the application.
 	*/
	public void mousePressed() {
		this.mousePressedX = this.mouseX; //sets mousepressedX coordinate to x coordinate of mouse on screen
		this.mousePressedY = this.mouseY; //sets mousepressedY coordinate to y coordinate of mouse on screen
		this.drawBox = true; //draws a box while the mouse is pressed
	}

	/**
	* PApplet Method that is called when a user's mouse button is released.
	* Used to get the necessary measurements in order to zoom into the region of the area determined by the mousePressed method.
 	*/
	public void mouseReleased() {
		int mouseReleasedX = this.mouseX; //sets mouserleaseX variable to PApplet given x coordinate of mouse on screen
		int mouseReleasedY = this.mouseY; //sets mouseReleaseY variable to PApplet given y coordinate of mouse on screen
		if (this.mouseButton == PConstants.LEFT) { //if the user left clicks
			if (mouseReleasedX != mousePressedX && mouseReleasedY != mousePressedY) { //if the mouseReleased x/y variables are not equal to their mousePressed counterparts
				int w = this.width; //sets int w to pixel width of display
				int h = this.height; //set int h to pixel height of display
				this.viewX += this.zoom * Math.min(mouseReleasedX, mousePressedX) / Math.min(w, h); //instance variable viewX increments by adding to itself the minimum x/y coordinate of where the mouse was clicked divided by the minimum int value of the width/height of the screen
				this.viewY += this.zoom * Math.min(mouseReleasedY, mousePressedY) / Math.min(w, h);	//instance variable viewY increments by adding to itself the minimum x/y coordinate of where the mouse was clicked divided by the minimum int value of the width/height of the screen
				//the viewX/Y & zoom variables need to be incremented as they must continously grow in value as the user tries to continously zoom in and magnify the image on the screen
				this.zoom *= Math.max((double)Math.abs(mouseReleasedX - mousePressedX) / w, (double)Math.abs(mouseReleasedY - mousePressedY) / h);
				//instance variable zoom increments itself by constantly multiplying itself by the max double value between the two positive expressions of the (mouseReleased X coordinate subtracted by the mousePressed X coordinate) all divided by the int width of the screen and the expression: (mouseReleasedY coordinate subtracted by the mousePressedY coordinate) all divided by the int height of the screen
			}

		}
		else if (this.mouseButton == PConstants.RIGHT) { //if the user right clicks
			this.max += max / 4; //max num of 64 (initialized as an instance int var) iterates, adding 1/4th of its current value to itself and lets the mandel method occur more often due to the while loop in the recursive case is partly based on the current value being less than the max variable.
		}
		else { //if the user doesn't left or right click
			this.max = 64; //instance max is reset to 64
			this.viewX = this.viewY = 0.0; //viewX/Y instances are set to zero
			this.zoom = 1.0; //zoom is reset to default 1.0 value
		}

		this.drawBox = false; //box has been drawn flag set to false
		this.renderNew = true; //new drawing render flag set to true
	}

	/* Main function calls PApplet class to run all the previous methods. */
	public static void main(String[] args) {
		PApplet.main("recursion_exercise.Mandelbrot");
	}


}