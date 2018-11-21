package com.scl.influxdb.demo;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.io.Serializable;

public class InfluxDBTest2 extends AbstractJavaSamplerClient implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sr = new SampleResult();
        InfluxDBTest test = new InfluxDBTest();
        test.testInsert();
        return sr;
    }

    public void teardownTest(JavaSamplerContext context) {

    }

    public static void main(String[] args) {
        InfluxDBTest2 v0 = new InfluxDBTest2();
        Arguments arguments = v0.getDefaultParameters();
        JavaSamplerContext context = new JavaSamplerContext(arguments);
        v0.setupTest(context);
        v0.runTest(context);
        v0.teardownTest(context);
    }

}
