package com.f.a.kobe.pattern.Decorator;

public class RedShapeDecorator extends ShapeDecorator {

	public RedShapeDecorator(Shape decoratedShape) {
		super(decoratedShape);
	}

	@Override
	public void draw() {
		decoratedShape.draw();
		setRedBorder(decoratedShape);
	}

	private void setRedBorder(Shape decoratedShape) {
		System.out.println("Border Color: Red");
	}

	public static void main(String[] args) {
		Shape circle = new Circle();
		ShapeDecorator redCircle = new RedShapeDecorator(new Circle());
		System.out.println("Circle with normal border");
		circle.draw();
		System.out.println("\nCircle of red border");
	      redCircle.draw();
	}
}
