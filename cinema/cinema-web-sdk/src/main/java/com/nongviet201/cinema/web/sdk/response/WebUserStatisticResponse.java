package com.nongviet201.cinema.web.sdk.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WebUserStatisticResponse {
    private int points;
    private String totalSpending;
    private String userRank;
    private double rankPercent;
}
