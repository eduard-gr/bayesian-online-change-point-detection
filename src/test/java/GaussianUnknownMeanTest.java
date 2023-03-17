import com.bocpd.model.GaussianUnknownMean;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GaussianUnknownMeanTest {

    @Test
    void NormalDistributionValue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final var model = new GaussianUnknownMean(3891964, 28660621.78, 14330310.89);

        Class[] arguments = new Class[]{
                double.class,
                double.class,
                double.class};

        Method method = model.getClass().getDeclaredMethod(
                "getNormalDistributionValue",
                arguments);

        method.setAccessible(true);
        Object r = method.invoke(model,6556.74711042, 3891964, 3891964);

        assertEquals(-9.70718842484593, r);
    }

    @Test
    void ComputePredictiveProbabilities() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final var model = new GaussianUnknownMean(3891964, 28660621.78, 14330310.89);
        double[] posterior;

        posterior = model.computePredictiveProbabilities(3891964);
        assertArrayEquals(new double[]{-9.707188424845917}, posterior);
        model.update(3891964);

        posterior = model.computePredictiveProbabilities(3880357);
        assertArrayEquals(new double[]{-11.274058932565936, -12.233662006290663}, posterior);
        model.update(3880357);
    }
}
