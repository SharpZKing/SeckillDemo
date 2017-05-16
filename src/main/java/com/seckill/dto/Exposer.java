package com.seckill.dto;

/**
 * Created by zjfsharp on 2017/5/16.
 */
public class Exposer {

    private boolean exposed;

    private String md5;

    private long seckilId;

    private long now;

    private long start;

    private long end;

    public Exposer(boolean exposed, String md5, long seckilId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckilId = seckilId;
    }

    public Exposer(boolean exposed, long seckilId,long now, long start, long end) {
        this.exposed = exposed;
        this.seckilId = seckilId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Exposer(boolean exposed, long seckilId) {
        this.exposed = exposed;
        this.seckilId = seckilId;
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSeckilId() {
        return seckilId;
    }

    public void setSeckilId(long seckilId) {
        this.seckilId = seckilId;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
