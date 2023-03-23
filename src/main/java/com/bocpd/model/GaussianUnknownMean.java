package com.bocpd.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class GaussianUnknownMean implements DistributionModel {


    private final static double SQRT2PI = 2.5066282746310002;

    private static final Logger logger = LogManager.getLogger(GaussianUnknownMean.class);

    private final double mean0;
    private final double var0;
    private final double varx;


    private double[] posterior_means;
    private double[] posterior_variance;

    public GaussianUnknownMean(double mean0, double var0, double varx) {
        this.mean0 = mean0;
        this.var0 = var0;
        this.varx = varx;

        this.posterior_means = new double[]{this.mean0};
        this.posterior_variance = new double[]{1 / this.var0};
    }

    @Override
    public void reset() {
        this.posterior_means = new double[]{this.mean0};
        this.posterior_variance = new double[]{1 / this.var0};
    }

    /**
     * Normal distribution
     * @param q standard deviation
     * @param u mean or expextation
     * @param x observation
     * @return
     */
    private double getNormalDistributionValue(
        double q,
        double u,
        double x
    ){
        //https://en.wikipedia.org/wiki/Normal_distribution
        return Math.log((1 / (q * SQRT2PI)) * Math.pow(Math.E, -0.5 * Math.pow((x-u) / q,2)));
    }

    private double getPosteriorStandardDeviation(
        double variance
    ){
        return Math.sqrt(1. / variance + this.varx);
    }

    @Override
    public double[] computePredictiveProbabilities(
        double x
    ) {
/*
        Compute predictive probabilities \pi, i.e. the posterior predictive
        for each run length hypothesis.
        Posterior predictive: see eq. 40 in (Murphy 2007).
 */
        double[] predictive_probabilities = new double[this.posterior_means.length];

        for(int i=0; i < predictive_probabilities.length;i++){

            predictive_probabilities[i] = this.getNormalDistributionValue(
                this.getPosteriorStandardDeviation(
                    this.posterior_variance[i]),
                this.posterior_means[i],
                x);
        }

//        logger.debug("predictive probabilities:{}",
//            Arrays.toString(predictive_probabilities));

        return predictive_probabilities;
    }

    @Override
    public void update(
        double x
    ) {
        //update all run length hypotheses.
        final double[] variances = new double[this.posterior_variance.length + 1];
        final double[] means = new double[this.posterior_means.length + 1];

        variances[0] = 1 / this.var0;
        means[0] = this.mean0;

        for(int i = 0; i<this.posterior_variance.length; i++){
            //# See eq. 19 in (Murphy 2007).
            double variance = this.posterior_variance[i] + (1 / this.varx);
            variances[i+1] = variance;

            //See eq. 24 in (Murphy 2007).
            means[i+1] = (this.posterior_means[i] * variances[i] + (x / this.varx)) / variance;
        }


        this.posterior_variance = variances;
        this.posterior_means = means;

//        logger.debug("posterior_variance:{}", Arrays.toString(posterior_variance));
//        logger.debug("posterior_means:{}", Arrays.toString(posterior_means));
    }
}
