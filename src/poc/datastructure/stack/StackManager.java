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
		stack.push("navin");
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
	private E[] stack;
	private int top;

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
			resize();
		}
		stack[++top] = element;
	}

	private void resize() {
		final E[] temp = (E[]) new Object[2 * stack.length];
		System.arraycopy(stack, 0, temp, 0, stack.length);
		stack = temp;
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

	public String toString() {
		return "Stack => " + Arrays.toString(stack);
	}

}
