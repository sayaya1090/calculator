package net.sayaya.calculator;

import elemental2.core.RegExpResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Calculator {
	private final Token[] tokens;
	public Calculator() {
		this(TokenInstance.instances().stream().toArray(Token[]::new));
	}
	public Calculator(Token[] tokens) {
		this.tokens = tokens;
	}
	private List<Object> tokenize(String expression) {
		List<Object> list = new LinkedList<>();
		int prevSize;
		expression = expression.trim();
		do {
			prevSize = list.size();
			RegExpResult match = null;
			for (Token token : tokens) {
				match = token.match(expression);
				if (match != null && match.length > 0) {
					String tokenMatch = match.getAt(0);
					if (token == TokenInstance.NUMBER) {
						Double parsed = Double.parseDouble(tokenMatch);
						list.add(parsed);
						expression = expression.substring(tokenMatch.length()).trim();
					} else if (token == TokenInstance.BOOLEAN) {
						boolean parsed = Boolean.parseBoolean(tokenMatch);
						list.add(parsed);
						expression = expression.substring(tokenMatch.length()).trim();
					} else if (token == TokenInstance.STRING) {
						list.add(tokenMatch);
						expression = expression.substring(tokenMatch.length()).trim();
					} else if (token instanceof Function) {
						Function func = (Function) token;
						String exp = match.getAt(1);
						TokenizeFunctionParam parsed = TokenizeFunctionParam.tokenize(exp);
						List<Object> calculated = parsed.tokens.stream()
															   .map(this::calculate)
															   .collect(Collectors.toList());
						Object[] params = new Object[func.requiredOperandCount()];
						if (calculated.size() != func.requiredOperandCount()) throw new FormulaFormatException("fe");
						for (int j = 0; j < params.length; ++j)
							params[j] = (calculated.size() > j) ? calculated.get(j) : null;
						Object value = func.apply(params);
						list.add(value);
						expression = expression.substring(expression.indexOf(exp) + parsed.idx + 1).trim();
					} else {
						list.add(token);
						expression = expression.substring(tokenMatch.length()).trim();
					}
					break;
				}
			}
			if(match==null) throw new FormulaFormatException("Unknwon token:[" + expression + "]");
		} while(prevSize!=list.size() && !expression.isEmpty());
		return list;
	}

	private final static class TokenizeFunctionParam {
		private int idx;
		private List<String> tokens;
		
		TokenizeFunctionParam(int idx, List<String> tokens) {
			this.idx = idx;
			this.tokens = tokens;
		}
		
		private static TokenizeFunctionParam tokenize(String str) {
			if(str == null) return new TokenizeFunctionParam(0, Collections.emptyList());
			List<String> list = new LinkedList<>();
			char[] chars = str.toCharArray();
			StringBuilder sb = new StringBuilder();
			int i = 0;
			for(; i < chars.length; ++i) {
				char ch = chars[i];
				if(ch == '"') sb.append(ch);
				else if(ch == '(') {
					sb.append(ch);
					int cnt = 1;
					while(cnt > 0 && chars.length>i+1) {
						ch = chars[++i];
						sb.append(ch);
						if(ch==')') --cnt;
						else if(ch == '(') ++cnt;
					}
				} else if(ch == ')') break;
				else if(ch == ',') {
					list.add(sb.toString().trim());
					sb = new StringBuilder();
				} else sb.append(ch);
			}
			list.add(sb.toString().trim());
			return new TokenizeFunctionParam(i, list);
		}
	}
	
	private List<Object> infix2Postfix(List<Object> tokens) {
		List<Object> list = new LinkedList<>();
		Stack<Token> stack = new Stack<>();
		boolean hasOperand = false;
		for(Object token: tokens) {
			if(token instanceof Number) {
				list.add(token);
				hasOperand = true;
			} else if(token instanceof Boolean) {
				list.add(token);
				hasOperand = true;
			} else if(token instanceof String) {
				list.add(token);
				hasOperand = true;
			} else if(token == TokenInstance.BRACKET_OPEN) {
				stack.push(TokenInstance.BRACKET_OPEN);
				hasOperand = false;
			} else if(token == TokenInstance.BRACKET_CLOSE) {
				while(stack.peek() != TokenInstance.BRACKET_OPEN) {
					Token prev = stack.pop();
					list.add(prev);
				}
				if(stack.size() > 0) stack.pop();
				hasOperand = true;
			} else if(token instanceof Token) {
				Token cast = (Token)token;
				while(!stack.isEmpty() && stack.peek().getOrder() <= cast.getOrder()) list.add(stack.pop());
				stack.push(cast);
				if(!hasOperand) list.add(0.0);
				hasOperand = false;
			}
		}
		while(!stack.isEmpty()) list.add(stack.pop());
		return list;
	}
	
	private Object calculate(List<Object> tokens) {
		Stack<Object> stack = new Stack<>();
		tokens.forEach(token->{
			if(token instanceof Double) stack.add(token);
			else if(token instanceof Boolean) stack.add(token);
			else if(token instanceof String) stack.add(token);
			else if(token instanceof Operator) {
				Operator op = (Operator)token;
				int required = op.requiredOperandCount();
				Object[] values = new Object[required];
				for(int i = required-1; !stack.isEmpty() && i >= 0; --i) values[i] = stack.pop();
				stack.push(op.apply(values));
			}
		});
		if(stack.isEmpty()) return null;
		Object result = stack.pop();
		if(result instanceof String) {
			String str = (String)result;
			if(str.startsWith("\"") && str.endsWith("\"")) result = str.substring(1, str.length()-1);
		}
		return result;
	}
	
	public Object calculate(String formula) {
		return calculate(infix2Postfix(tokenize(formula)));
	}
	
	public static class FormulaFormatException extends RuntimeException {
		FormulaFormatException(String msg) {
			super(msg);
		}
		public FormulaFormatException(){}
	}
}
