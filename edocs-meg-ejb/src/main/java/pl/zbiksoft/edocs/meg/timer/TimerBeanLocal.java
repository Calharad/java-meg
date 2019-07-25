/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.timer;

import java.util.Date;
import javax.ejb.Local;
import javax.ejb.Timer;
import pl.zbiksoft.edocs.meg.beans.simulation.MachineSimulator;
import pl.zbiksoft.edocs.meg.timer.TimerBean.TimerMode;

/**
 *
 * @author ZbikKomp
 */
@Local
public interface TimerBeanLocal {

    Timer createTimer(int owner, Date d, TimerMode mode);

    Timer createTimer(int owner, long d, TimerMode mode);

}
