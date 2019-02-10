package com.basecamp.wire;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetRaceStatusResponse implements Response {

    private final String status;

}
