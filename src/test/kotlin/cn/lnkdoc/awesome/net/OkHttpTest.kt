package cn.lnkdoc.awesome.net

import cn.langkye.awesome.net.util.OkHttpUtil
import cn.langkye.awesome.validate.HibernateValidateUtil
import cn.langkye.awesome.java.Model
import cn.langkye.awesome.validate.ValidatorUtils
import okhttp3.Call
import org.slf4j.Logger
import org.slf4j.LoggerFactory


/**
 *
 *
 * @author langkye
 * @date 2021/10/20
 */
class OkHttpTest {
    companion object {
        @JvmStatic
        val logger : Logger = LoggerFactory.getLogger(OkHttpUtil::class.java)
    }
}

fun main() {
    //m0()
    //m1()
    //m2()
    //m3()
    val model = Model()
    val validate = HibernateValidateUtil.validateJavaBean(model)
    OkHttpTest.logger.error("validate: $validate")
}

fun m0() {
    // get请求，方法顺序按照这种方式，切记选择post/get一定要放在倒数第二，同步或者异步倒数第一，才会正确执行
    val response = OkHttpUtil.builder().url("http://159.75.85.187:5083/api/public/captcha") // 有参数的话添加参数，可多个
        .get() // 可选择是同步请求还是异步请求
        //.async();
        .sync()

    println(response)
}
fun m1() {
    // post请求，分为两种，一种是普通表单提交，一种是json提交
    val response = OkHttpUtil.builder().url("http://159.75.85.187:5083/api/cm/auth/login") // 有参数的话添加参数，可多个
        .addParam("captchaImageId", "84918796420096075")
        .addParam("code", "bhgq")
        .addParam("mobiles", "langkye")
        .addParam("passWord", "TGFuZ2t5ZTEyMy4u")
        .addParam("type", "account")
        .addHeader(
            "Content-Type",
            "application/json; charset=utf-8"
        ) // 如果是true的话，会类似于postman中post提交方式的raw，用json的方式提交，不是表单
        // 如果是false的话传统的表单提交
        .post(true)
        .sync()

    println("-->$response")
}


fun m2() {
    // 选择异步有两个方法，一个是带回调接口，一个是直接返回结果
    val response = OkHttpUtil.builder().url("http://159.75.85.187:5083/api/public/captcha")
        .post(false)
        .async()
    println("response = $response")
}
fun m3(){
    OkHttpUtil
        .builder()
        .url("ahttp://159.75.85.187:5083/api/cm/auth/login")
        .post(true)
        .async(object : OkHttpUtil.ICallBack {
            override fun onSuccessful(call: Call?, data: String?) {
                println("call success: $data")
                println(call?.request().toString())
            }

            override fun onFailure(call: Call?, errorMsg: String?) {
                // 请求失败后的处理
                println("call failed: $errorMsg")
                println(call?.request().toString())
            }
        })

}