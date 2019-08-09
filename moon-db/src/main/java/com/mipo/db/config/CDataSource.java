package com.mipo.db.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CDataSource extends AbstractRoutingDataSource {

    public static Map<String, List<String>> METHODTYPE = new HashMap<>();

    //设置方法名前缀对应的数据源
    static {
        Map<String, String> map = new HashMap<>();
        map.put("read", ",get,count,find,list,query,");
        map.put("write", ",add,insert,create,update,delete,remove,");
        map.forEach((key, value) -> {
            List<String> list = Arrays.asList(map.get(key)
                    .split(","))
                    .stream()
                    .filter(type -> StringUtils.isNotEmpty(type))
                    .collect(Collectors.toList());
            METHODTYPE.put(key, list);
        });
    }

    //获取数据源名称
    @Override
    protected Object determineCurrentLookupKey() {
        return HandleDatasource.getDatasource();
    }

}
