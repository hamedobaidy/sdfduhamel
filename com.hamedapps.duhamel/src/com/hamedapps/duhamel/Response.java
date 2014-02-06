/**
 * 
 */
package com.hamedapps.duhamel;

/**
 * @author Hamed Mohammadi
 *
 */
public class Response {
	private double t, u, p, v, a, vSup;

	/**
	 * 
	 */
	public Response(double t, double p, double u, double v, double a, double vSup) {
		this.t = t;
		this.u = u;
		this.p = p;
		this.u = u;
		this.v = v;
		this.a = a;
		this.vSup = vSup;
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

	/**
	 * @return the v
	 */
	public double getV() {
		return v;
	}

	/**
	 * @param v the v to set
	 */
	public void setV(double v) {
		this.v = v;
	}

	/**
	 * @return the a
	 */
	public double getA() {
		return a;
	}

	/**
	 * @param a the a to set
	 */
	public void setA(double a) {
		this.a = a;
	}

	/**
	 * @return the vSup
	 */
	public double getvSup() {
		return vSup;
	}

	/**
	 * @param vSup the vSup to set
	 */
	public void setvSup(double vSup) {
		this.vSup = vSup;
	}

}
