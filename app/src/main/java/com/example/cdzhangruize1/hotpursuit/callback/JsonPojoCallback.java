package com.example.cdzhangruize1.hotpursuit.callback;

import com.example.cdzhangruize1.hotpursuit.constant.Constant;
import com.example.cdzhangruize1.hotpursuit.pojo.JsonPojo;

abstract class JsonPojoCallback extends JsonCallback<JsonPojo> {
    JsonPojoCallback() {
        super(Constant.TYPE_JSON_POJO);
    }
}
