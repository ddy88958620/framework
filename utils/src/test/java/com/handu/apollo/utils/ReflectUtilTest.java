package com.handu.apollo.utils;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

public class ReflectUtilTest {

    @Test
    public void testGetAllFieldsForClass() throws Exception {
        Assert.assertEquals(ReflectUtil.getAllFieldsForClass(TypeVo.class, TypeVo.class).size(), 11);
    }

    @Test
    public void testIsBaseType() throws Exception {
        Field l = TypeVo.class.getDeclaredField("l");
        l.setAccessible(true);
        Field lon = TypeVo.class.getDeclaredField("lon");
        lon.setAccessible(true);
        Assert.assertTrue(ReflectUtil.isBaseType(l));
        Assert.assertFalse(ReflectUtil.isBaseType(lon));
    }

    @Test
    public void testSetValueToDefault() throws Exception {
        TypeVo vo = new TypeVo();
        List<Field> fields = ReflectUtil.getAllFieldsForClass(vo.getClass(), TypeVo.class);
        for (Field field : fields) {
            field.setAccessible(true);
            ReflectUtil.setValueToDefault(field, vo);
        }
    }

}

class TypeVo {
    private long l;
    private int i;
    private byte b;
    private char c;
    private short s;
    private float f;
    private double d;
    private boolean boo;
    private Long lon;
    private long[] longs;
    private Object o;

    public Object getO() {
        return o;
    }

    public void setO(Object o) {
        this.o = o;
    }

    public long[] getLongs() {
        return longs;
    }

    public void setLongs(long[] longs) {
        this.longs = longs;
    }

    public Long getLon() {
        return lon;
    }

    public void setLon(Long lon) {
        this.lon = lon;
    }

    public long getL() {
        return l;
    }

    public void setL(long l) {
        this.l = l;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public byte getB() {
        return b;
    }

    public void setB(byte b) {
        this.b = b;
    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }

    public short getS() {
        return s;
    }

    public void setS(short s) {
        this.s = s;
    }

    public float getF() {
        return f;
    }

    public void setF(float f) {
        this.f = f;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public boolean isBoo() {
        return boo;
    }

    public void setBoo(boolean boo) {
        this.boo = boo;
    }
}