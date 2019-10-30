package com.desktopip.exploriztic.tootanium.interfaces;

import com.android.volley.VolleyError;

public interface IRequestResult<T> {
    void getResult(T object);
    void getError(VolleyError object);
}
