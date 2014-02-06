/**
 * 
 */
package com.hamedapps.duhamel;

import java.util.Vector;

/**
 * @author Hamed Mohammadi
 * 
 */
public class Duhamel {
	private Vector<InputForce> inputForces;
	private Vector<InputForce> forces;
	private Vector<Response> responses;

	private boolean interpolate = false;
	private boolean forceIsGroundAcceleration = false;
	private double kesi;
	private double m;
	private double omega;
	private double omega_D;
	private double dt;
	private double tMax;
	private double k;
	private double gr;
	private double uMax;
	private double vMax;
	private double aMax;
	private double vSupMax;

	public Duhamel(Vector<InputForce> inputForces, double kesi, double m,
			double omega, double omega_D) {
		responses = new Vector<>();
		this.inputForces = inputForces;
		this.kesi = kesi;
		this.m = m;
		this.omega = omega;
		this.omega_D = omega_D;

		forces = new Vector<>();
	}

	public void compute() {
		responses.clear();

		double dampC = kesi * 2 * Math.sqrt(k * m);

		uMax = 0.0;
		vMax = 0.0;
		aMax = 0.0;
		vSupMax = 0.0;

		// interpolate if required
		if (interpolate)
			computeForces();
		else
			forces = inputForces;

		// if records are base acceleration
		if (forceIsGroundAcceleration)
			computeForceFromGroundAcceleration();

		// Compute for first time force record or time zero
		// Compute A(t_0) and B(t_0) assuming A(t_-1)=B(t_-1)=0
		double a_ti_1 = 0;
		double b_ti_1 = 0;

		// and than add this response record to responses
		Response r0 = new Response(forces.get(0).getT(), forces.get(0).getP(),
				0.0, 0.0, forces.get(0).getP() / m, 0.0);
		responses.add(r0);

		// While there is force record
		// Compute I_1 to I_4 for t_i
		// Compute A(t_i) and B(t_i)
		// Compute u(t_i)
		// Compute u'(t_i)
		// Compute u"(t_i)
		for (int i = 1; i < forces.size(); i++) {
			InputForce fi = forces.get(i);
			InputForce fi_1 = forces.get(i - 1);
			double ti = fi.getT();
			double ti_1 = fi_1.getT();
			System.out.println("ti = " + ti + " ti_1 = " + ti_1);
			double pi = fi.getP();
			double pi_1 = fi_1.getP();
			System.out.println("Pi = " + pi + " Pi_1 = " + pi_1);
			double i1 = int1(ti) - int1(ti_1);
			double i2 = int2(ti) - int2(ti_1);
			double i3 = int3(ti) - int3(ti_1);
			double i4 = int4(ti) - int4(ti_1);
			System.out.println("i1= " + i1 + " i2 = " + i2 + "i3 = " + i3
					+ " i4 = " + i4);
			System.out.println("Dp/Dt = " + (pi - pi_1) / (ti - ti_1));
			double at = a_ti_1 + (pi_1 - ti_1 * ((pi - pi_1) / (ti - ti_1)))
					* i1 + ((pi - pi_1) / (ti - ti_1)) * i4;
			double bt = b_ti_1 + (pi_1 - ti_1 * ((pi - pi_1) / (ti - ti_1)))
					* i2 + ((pi - pi_1) / (ti - ti_1)) * i3;

			double c = Math.exp(-kesi * omega * ti) / (m * omega_D);
			double cp = -kesi * omega * c;
			double d = at * Math.sin(omega_D * ti) - bt
					* Math.cos(omega_D * ti);
			double dp = omega_D * at * Math.cos(omega_D * ti) + omega_D * bt
					* Math.sin(omega_D * ti);

			double u = c * d;
			double v = cp * d + c * dp;
			double a = (pi - dampC * v - k * u) / m;

			double vSup = Math
					.sqrt(Math.pow(u * k, 2) + Math.pow(v * dampC, 2));

			compareMaxValues(u, v, a, vSup);

			Response r = new Response(ti, pi, u, v, a, vSup);
			responses.add(r);

			a_ti_1 = at;
			b_ti_1 = bt;
		}

		// Set u(t_n)=u_0 and u'(t_n)=u'_0 for free vibration of the system
		// after forced vibration,
		// where n is the last time force recorded
		double u0 = responses.get(responses.size() - 1).getU();
		double v0 = responses.get(responses.size() - 1).getV();
		double tn = responses.get(responses.size() - 1).getT();

		double tau = tn;

		// Compute u, u', u" for free vibration until desired time
		while (tau < tMax) {
			tau += dt;
			double t = tau - tn;
			double e = Math.pow(Math.E, -kesi * omega * t);
			double ep = -kesi * omega * e;
			double epp = -kesi * omega * ep;
			double f = u0 * Math.cos(omega_D * t)
					+ ((v0 + kesi * omega * u0) / omega_D)
					* Math.sin(omega_D * t);
			double fp = -u0 * omega_D * Math.sin(omega_D * t)
					+ ((v0 + kesi * omega * u0) / omega_D) * omega_D
					* Math.cos(omega_D * t);
			double fpp = -u0 * omega_D * omega_D * Math.cos(omega_D * t)
					- ((v0 + kesi * omega * u0) / omega_D) * omega_D * omega_D
					* Math.sin(omega_D * t);

			double u = e * f;
			double v = ep * f + e * fp;
			double a = epp * f + 2 * ep * fp + e * fpp;

			double vSup = Math
					.sqrt(Math.pow(u * k, 2) + Math.pow(v * dampC, 2));

			compareMaxValues(u, v, a, vSup);

			Response r = new Response(tau, 0.0, u, v, a, vSup);
			responses.add(r);
		}
	}

