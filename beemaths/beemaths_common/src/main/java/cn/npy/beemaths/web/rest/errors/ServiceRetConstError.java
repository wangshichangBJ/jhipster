package cn.npy.beemaths.web.rest.errors;

public enum ServiceRetConstError {

    SUCCESS(0, "成功"),

    INNER_ERROR(13, "内部错误"),

    APPID_OR_TOKEN_NOT_FOUND(21, "token缺失"),
    APPID_OR_TOKEN_CHECK_ERROR(22, "token验证失败"),

    SPECIFIED_OBJECT_NOT_FOUND(31, "指定的对象不存在"),
    PARAMS_IS_NULL(41, "参数为空"),

    // usercenter start 100XXX
    // 登录失败
    USERCENTER_LOGIN_ERROR(100001, "login error"),
    // 注册失败
    USERCENTER_REGISTER_ERROR(100002, "register error"),
    // 用户已存在
    USERCENTER_USER_EXIST_ERROR(100003, "user exist error"),
    // 用户不存在
    USERCENTER_USER_UNEXIST_ERROR(100004, "user not exist"),
    // 更新用户信息失败
    USERCENTER_USER_UPDATE_ERROR(100006, "user info update error"),
    // 用户验证失败
    USERCENTER_USER_CHECK_ERROR(100100, "user token check error"),
    // 更新用户token状态失败
    USERCENTER_USER_UPDATE_TOKEN_ERROR(100007, "user token state update error"),
    // 查询用户id参数为空
    USERCENTER_USER_QUEYR_IDNULL_ERROR(100008, "user ids null error"),
    // 密码错误
    USERCENTER_USER_PASSWORD_ERROR(100009, "密码错误"),
    // 手机号已存在
    USERCENTER_USER_DUPLICATE_USER_PHONE(100010, "手机号已存在"),
    // 用户名已存在
    USERCENTER_USER_DUPLICATE_USERNAME(100011, "用户名已存在"),
    // 旧密码错误
    USERCENTER_USER_OLD_PASSWORD_ERROR(100012, "旧密码错误"),
    // 用户名不合法
    USERCENTER_USERNAME_ERROR(100013, "用户名不合法"),
    // 密码格式
    USERCENTER_USER_PASSWORD_FORMAT_ERROR(100020, "密码格式不合法，当前系统的密码规则为：长度{0}-{1}位{2}"),
    // usercenter end

    REPEAT_INVOKE_ERROR(600, "重复的请求{0}秒内，只能请求一次"),
    ;

    private int errno;

    private String errmsg;

    ServiceRetConstError(int errno) {
        this.errno = errno;
        this.errmsg = "";
    }

    ServiceRetConstError(int errno, String message) {
        this.errno = errno;
        this.errmsg = message;
    }

    public int errno() {
        return this.errno;
    }

    public String errmsg() {
        return this.errmsg;
    }
}
