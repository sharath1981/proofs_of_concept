/**
 * 
 */
package poc.datastructure.queue;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.sun.xml.internal.txw2.IllegalSignatureException;


/**
 * @author Sharath Kumar B <sharath1981@gmail.com>
 *
 */
public class QueueManager {

	public static void main(String[] args) {
		final Queue<String> queue = new Queue<>(5);
		System.out.println(queue);
		System.out.println("size => " + queue.size());
		System.out.println("isEmpty => " + queue.isEmpty());
		System.out.println("isFull => " + queue.isFull());
		System.out.println("===========================");
		queue.enQueue("sharath");
		queue.enQueue("vijay");
		queue.enQueue("vivek");
		queue.enQueue("ravi");
		queue.enQueue("gopal");
		queue.enQueue("navin");
		System.out.println(queue);
		System.out.println("size => " + queue.size());
		System.out.println("isEmpty => " + queue.isEmpty());
		System.out.println("isFull => " + queue.isFull());
		System.out.println("contains => " + queue.contains("vivek"));
		System.out.println("===========================");
		IntStream.range(0, queue.size()).boxed().forEach(i -> System.out.println("deQueued Element => " + queue.deQueue()));
		System.out.println(queue);
		System.out.println("size => " + queue.size());
		System.out.println("isEmpty => " + queue.isEmpty());
		System.out.println("isFull => " + queue.isFull());

	}

}

/* Generic Queue using an Array */
class Queue<E> {
	private E[] queue;
	private int index;

	public Queue(final int capacity) {
		queue = (E[]) new Object[capacity];
		index = -1;
	}

	public int size() {
		return index + 1;
	}

	public boolean isEmpty() {
		return index == -1;
	}

	public boolean isFull() {
		return index == queue.length - 1;
	}

	public void enQueue(final E element) {
		if (isFull()) {
			ensureCapacity();
		}
		queue[++index] = element;
	}

	private void ensureCapacity() {
		final E[] temp = (E[]) new Object[queue.length * 2];
		System.arraycopy(queue, 0, temp, 0, queue.length);
		queue = temp;
	}

	public E deQueue() {
		if (isEmpty()) {
			throw new IllegalSignatureException("Queue Underflow...");
		}
		final E element = queue[0];
		IntStream.range(0, index).forEach(i -> queue[i] = queue[i + 1]);
		queue[index--] = null;
		return element;
	}

	public boolean contains(final E element) {
		return element != null && Arrays.stream(queue).anyMatch(element::equals);
	}

	public String toString() {
		return "Queue => " + Arrays.toString(queue);
	}

}
