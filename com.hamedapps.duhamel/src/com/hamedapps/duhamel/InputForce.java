/**
 * 
 */
package com.hamedapps.duhamel;

/**
 * @author Hamed Mohammadi
 *
 */
public class InputForce {
	private double t, p;
	
	public InputForce( double t, double p) {
		this.setT(t);
		this.p = p;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}

	public double getT() {
		return t;
	}

	public void setT(double t) {
		this.t = t;
	}
}
