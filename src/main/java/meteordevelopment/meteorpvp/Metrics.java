package meteordevelopment.meteorpvp;

import com.sun.management.OperatingSystemMXBean;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;
import meteordevelopment.meteorpvp.utils.RollingAverage;
import meteordevelopment.meteorpvp.utils.TpsRollingAverage;
import org.bukkit.Bukkit;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;

/** TPS and CPU monitoring ported from <a href="https://github.com/lucko/spark/blob/master/spark-common/src/main/java/me/lucko/spark/common/monitor">Spark</a> */
public class Metrics {
    private static final String OPERATING_SYSTEM_BEAN = "java.lang:type=OperatingSystem";
    private static final OperatingSystemMXBean BEAN;

    private static final TpsRollingAverage TPS_AVG = new TpsRollingAverage(TpsRollingAverage.TPS * 15);

    private static final RollingAverage SYSTEM_CPU_AVG = new RollingAverage(15);
    private static final RollingAverage PROCESS_CPU_AVG = new RollingAverage(15);

    private static long LAST;

    private static HTTPServer SERVER;

    static {
        try {
            MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName diagnosticBeanName = ObjectName.getInstance(OPERATING_SYSTEM_BEAN);
            BEAN = JMX.newMXBeanProxy(beanServer, diagnosticBeanName, OperatingSystemMXBean.class);
        } catch (Exception e) {
            throw new UnsupportedOperationException("OperatingSystemMXBean is not supported by the system", e);
        }

        Gauge.build()
                .name("mpvp_player_count")
                .help("Number of players currently online.")
                .create()
                .setChild(new Gauge.Child() {
                    @Override
                    public double get() {
                        return MeteorPvp.INSTANCE.getServer().getOnlinePlayers().size();
                    }
                })
                .register();

        Gauge.build()
                .name("mpvp_tps")
                .help("Current TPS from the past 15 seconds.")
                .create()
                .setChild(new Gauge.Child() {
                    @Override
                    public double get() {
                        return TPS_AVG.mean();
                    }
                })
                .register();

        Gauge cpu = Gauge.build()
                .name("mpvp_cpu_usage")
                .help("CPU usage 0-1 averaged from the last 15 seconds.")
                .labelNames("type")
                .create();
        cpu.setChild(new Gauge.Child() {
            @Override
            public double get() {
                return SYSTEM_CPU_AVG.mean();
            }
        }, "system");
        cpu.setChild(new Gauge.Child() {
            @Override
            public double get() {
                return PROCESS_CPU_AVG.mean();
            }
        }, "process");
        cpu.register();

        Gauge.build()
                .name("mpvp_memory_max")
                .help("Current amount of maximum available memory in bytes.")
                .create()
                .setChild(new Gauge.Child() {
                    @Override
                    public double get() {
                        return Runtime.getRuntime().maxMemory();
                    }
                })
                .register();

        Gauge.build()
                .name("mpvp_memory_free")
                .help("Current amount of free memory in bytes.")
                .create()
                .setChild(new Gauge.Child() {
                    @Override
                    public double get() {
                        return Runtime.getRuntime().freeMemory();
                    }
                })
                .register();
    }

    private static void collect() {
        // TPS
        long now = System.nanoTime();

        if (LAST != 0) {
            long diff = now - LAST;
            BigDecimal currentTps = TpsRollingAverage.TPS_BASE.divide(BigDecimal.valueOf(diff), 30, RoundingMode.HALF_UP);
            BigDecimal total = currentTps.multiply(BigDecimal.valueOf(diff));

            TPS_AVG.add(currentTps, diff, total);
        }

        LAST = now;

        // CPU
        BigDecimal systemCpuLoad = BigDecimal.valueOf(BEAN.getCpuLoad());
        BigDecimal processCpuLoad = BigDecimal.valueOf(BEAN.getProcessCpuLoad());

        if (systemCpuLoad.signum() != -1) SYSTEM_CPU_AVG.add(systemCpuLoad);
        if (processCpuLoad.signum() != -1) PROCESS_CPU_AVG.add(processCpuLoad);
    }

    public static void start() {
        if (SERVER != null) return;

        Bukkit.getScheduler().runTaskTimer(MeteorPvp.INSTANCE, Metrics::collect, 0, 20);

        try {
            SERVER = new HTTPServer.Builder()
                    .withPort(25693)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void stop() {
        if (SERVER == null) return;

        SERVER.close();
        SERVER = null;
    }
}
