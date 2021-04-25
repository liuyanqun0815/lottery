package com.cj.lottery.enums;

import lombok.Getter;

/**
 *
 * @author
 */
@Getter
public enum RuleOperatorTypeEnum {

    // ==
    EQUALS(1,"=="),

    // !=
    NOT_EQUALS(2, "!="),

    // >
    GREATER_THAN(3, ">"),

    // <
    LESS_THEN(4, "<"),

    // >=
    GREATER_EQUALS_THAN(5,">="),

    // <=
    LESS_EQUALS_THEN(6,"<="),

    // in
    CONTAIN(7,"in"),

    // not in
    NOT_COTAIN(8, "not in"),

    // between
    BETWEEN(9,"between"),

    // 正则表达式能匹配上
    EXPRESSION_MATCH(10,"正则匹配"),
    ;

    public static RuleOperatorTypeEnum parse(int code){

        for(RuleOperatorTypeEnum operatorTypeEnum : RuleOperatorTypeEnum.values()){
            if(operatorTypeEnum.getCode() == code){
                return operatorTypeEnum;
            }
        }
        throw new IllegalArgumentException();
    }

    RuleOperatorTypeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    int code;

    String msg;
}
