package com.cj.lottery.domain.view;

import com.cj.lottery.constant.ContextCons;
import com.cj.lottery.enums.ErrorEnum;
import com.cj.lottery.util.ContextUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CjResult<T> {

    @ApiModelProperty("返回码, 非0一律表示失败")
    private int code;

    @ApiModelProperty("返回文案")
    private String msg;

    @ApiModelProperty("返回内容，可能为Null")
    private T data;

    private String traceId;

    private CjResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.traceId = ContextUtils.getTraceId();
    }

    public static <T> CjResult<T> success(T data) {
        return new CjResult(ErrorEnum.SUCCESS.code, ErrorEnum.SUCCESS.getDesc(), data);
    }

    public static <T> CjResult<T> success() {
        return new CjResult(ErrorEnum.SUCCESS.code, ErrorEnum.SUCCESS.getDesc(), null);
    }

    public static <T> CjResult<T> fail(ErrorEnum errorCode) {
        return new CjResult(errorCode.getCode(), errorCode.getDesc(), null);
    }

    public static <T> CjResult<T> fail(String msg) {
        return new CjResult(ErrorEnum.SYSTEM_ERROR.getCode(), msg, null);
    }

}
