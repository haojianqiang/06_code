package com.gpdi.app.data.nim;


public enum NimConfig {
    AppKey("85661eed678f54bea3e6b74302adddc6"),
    AppSecret("f456391fb6d1"),


    refreshToken("https://api.netease.im/nimserver/user/refreshToken.action");
    private String url;
    NimConfig(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

//    public static void main(String[] args){
//        System.out.println(NimConfig.AppKey.url);
//    }
}
