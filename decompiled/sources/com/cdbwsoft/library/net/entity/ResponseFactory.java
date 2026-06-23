package com.cdbwsoft.library.net.entity;

/* JADX INFO: loaded from: classes.dex */
public class ResponseFactory extends BaseResponseFactory<Response> {
    @Override // com.cdbwsoft.library.net.entity.BaseResponseFactory
    public Response newInstance(int i) {
        return Response.result(i);
    }

    @Override // com.cdbwsoft.library.net.entity.BaseResponseFactory
    public Response newInstance(int i, String str) {
        return Response.result(i, str);
    }

    @Override // com.cdbwsoft.library.net.entity.BaseResponseFactory
    public Response newInstance(int i, String str, String str2) {
        return Response.result(i, str, str2);
    }
}
