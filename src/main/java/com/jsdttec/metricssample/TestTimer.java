package com.jsdttec.metricssample;

import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;

public class TestTimer {
	
	private static final MetricRegistry metrics = new MetricRegistry();
	
	private Timer timer = metrics.timer(MetricRegistry.name(TestTimer.class, "response-timer"));
	 private static ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).build();
	 
	
	public void handleRequest() throws InterruptedException{
		Context context = timer.time();
		for(int i = 0 ; i < 2 ; i++){
			Thread.sleep(1);
		}
		context.stop();
	}
	public static void main(String[] args) throws InterruptedException{
		reporter.start(3, TimeUnit.SECONDS);
		TestTimer learnTimer = new TestTimer();
		for(int time = 0 ; time < 10000 ; time++){
			learnTimer.handleRequest();
		}
		Thread.sleep(10000);
	}
}
