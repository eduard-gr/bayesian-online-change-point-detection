import com.bocpd.BayesianOnlineChangePointDetection;
import com.bocpd.model.GaussianUnknownMean;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import static org.junit.jupiter.api.Assertions.*;

public class BayesianOnlineChangePointDetectionTest {


    @Test
    void GaussianUnknownMean() throws NoSuchFieldException, IllegalAccessException {

        final var model = new GaussianUnknownMean(3891964, 28660621.78, 14330310.89);
        final var bocpd = new BayesianOnlineChangePointDetection(model, 1.0 / 1000);
        final Field field = bocpd.getClass().getDeclaredField("outcomes");
        field.setAccessible(true);

        assertFalse(bocpd.isChangePoint(3891964));
        assertArrayEquals(
            new double[]{-6.907755278982137, -0.001000500333583787},
            (double[]) field.get(bocpd));

        assertFalse(bocpd.isChangePoint(3880357));
        assertArrayEquals(
                new double[]{-6.907755278982137, -5.950762069897433, -0.0036103649736087817},
                (double[]) field.get(bocpd));

        assertFalse(bocpd.isChangePoint(3893750));
        assertArrayEquals(
                new double[]{-6.907755278982137, -6.296143653694305, -6.907068787992738, -0.0038514940929914587},
                (double[]) field.get(bocpd));
    }

    @Test
    void UnknownVariance() throws NoSuchFieldException, IllegalAccessException, IOException, URISyntaxException {

        //final String source = "961.csv";
        final String source = "943.csv";

        final URL resource = getClass().getClassLoader().getResource(source);
        Scanner reader = new Scanner(new File(resource.toURI()));
        final List<Double> samples = new ArrayList<>();

        /**
         * Skip header
         */
        reader.nextLine();

        while (reader.hasNextLine()) {
            String[] row = reader.nextLine().split(",",2);
            samples.add(Double.parseDouble(row[0]));

            /**
             * Измненения количества точек не влияет на точность
             */
            if(samples.size() > 50){
                break;
            }
        }

        double mean = 0.0;
        for(int i = 0; i< samples.size(); i++) {
            mean += samples.get(i);
        }
        mean /= samples.size();

        /**
         * Формула выборочной дисперсии
         */
        double variance_0 = 0;
        for(int i = 0; i< samples.size(); i++) {
            variance_0 += Math.pow(samples.get(i) - mean, 2);
        }
        variance_0 /= (samples.size() - 1);

        double variance_x = variance_0 * 0.50;

        final var model = new GaussianUnknownMean(samples.get(0), variance_0, variance_x);
        //0.000001
        final var bocpd = new BayesianOnlineChangePointDetection(model, 0.1 / 100);

        reader.close();
        reader = new Scanner(new File(resource.toURI()));

        /**
         * Skip header
         */
        reader.nextLine();

        while (reader.hasNextLine()) {

            String[] row = reader.nextLine().split(",",2);
            bocpd.isChangePoint(Double.parseDouble(row[0]));
        }

        reader.close();
    }
}
