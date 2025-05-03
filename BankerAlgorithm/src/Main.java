import java.util.Random;

public class Main {

	/*
	 * The first argument is the number of customers, and the following args are the
	 * amount of available instances for each resource
	 */
	public static void main(String[] args) {
		Random rd = new Random();
		if (args.length > 1) {
			int customersCount = Integer.parseInt(args[0]);
			if (customersCount > 0) {
				int resourcesCount = args.length - 1;
				int[] available = new int[resourcesCount];
				for (int i = 0; i < resourcesCount; i++) {
					available[i] = Integer.parseInt(args[i + 1]);
				}

				int[][] maximum = new int[customersCount][resourcesCount];
				int[][] allocation = new int[customersCount][resourcesCount];
				int[][] need = new int[customersCount][resourcesCount];

				/*
				 * The maximum amount of instances of each resource to be requested by each
				 * customer is a random number between 0 and the number of available instances
				 * of each resource. The need is initially equal to the maximum, because there
				 * are no resources allocated yet.
				 */
				for (int i = 0; i < maximum.length; i++) {
					for (int j = 0; j < maximum[0].length; j++) {
						int max = rd.nextInt(available[j]);
						maximum[i][j] = need[i][j] = max;
					}
				}

				Banker banker = new Banker(customersCount, resourcesCount, maximum, allocation, need, available);
				Thread[] threads = new Thread[customersCount];
				for (int i = 0; i < customersCount; i++) {
					Customer cust = new Customer(i, banker);
					threads[i] = new Thread(cust);
					threads[i].start();
				}

			} else
				System.out.println("There are no customers to request any resources!");
		} else
			System.out.println("There are not enough args!");
	}
}
