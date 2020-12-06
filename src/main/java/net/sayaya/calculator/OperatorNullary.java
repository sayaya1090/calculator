package net.sayaya.calculator;

import java.util.function.Supplier;

public class OperatorNullary extends Operator {
	private final Supplier<Object> func;
	public OperatorNullary(String keyword, int order, Supplier<Object> func) {
		super(keyword, order);
		this.func = func;
	}
	
	@Override
	public final Object apply(Object... values) {
		return func.get();
	}

	@Override
	public final int requiredOperandCount() {
		return 0;
	}
}
