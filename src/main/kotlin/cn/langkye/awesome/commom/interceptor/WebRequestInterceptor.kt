//package cn.langkye.awesome.commom.interceptor
//
//import cn.langkye.awesome.commom.annotation.WrapperResponseBody
//import cn.langkye.awesome.commom.constant.CODE
//import cn.langkye.awesome.commom.exception.AuthException
//import com.devskiller.friendly_id.FriendlyId
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import org.slf4j.MDC
//import org.springframework.stereotype.Component
//import org.springframework.ui.ModelMap
//import org.springframework.web.context.request.WebRequest
//import org.springframework.web.context.request.WebRequestInterceptor
//import org.springframework.web.method.HandlerMethod
//import org.springframework.web.servlet.HandlerInterceptor
//import org.springframework.web.servlet.ModelAndView
//import javax.servlet.http.HttpServletRequest
//import javax.servlet.http.HttpServletResponse
//
//
///**
// * 请求拦截器
// *
// * @author langkye
// * @date 2021/10/13
// */
//@Component
//class WebRequestInterceptor :
//    HandlerInterceptor
//    , org.springframework.web.context.request.WebRequestInterceptor
//    //, org.springframework.web.context.request.WebRequestInterceptor
//{
//    val RESPONSE_RESULT_ANN = "RESPONSE-RESULT-ANN"
//    val logger : Logger = LoggerFactory.getLogger(WebRequestInterceptor::class.java)
//
//    /**
//     *
//     * @param request current HTTP request
//     * @param response current HTTP response
//     * @param handler chosen handler to execute, for type and/or instance evaluation
//     * @return `true` if the execution chain should proceed with the
//     * next interceptor or the handler itself. Else, DispatcherServlet assumes
//     * that this interceptor has already dealt with the response itself.
//     * @throws Exception in case of errors
//     */
//    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
//        val clientIp = request.getHeader("Host")
//        val traceId = FriendlyId.createFriendlyId()
//
//        MDC.put("traceId", traceId)
//        MDC.put("clientIp", clientIp)
//
//        val requestURL = request.requestURL
//        val requestURI = request.requestURI
//
//        logger.info("[clientIp=$clientIp],[traceId=$traceId],[requestURL=$requestURL],[requestURI=$requestURI]")
//        val status = response.status
//        if (status == 500) {
//            throw AuthException(CODE.SERVER_ERROR.MESSAGE, CODE.SERVER_ERROR.INTERNAL_SERVER_ERROR)
//        } else if (status == 404) {
//            throw AuthException("the api [$requestURI] not support", CODE.CLIENT_ERROR.NOT_FOUND)
//        }
//        if (handler is HandlerMethod) {
//            val handlerMethod = handler
//            val clazz = handlerMethod.beanType
//            val method: java.lang.reflect.Method = handlerMethod.method
//            // 判断是否在类对象上添加了注解
//            if (clazz.isAnnotationPresent(WrapperResponseBody::class.java)) {
//                // 设置此请求返回体，需要包装，往下传递，在ResponseBodyAdvice接口进行判断
//                request.setAttribute(RESPONSE_RESULT_ANN, clazz.getAnnotation(WrapperResponseBody::class.java))
//            }
//            else if (method.isAnnotationPresent(WrapperResponseBody::class.java)) {
//                request.setAttribute(RESPONSE_RESULT_ANN, method.getAnnotation(WrapperResponseBody::class.java))
//            }
//        }
//        return true
//    }
//
//    /**
//     *
//     * @param request current HTTP request
//     * @param response current HTTP response
//     * @param handler the handler (or [HandlerMethod]) that started asynchronous
//     * execution, for type and/or instance examination
//     * @param modelAndView the `ModelAndView` that the handler returned
//     * (can also be `null`)
//     * @throws Exception in case of errors
//     */
//    override fun postHandle(
//        request: HttpServletRequest,
//        response: HttpServletResponse,
//        handler: Any,
//        modelAndView: ModelAndView?
//    ) {
//        //super.postHandle(request, response, handler, modelAndView)
//    }
//
//    /**
//     *
//     * @param request current HTTP request
//     * @param response current HTTP response
//     * @param handler the handler (or [HandlerMethod]) that started asynchronous
//     * execution, for type and/or instance examination
//     * @param ex any exception thrown on handler execution, if any; this does not
//     * include exceptions that have been handled through an exception resolver
//     * @throws Exception in case of errors
//     */
//    override fun afterCompletion(
//        request: HttpServletRequest,
//        response: HttpServletResponse,
//        handler: Any,
//        ex: Exception?
//    ) {
//        //super.afterCompletion(request, response, handler, ex)
//    }
//
//    override fun preHandle(request: WebRequest) {
//        TODO("Not yet implemented")
//    }
//
//    override fun postHandle(request: WebRequest, model: ModelMap?) {
//        TODO("Not yet implemented")
//    }
//
//    override fun afterCompletion(request: WebRequest, ex: java.lang.Exception?) {
//        TODO("Not yet implemented")
//    }
//}