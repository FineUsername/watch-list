package com.mygroup.watchlist;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.scheduling.annotation.Scheduled;

// keep the application alive
public class PingTask {

  // a bit less than 30 minutes
  @Scheduled(fixedRate = 1750000)
  public void pingGoogle() {
    try {
      InetAddress address = InetAddress.getByName("google.com");
      boolean isReachable = address.isReachable(50000);
      System.out.println("GOOGLE IS PINGED " + (isReachable));
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
