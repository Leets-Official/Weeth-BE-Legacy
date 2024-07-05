package leets.weeth.global.common.response;

import lombok.Getter;

@Getter
public class CommonResponse<T> {    // 응답 객체

    private int code;
    private String message;
    private T data;

    public static <T> CommonResponse<T> createSuccess() {
        return createSuccess(null);
    }

    public static <T> CommonResponse<T> createSuccess(T data) {     // 성공
        return new CommonResponse<T>(200, "Success", data);
    }

    public static <T> CommonResponse<T> createFailure(int code, String message) {   // 실패
        return new CommonResponse<>(code, message, null);
    }

    public CommonResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
