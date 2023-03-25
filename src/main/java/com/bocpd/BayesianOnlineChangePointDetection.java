package com.bocpd;

import com.bocpd.model.DistributionModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.Math;
import java.util.Arrays;

public class BayesianOnlineChangePointDetection {

    private static final Logger logger = LogManager.getLogger(BayesianOnlineChangePointDetection.class);

    private final DistributionModel model;

    private double[] outcomes;

    private final double probability_occur;
    private final double probability_not_occur;

    private double mean = 0;

    public BayesianOnlineChangePointDetection(
            final DistributionModel model,
            final double change_probability
    ) {

        logger.debug("model:{} change probability:{}",
                model.getClass().getCanonicalName(),
                change_probability);

        this.model = model;
        this.outcomes = new double[]{0.0};
        this.probability_occur = Math.log(change_probability);
        this.probability_not_occur = Math.log(1 - change_probability);
    }

    private void reseat(double y){
        this.outcomes = new double[]{0.0};
        this.model.reset(y);
    }


    public boolean isChangePoint(double y){

        //3. Evaluate predictive probabilities.
        final double[] posteriors = this.model.computePredictiveProbabilities(y);

        final double[] outcomes = new double[posteriors.length + 1];

        double change_point_probabilities = 0;
        double evidence = 0;

        for(int i=0,j=1;i<posteriors.length;i++,j++){

            final double probabilities = posteriors[i] + this.outcomes[i];

            // 4. Calculate growth probabilities.
            final double growth_probabilities = probabilities + this.probability_not_occur;
            outcomes[j] = growth_probabilities;
            evidence += Math.exp(growth_probabilities);

            // 5. Calculate change point probabilities.
            change_point_probabilities += Math.exp(probabilities + this.probability_occur);

            evidence += Math.exp(probabilities + this.probability_occur);
        }

        outcomes[0] = Math.log(change_point_probabilities);

        evidence = Math.log(evidence);

        for(int i=0;i<outcomes.length;i++) {
            outcomes[i] -= evidence;
        }

        //Pass message
        this.outcomes = outcomes;

        int max = 0;
        for(int i=0;i<this.outcomes.length;i++){
            if(this.outcomes[i] > this.outcomes[max]){
                max = i;
            }
        }

//        logger.debug("{} {} {}",
//                y,
//                max,
//                Arrays.toString(outcomes));

        if(max < this.outcomes.length -1){
            this.reseat(y);
            return true;
        }

        //# 8. Update sufficient statistics.
        this.model.update(y);

        return false;
    }
}
