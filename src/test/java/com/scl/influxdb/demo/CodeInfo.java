package com.scl.influxdb.demo;
import lombok.Data;

import java.io.Serializable;

@Data
public class CodeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer flag;
    private String name;
    private String code;
    private String descr;
    private String descrE;
    private String createdBy;
    private String createdAt;
    private String time;
    private String tagCode;
    private String tagName;

}

