import com.bocpd.BayesianOnlineChangePointDetection;
import com.bocpd.model.GaussianUnknownMean;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
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
}
