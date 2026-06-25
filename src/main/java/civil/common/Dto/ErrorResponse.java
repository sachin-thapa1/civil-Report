package civil.common.Dto;

import lombok.Data;

@Data
public class ErrorResponse {
    private boolean success = false;
    private ErrorDetail error;

    @Data
    public static class ErrorDetail {
        private String code;
        private String message;
        private int statusCode;
    }
}