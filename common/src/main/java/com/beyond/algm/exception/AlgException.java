package com.beyond.algm.exception;

import com.beyond.algm.constant.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AlgException extends Exception {

    public AlgException(String message) {
        super(message);
    }

    /**
     * @param message 错误信息/错误编码
     * @param params  占位符使用的参数
     */
    public AlgException(String message, Object[] params) {
        super(message + Constant.SCRIPT_DELIMIT + toJson(params));
    }

    /**
     * @param message 错误信息/错误编码
     * @param params  占位符使用的参数
     * @param cause   异常对象
     */
    public AlgException(String message, Object[] params, Throwable cause) {
        super(message + Constant.SCRIPT_DELIMIT + toJson(params), cause);
    }

    public AlgException(Throwable cause) {
        super(cause);
    }

    public AlgException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 数据转化为JSON格式(jackson)
     *
     * @param data
     * @return
     */
    private static String toJson(Object data) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(data);
        } catch (Exception e) {
        }
        return null;
    }

}