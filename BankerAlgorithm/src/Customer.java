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
					if(max > 0) {
						int[] auxArray = new int[2 * max];
						/*
						 * A half of the auxArray is filled with 0's, and the other half is filled with
						 * numbers from 1 to max, so there is 50% of chance that the request for the
						 * resource i is 0
						 */
						for (int j = max; j < (2 * max); j++) {
							auxArray[j] = j - max + 1;
						}

						int rdIndex = random.nextInt(2 * max);
						randReq[i] = auxArray[rdIndex];
					}
				}

				// The customer requests and waits until he gets acces to the resources he needs
				while (banker.requestResources(customerCode, randReq) == -1) {
					Thread.sleep(5*1000);
				}

				Thread.sleep(10 * 1000);
				
				// Caso todo cliente libere os mesmos recursos recentemente alocados logo após o uso, sem retenção estendida
				//int[] randRelease = randReq.clone();

				//Caso todo cliente libere uma quantidade aleatória de recursos logo após o uso
				int[] randRelease = new int[resCount];
				for (int i = 0; i < resCount; i++) {
					// The maximum amount to be released is the allocation
					int max = banker.getAllocation(customerCode, i);
					randRelease[i] = random.nextInt(max + 1);
				}

				// The customer releases a random amount of resources
				while (banker.releaseResources(customerCode, randRelease) == -1)
					;
			}
		} catch (InterruptedException e) {

		}

	}
}