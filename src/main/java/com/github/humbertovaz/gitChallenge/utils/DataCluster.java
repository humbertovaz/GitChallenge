package com.github.humbertovaz.gitChallenge.utils;

import java.io.BufferedReader;
import java.io.InputStream;

public class DataCluster {
    InputStream in;
    BufferedReader br;

    public DataCluster(InputStream in, BufferedReader br) {
        this.in = in;
        this.br = br;
    }
    /**
     * This getter is responsible to get InputStream (in) field
     */
    public InputStream getIn() {
        return in;
    }

    /**
     * This getter is responsible to get BufferedReader (br) field
     */
    public BufferedReader getBr() {
        return br;
    }
}
