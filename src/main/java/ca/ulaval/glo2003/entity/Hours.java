package ca.ulaval.glo2003.entity;

import java.time.LocalTime;

public class Hours {

    private LocalTime open;
    private LocalTime close;

    public Hours(LocalTime open, LocalTime close) {
        this.open = open;
        this.close = close;

    }

    public LocalTime getOpen() {
        return open;
    }

    public void setOpen(LocalTime open) {
        this.open = open;
    }

    public LocalTime getClose() {
        return close;
    }

    public void setClose(LocalTime close) {
        this.close = close;
    }
}
