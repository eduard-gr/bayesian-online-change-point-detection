package com.bocpd.model;

public interface DistributionModel {

    public void reset();

    public double[] computePredictiveProbabilities(double y);

    void update(double y);
}
