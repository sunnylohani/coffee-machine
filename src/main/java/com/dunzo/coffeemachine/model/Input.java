package com.dunzo.coffeemachine.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class Input {

    private Machine machine;

    @Data
    public class Machine {

        private Outlets outlets;

        @JsonProperty("total_items_quantity")
        private Map<String, Integer> totalItemsQty;

        private Map<String, Map<String, Integer>> beverages;

        @Data
        public class Outlets {
            @JsonProperty("count_n")
            private Integer count;
        }
    }
}
