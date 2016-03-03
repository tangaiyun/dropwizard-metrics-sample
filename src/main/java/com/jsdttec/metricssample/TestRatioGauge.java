package com.jsdttec.metricssample;

import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.RatioGauge;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;

public class TestRatioGauge extends RatioGauge{
	
	/**
     * 实例化一个registry，最核心的一个模块，相当于一个应用程序的metrics系统的容器，维护一个Map
     */
    private static final MetricRegistry metrics = new MetricRegistry();

    /**
     * 在控制台上打印输出
     */
    private static ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).build();

    /**
     * 实例化一个Meter
     */
    private static final Meter hits = metrics.meter(MetricRegistry.name(TestMeters.class, "hits"));
    
    private static final Timer calls = metrics.timer(MetricRegistry.name(TestTimer.class, "calls"));
    
    
	
	@Override
	protected Ratio getRatio() {
		return Ratio.of(hits.getOneMinuteRate(),
                calls.getOneMinuteRate());
	}
	
	public static void main(String[] args) {
		 reporter.start(3, TimeUnit.SECONDS);
		 TestRatioGauge trg = new TestRatioGauge();
		 metrics.register(MetricRegistry.name(TestRatioGauge.class, "ratio"), trg); 
		 new Thread(new Runnable(){  
	            public void run(){  
	            	while(true) {
	            		hits.mark();
	            		try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            	}
	            }}).start();
		 new Thread(new Runnable(){  
	            public void run(){
	            	while(true) {
	            	Context context = calls.time();
	            	try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        		context.stop();
	            	}
	            }}).start();
	}
}
