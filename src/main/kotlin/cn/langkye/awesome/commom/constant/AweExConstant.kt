package cn.langkye.awesome.commom.constant


/**
 * 异常状态码
 * public static void a() {
 * }
 * @author langkye
 * @date 2021/10/20
 */
enum class AweExConstant(val code: String, val message: String) {
    EX("999-999-999", "Unknown exception occur.")
    ,NET_EX("400-900-000", "Network-Request exception occur.")
    ,PARAMS_EX("400-000-000", "Params-Required exception occur.")
    ;
}