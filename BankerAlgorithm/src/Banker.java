
public class Banker {
	private int customersCount, resourcesCount;
	private int[][] maximum, allocation, need;
	private int[] available;

	public Banker(int customersCount, int resourcesCount, int[][] maximum, int[][] allocation, int[][] need,
			int[] available) {
		this.customersCount = customersCount;
		this.resourcesCount = resourcesCount;
		this.maximum = maximum;
		this.allocation = allocation;
		this.need = need;
		this.available = available;
	}

	/*
	 * returns 1 if the release is succesfull, and is synchronized in order to
	 * prevent race conditions
	 */
	public synchronized int releaseResources(int customer, int[] release) {

		// available = available + release
		available = sumArrays(available, release);
		// allocation = allocation - release
		allocation[customer] = decreaseArrays(allocation[customer], release);
		// need = need + release
		need[customer] = sumArrays(need[customer], release);

		System.out.println("The customer " + customer + " makes the following release: ");
		for (int i = 0; i < resourcesCount; i++) {
			System.out.println("Resource " + i + ": " + release[i] + " instances.\t");
		}

		return 1;
	}

	/*
	 * returns 1 if the request is succesfull, and -1 if the request is denied, and
	 * is synchronized in order to prevent race conditions
	 */
	public synchronized int requestResources(int customer, int[] request) {
		/*
		 * The resources can only be provided if the customer doesn't exceed its maximum
		 * request and there are enough available resources at the moment
		 */

		System.out.println("The customer " + customer + " makes the following request: ");
		for (int i = 0; i < resourcesCount; i++) {
			System.out.println("Resource " + i + ": " + request[i] + " instances.\t");
		}

		if (isMinorOrEqual(request, need[customer]) && isMinorOrEqual(request, available)) {
			// the allocation is simulated in order to test if the system is still safe

			// available = available - request
			available = decreaseArrays(available, request);
			// allocation = allocation + request
			allocation[customer] = sumArrays(allocation[customer], request);
			// need = need - request
			need[customer] = decreaseArrays(need[customer], request);

			if (isSafe()) {
				System.out.println("\nWhich has been accepted.");
				return 1;
			} else {
				/*
				 * if the system is not safe, it has to return to the previous state and the
				 * request is denied
				 */

				// available = available + request
				available = sumArrays(available, request);
				// allocation = allocation - request
				allocation[customer] = decreaseArrays(allocation[customer], request);
				// need = need + request
				need[customer] = sumArrays(need[customer], request);

			}
		}
		System.out.println("\nWhich has been denied.");
		return -1;
	}

	private boolean isSafe() {
		int[] work = available.clone();
		boolean[] finish = new boolean[customersCount];

		int index;
		do {
			// The i given that finish[i] is false and need[i] <= available is found
			for (index = 0; index < customersCount; index++) {
				if (!finish[index] && isMinorOrEqual(need[index], work)) {
					work = sumArrays(work, allocation[index]);
					finish[index] = true;
				}
			}
		} while (index < customersCount);

		// if every customer is finished in the simulation, the system is safe
		return isAllTrue(finish);

	}

	private boolean isAllTrue(boolean[] array) {
		for (int i = 0; i < array.length; i++) {
			if (!array[i])
				return false;
		}
		return true;
	}

	private boolean isMinorOrEqual(int[] a1, int[] a2) {
		if (a1.length != a2.length)
			return false;
		else {
			for (int i = 0; i < a1.length; i++) {
				if (a1[i] > a2[i])
					return false;
			}
		}
		return true;
	}

	private int[] sumArrays(int[] a1, int[] a2) {
		if (a1.length == a2.length) {
			int[] answer = new int[a1.length];
			for (int i = 0; i < a1.length; i++) {
				answer[i] = a1[i] + a2[i];
			}
			return answer;
		} else
			return null;

	}

	private int[] decreaseArrays(int[] a1, int[] a2) {
		if (a1.length == a2.length) {
			int[] answer = new int[a1.length];
			for (int i = 0; i < a1.length; i++) {
				answer[i] = a1[i] - a2[i];
			}
			return answer;
		} else
			return null;

	}

	public int getResourcesCount() {
		return resourcesCount;
	}

	public synchronized int[][] getMax() {
		return maximum;
	}

	public synchronized int[] getMax(int customer) {
		return maximum[customer];
	}

	public synchronized int getMax(int customer, int resource) {
		return maximum[customer][resource];
	}

	public synchronized int getAllocation(int customer, int resource) {
		return allocation[customer][resource];
	}
}
