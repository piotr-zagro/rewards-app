package org.infogain.application.rest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiPaths {

    public static final String BASE_PATH = "/api/v1";
    public static final String TRANSACTION_PATH = BASE_PATH + "/transaction";
    public static final String REWARD_PATH = BASE_PATH + "/reward";

}
