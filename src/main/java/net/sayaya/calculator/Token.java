package net.sayaya.calculator;

import elemental2.core.JsRegExp;
import elemental2.core.RegExpResult;

public class Token implements Comparable<Token> {
	private final String keyword;
	private final JsRegExp pattern;
	private final int order;
	
	public Token(String keyword, int order) {
		this.keyword = keyword;
		pattern = new JsRegExp("^"+keyword);
		this.order = order;
	}
	final String getKeyword() {
		return keyword;
	}
	public final RegExpResult match(String expression) {
		return pattern.exec(expression);
	}
	public final int getOrder() {
		return order;
	}
	@Override
	public final int compareTo(Token other) {
		if(other == null) return -1;
		return Integer.compare(order, other.order);
	}
}
