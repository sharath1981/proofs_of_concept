/**
 * 
 */
package poc.datastructure.stack;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @author Sharath Kumar B <sharath1981@gmail.com>
 *
 */
public class StackManager {

	public static void main(String[] args) {
		final Stack<String> stack = new Stack<>(5);
		System.out.println(stack);
		System.out.println("size => " + stack.size());
		System.out.println("isEmpty => " + stack.isEmpty());
		System.out.println("isFull => " + stack.isFull());
		System.out.println("===========================");
		stack.push("sharath");
		stack.push("vijay");
		stack.push("vivek");
		stack.push("ravi");
		stack.push("gopal");
		System.out.println(stack);
		System.out.println("size => " + stack.size());
		System.out.println("isEmpty => " + stack.isEmpty());
		System.out.println("isFull => " + stack.isFull());
		System.out.println("peek => " + stack.peek());
		System.out.println("contains => " + stack.contains("vivek"));
		System.out.println("===========================");
		IntStream.range(0, 5).boxed().forEach(i -> System.out.println("Popped Element => " + stack.pop()));
		System.out.println(stack);
		System.out.println("size => " + stack.size());
		System.out.println("isEmpty => " + stack.isEmpty());
		System.out.println("isFull => " + stack.isFull());
	}

}

/* Generic Stack using an Array */

class Stack<E> {
	private final E[] stack;
	private int top;

	@SuppressWarnings("unchecked")
	public Stack(final int capacity) {
		stack = (E[]) new Object[capacity];
		top = -1;
	}

	public int size() {
		return top + 1;
	}

	public boolean isEmpty() {
		return top == -1;
	}

	public boolean isFull() {
		return top == stack.length - 1;
	}

	public void push(final E element) {
		if (isFull()) {
			// TODO: Resizing logic required
			throw new IllegalStateException("Stack Overflow...");
		}
		stack[++top] = element;
	}

	public E pop() {
		final E element = peek();
		stack[top--] = null;
		return element;
	}

	public E peek() {
		if (isEmpty()) {
			throw new IllegalStateException("Stack Underflow...");
		}
		return stack[top];
	}

	public boolean contains(final E element) {
		return element != null && Arrays.stream(stack).anyMatch(element::equals);
	}

	@Override
	public String toString() {
		return "Stack " + Arrays.toString(stack);
	}

}
