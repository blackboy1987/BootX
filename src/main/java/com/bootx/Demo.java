package com.bootx;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;

public class Demo {

  public static void main(String[] args) {
    String userAgent="Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36";


    UserAgent userAgent1 = UserAgent.parseUserAgentString(userAgent);

    System.out.println(userAgent1);
    Browser browser = userAgent1.getBrowser();
    Version version = userAgent1.getBrowserVersion();
    OperatingSystem operatingSystem = userAgent1.getOperatingSystem();
    System.out.println("111");

  }
}
