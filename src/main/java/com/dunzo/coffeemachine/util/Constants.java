package com.dunzo.coffeemachine.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final int ORDER_QUEUE_CAPACITY = 500;
    public static final long BEVERAGE_PREP_TIME_MS = 1000;
    public static final long INGREDIENTS_INDICATOR_INTERVAL_MS = 5000;
    public static final int INGREDIENTS_LOW_THRESHOLD = 10;
}
