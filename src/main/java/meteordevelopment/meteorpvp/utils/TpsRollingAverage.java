package meteordevelopment.meteorpvp.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

/** Ported from <a href="https://github.com/lucko/spark/blob/master/spark-common/src/main/java/me/lucko/spark/common/monitor/tick/TickStatistics.java#L150">Spark</a> */
public class TpsRollingAverage {
    public static final long SEC_IN_NANO = TimeUnit.SECONDS.toNanos(1);
    public static final int TPS = 20;
    public static final int TPS_SAMPLE_INTERVAL = 20;
    public static final BigDecimal TPS_BASE = new BigDecimal(SEC_IN_NANO).multiply(new BigDecimal(TPS_SAMPLE_INTERVAL));

    private final int size;
    private long time;
    private BigDecimal total;
    private int index = 0;
    private final BigDecimal[] samples;
    private final long[] times;

    public TpsRollingAverage(int size) {
        this.size = size;
        this.time = size * SEC_IN_NANO;
        this.total = new BigDecimal(TPS).multiply(new BigDecimal(SEC_IN_NANO)).multiply(new BigDecimal(size));
        this.samples = new BigDecimal[size];
        this.times = new long[size];
        for (int i = 0; i < size; i++) {
            this.samples[i] = new BigDecimal(TPS);
            this.times[i] = SEC_IN_NANO;
        }
    }

    public synchronized void add(BigDecimal x, long t, BigDecimal total) {
        this.time -= this.times[this.index];
        this.total = this.total.subtract(this.samples[this.index].multiply(new BigDecimal(this.times[this.index])));
        this.samples[this.index] = x;
        this.times[this.index] = t;
        this.time += t;
        this.total = this.total.add(total);
        if (++this.index == this.size) {
            this.index = 0;
        }
    }

    public synchronized double mean() {
        return this.total.divide(new BigDecimal(this.time), 30, RoundingMode.HALF_UP).doubleValue();
    }
}