	/**
	 * @param u
	 * @param v
	 * @param a
	 * @param vSup
	 */
	private void compareMaxValues(double u, double v, double a, double vSup) {
		if (Math.abs(u) > Math.abs(uMax))
			uMax = u;
		if (Math.abs(v) > Math.abs(vMax))
			vMax = v;
		if (Math.abs(a) > Math.abs(aMax))
			aMax = a;
		if (Math.abs(vSup) > Math.abs(vSupMax))
			vSupMax = vSup;
	}

	/**
	 * 
	 */
	private void computeForceFromGroundAcceleration() {
		for (InputForce force : forces) {
			force.setP(-force.getP() * m * gr);
		}
	}

	/**
	 * @param t_0
	 * @param i1_i_1
	 * @param i2_i_1
	 * @return
	 */
	private double int4(double t) {
		return c1(t) * int1(t) - c2(t) * int2(t);
	}

	/**
	 * @param t_0
	 * @param i1_i_1
	 * @param i2_i_1
	 * @return
	 */
	private double int3(double t) {
		return c1(t) * int2(t) + c2(t) * int1(t);
	}

	/**
	 * @param t
	 * @return
	 */
	private double c2(double t) {
		return omega_D / (Math.pow(kesi * omega, 2) + Math.pow(omega_D, 2));
	}

	/**
	 * @param t
	 * @return
	 */
	private double c1(double t) {
		return t
				- ((kesi * omega) / (Math.pow(kesi * omega, 2) + Math.pow(
						omega_D, 2)));
	}

	/**
	 * @param t_0
	 * @return
	 */
	private double int2(double t) {
		return mult1(t)
				* (kesi * omega * Math.sin(omega_D * t) - omega_D
						* Math.cos(omega_D * t));
	}

	/**
	 * @param d
	 * @return
	 */
	private double int1(double t) {
		return mult1(t)
				* (kesi * omega * Math.cos(omega_D * t) + omega_D
						* Math.sin(omega_D * t));
	}

	/**
	 * @param t
	 * @return
	 */
	private double mult1(double t) {
		return Math.exp(kesi * omega * t)
				/ (Math.pow(kesi * omega, 2) + Math.pow(omega_D, 2));
	}

	/**
	 * 
	 */
	private void computeForces() {
		forces.clear();
		double t = 0;

		for (int i = 1; i < inputForces.size(); i++) {
			InputForce force1 = inputForces.get(i - 1);
			double t1 = force1.getT();
			double f1 = force1.getP();
			InputForce force2 = inputForces.get(i);
			double t2 = force2.getT();
			double f2 = force2.getP();

			double ml = (f2 - f1) / (t2 - t1);
			int j = 0;
			while (t < (t2 - dt)) {
				t = t1 + j * dt;
				double f = f1 + ml * j * dt;
				InputForce force = new InputForce(t, f);
				forces.add(force);
				j++;
			}

		}
	}

