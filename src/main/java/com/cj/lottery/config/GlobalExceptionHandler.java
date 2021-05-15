package com.cj.lottery.config;

import com.alibaba.fastjson.JSON;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.enums.ErrorEnum;
import com.cj.lottery.util.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * 全局异常捕捉
 *
 * @author liuyanqun
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    private MailUtils mailUtils;

    /**
     * 处理空指针的异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public CjResult exceptionHandler(HttpServletRequest req, NullPointerException e) {
        log.error("发生空指针异常！原因是:", e);
        mailUtils.sendMail(JSON.toJSONString(e));
        return CjResult.fail(ErrorEnum.NullPointer);
    }

    /**
     * sql异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = SQLException.class)
    @ResponseBody
    public CjResult exceptionHandler(HttpServletRequest req, SQLException e) {
        log.error("sql异常！原因是:", e);
        mailUtils.sendMail(JSON.toJSONString(e));
        return CjResult.fail(ErrorEnum.SQL_ERROR);
    }

    /**
     * 处理其他异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CjResult exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("未知异常！原因是:", e);
        mailUtils.sendMail(JSON.toJSONString(e));
        return CjResult.fail(ErrorEnum.SYSTEM_ERROR);
    }

}
