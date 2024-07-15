package leets.weeth.global.common.error;

import leets.weeth.global.common.error.exception.ExceptionType;
import leets.weeth.global.common.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class CommonExceptionController {

    @ExceptionHandler(Exception.class)  // 모든 Exception 처리
    public CommonResponse<Void> handle(Exception ex) {
        int status = 500;

        Optional<ExceptionType> e = ExceptionType.findException(ex);    // ExceptionType 찾기
        if (e.isPresent()) {    // 클라이언트 오류 시
            status = e.get().getHttpStatus().value();   // 상태 값 4XX로 설정
        }
        log.error("Error", ex);

        return CommonResponse.createFailure(status, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)  // BindException == @ModelAttribute 어노테이션으로 받은 파라미터의 @Valid 통해 발생한 Exception
    public CommonResponse<Void> handle(MethodArgumentNotValidException e) {   // 클라이언트의 오류일 경우
        int status = 400;   // 파라미터 값 실수이므로 4XX
        log.error("Error", e);
        return CommonResponse.createFailure(status, e.getAllErrors().get(0).getDefaultMessage());   // 디폴트 메세지 가져오기
    }

    @ExceptionHandler(BindException.class)  // BindException == @ModelAttribute 어노테이션으로 받은 파라미터의 @Valid 통해 발생한 Exception
    public CommonResponse<Void> handle(BindException e) {   // 클라이언트의 오류일 경우
        int status = 400;   // 파라미터 값 실수이므로 4XX

        if (e instanceof ErrorResponse) {   // Exception이 ErrorResponse의 인스턴스라면
            status = ((ErrorResponse) e).getStatusCode().value();   // ErrorResponse에서 상태 값 가져오기
        }
        log.error("Error", e);

        return CommonResponse.createFailure(status, e.getFieldError().getField() + " : " + e.getFieldError().getDefaultMessage());
    }
}