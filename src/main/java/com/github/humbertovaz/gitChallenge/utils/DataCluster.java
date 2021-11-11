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

    public InputStream getIn() {
        return in;
    }

    public BufferedReader getBr() {
        return br;
    }
}
