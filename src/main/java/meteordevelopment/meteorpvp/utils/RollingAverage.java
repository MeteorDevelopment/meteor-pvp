package meteordevelopment.meteorpvp.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Queue;

/** Ported from <a href="https://github.com/lucko/spark/blob/master/spark-common/src/main/java/me/lucko/spark/common/util/RollingAverage.java">Spark</a> */
public class RollingAverage {
    private final Queue<BigDecimal> samples;
    private final int windowSize;
    private BigDecimal total = BigDecimal.ZERO;

    public RollingAverage(int windowSize) {
        this.windowSize = windowSize;
        this.samples = new ArrayDeque<>(this.windowSize + 1);
    }

    public void add(BigDecimal num) {
        synchronized (this) {
            this.total = this.total.add(num);
            this.samples.add(num);

            if (this.samples.size() > this.windowSize) {
                this.total = this.total.subtract(this.samples.remove());
            }
        }
    }

    public double mean() {
        synchronized (this) {
            if (this.samples.isEmpty())  return 0;

            BigDecimal divisor = BigDecimal.valueOf(this.samples.size());
            return this.total.divide(divisor, 30, RoundingMode.HALF_UP).doubleValue();
        }
    }
}
