package com.scut.p2ploanplatform.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scut.p2ploanplatform.service.impl.RepayExecutionServiceImpl;
import com.scut.p2ploanplatform.vo.ResultVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class ThirdPartyOperationInterface {
    private static String THIRD_PARTY_API_URL = "http://localhost:8080/third_party/";
    private static String API_KEY = "";

    public static String getThirdPartyApiUrl() {
        return THIRD_PARTY_API_URL;
    }

    public static void setThirdPartyApiUrl(String thirdPartyApiUrl) {
        if (!thirdPartyApiUrl.endsWith("/"))
            thirdPartyApiUrl = thirdPartyApiUrl + "/";
        THIRD_PARTY_API_URL = thirdPartyApiUrl;
    }

    public static String getApiKey() {
        return API_KEY;
    }

    public static void setApiKey(String apiKey) {
        API_KEY = apiKey;
    }

    private static String computeThirdPartyParamSign(String paramString) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] sha256 = digest.digest(paramString.getBytes());
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : sha256) {
            String hex = Integer.toHexString(b & 0xff);
            if (hex.length() == 1)
                stringBuilder.append('0');
            stringBuilder.append(hex);
        }
        return stringBuilder.toString();
    }

    public static ResultVo transfer(String payerId, String payeeId, BigDecimal amount) throws Exception {
        String url = THIRD_PARTY_API_URL + "transfer";
        List<NameValuePair> queryParams = new LinkedList<>();
        queryParams.add(new BasicNameValuePair("payer_id", payerId));
        queryParams.add(new BasicNameValuePair("payee_id", payeeId));
        queryParams.add(new BasicNameValuePair("amount", amount.toString()));
        queryParams.add(new BasicNameValuePair("api_key", API_KEY));
        queryParams.add(new BasicNameValuePair("signature", computeThirdPartyParamSign(payerId + payeeId + amount.toString() + API_KEY)));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI uri = new URIBuilder(url).addParameters(queryParams).build();

        HttpPut httpPut = new HttpPut(uri);
        try (CloseableHttpResponse httpResponse = httpClient.execute(httpPut)) {
            HttpEntity entity = httpResponse.getEntity();
            String content = EntityUtils.toString(entity);
            ObjectMapper objectMapper = new ObjectMapper();
            ResultVo result = objectMapper.readValue(content, ResultVo.class);
            log.info(content);
            return result;
        }
    }

    public static ResultVo purchase(String payerId, String payeeId, BigDecimal amount, String paymentPassword)throws Exception{
        String url = THIRD_PARTY_API_URL + "purchase";
        List<NameValuePair> queryParams = new LinkedList<>();
        queryParams.add(new BasicNameValuePair("payer_id", payerId));
        queryParams.add(new BasicNameValuePair("payee_id", payeeId));
        queryParams.add(new BasicNameValuePair("amount", amount.toString()));
        queryParams.add(new BasicNameValuePair("payment_password", paymentPassword));
        queryParams.add(new BasicNameValuePair("api_key", API_KEY));
        queryParams.add(new BasicNameValuePair("signature", computeThirdPartyParamSign(payerId + payeeId + amount.toString() + API_KEY)));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI uri = new URIBuilder(url).addParameters(queryParams).build();

        HttpPut httpPut = new HttpPut(uri);
        try (CloseableHttpResponse httpResponse = httpClient.execute(httpPut)) {
            HttpEntity entity = httpResponse.getEntity();
            String content = EntityUtils.toString(entity);
            ObjectMapper objectMapper = new ObjectMapper();
            ResultVo result = objectMapper.readValue(content, ResultVo.class);
            log.info(content);
            return result;
        }
    }
}
