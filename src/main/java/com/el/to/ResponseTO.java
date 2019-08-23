package com.el.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * User: Rolandz
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseTO implements Serializable {
    private int status;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String errorMessage;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseTO{" +
                "status=" + status +
                ", errorMessage='" + errorMessage + '\'' +
                ", data=" + data +
                '}';
    }

    public static ResponseTO init(int status, Object data, String errorMessage) {
        ResponseTO responseTO = new ResponseTO();
        responseTO.status = status;
        responseTO.data = data;
        responseTO.errorMessage = errorMessage;
        return responseTO;
    }
}
