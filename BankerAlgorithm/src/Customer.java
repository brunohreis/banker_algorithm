import java.util.Random;

public class Customer implements Runnable {
	private Random random;
	private int customerCode;
	private Banker banker;

	public Customer(int customerCode, Banker banker) {
		this.banker = banker;
		this.customerCode = customerCode;
		random = new Random();
	}

	public void run() {
		try {
			while (true) {
				int resCount = banker.getResourcesCount();
				int[] randReq = new int[resCount];
				for (int i = 0; i < resCount; i++) {
					int max = banker.getMax(customerCode, i);
					int[] auxArray = new int[2 * max];
					/*
					 * A half of the auxArray is filled with 0's, and the other half is filled with
					 * numbers from 0 to max, so there is 50% of chance that the request for the
					 * resource i is 0
					 */
					for (int j = 0; j < max; j++) {
						auxArray[j] = 0;
					}
					for (int j = max; j < (2 * max); j++) {
						auxArray[j] = j - max;
					}

					int rdIndex = random.nextInt(2 * max);
					randReq[i] = auxArray[rdIndex];
				}

				// The customer requests and waits until he gets acces to the resources he needs
				while (banker.requestResources(customerCode, randReq) == -1)
					;

				Thread.sleep(40 * 1000);

				int[] randRelease = new int[resCount];
				for (int i = 0; i < resCount; i++) {
					// The maximum amount to be released is the allocation
					int max = banker.getAllocation(customerCode, i);
					randRelease[i] = random.nextInt(max);
				}

				// The customer releases a random amount of resources
				while (banker.releaseResources(customerCode, randRelease) == -1)
					;
			}
		} catch (InterruptedException e) {

		}

	}
}