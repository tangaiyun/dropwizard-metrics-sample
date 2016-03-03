package com.jsdttec.metricssample;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;

public class TestHistogram {
	private static final MetricRegistry metrics = new MetricRegistry();
	private List<String> stringList = new LinkedList<String>();
	private Histogram histogram = metrics.histogram(MetricRegistry.name(TestHistogram.class, "size-histogram"));
	/**
     * 在控制台上打印输出
     */
    private static ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).build();
    
	public void push(String input){
		stringList.add(input);
	}
	public void pop(String output){
		stringList.remove(output);
	}
	public void updateHisto(){
		histogram.update(stringList.size());
	}
	public static void main(String[] args) throws InterruptedException{
		
		reporter.start(3, TimeUnit.SECONDS);
		TestHistogram learnHistogram = new TestHistogram();
		for(int time = 0 ; time < 100000 ; time++){
			learnHistogram.push(String.valueOf(time));
			if(time%10 == 0){
				learnHistogram.updateHisto();
			}
			if(time%2 == 2){
				learnHistogram.pop(String.valueOf(time));
			}
			Thread.sleep(1);
		}
	}
}
