package com.anderfred.skytecgamesTest;

import com.anderfred.skytecgamesTest.spring.entity.Player;

public class Duel {
    private Player p1, p2;
    private int idP1, idP2;

    public Player getP1() {
        return p1;
    }

    public void setP1(Player p1) {
        this.p1 = p1;
    }

    public Player getP2() {
        return p2;
    }

    public void setP2(Player p2) {
        this.p2 = p2;
    }

    public int getIdP1() {
        return idP1;
    }


    public int getIdP2() {
        return idP2;
    }


    public Duel(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.idP1 = p1.getId();
        this.idP2 = p2.getId();
    }
}
