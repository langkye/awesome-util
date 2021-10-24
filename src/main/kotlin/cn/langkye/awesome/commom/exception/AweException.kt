package cn.langkye.awesome.commom.exception

import cn.langkye.awesome.commom.constant.AweExConstant
import org.apache.commons.lang3.StringUtils

/**
 * 自定义异常
 *
 * @author langkye
 * @date 2021/10/20
 */
class AweException private constructor(builder : AweException.Builder): RuntimeException() {
    override var message : String = "Unknown exception occur."
    var code : String = "999-999-999"
        get() = aweExConstant.name
        private set
    var aweExConstant : AweExConstant = AweExConstant.EX
        set(value) {
            if (AweExConstant.EX.code == code || StringUtils.isBlank(code)) {
                this.code = value.code
            }
            if (AweExConstant.EX.message == message || StringUtils.isBlank(message)) {
                this.message = value.message
            }
            field = value
        }

    init {
        //--this code must be in first initialize, because it will override "code" and "message"
        this.aweExConstant = builder.aweExConstant
        this.code = builder.code
        this.message = builder.message
    }

    class Builder(){
        var message : String = "Unknown exception occur."
            get
        var code : String = "999-999-999"
            get() = aweExConstant.name
            private set
        var aweExConstant : AweExConstant = AweExConstant.EX
            set(value) {
                if (AweExConstant.EX.code == code || StringUtils.isBlank(code)) {
                    this.code = value.code
                }
                if (AweExConstant.EX.message == message || StringUtils.isBlank(message)) {
                    this.message = value.message
                }
                field = value
            }
            get

        fun message(message: String) : Builder {
            this.message = message
            return this
        }

        fun aweExConstant(aweExConstant: AweExConstant) : Builder {
            this.aweExConstant = aweExConstant
            if (AweExConstant.EX.code == code || StringUtils.isBlank(code)) {
                this.code = aweExConstant.code
            }
            if (AweExConstant.EX.message == message || StringUtils.isBlank(message)) {
                this.message = aweExConstant.message
            }
            return this
        }

        fun build() : AweException {
            val aweException = AweException(this)
            return aweException
        }
    }

    //constructor() : this()
    //
    //constructor(message: String) : super(message) {
    //    this.message = message
    //}
    //constructor(aweExConstant : AweExConstant) : super() {
    //    this.aweExConstant = aweExConstant
    //    this.code = aweExConstant.code
    //}
    //
    //
    //constructor(message: String, throwable: Throwable?) : super(message, throwable) {
    //    this.message = message
    //}
    //
    //constructor(message: String, throwable: Throwable?, aweExConstant: AweExConstant) : super(message, throwable) {
    //    this.message = message
    //    this.aweExConstant = aweExConstant
    //    this.code = aweExConstant.code
    //}
    //
    //constructor(throwable: Throwable?, aweExConstant: AweExConstant) : super(throwable) {
    //    this.aweExConstant = aweExConstant
    //    this.code = aweExConstant.code
    //}
    //
    //constructor(message: String, aweExConstant: AweExConstant) : super(message) {
    //    this.message = message
    //    this.aweExConstant = aweExConstant
    //    this.code = aweExConstant.code
    //}

    companion object {
        private const val serialVersionUID = 1L

        @JvmStatic
        fun builder() : Builder {
            return Builder()
        }
    }

    override fun toString(): String {
        //return JSONObject.toJSONString(this)
        return super.toString()
    }
}
