package com.handu.apollo.security.utils;

import com.handu.apollo.base.Baseable;

import java.io.Serializable;
import java.util.Date;

public class VoUtil {
    public static void generateCreated(Baseable base) {
        base.setCreated(new Date());
        try {
            base.setCreater(SessionUtil.getUserId());
        } catch (Exception e) {
            base.setCreater(0L);
        }
    }

    public static void generateModified(Baseable base) {
        base.setModified(new Date());
        try {
            base.setModifier(SessionUtil.getUserId());
        } catch (Exception e) {
            base.setModifier(0L);
        }
    }

    public static void generateRemoved(Baseable base) {
        base.setRemoved(new Date());
        try {
            base.setRemover(SessionUtil.getUserId());
        } catch (Exception e) {
            base.setRemover(0L);
        }
    }

    public static Serializable getUserId() {
        try {
            return SessionUtil.getUserId();
        } catch (Exception e) {
            return 0L;
        }
    }
}