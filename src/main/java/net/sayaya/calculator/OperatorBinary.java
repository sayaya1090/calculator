package net.sayaya.calculator;

import java.util.function.BiFunction;

public class OperatorBinary extends Operator {
	private final BiFunction<Object, Object, Object> func;
	OperatorBinary(String keyword, int order, BiFunction<Object, Object, Object> func) {
		super(keyword, order);
		this.func = func;
	}
	
	@Override
	public final Object apply(Object... values) {
		return func.apply(values[0], values[1]);
	}

	@Override
	public int requiredOperandCount() {
		return 2;
	}
}
