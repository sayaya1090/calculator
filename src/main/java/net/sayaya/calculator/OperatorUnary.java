package net.sayaya.calculator;

import java.util.function.Function;

public class OperatorUnary extends Operator {
	private final Function<Object, Object> func;
	OperatorUnary(String keyword, int order, Function<Object, Object> func) {
		super(keyword, order);
		this.func = func;
	}
	
	@Override
	public final Object apply(Object... values) {
		return func.apply(values[0]);
	}

	@Override
	public int requiredOperandCount() {
		return 1;
	}
}
