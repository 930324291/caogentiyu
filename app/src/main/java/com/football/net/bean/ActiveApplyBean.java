package com.football.net.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by footman on 2017/2/22.
 */

public class ActiveApplyBean implements Serializable {

    List<TankInTeamBean> tanks;

    public List<TankInTeamBean> getTanks() {
        return tanks;
    }

    public void setTanks(List<TankInTeamBean> tanks) {
        this.tanks = tanks;
    }
}
