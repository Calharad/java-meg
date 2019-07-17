/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.util;

/**
 *
 * @author ZbikKomp
 */
public class StopWatch {

  private long startTime = 0;
  private long stopTime = 0;
  private boolean running = false;
  
  public void start() {
    this.startTime = System.currentTimeMillis();
    this.running = true;
  }


  public void stop() {
    this.stopTime = System.currentTimeMillis();
    this.running = false;
  }


  //elaspsed time in milliseconds
  public int getElapsedTime() {
    long elapsed;
    if (running) {
      elapsed = (System.currentTimeMillis() - startTime);
    } else {
      elapsed = (stopTime - startTime);
    }
    return (int) elapsed;
  }
}
