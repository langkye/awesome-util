package cn.langkye.awesome.net.util

import cn.langkye.awesome.commom.constant.AweExConstant
import cn.langkye.awesome.commom.exception.AweException
import com.alibaba.fastjson.JSON
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.URLEncoder
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


class OkHttpUtil private constructor() {
    private val logger : Logger = LoggerFactory.getLogger(OkHttpUtil::class.java)

    private var heads: Headers.Builder = Headers.Builder()
    private var params: MutableMap<String, String> = LinkedHashMap(16)
    private var url: String = ""
    private var request: Request.Builder = Request.Builder().get()

    /**
     * set url
     *
     * @param url your url
     * @return OkHttpUtil
     */
    fun url(url: String): OkHttpUtil {
        if (StringUtils.isBlank(url) || !url.startsWith("https") || !url.startsWith("https")) {
            val aweException = AweException.Builder()
                .aweExConstant(AweExConstant.NET_EX)
                .message("${AweExConstant.NET_EX.message} url must start with \"http\" or \"https\"")
                .build()
            //aweException.aweExConstant = AweExConstant.NET_EX
            //aweException.message = "${aweException.message} url must start with \"http\" or \"https\""
            throw aweException
        }
        this.url = url
        return this
    }

    /**
     * 添加参数
     *
     * @param key   参数名
     * @param value 参数值
     * @return OkHttpUtil
     */
    fun addParam(key: String, value: String): OkHttpUtil {
        params[key] = value
        return this
    }

    /**
     * 添加请求头
     *
     * @param key   参数名
     * @param value 参数值
     * @return OkHttpUtil
     */
    fun addHeader(key: String, value: String): OkHttpUtil {
        heads.add(key, value)
        return this
    }

    /**
     * init
     *
     * @return OkHttpUtil
     */
    fun get(): OkHttpUtil {
        request = Request.Builder().get()
        val urlBuilder = StringBuilder(url)
        if (params.isNotEmpty()) {
            urlBuilder.append("?")
            try {
                for ((key, value) in params) {
                    urlBuilder.append(
                        URLEncoder.encode(key, "utf-8")).append("=")
                        .append(URLEncoder.encode(value, "utf-8")).append("&")
                }
            } catch (ex: Exception) {
                val aweException = AweException.Builder()
                    .aweExConstant(AweExConstant.NET_EX)
                    .message("${AweExConstant.NET_EX.message} failed add arg to url's parameter. ${ex.message}")
                    .build()
                //aweException.aweExConstant = AweExConstant.NET_EX
                //aweException.message = "${aweException.message} failed add arg to url's parameter. ${ex.message}"
                throw aweException
            }
            urlBuilder.deleteCharAt(urlBuilder.length - 1)
        }
        request.url(urlBuilder.toString())
        return this
    }

    /**
     * 初始化post方法
     *
     * @param isJsonPost
     *      <br/>true: json的方式提交数据，类似postman里post方法的raw。
     *      <br/>false: 等于普通的表单提交
     *      <hr/>
     * @return OkHttpUtil
     */
    fun post(isJsonPost: Boolean): OkHttpUtil {
        val requestBody: RequestBody = if (isJsonPost) {
            val json: String = JSON.toJSONString(params)
            json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        } else {
            val formBody = FormBody.Builder()
            params.forEach(formBody::add)
            formBody.build()
        }

        request = Request.Builder().headers(heads.build()).post(requestBody).url(url)
        return this
    }

    /**
     * 初始化post方法
     *
     * @return OkHttpUtil
     */
    @SuppressWarnings(value = ["Unused"])
    fun post(): OkHttpUtil {
        val requestBody: RequestBody
        val formBody = FormBody.Builder()
        params.forEach(formBody::add)
        requestBody = formBody.build()
        request = Request.Builder().headers(heads.build()).post(requestBody).url(url)
        return this
    }

    /**
     * 同步请求
     *
     * @return
     */
    fun sync(): String {
        setHeader(request)
        return try {
            val response = okHttpClient?.newCall(request.build())?.execute()
            response?.body!!.string()
        } catch (ex: Exception) {
            logger.error("请求失败:${ex.message}")
            val aweException = AweException.Builder()
                .aweExConstant(AweExConstant.NET_EX)
                .message("${AweExConstant.NET_EX.message} sync execute failed. ${ex.message}")
                .build()
            //aweException.aweExConstant = AweExConstant.NET_EX
            //aweException.message = "${aweException.message} sync execute failed. ${ex.message}"
            throw aweException
        }
    }

