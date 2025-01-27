package com.bns.business.reports.reportsbddexp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultJson {
    private int code;
    private String message;
    private Object result;
}
