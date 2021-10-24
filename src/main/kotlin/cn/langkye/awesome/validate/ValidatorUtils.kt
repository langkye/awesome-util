package cn.langkye.awesome.validate

import cn.langkye.awesome.commom.constant.AweExConstant
import cn.langkye.awesome.commom.exception.AweException
import javax.validation.Validation
import javax.validation.Validator


/**
 * hibernate-validator校验工具类
 *      implementation("org.hibernate.validator:hibernate-validator:7.0.1.Final")
 *      implementation("javax.validation:validation-api:2.0.1.Final")
 *
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 *
 * @author Mark sunlightcs@gmail.com
 */
object ValidatorUtils {

    private var validator: Validator = Validation.buildDefaultValidatorFactory().validator

    /**
     * 校验对象
     *
     * @param object 待校验对象
     * @param groups 待校验的组
     * @throws AweException 校验不通过，则报RRException异常
     */
    @Throws(AweException::class)
    fun validateEntity(`object`: Any, vararg groups: Class<*>?) {
        val constraintViolations = validator.validate(`object`, *groups)
        if (!constraintViolations.isEmpty()) {
            var msg: StringBuilder? = null
            for (constraint in constraintViolations) {
                if (null == msg) {
                    msg = StringBuilder()
                    msg.append(constraint.message)
                } else {
                    msg.append("，").append(constraint.message)
                }
            }
            throw AweException.builder()
                .message(msg.toString())
                .aweExConstant(AweExConstant.PARAMS_EX)
                .build()
        }
    }

    init {
        //validator = Validation.buildDefaultValidatorFactory().validator
    }
}
