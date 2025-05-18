package com.yoo.hiddenpixels.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.JSONObject;

@Service
@AllArgsConstructor
public class SteamPriceService {

    private final RestTemplate restTemplate;


    public SteamPriceInfo getSteamGamePrice(Long appId) {
        String url = "https://store.steampowered.com/api/appdetails";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("appids", appId)
                .queryParam("cc", "KR")  // 한화로 가져오기 위해 'KR' (한국) 사용
                .queryParam("l", "korean");  // 한글로 표시

        ResponseEntity<String> response = restTemplate.getForEntity(uriBuilder.toUriString(), String.class);
        JSONObject responseJson = new JSONObject(response.getBody());

        // 응답에서 가격 정보와 URL 추출
        if (responseJson.getJSONObject(appId.toString()).getBoolean("success")) {
            JSONObject data = responseJson.getJSONObject(appId.toString()).getJSONObject("data");

            // 가격 정보를 '₩' 형식으로 가져옴
            String finalFormattedPrice = data.getJSONObject("price_overview").getString("final_formatted");

            // Steam 상점 URL (기본 URL에 appId를 추가)
            String storeUrl = "https://store.steampowered.com/app/" + appId;

            // 반환 값
            return new SteamPriceInfo(finalFormattedPrice, storeUrl);
        } else {
            return new SteamPriceInfo("가격 정보 없음", "");
        }
    }

    // 가격과 URL을 담을 객체 (vo)
    public static class SteamPriceInfo {
        private final String price;
        private final String storeUrl;

        public SteamPriceInfo(String price, String storeUrl) {
            this.price = price;
            this.storeUrl = storeUrl;
        }

        public String getPrice() {
            return price;
        }

        public String getStoreUrl() {
            return storeUrl;
        }
    }
}
