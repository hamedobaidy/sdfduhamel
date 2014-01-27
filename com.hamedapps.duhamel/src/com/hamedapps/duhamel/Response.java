/**
 * 
 */
package com.hamedapps.duhamel;

/**
 * @author Hamed Mohammadi
 *
 */
public class Response {
	private double t, u, p;

	/**
	 * 
	 */
	public Response(double t, double u) {
		this.t = t;
		this.u = u;
	}
	
	public double getU() {
		return u;
	}

	public void setU(double u) {
		this.u = u;
	}

	public double getT() {
		return t;
	}

	public void setT(double t) {
		this.t = t;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}

}