	/**
	 * @return the inputForces
	 */
	public Vector<InputForce> getInputForces() {
		return inputForces;
	}

	/**
	 * @param inputForces
	 *            the inputForces to set
	 */
	public void setInputForces(Vector<InputForce> inputForces) {
		this.inputForces = inputForces;
	}

	/**
	 * @return the kesi
	 */
	public double getKesi() {
		return kesi;
	}

	/**
	 * @param kesi
	 *            the kesi to set
	 */
	public void setKesi(double kesi) {
		this.kesi = kesi;
	}

	/**
	 * @return the m
	 */
	public double getM() {
		return m;
	}

	/**
	 * @param m
	 *            the m to set
	 */
	public void setM(double m) {
		this.m = m;
	}

	/**
	 * @return the omega
	 */
	public double getOmega() {
		return omega;
	}

	/**
	 * @param omega
	 *            the omega to set
	 */
	public void setOmega(double omega) {
		this.omega = omega;
	}

	/**
	 * @return the omega_D
	 */
	public double getOmega_D() {
		return omega_D;
	}

	/**
	 * @param omega_D
	 *            the omega_D to set
	 */
	public void setOmega_D(double omega_D) {
		this.omega_D = omega_D;
	}

	public Vector<Response> getResponses() {
		return responses;
	}

	public void setResponses(Vector<Response> responses) {
		this.responses = responses;
	}

	/**
	 * @return the dt
	 */
	public double getDt() {
		return dt;
	}

	/**
	 * @param dt
	 *            the dt to set
	 */
	public void setDt(double dt) {
		this.dt = dt;
	}

	/**
	 * @return the interpolate
	 */
	public boolean isInterpolate() {
		return interpolate;
	}

	/**
	 * @param interpolate
	 *            the interpolate to set
	 */
	public void setInterpolate(boolean interpolate) {
		this.interpolate = interpolate;
	}

	/**
	 * @return the forceIsGroundAcceleration
	 */
	public boolean isForceIsGroundAcceleration() {
		return forceIsGroundAcceleration;
	}

	/**
	 * @param forceIsGroundAcceleration
	 *            the forceIsGroundAcceleration to set
	 */
	public void setForceIsGroundAcceleration(boolean forceIsGroundAcceleration) {
		this.forceIsGroundAcceleration = forceIsGroundAcceleration;
	}

	/**
	 * @return the k
	 */
	public double getK() {
		return k;
	}

	/**
	 * @param k
	 *            the k to set
	 */
	public void setK(double k) {
		this.k = k;
	}

	/**
	 * @return the tMax
	 */
	public double gettMax() {
		return tMax;
	}

	/**
	 * @param tMax
	 *            the tMax to set
	 */
	public void settMax(double tMax) {
		this.tMax = tMax;
	}

	/**
	 * @return the gr
	 */
	public double getGr() {
		return gr;
	}

	/**
	 * @param gr
	 *            the gr to set
	 */
	public void setGr(double gr) {
		this.gr = gr;
	}

	/**
	 * @return the uMax
	 */
	public double getuMax() {
		return uMax;
	}

	/**
	 * @param uMax
	 *            the uMax to set
	 */
	public void setuMax(double uMax) {
		this.uMax = uMax;
	}

	/**
	 * @return the vMax
	 */
	public double getvMax() {
		return vMax;
	}

	/**
	 * @param vMax
	 *            the vMax to set
	 */
	public void setvMax(double vMax) {
		this.vMax = vMax;
	}

	/**
	 * @return the aMax
	 */
	public double getaMax() {
		return aMax;
	}

	/**
	 * @param aMax
	 *            the aMax to set
	 */
	public void setaMax(double aMax) {
		this.aMax = aMax;
	}

	/**
	 * @return the vSupMax
	 */
	public double getvSupMax() {
		return vSupMax;
	}

	/**
	 * @param vSupMax
	 *            the vSupMax to set
	 */
	public void setvSupMax(double vSupMax) {
		this.vSupMax = vSupMax;
	}
}
