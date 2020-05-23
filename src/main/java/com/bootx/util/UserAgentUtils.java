package com.bootx.util;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;

import java.util.HashMap;
import java.util.Map;

public final class UserAgentUtils {

  private UserAgentUtils(){

  }

  public static Map<String, String> getClientInfo(String userAgent){
    Map<String,String> data = new HashMap<>();
    UserAgent userAgent1 = UserAgent.parseUserAgentString(userAgent);
    Browser browser = userAgent1.getBrowser();
    Version version = userAgent1.getBrowserVersion();
    OperatingSystem operatingSystem = userAgent1.getOperatingSystem();
    try {
      data.put("browser",browser.getName());
    }catch (Exception e){
      e.printStackTrace();
    }
    try {
      data.put("browserRenderingEngine",browser.getRenderingEngine().getName());
    }catch (Exception e){
      e.printStackTrace();
    }
    try {
      data.put("browserType",browser.getBrowserType().getName());
    }catch (Exception e){
      e.printStackTrace();
    }
    try {
      data.put("browserVersion", version.getVersion());
    }catch (Exception e){
      e.printStackTrace();
    }
    try {
      data.put("browserManufacturer",browser.getManufacturer().getName());
    }catch (Exception e){
      e.printStackTrace();
    }
    try {
      data.put("os",operatingSystem.getName());
    }catch (Exception e){
      e.printStackTrace();
    }
    try {
      data.put("deviceType",operatingSystem.getDeviceType().getName());;
    }catch (Exception e){
      e.printStackTrace();
    }
    try {
      data.put("manufacturer",operatingSystem.getManufacturer().getName());
    }catch (Exception e){
      e.printStackTrace();
    }






    return data;
  }

}
