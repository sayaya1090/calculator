package net.sayaya.calculator;

public abstract class Function<T> extends Token {
	public Function(String name) {
		super(name + "[(](.+)[)]", 0);
	}
	public abstract int requiredOperandCount();
	public abstract T apply(Object... arguments);
}
