package com.gpdi.origin.service.source;

import java.util.Map;

/**
 * 溯源服务
 */
public interface IndexService {

    Map<String, Object> traceOrigin(String originCode);

}
