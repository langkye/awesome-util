package cn.langkye.awesome.commom.exception

import cn.langkye.awesome.commom.annotation.NoArgKt


/**
 * 认证异常
 *
 * @author langkye
 * @date 2021/10/13
 */
@NoArgKt
class AuthException(
    override var message : String = ""
    ,var code : Int = 200
) : RuntimeException(){
}