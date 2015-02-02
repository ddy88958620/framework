package com.handu.apollo.utils;

import org.junit.Test;

import java.util.Date;

/**
 * Created by markerking on 14-4-14.
 */
public class IDTest {
    @Test
    public void testGen() throws Exception {
        System.out.println(ID.gen(99L));
        System.out.println(ID.gen(199L, "SL"));
        System.out.println(ID.gen(1999L, "SL", 20));
    }

    @Test
    public void testDate() throws Exception {
        System.out.println(ID.date(99L));
        System.out.println(ID.date(199L, 10));
        System.out.println(ID.date(1999L, new Date(), 20));
        System.out.println(ID.date(19999L, new Date(), "yyMMdd", 30));
    }
}
