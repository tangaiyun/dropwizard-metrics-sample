package com.jsdttec.metricssample;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;

public class TestMetersReportToGraphite {
	/**
     * 实例化一个registry，最核心的一个模块，相当于一个应用程序的metrics系统的容器，维护一个Map
     */
    private static final MetricRegistry metrics = new MetricRegistry();

    /**
     * 在graphite上输出
     */
    
    private static final Graphite graphite = new Graphite(new InetSocketAddress("192.168.30.205", 2003));
    private static final GraphiteReporter reporter = GraphiteReporter.forRegistry(metrics)
                                                      .prefixedWith("web1.jsdttec.com")
                                                      .convertRatesTo(TimeUnit.SECONDS)
                                                      .convertDurationsTo(TimeUnit.MILLISECONDS)
                                                      .filter(MetricFilter.ALL)
                                                      .build(graphite);
    
    /**
     * 实例化一个Meter
     */
    private static final Meter requests = metrics.meter(MetricRegistry.name(TestMeters.class, "request"));

    public static void handleRequest() {
        requests.mark();
    }

    public static void main(String[] args) throws InterruptedException {
        reporter.start(2, TimeUnit.SECONDS);
        Random r = new java.util.Random();
        while(true){
            handleRequest();
            Thread.sleep(r.nextInt(1000));
        }
    }
}
