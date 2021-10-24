//package cn.lnkdoc.auth.interceptor
//
//import cn.langkye.awesome.commom.annotation.WrapperResponseBody
//import cn.langkye.awesome.web.model.HttpResponseBody
//import org.springframework.core.MethodParameter
//import org.springframework.http.MediaType
//import org.springframework.http.converter.HttpMessageConverter
//import org.springframework.http.server.ServerHttpRequest
//import org.springframework.http.server.ServerHttpResponse
//import org.springframework.web.bind.annotation.ControllerAdvice
//import org.springframework.web.context.request.RequestContextHolder
//import org.springframework.web.context.request.ServletRequestAttributes
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
//
//
///**
// * web mvc 响应处理
// * @doc https://www.cnblogs.com/sw-code/p/13956522.html
// * @author langkye
// * @date 2021/10/13
// */
//@ControllerAdvice
//class WebResponseHandler : ResponseBodyAdvice<Any> {
//    // 标记名称
//    val RESPONSE_RESULT_ANN = "RESPONSE-RESULT-ANN"
//
//    /**
//     * 判断是否要执行 beforeBodyWrite 方法，true为执行，false不执行，有注解标记的时候处理返回值
//     * @param returnType the return type
//     * @param converterType the selected converter type
//     * @return `true` if [.beforeBodyWrite] should be invoked;
//     * `false` otherwise
//     */
//    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
//        val sra = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?
//        val request = sra!!.request
//        // 判断请求是否有包装标记
//        val attribute = request.getAttribute(RESPONSE_RESULT_ANN)
//        if (attribute == null ) {
//            return false
//        }
//        else {
//            val responseResultAnn: WrapperResponseBody = attribute as WrapperResponseBody
//            return if (responseResultAnn == null) false else true
//        }
//    }
//
//    /**
//     * 对返回值做包装处理
//     * @param body the body to be written
//     * @param returnType the return type of the controller method
//     * @param selectedContentType the content type selected through content negotiation
//     * @param selectedConverterType the converter type selected to write to the response
//     * @param request the current request
//     * @param response the current response
//     * @return the body that was passed in or a modified (possibly new) instance
//     */
//    override fun beforeBodyWrite(
//        body: Any?,
//        returnType: MethodParameter,
//        selectedContentType: MediaType,
//        selectedConverterType: Class<out HttpMessageConverter<*>>,
//        request: ServerHttpRequest,
//        response: ServerHttpResponse
//    ): Any? {
//        if (body is HttpResponseBody) {
//            return body
//        } else if (body is String) {
//            return body
//        }
//        return body?.let { HttpResponseBody().success(it) }
//    }
//
//}