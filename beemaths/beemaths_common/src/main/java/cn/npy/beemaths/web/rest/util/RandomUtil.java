package cn.npy.beemaths.web.rest.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * RandomUtil
 */
@Slf4j
public class RandomUtil {

    /**
     * 获取一个二十四位带字母的随机数
     *
     * @return java.lang.String
     * @author cuishilei
     * @date 2018/8/24
     */
    public static synchronized String get24DateTimeRandomUnique() {
        String seq17Num = DateTimeUtils.getNowDateStr(DateTimeUtils.DATETIME_FORMAT_YYYYMMDDHHMMSSSSS);
        String random2str = getRandomString(2);
        String random5Num = RandomStringUtils.randomNumeric(5);
        return seq17Num + random2str + random5Num;
    }

    /**
     * 随机生成字符串(自定义长度)
     *
     * @param length 自定义长度
     * @return java.lang.String
     * @author cuishilei
     * @date 2018/8/24
     */
    public static String getRandomString(int length) {
        String radStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder generateRandStr = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            int randNum = rand.nextInt(26);
            generateRandStr.append(radStr, randNum, randNum + 1);
        }
        return generateRandStr.toString();
    }

    public static List<String> getUniquenessTwentyLengthStr(Integer wantGetUniquenessTwentyLengthStrCount) {
        // 全局count,sleep，保证编码唯一
        int getUniquenessKeyCount = 0;
        List<String> uniquenessTwentyLengthStrList = new ArrayList<>();
        for (int i = 0; i < wantGetUniquenessTwentyLengthStrCount; i++) {
            if (getUniquenessKeyCount % 1000 == 0) {
                if (getUniquenessKeyCount != 0) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        log.error("sleep error", e);
                    }
                }
                getUniquenessKeyCount = 10000 + (int) (Math.random() * 10000);
            }
            // 为每一个券生成一个券编码
            int startNum = new Random().nextInt(9) + 1;
            String autoPriStr = String.valueOf(getUniquenessKeyCount).substring(String.valueOf(getUniquenessKeyCount).length() - 4, String.valueOf(getUniquenessKeyCount).length());
            String uniquenessTwentyLengthStr = String.valueOf(startNum) + (System.currentTimeMillis() + "").substring(1) + autoPriStr;
            uniquenessTwentyLengthStrList.add(uniquenessTwentyLengthStr);
            getUniquenessKeyCount += 1;
        }
        return uniquenessTwentyLengthStrList;
    }

    /**
     * 从List集合中随机获取n个记录，并返回
     * @param list 源集合
     * @param n 数量
     * @return
     */
    public static List generateRandomDataNoRepeat(List list, int n) {
        HashMap<Object, Object> map = new HashMap<>();
        List listNew = new ArrayList();
        if (list.size() <= n) {
            return list;
        } else {
            while (map.size() < n) {
                int random = (int) (Math.random() * list.size());
                if (!map.containsKey(random)) {
                    map.put(random, "");
                    listNew.add(list.get(random));
                }
            }
            return listNew;
        }
    }

}
