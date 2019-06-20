package com.wys.baselib.net.common;

import java.util.Map;

/**
 * Created by yas on 2019/6/20
 * Describe:
 */
public interface ICommonParams {
    Map<String,Object> getParams();
    Map<String,String> getHeaders();
}
