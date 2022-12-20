import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pojo.LegendDealerShip;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lujunqi
 * @version 1.0
 * @date 2022/12/20 09:38
 */
public class Gt7InfoCollector {

    private static final String GT7_DATA_URL = "https://ddm999.github.io/gt7info/data.json";
    private final OkHttpClient client;
    private final Request request;
    private final ObjectMapper objectMapper;
    private TreeNode todayGt7Info;
    private LegendDealerShip todayLegendDealerShip;

    public Gt7InfoCollector() {
        client = new OkHttpClient();
        request = new Request.Builder()
                .get()
                .url(GT7_DATA_URL)
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private void updateTodayGt7Info() throws IOException {
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            // 403 message code
            // TODO 打日志, 发错误信息邮件
            todayGt7Info = null;
            return;
        }
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            // TODO 打日志, 发错误信息邮件
            todayGt7Info = null;
            return;
        }
        todayGt7Info = objectMapper.readTree(responseBody.string());
    }

    private void updateTodayLegendDealerShip() {
        if (todayGt7Info == null) {
            return;
        }
        todayLegendDealerShip = objectMapper.convertValue(todayGt7Info.get("legend"), LegendDealerShip.class);
    }

    private boolean checkTodayGt7Info() {
        if (todayGt7Info == null) {
            return false;
        }
        String updateTimeStampStr = objectMapper.convertValue(todayGt7Info.get("updatetimestamp"), String.class);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return isToday(updateTimeStampStr, dateTimeFormatter);
    }

    private boolean checkTodayLegendDealerShip() {
        if (todayLegendDealerShip == null) {
            return false;
        }
        String updateTimeStampStr = todayLegendDealerShip.getDate();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd");
        return isToday(updateTimeStampStr, dateTimeFormatter);
    }

    private boolean isToday(String updateTimeStampStr, DateTimeFormatter dateTimeFormatter) {
        ZonedDateTime updateTimeInChina = LocalDateTime
                .parse(updateTimeStampStr, dateTimeFormatter)
                .atZone(ZoneId.of("Europe/London"))
                .withZoneSameInstant(ZoneId.of("Asia/Shanghai"));
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        return updateTimeInChina.getYear() == now.getYear()
                && updateTimeInChina.getMonth() == now.getMonth()
                && updateTimeInChina.getDayOfMonth() == now.getDayOfMonth();
    }

    public LegendDealerShip getTodayLegendDealerShip() throws IOException {
        if (checkTodayLegendDealerShip()) {
            return todayLegendDealerShip;
        }
        if (!checkTodayGt7Info()) {
            updateTodayGt7Info();
        }
        updateTodayLegendDealerShip();
        return todayLegendDealerShip;
    }
}
