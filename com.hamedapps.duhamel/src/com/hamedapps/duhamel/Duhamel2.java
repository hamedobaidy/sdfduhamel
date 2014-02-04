/**
 * 
 */
package com.hamedapps.duhamel;

import java.util.Vector;

/**
 * @author Hamed Mohammadi
 * 
 */
public class Duhamel2 {
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
	private double dwsq;
	private double wd;
	private double xiwd;

	public Duhamel2(Vector<InputForce> inputForces, double kesi, double m,
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

		
		// Compute A(t_0) and B(t_0) assuming A(t_-1)=B(t_-1)=0
		double a_ti_1 = 0;
		double b_ti_1 = 0;
		
		if(interpolate) 
			computeForces();
		else
			forces = inputForces;
		
		if(forceIsGroundAcceleration)
			computeForceFromGroundAcceleration();
		
		
		
		// Compute for first time force record or time zero
		// TODO firs iteration must be corrected
		
		// and than add this response record to responses
		// TODO the line below must be corrected
		Response r0 = new Response(forces.get(0).getT(), forces.get(0).getP(), 0.0, 0.0, forces.get(0).getP()/m);
		responses.add(r0);
		
		// While there is force record
		// Compute I_1 to I_4 for t_i
		// Compute A(t_i) and B(t_i)
		// Compute u(t_i)
		// Compute u'(t_i)
		// Compute u"(t_i)
		// TODO this loop must be corrected
		for (int i = 1; i < forces.size(); i++) {
			InputForce if_i = forces.get(i);
			InputForce if_i_1 = forces.get(i - 1);
			double ti = if_i.getT();
			double ti_1 = if_i_1.getT();
			double pi = if_i.getP();
			double pi_1 = if_i_1.getP();
			double i1 = computeI1limit(ti) - computeI1limit(ti_1);
			double i2 = computeI2limit(ti) - computeI2limit(ti_1);
			double i3 = computeI3limit(ti, i1, i2);
			double i4 = computeI4limit(ti, i1, i2)
					- computeI4limit(ti_1, i1, i2);
			double at = a_ti_1 + (pi_1 - ti_1 * ((pi - pi_1) / (ti - ti_1)))
					* i2 + ((pi - pi_1) / (ti - ti_1)) * i4;
			double bt = b_ti_1 + (pi_1 - ti_1 * ((pi - pi_1) / (ti - ti_1)))
					* i2 + ((pi - pi_1) / (ti - ti_1)) * i3;

			double c = Math.pow(Math.E, -kesi * omega * ti) / (m * omega_D);
			double cp = -kesi * omega * c;
			double cpp = -kesi * omega * cp;
			double d = at * Math.sin(omega_D * ti) - bt
					* Math.cos(omega_D * ti);
			double dp = omega_D * at * Math.cos(omega_D * ti) + omega_D * bt
					* Math.sin(omega_D * ti);
			double dpp = -Math.pow(omega_D, 2) * at * Math.sin(omega_D * ti)
					+ Math.pow(omega_D, 2) * bt * Math.cos(omega_D * ti);

			double u = c * d;
			double v = cp * d + c * dp;
			double a = cpp * d + 2 * cp * dp + c * dpp;

			Response r = new Response(ti, pi, u, v, a);
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

		double t = tn;
		
		// Compute u, u', u" for free vibration until desired time
		while(t<tMax) {
			t += dt;
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
			Response r = new Response(t, 0.0, u, v, a);
			responses.add(r);
		}
	}

	/**
	 * 
	 */
	private void computeForceFromGroundAcceleration() {
		for (InputForce force : forces) {
			force.setP(-force.getP()*m);
		}
	}

	/**
	 * @param t_0
	 * @param i1_i_1
	 * @param i2_i_1
	 * @return
	 */
	private double computeI4limit(double t, double i1, double i2) {
		double c1 = computeCI3_4_1(t);
		double c2 = computeCI3_4_2(t);
		return c1 * i1 - c2 * i2;
	}

	/**
	 * @param t_0
	 * @param i1_i_1
	 * @param i2_i_1
	 * @return
	 */
	private double computeI3limit(double t, double i1, double i2) {
		double c1 = computeCI3_4_1(t);
		double c2 = computeCI3_4_2(t);

		return c1 * i2 + c2 * i1;
	}

	/**
	 * @param t
	 * @return
	 */
	private double computeCI3_4_2(double t) {
		return omega_D / (Math.pow(kesi * omega, 2) + Math.pow(omega_D, 2));
	}

	/**
	 * @param t
	 * @return
	 */
	private double computeCI3_4_1(double t) {
		return t
				- ((kesi * omega) / (Math.pow(kesi * omega, 2) + Math.pow(
						omega_D, 2)));
	}

	/**
	 * @param t_0
	 * @return
	 */
	private double computeI2limit(double t) {
		double c = computeMultiplier1(t);
		return c
				* (kesi * omega * Math.sin(omega_D * t) - omega_D
						* Math.cos(omega_D * t));
	}

	/**
	 * @param d
	 * @return
	 */
	private double computeI1limit(double t) {
		double c = computeMultiplier1(t);

		return c
				* (kesi * omega * Math.cos(omega_D * t) + omega_D
						* Math.sin(omega_D * t));
	}

	/**
	 * @param t
	 * @return
	 */
	private double computeMultiplier1(double t) {
		return Math.pow(Math.E, kesi * omega * t)
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

			m = (f2 - f1) / (t2 - t1);
			int j = 0;
			while (t < (t2-dt)) {
				t = t1 + j * dt;
				double f = f1+ m*j*dt;
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
	 * @param interpolate the interpolate to set
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
	 * @param forceIsGroundAcceleration the forceIsGroundAcceleration to set
	 */
	public void setForceIsGroundAcceleration(boolean forceIsGroundAcceleration) {
		this.forceIsGroundAcceleration = forceIsGroundAcceleration;
	}

	/**
	 * @return the tMax
	 */
	public double gettMax() {
		return tMax;
	}

	/**
	 * @param tMax the tMax to set
	 */
	public void settMax(double tMax) {
		this.tMax = tMax;
	}
	
	public void testCompute() {
		
		// TODO rewrite to follow compltely bargi procedure including interpolation and base acceleration consideration
		
		responses.clear();
		
		if(interpolate) 
			computeForces();
		else
			forces = inputForces;
		
		if(forceIsGroundAcceleration)
			computeForceFromGroundAcceleration();
		
		double xi = kesi;
		
		InputForce f0 = forces.get(0);
		double fiM1 = f0.getP();
		double tiM1 = f0.getT();
		double ati = 0;
		double bti = 0;
		double dat = 0;
		double dbt = 0;
		double y = 0;
		double v = 0;
		double p = 0;
		double yMax = 0.0;
		double vMax = 0.0;
		double aMax = 0.0;
		double pMax = 0.0;
		omega = Math.sqrt(k/m);
		double crit = 2*Math.sqrt(k*m);
		double c = xi* crit;
		wd = omega * Math.sqrt(1-Math.pow(xi, 2));
		xiwd = xi*omega;
		dwsq = Math.pow(xiwd, 2)+ Math.pow(xi, 2);
		double acc = fiM1/m;
		
		Response r0 = new Response(tiM1, fiM1, y, v, acc);
		responses.add(r0);
		
		for(int i = 1; i < forces.size(); i++ ) {
			
			// Solve for displacement
			double fi = forces.get(i).getP();
			double ti = forces.get(i).getT();
			double dfti = fi - fiM1;
			double dti = ti - tiM1;
			double ft = dfti/dti;
			double g = fiM1 -tiM1*ft;
			double ai = int1(ti)-int1(tiM1);
			double bi = int2(ti)-int2(tiM1);
			double vs = int3(ti)-int3(tiM1);
			double vc = int4(ti)-int4(tiM1);
			ai = ai*g;
			bi = bi*g;
			bi = bi + ft*vs;
			ai = ai + ft*vc;
			ati = ati+ai;
			bti = bti + bi;
			y = Math.exp(-xiwd*ti)*(ati*Math.sin(wd*ti)-bti*Math.cos(wd*ti))/(m*wd);
			
			// Solve for velocity
			double da = (wd*bti-xiwd*ati)*Math.sin(wd*ti);
			double db = (wd*ati+xiwd*bti)*Math.cos(wd*ti);
			v= Math.exp(-xiwd*ti)*(da+db)/(m*wd);
			
			// Solve for acceleration
			acc = (fi-c*v-k*y)/m;
			
			// Solve for spring and damping forces
			double fs = y*k;
			double fd = v*c;
			p = Math.sqrt(Math.pow(fs, 2)+Math.pow(fd, 2));
			
			// Save the maximum value
			if(Math.abs(y)>Math.abs(yMax))
				yMax=y;
			if(Math.abs(v)>Math.abs(vMax))
				vMax = v;
			if(Math.abs(acc)>Math.abs(aMax))
				aMax=acc;
			
			// Increment variables
			tiM1 = ti;
			fiM1 = fi;
			
			Response r = new Response(ti, fi, y, v, acc);
			responses.add(r);
		}
	}

	/**
	 * @param tiM1
	 * @return
	 */
	private double int4(double ti) {
		return ti*int1(ti)-xiwd*int1(ti)/dwsq-wd*int2(ti)/dwsq;
	}

	/**
	 * @param ti
	 * @return
	 */
	private double int3(double ti) {
		return ti*int2(ti)-xiwd*int2(ti)/dwsq+wd*int1(ti)/dwsq;
	}

	/**
	 * @param ti
	 * @return
	 */
	private double int2(double ti) {
		return Math.exp(xiwd*ti)*(xiwd*Math.sin(wd*ti)-wd*Math.cos(wd*ti))/dwsq;
	}

	/**
	 * @param ti
	 * @return
	 */
	private double int1(double ti) {
		return Math.exp(xiwd*ti)*(xiwd*Math.cos(wd*ti)+wd*Math.sin(wd*ti))/dwsq;
	}

	/**
	 * @return the k
	 */
	public double getK() {
		return k;
	}

	/**
	 * @param k the k to set
	 */
	public void setK(double k) {
		this.k = k;
	}
}
