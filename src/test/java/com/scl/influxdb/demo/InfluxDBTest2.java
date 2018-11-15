//package com.scl.influxdb.demo;
//
//import junit.framework.Assert;
//import org.influxdb.dto.QueryResult;
//import org.influxdb.dto.QueryResult.Result;
//import org.influxdb.dto.QueryResult.Series;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.BeanWrapperImpl;
//
//import java.io.Serializable;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//public class InfluxDBTest2 extends AbstractJavaSamplerClient implements Serializable {
//    private InfluxDBConnect influxDBConnect;
//    private String username = "influxdb";// 用户名
//    private String password = "influxdb";// 密码
//    private String openurl = "http://192.168.101.43:8086";// 连接地址
//    //    private String openurl = "http://192.168.154.100:8086";// 连接地址
//    private String database = "demo";// 数据库
//    private String measurement = "sys_code";
//
//    //http://192.168.80.130:8086/query?q=CREATE+DATABASE+%22testDB%22&db=testDB
//
//    @Before
//    public void setUp() {
//        // 创建 连接
//        influxDBConnect = new InfluxDBConnect(username, password, openurl, database);
//
//        influxDBConnect.influxDbBuild();
//
//    }
//
//
//    @Test
//    public void testInsert() {// 测试数据插入
//        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
//        for (int j = 0; j < 100; j++) {
//            Map<String, String> tags = new HashMap<String, String>();
//            Map<String, Object> fields = new HashMap<String, Object>();
//            List<CodeInfo> list = new ArrayList<CodeInfo>();
//            for (int i = 1; i <= 10000; i++) {
//                CodeInfo info1 = new CodeInfo();
//                info1.setId(i);
//                info1.setFlag(1);
//                info1.setName("BANKS");
//                info1.setCode("ABC");
//                info1.setDescr("中国农业银行");
//                info1.setDescrE("ABC");
//                info1.setTime(new Date().getTime() + "");
//                info1.setCreatedBy("system");
//                info1.setCreatedAt(formatter.format(new Date()));
//                list.add(info1);
//            }
//            System.out.println("insert data:" + list.size());
//            for (CodeInfo info : list) {
//                tags.put("TAG_CODE", info.getCode());
//                tags.put("TAG_NAME", info.getName());
//
//                fields.put("ID", info.getId());
//                fields.put("FLAG", info.getFlag());
//                fields.put("NAME", info.getName());
//                fields.put("CODE", info.getCode());
//                fields.put("DESCR", info.getDescr());
//                fields.put("DESCR_E", info.getDescrE());
//                fields.put("CREATED_BY", info.getCreatedBy());
//                fields.put("CREATED_AT", info.getCreatedAt());
//                fields.put("TIME", info.getTime());
//
//                influxDBConnect.insert(measurement, tags, fields);
//            }
//        }
//    }
//
//    @Test
//    public void testQuery() {// 测试数据查询
//        String command = "select * from sys_code";
//        QueryResult results = influxDBConnect.query(command);
//
//        if (results.getResults() == null) {
//            return;
//        }
//        List<CodeInfo> lists = new ArrayList<CodeInfo>();
//        getLists(results, lists);
//
//        Assert.assertTrue((!lists.isEmpty()));
//        Assert.assertEquals(21, lists.size());
//    }
//
//    @Test
//    public void testQueryWhere() {// tag 列名 区分大小写
//        String command = "select * from sys_code where TAG_CODE='ABC'";
//        QueryResult results = influxDBConnect.query(command);
//
//        if (results.getResults() == null) {
//            return;
//        }
//        List<CodeInfo> lists = new ArrayList<CodeInfo>();
//        getLists(results, lists);
//
//        Assert.assertTrue((!lists.isEmpty()));
//        Assert.assertEquals(21, lists.size());
//
//        CodeInfo info = lists.get(0);
//
//        Assert.assertEquals(info.getCode(), "ABC");
//
//    }
//
//    private void getLists(QueryResult results, List<CodeInfo> lists) {
//        for (Result result : results.getResults()) {
//
//            List<Series> series = result.getSeries();
//            for (Series serie : series) {
//                List<List<Object>> values = serie.getValues();
//                List<String> columns = serie.getColumns();
//
//                lists.addAll(getQueryData(columns, values));
//            }
//        }
//    }
//
//    @Test
//    public void deletMeasurementData() {
//        String command = "delete from sys_code where TAG_CODE='ABC'";
//        String err = influxDBConnect.deleteMeasurementData(command);
//        Assert.assertNull(err);
//    }
//
//    /*** 整理列名、行数据 ***/
//    private List<CodeInfo> getQueryData(List<String> columns,
//                                        List<List<Object>> values) {
//        List<CodeInfo> lists = new ArrayList<CodeInfo>();
//
//        for (List<Object> list : values) {
//            CodeInfo info = new CodeInfo();
//            BeanWrapperImpl bean = new BeanWrapperImpl(info);
//            for (int i = 0; i < list.size(); i++) {
//
//                String propertyName = setColumns(columns.get(i));// 字段名
//                Object value = list.get(i);// 相应字段值
//                bean.setPropertyValue(propertyName, value);
//            }
//
//            lists.add(info);
//        }
//
//        return lists;
//    }
//
//    /*** 转义字段 ***/
//    private String setColumns(String column) {
//        String[] cols = column.split("_");
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < cols.length; i++) {
//            String col = cols[i].toLowerCase();
//            if (i != 0) {
//                String start = col.substring(0, 1).toUpperCase();
//                String end = col.substring(1).toLowerCase();
//                col = start + end;
//            }
//            sb.append(col);
//        }
//        return sb.toString();
//    }
//
///*
//    @Test
//    public void MultiRequestsTest() {
//        // 构造一个Runner
//        TestRunnable runner = new TestRunnable() {
//            @Override
//            public void runTest() throws Throwable {
//
//                //你的测试内容
//                testInsert();
//            }
//        };
//        int runnerCount = 30;
//        //Rnner数组，想当于并发多少个。
//        TestRunnable[] trs = new TestRunnable[runnerCount];
//        for (int i = 0; i < runnerCount; i++) {
//            trs[i] = runner;
//        }
//        // 用于执行多线程测试用例的Runner，将前面定义的单个Runner组成的数组传入
//        MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);
//        try {
//            // 开发并发执行数组里定义的内容
//            mttr.runTestRunnables();
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }*/
//
//}
