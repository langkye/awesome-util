package cn.langkye.awesome.web.model

import cn.langkye.awesome.commom.annotation.NoArgKt
import cn.langkye.awesome.commom.constant.CODE
import com.alibaba.fastjson.JSONObject
import java.io.Serializable

@NoArgKt
data class HttpResponseBody (
    var code : Int = CODE.SUCCESS.OK
    /**业务代码：*/
    ,var bizCode : String = "00000000"
    ,var bizCodeName : String = "OK"
    ,var message: String = "OK"
    ,var body : Any = JSONObject()
) : Serializable {
    companion object {
        //@JvmField
        //const val serialVersionUID : Long = 1L

        //@JvmStatic
        //fun geSerialVersionUID() : Long {
        //    return serialVersionUID
        //}
    }

    fun success() : HttpResponseBody {
        return this;
    }

    fun success(code: Int) : HttpResponseBody {
        this.code = code
        return this
    }

    fun success(body: Any) : HttpResponseBody {
        this.body = body
        return this
    }

    fun success(message: String) : HttpResponseBody {
        this.message = message
        return this
    }

    fun success(body: Any, message: String) : HttpResponseBody {
        this.body = body
        this.message = message
        return this
    }

    fun success(code: Int, message: String) : HttpResponseBody {
        this.code = code
        this.message = message
        return this
    }

    fun success(code: Int, message: String, body: Any) : HttpResponseBody {
        this.code = code
        this.body = body
        this.message = message
        return this
    }

    fun error() : HttpResponseBody {
        this.code = CODE.SERVER_ERROR.INTERNAL_SERVER_ERROR
        this.message = CODE.SERVER_ERROR.MESSAGE
        return this;
    }

    fun error(code: Int) : HttpResponseBody {
        this.code = code
        this.message = CODE.SERVER_ERROR.MESSAGE
        return this
    }

    fun error(body: Any) : HttpResponseBody {
        this.code = CODE.SERVER_ERROR.INTERNAL_SERVER_ERROR
        this.message = CODE.SERVER_ERROR.MESSAGE
        this.body = body
        return this
    }

    fun error(message: String) : HttpResponseBody {
        this.code = CODE.SERVER_ERROR.INTERNAL_SERVER_ERROR
        this.message = message
        return this
    }

    fun error(body: Any, message: String) : HttpResponseBody {
        this.code = CODE.SERVER_ERROR.INTERNAL_SERVER_ERROR
        this.body = body
        this.message = message
        return this
    }

    fun error(code: Int, message: String) : HttpResponseBody {
        this.code = code
        this.message = message
        return this
    }

    fun error(code: Int, message: String, body: Any) : HttpResponseBody {
        this.code = code
        this.body = body
        this.message = message
        return this
    }

}
