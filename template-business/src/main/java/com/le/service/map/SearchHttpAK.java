/**
  * 默认：
  */

package com.le.service.map;
/**
 * 选择了ak或使用IP白名单校验：
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class SearchHttpAK {

    public static String URL = "https://api.map.baidu.com/place/v2/search?";

    public static String AK = "b1gsbQKvGjZRQPnEWCeklUuARgqA8CuC";

    public static void main(String[] args) throws Exception {

        SearchHttpAK snCal = new SearchHttpAK();

        Map params = new LinkedHashMap<String, String>();
        params.put("query", "蜜糖国教育集团儿童乐园");
//        params.put("tag", "游乐园");
        params.put("region", "北京");
        params.put("output", "json");
        params.put("page_size","20");
        params.put("scope","2");

        params.put("ak", AK);

        for(int i =0 ; i < 5 ; i++){
            System.out.println("第"+i+"页");
            params.put("page_num",String.valueOf(i));

            snCal.requestGetAK(URL, params);

        }

    }

    /**
     * 默认ak
     * 选择了ak，使用IP白名单校验：
     * 根据您选择的AK以为您生成调用代码
     * 检测到您当前的ak设置了IP白名单校验
     * 您的IP白名单中的IP非公网IP，请设置为公网IP，否则将请求失败
     * 请在IP地址为0.0.0.0/0 外网IP的计算发起请求，否则将请求失败
     */
    public void requestGetAK(String strUrl, Map<String, String> param) throws Exception {
        if (strUrl == null || strUrl.length() <= 0 || param == null || param.size() <= 0) {
            return;
        }

        StringBuffer queryString = new StringBuffer();
        queryString.append(strUrl);
        for (Map.Entry<?, ?> pair : param.entrySet()) {
            queryString.append(pair.getKey() + "=");
            queryString.append(URLEncoder.encode((String) pair.getValue(),
                    "UTF-8") + "&");
        }

        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }

        java.net.URL url = new URL(queryString.toString());
        System.out.println(queryString.toString());
        URLConnection httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.connect();

        InputStreamReader isr = new InputStreamReader(httpConnection.getInputStream());
        BufferedReader reader = new BufferedReader(isr);
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reader.close();
        isr.close();
        System.out.println(buffer.toString());
    }
}