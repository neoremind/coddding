package net.neoremind.mycode.concurrent.happensbefore;

public class ReorderingDemo {

	static int x = 0, y = 0, a = 0, b = 0;

	public static void main(String[] args) throws Exception {

		for (int i = 0; i < 100; i++) {
			x = y = a = b = 0;
			Thread one = new Thread() {
				public void run() {
					a = 1;
					x = b;
				}
			};
			Thread two = new Thread() {
				public void run() {
					b = 1;
					y = a;
				}
			};
			one.start();
			two.start();
			one.join();
			two.join();
			System.out.println(x + " " + y);
		}
	}
}
