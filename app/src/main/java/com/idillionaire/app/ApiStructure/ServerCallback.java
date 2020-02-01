package com.idillionaire.app.ApiStructure;

import org.json.JSONObject;

public interface ServerCallback {
    void onSuccess(JSONObject result, String ERROR);
}
