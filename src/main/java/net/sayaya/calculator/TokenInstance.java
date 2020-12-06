package net.sayaya.calculator;

import java.util.Collection;
import java.util.PriorityQueue;

public class TokenInstance {
	private static Token PLUS				= new OperatorBinary("[+]", 9, (a, b)->{
		if(a == null && b == null) return null;
		else if(a == null) return b;
		else if(b == null) return a;
		else if(a instanceof Double && b instanceof Double) return (Double)a+(Double)b;
		else return null;
	});
	private static Token MINUS				= new OperatorBinary("[-]", 9, (a, b)->{
		if(a == null && b == null) return null;
		else if(a == null) {
			if(b instanceof Double) return -(Double)b;
			else return null;
		} else if(b == null) return a;
		else if(a instanceof Double && b instanceof Double) return (Double)a-(Double)b;
		else return null;
	});
	private static Token MULTIPLY			= new OperatorBinary("[*]", 5, (a, b)->{
		if(a == null && b == null) return null;
		else if(a == null) return b;
		else if(b == null) return a;
		else if(a instanceof Double && b instanceof Double) return (Double)a*(Double)b;
		else return null;
	});
	private static Token DEVIDE				= new OperatorBinary("[/]", 5, (a, b)->{
		if(a == null && b == null) return null;
		else if(a == null) {
			if(b instanceof Double) return 1/(Double)b;
			else return null;
		} else if(b == null) {
			if(a instanceof Double) return a;
			else return null;
		} else if(a instanceof Double && b instanceof Double) return (Double)a/(Double)b;
		else return null;
	});
	private static Token KILO				= new OperatorUnary("[K]", 2, a->{
		if(a == null || !(a instanceof Double)) return null;
		else return (Double)a*1000;
	});
	private static Token MEGA				= new OperatorUnary("[M]", 2, a->{
		if(a == null || !(a instanceof Double)) return null;
		else return (Double)a*1000000;
	});
	private static Token GIGA				= new OperatorUnary("[G]", 2, a->{
		if(a == null || !(a instanceof Double)) return null;
		else return (Double)a*1000000000;
	});
	private static Token PI					= new Operator("PI", 0) {
		@Override
		public int requiredOperandCount() {
			return 0;
		}
		@Override
		public Object apply(Object... numbers) {
			return 3.14;
		}
	};
	private static Token POW				= new Function("POW") {
		@Override
		public int requiredOperandCount() {
			return 2;
		}
		@Override
		public Object apply(Object... arguments) {
			return Math.pow((Double)arguments[0], (Double)arguments[1]);
		}
	};
	private static Token LOG10				= new Function("LOG10") {
		@Override
		public int requiredOperandCount() {
			return 1;
		}
		@Override
		public Object apply(Object... arguments) {
			return Math.log10((Double)arguments[0]);
		}
	};
	private static Token LOGE				= new Function("LOGE") {
		@Override
		public int requiredOperandCount() {
			return 1;
		}
		@Override
		public Object apply(Object... arguments) {
			return Math.log((Double)arguments[0]);
		}
	};
	private static Token ROUND 				= new Function("ROUND") {
		@Override
		public int requiredOperandCount() {
			return 1;
		}

		@Override
		public Object apply(Object... arguments) {
			return (double) Math.round((Double) arguments[0]);
		}
	};
	private static Token LESS				= new OperatorBinary("<", 11, (a, b)->{
		if(a == null || b == null) return null;
		else if(a instanceof Double && b instanceof Double) return (Double)a < (Double)b;
		else return null;
	});
	private static Token LESS_EQUAL			= new OperatorBinary("<=", 10, (a, b)->{
		if(a == null || b == null) return null;
		else if(a instanceof Double && b instanceof Double) return (Double)a <= (Double)b;
		else return null;
	});
	private static Token LARGER				= new OperatorBinary("[>]", 11, (a, b)->{
		if(a == null || b == null) return null;
		else if(a instanceof Double && b instanceof Double) return (Double)a > (Double)b;
		else return null;
	});
	private static Token LARGER_EQUAL		= new OperatorBinary(">=", 10, (a, b)->{
		if(a == null || b == null) return null;
		else if(a instanceof Double && b instanceof Double) return (Double)a >= (Double)b;
		else return null;
	});
	private static Token AND				= new OperatorBinary("AND", 15, (a, b)->{
		if(a == null || b == null) return null;
		else if(a instanceof Double && b instanceof Double) return (boolean)a & (boolean)b;
		else return null;
	});
	private static Token OR					= new OperatorBinary("OR", 15, (a, b)->{
		if(a == null || b == null) return null;
		else if(a instanceof Double && b instanceof Double) return (boolean)a | (boolean)b;
		else return null;
	});
	static Token BRACKET_OPEN		= new Token("[(]", 999);
	static Token BRACKET_CLOSE		= new Token("[)]", 999);
	static Token NUMBER				= new Token("[-+]*[0-9]+[.]*[0-9]*", 999);
	static Token BOOLEAN			= new Token("true|false", 999);
	static Token STRING				= new Token("\".*\"", 998);
	private static Function EQUALS	= new Function<Boolean>("EQ") {
		@Override
		public final int requiredOperandCount() {
			return 2;
		}
		@Override
		public Boolean apply(Object... arguments) {
			Object a = arguments[0];
			Object b = arguments[1];
			if(a!=null) return a.equals(b);
			else return b == null;
		}
	};

	private static Function IF_ELSE = new Function("IF") {
		@Override
		public int requiredOperandCount() {
			return 3;
		}

		@Override
		public Object apply(Object... arguments) {
			Object a = arguments[0];
			Object b = arguments[1];
			Object c = arguments[2];
			if(!(a instanceof Boolean)) return null;
			else if((Boolean)a) return b;
			else return c;
		}
	};
	
	private final static PriorityQueue<Token> INSTANCES = new PriorityQueue<>();
	static {
		INSTANCES.add(PLUS);
		INSTANCES.add(MINUS);
		INSTANCES.add(MULTIPLY);
		INSTANCES.add(DEVIDE);
		INSTANCES.add(KILO);
		INSTANCES.add(MEGA);
		INSTANCES.add(GIGA);
		INSTANCES.add(PI);
		INSTANCES.add(POW);
		INSTANCES.add(LOG10);
		INSTANCES.add(LOGE);
		INSTANCES.add(ROUND);
		INSTANCES.add(BRACKET_OPEN);
		INSTANCES.add(BRACKET_CLOSE);
		INSTANCES.add(NUMBER);
		INSTANCES.add(STRING);
		INSTANCES.add(BOOLEAN);
		INSTANCES.add(LESS_EQUAL);
		INSTANCES.add(LARGER_EQUAL);
		INSTANCES.add(LESS);
		INSTANCES.add(LARGER);
		INSTANCES.add(EQUALS);
		INSTANCES.add(IF_ELSE);
		INSTANCES.add(AND);
		INSTANCES.add(OR);
	}
	public static Collection<Token> instances() {
		return INSTANCES;
	}
}
