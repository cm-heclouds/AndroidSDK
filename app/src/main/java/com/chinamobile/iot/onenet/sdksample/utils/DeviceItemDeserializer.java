package com.chinamobile.iot.onenet.sdksample.utils;

import com.chinamobile.iot.onenet.sdksample.model.ActivateCode;
import com.chinamobile.iot.onenet.sdksample.model.DeviceItem;
import com.chinamobile.iot.onenet.sdksample.model.Location;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class DeviceItemDeserializer implements JsonDeserializer<DeviceItem> {
    @Override
    public DeviceItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        DeviceItem deviceItem = new DeviceItem();
        deviceItem.setId(jsonObject.get("id").getAsString());
        deviceItem.setTitle(jsonObject.get("title").getAsString());
        JsonElement desc = jsonObject.get("desc");
        deviceItem.setDesc(desc != null ? desc.getAsString() : "");
        JsonElement isPrivate = jsonObject.get("private");
        deviceItem.setPrivate(isPrivate != null ? isPrivate.getAsBoolean() : true);
        JsonElement protocol = jsonObject.get("protocol");
        deviceItem.setProtocol(protocol != null ? protocol.getAsString() : "HTTP");
        deviceItem.setOnline(jsonObject.get("online").getAsBoolean());
        JsonElement locationElement = jsonObject.get("location");
        if (locationElement != null) {
            JsonObject locationObject = locationElement.getAsJsonObject();
            if (locationObject != null) {
                Location location = new Location();
                location.setLat(locationObject.get("lat").getAsString());
                location.setLon(locationObject.get("lon").getAsString());
                deviceItem.setLocation(location);
            }
        }
        deviceItem.setCreateTime(jsonObject.get("create_time").getAsString());
        JsonElement authInfo = jsonObject.get("auth_info");
        if (authInfo != null) {
            if (authInfo.isJsonObject()) {
                deviceItem.setAuthInfo(authInfo.getAsJsonObject().toString());
            } else {
                deviceItem.setAuthInfo(authInfo.getAsString());
            }
        }
        JsonElement activateCodeElement = jsonObject.get("activate_code");
        if (activateCodeElement != null) {
            JsonObject activateCodeObject = activateCodeElement.getAsJsonObject();
            if (activateCodeObject != null) {
                ActivateCode activateCode = new ActivateCode();
                activateCode.setMt(activateCodeObject.get("mt").getAsString());
                activateCode.setMid(activateCodeObject.get("mid").getAsString());
                deviceItem.setActivateCode(activateCode);
            }
        }
        return deviceItem;
    }
}
