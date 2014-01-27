/**
 * 
 */
package com.hamedapps.duhamel;

import java.util.Vector;

/**
 * @author Hamed Mohammadi
 * 
 */
public class DuhamelTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Vector<InputForce> inputForces = new Vector<>();

		for (int i = 1; i <= 100; i++) {
			double p = 0;
			if (i <= 10)
				p = 100;
			else if (i <= 20)
				p = -100;
			else if (i <= 30)
				p = 100;
			else if (i <= 40)
				p = -100;
			else if (i <= 50)
				p = 100;
			else if (i <= 60)
				p = -100;
			else if (i <= 70)
				p = 100;
			else if (i <= 80)
				p = -100;
			else if (i <= 90)
				p = 100;
			else
				p = -100;

			InputForce inf = new InputForce(i, p);
			
			inputForces.add(inf);
		}

		Duhamel d = new Duhamel(inputForces, 0.2, 1000, 30, 31);
		d.compute();
		Vector<Response> responses = d.getResponses();

		for (Response response : responses) {
			System.out.println("t + " + response.getT() + "p = "
					+ response.getP() + ", u = " + response.getU());
		}
	}

}
