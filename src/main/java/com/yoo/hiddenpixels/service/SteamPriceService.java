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
                .queryParam("cc", "KR")    // 한화로 가져오기 위해 'KR' (한국) 사용
                .queryParam("l", "korean"); // 한글로 표시

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(uriBuilder.toUriString(), String.class);
            JSONObject responseJson = new JSONObject(response.getBody());

            if (responseJson.getJSONObject(appId.toString()).getBoolean("success")) {
                JSONObject data = responseJson.getJSONObject(appId.toString()).getJSONObject("data");

                // price_overview가 존재하는지 체크
                if (data.has("price_overview")) {
                    String finalFormattedPrice = data.getJSONObject("price_overview").getString("final_formatted");
                    String storeUrl = "https://store.steampowered.com/app/" + appId;
                    return new SteamPriceInfo(finalFormattedPrice, storeUrl);
                } else {
                    // 무료 게임 등 가격 정보가 없는 경우
                    String storeUrl = "https://store.steampowered.com/app/" + appId;
                    return new SteamPriceInfo("가격 정보 없음", storeUrl);
                }
            } else {
                return new SteamPriceInfo("가격 정보 없음", "");
            }
        } catch (Exception e) {
            // 네트워크 오류, JSON 파싱 오류 등 예외 발생 시
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
