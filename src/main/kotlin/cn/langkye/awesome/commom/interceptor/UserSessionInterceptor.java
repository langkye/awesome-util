//package cn.langkye.awesome.commom.interceptor;
//
//import com.nswt.common.exception.BizException;
//import com.nswt.common.util.IdUtils;
//import com.usrp.cm.user.dao.CmUserTransferDAO;
//import com.usrp.cm.user.model.CmUserTransferDO;
//import com.usrp.common.model.UserSession;
//import com.usrp.common.util.JwtUtil;
//import com.usrp.tm.merchant.dao.TmMerchantDao;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.MDC;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.context.request.ServletWebRequest;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.context.request.WebRequestInterceptor;
//
//import javax.annotation.Resource;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * 用户会话拦截器
// *
// * @author zhangfengyang
// */
//@Slf4j
//@Component
//public class UserSessionInterceptor implements WebRequestInterceptor {
//
//    @Value("${sessionInvalid}")
//    private long sessionInvalid;
//    @Value("${cert.key}")
//    private String key;
//
//    @Override
//    public void afterCompletion(WebRequest request, Exception arg1) throws Exception {
//
//    }
//
//    @Override
//    public void postHandle(WebRequest request, ModelMap arg1) throws Exception {
//    }
//
//    @Override
//    public void preHandle(WebRequest webRequest) throws Exception {
//        //获取客户端IP
//        String clientIp = webRequest.getHeader("Host");
//        //生成日志请求ID
//        String requestId = IdUtils.create();
//        //添加自定义日志参数到日志对象
//        MDC.put("traceId", requestId);
//        MDC.put("clientIp", clientIp);
//
//        //获取客户端请求头信息
//        String token = webRequest.getHeader("Authentication");
//        String lastOperatTime = webRequest.getHeader("lastOperatTime");
//        //处理servlet请求
//        if (webRequest instanceof ServletWebRequest) {
//            //获取客户端请求URI
//            ServletWebRequest request = (ServletWebRequest) webRequest;
//            String uri = request.getRequest().getRequestURI();
//            log.info("requestUri:{}", uri);
//            MDC.put("requestUri", uri);
//            //需要进行token校验
//            if (!SESSION_LESS_URIS.contains(uri)) {
//                verifyToken(token,lastOperatTime);
//                tokenStatus();
//                checkStatus();
//                //添加用户ID到日志对象
//                try {
//                    final UserSession sessionInfo = jwtUtil.getSessionInfo(token);
//                    MDC.put("userId", sessionInfo.getUserCode());
//                } catch (Exception e) {
//                    //token expired or no provide token
//                }
//            }
//            //不需要进行token校验
//            else {
//                //当请求带token则记录，否则置空，防止用户ID不匹配
//                try {
//                    final UserSession sessionInfo = jwtUtil.getSessionInfo(token);
//                    MDC.put("userId", sessionInfo.getUserCode());
//                } catch (Exception e) {
//                    //token expired or no provide token
//                    MDC.put("userId", "");
//                }
//            }
//        }
//    }
//
//    public void verifyToken(String token,String lastOperatTime) throws BizException {
//        try {
//            UserSession userSession = jwtUtil.getSessionInfo(token);
//            if(!StringUtils.isBlank(lastOperatTime)){
//                long lastOperatTimeMills = Long.parseLong(lastOperatTime);
//                long currentTimeMills = System.currentTimeMillis();
//                long sessionInvalidMills = sessionInvalid * 1000 * 60;
//                if((currentTimeMills-lastOperatTimeMills) > sessionInvalidMills){
//                    throw new BizException("invalid_session", "登录会话失效，请重新登录。");
//                }
//            }
//            UserSession.setSessionInfo(userSession);
//            log.info("userSession:{}", userSession);
//        } catch (Exception e) {
//            log.error("token校验失败:", e);
//            throw new BizException("invalid_session", "登录会话失效，请重新登录。");
//        }
//    }
//
//    //商户token判断
//    public void tokenStatus() throws BizException {
//        UserSession sessionInfo = UserSession.getSessionInfo();
//        if(sessionInfo != null){
//            String tokenId = sessionInfo.getTokenId() != null?
//                    sessionInfo.getTokenId() : sessionInfo.getAppTokenId();
//            String userCode = sessionInfo.getUserCode();
//            CmUserTransferDO cmUserTransferDO = cmUserTransferDAO.selectByUserCode(userCode, key);
//            String tokenId2 = sessionInfo.getTokenId() != null?
//                    cmUserTransferDO.getTokenId() : cmUserTransferDO.getAppTokenId();
//            if(tokenId2 == null || cmUserTransferDO == null || !tokenId2.equals(tokenId)){
//                throw new BizException("invalid_session", "登录会话失效，请重新登录。");
//            }
//        }
//    }
//
//    public void checkStatus() throws BizException {
//        UserSession session=UserSession.getSessionInfo();
//        if(session!=null){
//            String orgCode = session.getOrgCode();
//            String status = tmMerchantDao.checkStatus(orgCode);
//            if(status!=null){
//                if(status.equals("02")){
//                    throw new BizException("9999","该账户已停用，请联系客服人员！");
//                }
//            }
//        }else{
//            throw new BizException("9999","商户状态异常，为空！");
//        }
//    }
//}
