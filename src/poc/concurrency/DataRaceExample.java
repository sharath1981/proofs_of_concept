/**
 * 
 */
package poc.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Sharath Kumar B <sharath1981@gmail.com>
 *
 */
public class DataRaceExample {

	static SharedResource resource = new SharedResource();

	public static void main(String[] args) {

		ExecutorService service = Executors.newFixedThreadPool(2);
		try {
			for (int i = 0; i < 1; i++) {
				service.submit(DataRaceExample::increment);
				service.submit(DataRaceExample::check);
			}
		} finally {
			service.shutdown();
		}

	}

	private static void increment() {
		for (int i = 0; i < 100000; i++) {
			resource.increment();
		}
	}

	private static void check() {
		for (int i = 0; i < 100000; i++) {
			resource.checkForDataRace();
		}
	}

	static class SharedResource {
		private long x;
		private long y;

		private void increment() {
			x++;
			y++;
		}

		private void checkForDataRace() {
			if (y > x) {
				System.out.println("Data race detected in "+Thread.currentThread().getName());
			}
		}
	}

}