    /**
     * 异步请求，有返回值
     */
    fun async(): String {
        val buffer = StringBuilder("")
        setHeader(request)
        okHttpClient?.newCall(request.build())?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                logger.warn("async error: ${e.message}")
                buffer.append("请求出错：").append(e.message)
            }

            @Throws(AweException::class)
            override fun onResponse(call: Call, response: Response) {
                try {
                    buffer.append(response.body!!.string())
                    semaphoreInstance!!.release()
                } catch (ex : Exception){
                    logger.error("请求失败:${ex.message}")
                    val aweException = AweException.Builder()
                        .aweExConstant(AweExConstant.NET_EX)
                        .message("${AweExConstant.NET_EX.message} async execute failed. ${ex.message}")
                        .build()
                    //aweException.aweExConstant = AweExConstant.NET_EX
                    //aweException.message = "${aweException.message} async execute failed. ${ex.message}"
                    throw aweException
                }
            }
        })
        try {
            semaphoreInstance!!.acquire()
        } catch (ex: InterruptedException) {
            logger.warn("async error: ${ex.message}")
        }
        return buffer.toString()
    }

    /**
     * 异步请求，带有接口回调
     *
     * @param callBack
     */
    fun async(callBack: ICallBack) {
        setHeader(request)
        okHttpClient?.newCall(request.build())?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callBack.onFailure(call, e.message)
            }

            @Throws(AweException::class)
            override fun onResponse(call: Call, response: Response) {
                callBack.onSuccessful(call, response.body!!.string())
            }
        })
    }

    /**
     * 为request添加请求头
     *
     * @param request
     */
    private fun setHeader(request: Request.Builder) {
        try {
            request.headers(heads.build())
        } catch (ex: Exception) {
            logger.warn("Error for add headers:${ex.message}")
        }
    }

    /**
     * 自定义一个接口回调
     */
    interface ICallBack {
        fun onSuccessful(call: Call?, data: String?)
        fun onFailure(call: Call?, errorMsg: String?)
    }

    companion object {
        private val logger : Logger = LoggerFactory.getLogger("$this.companion")

        @Volatile
        private var okHttpClient: OkHttpClient? = null

        @Volatile
        private var semaphore: Semaphore? = null//只能1个线程同时访问

        /**
         * 用于异步请求时，控制访问线程数，返回结果
         *
         * @return
         */
        private val semaphoreInstance: Semaphore?
            get() {
                //只能1个线程同时访问
                synchronized(OkHttpUtil::class.java) {
                    if (semaphore == null) {
                        semaphore = Semaphore(0)
                    }
                }
                return semaphore
            }

        /**
         * 创建OkHttpUtil
         *
         * @return
         */
        fun builder(): OkHttpUtil {
            return OkHttpUtil()
        }

        /**
         * 生成安全套接字工厂，用于https请求的证书跳过
         *
         * @return
         */
        private fun createSSLSocketFactory(trustAllCerts: Array<TrustManager>): SSLSocketFactory? {
            var ssfFactory: SSLSocketFactory? = null
            try {
                val sc = SSLContext.getInstance("SSL")
                sc.init(null, trustAllCerts, SecureRandom())
                ssfFactory = sc.socketFactory
            } catch (ex: Exception) {
                ex.printStackTrace()
                logger.warn("createSSLSocketFactory failed. ${ex.message}")
            }
            return ssfFactory
        }

        private fun buildTrustManagers(): Array<TrustManager> {
            return arrayOf(
                object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )
        }
    }

    /**
     * 初始化okHttpClient，并且允许https访问
     */
    init {
        if (okHttpClient == null) {
            synchronized(OkHttpUtil::class.java) {
                if (okHttpClient == null) {
                    val trustManagers : Array<TrustManager> = buildTrustManagers()
                    okHttpClient = createSSLSocketFactory(trustManagers)?.let {
                        OkHttpClient.Builder()
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .sslSocketFactory(
                                it,
                                trustManagers[0] as X509TrustManager
                            )
                            .hostnameVerifier { _: String?, _: SSLSession? -> true }
                            .retryOnConnectionFailure(true)
                            .build()
                    }!!
                    addHeader(
                        "User-Agent",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36"
                    )
                }
            }
        }
    }
}
