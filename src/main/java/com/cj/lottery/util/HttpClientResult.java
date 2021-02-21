package com.cj.lottery.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author: wanghui
 * @create: 2018/11/8 5:28 PM
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HttpClientResult implements Serializable {

    private static final long serialVersionUID = 2168152194164783950L;

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private String content;


}
