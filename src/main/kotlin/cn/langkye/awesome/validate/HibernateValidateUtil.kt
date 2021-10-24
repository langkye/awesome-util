package cn.langkye.awesome.validate

import cn.langkye.awesome.commom.constant.AweExConstant
import cn.langkye.awesome.commom.exception.AweException
import jakarta.validation.Validation
import org.hibernate.validator.HibernateValidator
import java.lang.reflect.Method


/**
 * hibernate.validator工具类
 *      implementation("org.springframework:spring-core:5.3.10")
 *      implementation("org.hibernate.validator:hibernate-validator:7.0.1.Final") {
 *          //if import bind jakarta.validation-api version under 3.0.0
 *          //you must manual import : implementation("jakarta.validation:jakarta.validation-api:3.0.0")
 *      }
 *      implementation("jakarta.validation:jakarta.validation-api:3.0.0")
 *      implementation("jakarta.el:jakarta.el-api:4.0.0")
 *      implementation("org.glassfish:jakarta.el:4.0.1")
 * @author langkye
 * @date 2021/10/20
 */
class HibernateValidateUtil {

    companion object {
        /**
         * 使用hibernate的注解来进行验证 failFast true:仅仅返回第一条错误信息 false返回所有错误，此处采用快速失败模式
         */
        @JvmStatic
        private val validator = Validation.byProvider(
            HibernateValidator::class.java
        ).configure().failFast(true).buildValidatorFactory().validator

        /**
         * 扩展的Validator
         */
        @JvmStatic
        private val executableValidator = validator.forExecutables()

        /**
         * 检验Java实体对象
         *
         * @param obj 校验的对象
         * @return String 错误提示信息
         */
        @JvmStatic
        fun <T> validateJavaBean(obj: T): String? {
            val validateResult = validator.validate(obj)
            if (validateResult.isEmpty()) {
                return null
            }
            val sb = StringBuilder()
            for (v in validateResult) {
                sb.append(v.message).append(".")
            }
            return sb.toString()
        }

        /**
         * 检验Java实体对象
         *
         * @param obj 校验的对象
         * @throws TxException 交易异常
         */
        @JvmStatic
        @Throws(AweException::class)
        fun <T> validateJavaBeanThrowsException(obj: T) {
            val validateResult = validator.validate(obj)
            if (validateResult.isEmpty()) {
                return
            }
            val sb = StringBuilder()
            for (v in validateResult) {
                sb.append(v.message).append(".")
            }
            val build = AweException
                .builder()
                .message(sb.toString())
                .aweExConstant(AweExConstant.PARAMS_EX)
                .build()
            throw build
        }

        /**
         * 检验方法局部参数
         *
         * @param obj 校验的对象
         * @return String 错误提示信息
         */
        @JvmStatic
        fun <T> validateMethodParameter(obj: T, method: Method?, args: Array<Any?>?): String? {
            val validateResult = executableValidator.validateParameters(obj, method, args)
            if (validateResult.isEmpty()) {
                return null
            }
            val sb = StringBuilder()
            for (v in validateResult) {
                sb.append(v.message).append(".")
            }
            return sb.toString()
        }

        /**
         * 检验方法局部参数
         *
         * @param obj 校验的对象
         * @return String 错误提示信息
         */
        @JvmStatic
        @Throws(AweException::class)
        fun <T> validateMethodParameterThrowsException(obj: T, method: Method?, args: Array<Any?>?) {
            val validateResult = executableValidator.validateParameters(obj, method, args)
            if (validateResult.isEmpty()) {
                return
            }
            val sb = StringBuilder()
            for (v in validateResult) {
                sb.append(v.message).append(".")
            }
            throw AweException.builder().message(sb.toString()).aweExConstant(AweExConstant.PARAMS_EX).build()
        }
    }

}