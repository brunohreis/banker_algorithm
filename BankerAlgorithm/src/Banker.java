
public class Banker {
	private int customersCount, resourcesCount;
	private int[][] maximum, allocation, need;
	private int[] available;
	
	// This object guarantees mutual exclusion when printing to the console
	private static final Object printLock = new Object();

	public Banker(int customersCount, int resourcesCount, int[][] maximum, int[][] allocation, int[][] need,
			int[] available) {
		this.customersCount = customersCount;
		this.resourcesCount = resourcesCount;
		this.maximum = maximum;
		this.allocation = allocation;
		this.need = need;
		this.available = available;
	}

	/**
	 * 
	 * <p>
	 * Releases the parameterized amount of resources of each type
	 * </p>
	 * <p>
	 * It is synchronized in order to prevent race conditions.
	 * </p>
	 * 
	 * @return 1 if the release is succesfull, and -1 if the request is deniedand
	 * @param customer The customer code/index
	 * @param release  The amount of instances of each resource to be released
	 */
	public synchronized int releaseResources(int customer, int[] release) {

		// available = available + release
		available = sumArrays(available, release);
		// allocation = allocation - release
		allocation[customer] = decreaseArrays(allocation[customer], release);
		// need = need + release
		need[customer] = sumArrays(need[customer], release);

		printlnSynchronized("\nThe customer " + customer + " makes the following release: ");
		for (int i = 0; i < resourcesCount; i++) {
			printSynchronized("Resource " + i + ": " + release[i] + " instances.\t");
		}

		return 1;
	}

	/**
	 * <p>
	 * Requests the parameterized amount of resources of each type.
	 * </p>
	 * <p>
	 * If the request is approved, the requested resources are allocated to the
	 * parameterized customer.
	 * </p>
	 * <p>
	 * if the request exceeds the maximum request or may lead to an unsafe state
	 * (e.g. deadlock), the request is denied
	 * </p>
	 * <p>
	 * It is synchronized in order to prevent race conditions.
	 * </p>
	 * @return 1 if the request is succesfull, and -1 if the request is denied, and
	 * @param customer The customer code/index
	 * @param request  The amount of instances of each resource to be requested
	 */
	public synchronized int requestResources(int customer, int[] request) {

		printlnSynchronized("\nThe customer " + customer + " makes the following request: ");
		for (int i = 0; i < resourcesCount; i++) {
			printSynchronized("Resource " + i + ": " + request[i] + " instances.\t");
		}

		/*
		 * The resources can only be provided if the customer doesn't exceed its maximum
		 * request and there are enough available resources at the moment
		 */
		if (isMinorOrEqual(request, need[customer]) && isMinorOrEqual(request, available)) {
			// the allocation is simulated in order to test if the system is still safe

			// available = available - request
			available = decreaseArrays(available, request);
			// allocation = allocation + request
			allocation[customer] = sumArrays(allocation[customer], request);
			// need = need - request
			need[customer] = decreaseArrays(need[customer], request);

			if (isSafe()) {
				printlnSynchronized("\nWhich has been accepted.");
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
		printlnSynchronized("\nWhich has been denied.");
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

	public synchronized int[] getMax(int customer) throws IndexOutOfBoundsException {
		if ((customer < 0) || (customer >= maximum.length)) {
			throw new IndexOutOfBoundsException();
		}
		return maximum[customer].clone();
	}

	public synchronized int[] getAllocation(int customer) throws IndexOutOfBoundsException {
		if ((customer < 0) || (customer >= allocation.length)) {
			throw new IndexOutOfBoundsException();
		}
		return allocation[customer].clone();
	}

	public synchronized int getMax(int customer, int resource) throws IndexOutOfBoundsException {
		if ((customer < 0) || (customer >= maximum.length) || (resource < 0) || (resource >= maximum[0].length)) {
			throw new IndexOutOfBoundsException();
		}
		return maximum[customer][resource];
	}

	public synchronized int getAllocation(int customer, int resource) throws IndexOutOfBoundsException {
		if ((customer < 0) || (customer >= allocation.length) || (resource < 0) || (resource >= allocation[0].length)) {
			throw new IndexOutOfBoundsException();
		}
		return allocation[customer][resource];
	}
	
	private void printSynchronized(String s) {
		System.out.print(s);
	}
	
	private void printlnSynchronized(String s) {
		System.out.println(s);
	}

}
