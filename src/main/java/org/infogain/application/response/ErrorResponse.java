package org.infogain.application.response;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
public class ErrorResponse {
    String errorMessage;
}
