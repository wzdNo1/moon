package com.mipo.core.express;

import com.mipo.core.express.config.ExpressProperties;

import java.util.Map;

public class ExpressService {
    //请求url
    private String ReqURL = "";

    private ExpressProperties properties;

    public ExpressProperties getProperties() {
        return properties;
    }

    public void setProperties(ExpressProperties properties) {
        this.properties = properties;
    }

    /**
     * @param vendorCode
     * @return
     */
    public String getVendorName(String vendorCode) {
        for (Map<String, String> item : properties.getVendors()) {
            if (item.get("code").equals(vendorCode))
                return item.get("name");
        }
        return null;
    }


}
