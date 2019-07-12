package cn.npy.beemaths.web.rest.util;

import cn.npy.beemaths.domain.ResultInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.codec.binary.Base64.encodeBase64;

/**
 * @author wangshichang
 * @date 2019-01-21 14:58
 * @descriptio
 * n 百度文字识别（OCR）工具类
 */
@Slf4j
public class OcrUtil {
    // Access_Token获取
    private static final String ACCESS_TOKEN_HOST = "https://aip.baidubce.com/oauth/2.0/token?";
    // 身份证识别请求URL
    private static final String OCR_HOST = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard?";
    // apiKey,secretKey
    private static final String APP_ID = "15459714";
    private static final String API_KEY = "Xnsdxe4Ei1frAfa6Z2cPIFvT";
    private static final String SECRET_KEY = "LHWncZUHc1SoGG2iSp1dcQv7GQI6V3xj";

    // 获取百度云OCR的授权access_token
    public static String getAccessToken() {
        return getAccessToken(API_KEY, SECRET_KEY);
    }

    /**
     * 获取百度云OCR的授权access_token
     *
     * @param apiKey
     * @param secretKey
     * @return
     */
    public static String getAccessToken(String apiKey, String secretKey) {
        String accessTokenURL = ACCESS_TOKEN_HOST
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + apiKey
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + secretKey;

        try {
            URL url = new URL(accessTokenURL);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // 获取响应头
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "---->" + map.get(key));
            }

            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                result.append(inputLine);
            }
            JSONObject jsonObject = JSONObject.parseObject(result.toString());
            return jsonObject.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取access_token失败");
        }
        return null;
    }

    /**
     * 获取身份证识别后的数据
     *
     * @param image      图片文件
     * @param idCardSide 身份证方正面
     * @return
     */
    public static String getStringIdentityCard(byte[] image, int idCardSide, String accessToken) {
        // 身份证OCR的http URL+鉴权token
        log.info("百度云OCR的授权access_token:" + accessToken);
        String OCRUrl = OCR_HOST + "access_token=" + accessToken;
        log.info("身份证OCR的http URL:" + OCRUrl);
        // 对图片进行base64处理
        String imageEncode = encodeImageToBase64(image);
        // 请求参数
        String CardSide;
        if (idCardSide == 1) {
            CardSide = "front";
        } else {
            CardSide = "back";
        }
        String requestParam = "detect_direction=true&id_card_side=" + CardSide + "&image=" + imageEncode;
        try {
            // 请求OCR地址
            URL url = new URL(OCRUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求方法为POST
            connection.setRequestMethod("POST");

            // 设置请求头
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("apiKey", API_KEY);
            connection.setDoOutput(true);
            connection.getOutputStream().write(requestParam.getBytes(StandardCharsets.UTF_8));
            connection.connect();

            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

            StringBuilder result = new StringBuilder();
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                result.append(inputLine);
            }
            bufferedReader.close();
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("身份证OCR识别异常");
            return null;
        }
    }

    /**
     * 根据图片url获取字节流(弃用，使用feignClient调用)
     *
     * @param imageUrl 图片url
     * @return
     */
    public static byte[] getByteArrayByImageUrl(String imageUrl) {
        InputStream in = null;
        byte[] data = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求方法为GET
            connection.setRequestMethod("GET");
            connection.setReadTimeout(50000);
            connection.setDoOutput(true);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                in = connection.getInputStream();
                data = IOUtils.toByteArray(in);
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("根据imageUrl:" + imageUrl + "从阿里云OSS获取图片失败");
        }
        return null;
    }

    /**
     * 对图片文件进行Base64编码处理
     *
     * @param image
     * @return
     */
    public static String encodeImageToBase64(byte[] image) {
        // 对其进行Base64编码处理
        try {
            if (image != null) {
                // 对字节数组Base64编码
                BASE64Encoder encoder = new BASE64Encoder();
                return URLEncoder.encode(encoder.encode(image), "UTF-8");
            } else {
                log.info("未获取到图片字节流");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 提取OCR识别身份证有效信息
     *
     * @param image
     * @param idCardSide
     * @return
     */
    public static ResultInfo<Map<String, String>> getIdCardInfo(byte[] image, int idCardSide, String accessToken) {
        ResultInfo<Map<String, String>> resultInfo = new ResultInfo<>();
        String value = getStringIdentityCard(image, idCardSide, accessToken);
        String side;
        if (idCardSide == 1) {
            side = "正面";
        } else {
            side = "背面";
        }
        Map<String, String> map = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(value);
        JSONObject words_result = jsonObject.getJSONObject("words_result");
        if (words_result == null || words_result.isEmpty()) {
            resultInfo.setErrno(0);
            resultInfo.setErrmsg("请提供身份证" + side + "图片");
            return resultInfo;
        }
        for (String key : words_result.keySet()) {
            JSONObject result = words_result.getJSONObject(key);
            String info = result.getString("words");
            switch (key) {
                case "姓名":
                    map.put("name", info);
                    break;
                case "性别":
                    map.put("gender", info);
                    break;
                case "民族":
                    map.put("ethnicity", info);
                    break;
                case "出生":
                    map.put("birthDate", info);
                    break;
                case "住址":
                    map.put("residentialAddress", info);
                    break;
                case "公民身份号码":
                    map.put("identificationNumber", info);
                    break;
                case "签发机关":
                    map.put("authority", info);
                    break;
                case "签发日期":
                    map.put("validityDurationStartTime", info);
                    break;
                case "失效日期":
                    map.put("validityDurationEndTime", info);
                    break;
            }
        }
        resultInfo.setData(map);
        return resultInfo;
    }

    public static int getAgefromBirthDate(String birthDate) {
        // 先截取到字符串中的年、月、日
        String strs[] = birthDate.trim().split("-");
        int selectYear = Integer.parseInt(strs[0]);
        int selectMonth = Integer.parseInt(strs[1]);
        int selectDay = Integer.parseInt(strs[2]);
        // 得到当前时间的年、月、日
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DATE);

        // 用当前年月日减去生日年月日
        int yearMinus = yearNow - selectYear - 1;
        int monthMinus = monthNow - selectMonth;
        int dayMinus = dayNow - selectDay;

        int age = yearMinus;
        if (yearMinus < 0) {// 选了未来的年份
            age = 0;
        } else if (yearMinus == 0) {// 同年的，要么为1，要么为0
            if (monthMinus < 0) {// 选了未来的月份
                age = 0;
            } else if (monthMinus == 0) {// 同月份的
                if (dayMinus < 0) {// 选了未来的日期
                    age = 0;
                } else if (dayMinus >= 0) {
                    age = 1;
                }
            } else if (monthMinus > 0) {
                age = 1;
            }
        } else if (yearMinus > 0) {
            if (monthMinus < 0) {// 当前月>生日月
            } else if (monthMinus == 0) {// 同月份的，再根据日期计算年龄
                if (dayMinus < 0) {
                } else if (dayMinus >= 0) {
                    age = age + 1;
                }
            } else if (monthMinus > 0) {
                age = age + 1;
            }
        }
        return age;
    }


    //=====================阿里云身份证识别=====================
    /*
     * 获取参数的json对象
     */
    public static JSONObject getParam(int type, String dataValue) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("dataType", type);
            obj.put("dataValue", dataValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static String getIdentityCardInfo(String side, byte[] image) {
        String host = "http://dm-51.data.aliyun.com";
        String path = "/rest/160601/ocr/ocr_idcard.json";
        String appcode = "3a1bdd79435d4cc9ab8abb86d232fc7a";
        Boolean is_old_format = false;//如果文档的输入中含有inputs字段，设置为True， 否则设置为False
        //请根据线上文档修改configure字段
        JSONObject configObj = new JSONObject();
        configObj.put("side", side);
        String config_str = configObj.toString();
        //            configObj.put("min_size", 5);
        //            String config_str = "";

        String method = "POST";
        Map<String, String> headers = new HashMap();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> querys = new HashMap();

        // 对图像进行base64编码
        String imgBase64 = new String(encodeBase64(image));

        // 拼装请求body的json字符串
        JSONObject requestObj = new JSONObject();
        try {
            if (is_old_format) {
                JSONObject obj = new JSONObject();
                obj.put("image", getParam(50, imgBase64));
                if (config_str.length() > 0) {
                    obj.put("configure", getParam(50, config_str));
                }
                JSONArray inputArray = new JSONArray();
                inputArray.add(obj);
                requestObj.put("inputs", inputArray);
            } else {
                requestObj.put("image", imgBase64);
                if (config_str.length() > 0) {
                    requestObj.put("configure", config_str);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String bodys = requestObj.toString();

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            int stat = response.getStatusLine().getStatusCode();
            if (stat != 200) {
                System.out.println("Http code: " + stat);
                System.out.println("http header error msg: " + response.getFirstHeader("X-Ca-Error-Message"));
                System.out.println("Http body error msg:" + EntityUtils.toString(response.getEntity()));
                return null;
            }

            String res = EntityUtils.toString(response.getEntity());
            JSONObject res_obj = JSON.parseObject(res);
            if (is_old_format) {
                JSONArray outputArray = res_obj.getJSONArray("outputs");
                String output = outputArray.getJSONObject(0).getJSONObject("outputValue").getString("dataValue");
                JSONObject out = JSON.parseObject(output);
                return out.toJSONString();
            } else {
                return res_obj.toJSONString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> StringToMap(String identityInfo) {
        Map<String, Object> map = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(identityInfo);
        JSONObject words_result = jsonObject.getJSONObject("words_result");
        for (String key : words_result.keySet()) {
            JSONObject result = words_result.getJSONObject(key);
            String info = result.getString("words");
            switch (key) {
                case "name":
                    map.put("name", info);
                    break;
                case "sex":
                    map.put("gender", info);
                    break;
                case "nationality":
                    map.put("ethnicity", info);
                    break;
                case "birth":
                    map.put("birthDate", info);
                    break;
                case "address":
                    map.put("residentialAddress", info);
                    break;
                case "num":
                    map.put("identificationNumber", info);
                    break;
                case "issue":
                    map.put("authority", info);
                    break;
                case "start_date":
                    map.put("validityDurationStartTime", info);
                    break;
                case "end_date":
                    map.put("validityDurationEndTime", info);
                    break;
                case "success":
                    map.put("result", info);
                    break;
            }
        }
        return map;
    }
}
