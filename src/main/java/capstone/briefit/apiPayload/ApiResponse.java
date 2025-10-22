package capstone.briefit.apiPayload;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
//@Schema(title = "API 공통 응답")
public class ApiResponse<T> {

    private String code;

    private String message;

    private T data;

    // 성공 응답 생성 메서드들
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .code("200")
                .message(message)
                .data(data)
                .build();
    }

    // 에러 응답 생성 메서드
    public static <T> ApiResponse<T> error(String errorCode, String message) {
        return ApiResponse.<T>builder()
                .code(errorCode)
                .message(message)
                .data(null)    // 에러 시에는 data를 null로
                .build();
    }

    // 에러 응답 생성 메서드(커스텀 메세지 추가)
    public static <T> ApiResponse<T> error(String message, String errorCode,T data) {
        return ApiResponse.<T>builder()
                .code(errorCode)
                .message(message)
                .data(data)
                .build();
    }
}
