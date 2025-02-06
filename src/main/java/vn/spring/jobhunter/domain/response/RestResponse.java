package vn.spring.jobhunter.domain.response;


import lombok.Data;


@Data
public class RestResponse<T> {
    private int statusCode;
    private String error;

    private Object message;
    private T data;
}
