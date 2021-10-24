//package cn.lnkdoc.auth.interceptor
//
//import cn.langkye.awesome.commom.constant.CODE
//import cn.langkye.awesome.commom.exception.AuthException
//import cn.langkye.awesome.web.model.HttpResponseBody
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import org.springframework.web.bind.annotation.ControllerAdvice
//import org.springframework.web.bind.annotation.ExceptionHandler
//import org.springframework.web.bind.annotation.ResponseBody
//import javax.servlet.http.HttpServletRequest
//
//
///**
// * 全局异常处理：web层
// *
// * @author langkye
// * @date 2021/10/13
// */
//@ControllerAdvice
//class GlobalExceptionHandler {
//    private val logger : Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
//
//    /**
//     * 处理自定义的业务异常
//     * @param httpServletRequest HttpServletRequest
//     * @param ex                 Exception
//     * @return                   cn.lnkdoc.auth.model.ResponseBody
//     */
//    @ExceptionHandler(value = [AuthException::class])
//    @ResponseBody
//    fun bizExceptionHandler(httpServletRequest: HttpServletRequest?, ex: AuthException): HttpResponseBody {
//        logger.error("发生业务异常！错误码：{},原因：{}", ex.code, ex.message)
//        ex.printStackTrace()
//        return HttpResponseBody().error(ex.code, ex.message)
//    }
//
//    /**
//     * 处理空指针的异常
//     *
//     * @param httpServletRequest HttpServletRequest
//     * @param ex                 Exception
//     * @return                   cn.lnkdoc.auth.model.ResponseBody
//     */
//    @ExceptionHandler(value = [NullPointerException::class])
//    @ResponseBody
//    fun exceptionHandler(httpServletRequest: HttpServletRequest?, ex: NullPointerException): HttpResponseBody {
//        logger.error("发生空指针异常！原因是:", ex.message)
//        ex.printStackTrace()
//        return HttpResponseBody().error(CODE.SERVER_ERROR.INTERNAL_SERVER_ERROR, "发生空指针异常:" + ex.message)
//    }
//
//    /**
//     * 其他异常
//     *
//     * @param httpServletRequest HttpServletRequest
//     * @param ex                 Exception
//     * @return                   cn.lnkdoc.auth.model.ResponseBody
//     */
//    @ExceptionHandler(value = [Exception::class])
//    @ResponseBody
//    fun exceptionHandler(httpServletRequest: HttpServletRequest?, ex: Exception): HttpResponseBody {
//        logger.error("未知异常！原因是:", ex.message)
//        ex.printStackTrace()
//        return HttpResponseBody().error(CODE.SERVER_ERROR.INTERNAL_SERVER_ERROR)
//    }
//}