package com.jsdttec.metricssample;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;

public class TestHealthCheck {
	
	static class Database {
		public boolean ping() {
			long n = System.currentTimeMillis();
			System.out.print(n+" ");
			if((n%2) == 0){
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	static class DatabaseHealthCheck extends HealthCheck {
	    private final Database database;

	    public DatabaseHealthCheck(Database database) {
	        this.database = database;
	    }

	    @Override
	    protected Result check() throws Exception {
	        if (database.ping()) {
	            return Result.healthy();
	        }
	        return Result.unhealthy("Can't ping database");
	    }
	}
	
	
    private static final HealthCheckRegistry healthChecks = new HealthCheckRegistry();
    
    public static void main(String[] args) throws InterruptedException {
    	Database db = new Database();
    	DatabaseHealthCheck dbhc = new DatabaseHealthCheck(db);
    	healthChecks.register(MetricRegistry.name(TestHealthCheck.class, "dbhealth"), dbhc); 
    	Random r = new java.util.Random();
    	while(true) {
    		final Map<String, HealthCheck.Result> results = healthChecks.runHealthChecks();
    		for (Entry<String, HealthCheck.Result> entry : results.entrySet()) {
    		    if (entry.getValue().isHealthy()) {
    		        System.out.println(entry.getKey() + " is healthy");
    		    } else {
    		        System.err.println(entry.getKey() + " is UNHEALTHY: " + entry.getValue().getMessage());
    		        final Throwable e = entry.getValue().getError();
    		        if (e != null) {
    		            e.printStackTrace();
    		        }
    		    }
    		}
    		Thread.sleep(r.nextInt(2000));
    	}
	}
}
