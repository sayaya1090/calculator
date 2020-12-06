package net.sayaya.calculator;

public abstract class Operator extends Token {
	public Operator(String keyword, int order) {
		super(keyword, order);
	}
	public abstract int requiredOperandCount();
	public abstract Object apply(Object... numbers);
	@Override
	public final String toString() {
		return getKeyword();
	}
}
