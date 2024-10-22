package com.dataeye.partner.bean.response;

/**
 * @author jaret
 * @date 2024/10/21 17:45
 * @description
 */
public enum StatusCode {

    /**成功**/
    SUCCESS(200, "Success"),

    /**跳转至其他**/
    SEE_OTHER(303, "See Other"),

    /**非法请求**/
    BAD_REQUEST(400, "Bad Request"),

    /**没有认证 需要重新登录**/
    UNAUTHORIZED(401, "Unauthorized"),

    /**需要付费**/
    PAYMENT_REQUIRED(402, "Payment Required"),

    /**禁止的请求**/
    FORBIDDEN(403, "Forbidden"),

    /**无效路径**/
    NOT_FOUND(404, "Not Found"),

    /**不允许的方法**/
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    /**不可以接受**/
    NOT_ACCEPTABLE(406, "Not Acceptable"),

    /**IP禁止访问**/
    IP_REGISTER_FORBIDDEN(407, "Ip Register Forbidden"),

    /**请求超时**/
    REQUEST_TIMEOUT(408, "Request Timeout"),

    /**冲突**/
    CONFLICT(409, "Conflict"),

    /**超过IP数限制**/
    OVER_IP_LIMIT(410, "请注意，账号存在安全风险"),

    /**超过设备数限制**/
    OVER_SESSION_LIMIT(411, "请注意，账号存在安全风险"),

    /**验证码不匹配**/
    CODE_NOT_MATCH(412, "Code Not Match"),

    /**已经试用过 没有次数**/
    NO_CHANCE(413, "No Chance"),

    /**Token无效**/
    TOKEN_INVALIDATE(414, "No Chance"),

    /**白名单限制**/
    WHITE_LIST_LIMIT(415, "请注意，账号存在安全风险"),

    /**黑名单限制**/
    BLACK_LIST_LIMIT(416, "请注意，账号存在安全风险"),

    /**频繁请求**/
    TOO_MANY_REQUESTS(429, "Too Many Requests"),

    /**服务器异常**/
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    /**未完成**/
    NOT_IMPLEMENTED(501, "Not Implemented"),

    /**无效网关**/
    BAD_GATEWAY(502, "Bad Gateway"),

    /**服务不可用**/
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),

    /**网关超时**/
    GATEWAY_TIMEOUT(504, "Gateway Timeout"),

    /**空指针异常**/
    ERROR_NULL_POINTER(505, "Error Null Pointer"),

    /**数据库异常**/
    ERROR_DB(506, "Error DB"),

    /**HTTP链接异常**/
    ERROR_HTTP(507, "Error Http"),

    /**签名错误**/
    ERROR_SIGN(508, "Error sign"),

    /**签名空错误**/
    ERROR_NULL_SIGN(509, "Error Null sign"),
    /**账号被封禁**/
    ACCOUNT_FORBIDDEN(510, "账号登录异常，请联系商务/客服处理"),

    /**警告提示**/
    WARN_TIP(600, "Warn Tip");

    private final int code;

    private final String reasonPhrase;

    StatusCode(int code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    @Override
    public String toString() {
        return String.valueOf(code);
    }

    public int getStatusCode() {
        return code;
    }

    public String getStatusCodeReason() {
        return reasonPhrase;
    }

}
