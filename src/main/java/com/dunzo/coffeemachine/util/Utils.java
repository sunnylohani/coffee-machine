package com.dunzo.coffeemachine.util;

import com.dunzo.coffeemachine.model.Input;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    public static Input parseJsonFile(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = Utils.class.getResourceAsStream(path);
        return mapper.readValue(is, Input.class);
    }
}
