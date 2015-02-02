package com.handu.apollo.data;

import com.handu.apollo.data.vo.Counter;
import com.handu.apollo.utils.CharPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by markerking on 14-4-14.
 */
@Repository
public class CounterDao {

    public static final String CLASS_NAME = CounterDao.class.getName() + CharPool.PERIOD;

    private static final String DEFAULT_NAME = "DEFAULT";
    private static final int START_ID = 1;

    @Autowired
    private Dao dao;

    public List<Counter> listAllCounters() {
        return dao.getList(CLASS_NAME, "listAllCounters");
    }

    /**
     * 生成默认ID
     *
     * @return
     * @throws Exception
     */
    public long increment() {
        return increment(DEFAULT_NAME);
    }

    /**
     * 按照增长值生成默认ID
     *
     * @param step
     * @return
     * @throws Exception
     */
    public long increment(int step) {
        return increment(DEFAULT_NAME, step);
    }

    /**
     * 生成ID
     *
     * @param name
     * @return
     * @throws Exception
     */
    public long increment(String name) {
        return increment(name, START_ID);
    }

    /**
     * 按照增长值生成ID
     *
     * @param name
     * @param step
     * @return
     * @throws Exception
     */
    public long increment(String name, int step) {
        Counter counter = dao.get(CLASS_NAME, "getCounterByName", name);
        if (counter == null) {
            counter = new Counter();
            counter.setName(name);
            counter.setCurrentId(step);
            dao.insert(CLASS_NAME, "insert", counter);
        } else {
            counter.setCurrentId(counter.getCurrentId() + step);
            dao.update(CLASS_NAME, "update", counter);
        }
        return counter.getCurrentId();
    }

    /**
     * 重命名
     *
     * @param oldName
     * @param newName
     */
    public void rename(String oldName, String newName) {
        if (oldName != null && newName != null && !DEFAULT_NAME.equals(oldName) && !DEFAULT_NAME.equals(newName)) {
            Counter counter = dao.get(CLASS_NAME, "getCounterByName", newName);
            if (counter == null) {
                counter = dao.get(CLASS_NAME, "getCounterByName", oldName);
                if (counter == null) {
                    increment(newName);
                } else {
                    counter.setName(newName);
                    dao.update(CLASS_NAME, "update", counter);
                }
            }
        }
    }

    /**
     * 重置默认ID（慎用）
     */
    public void reset() {
        reset(DEFAULT_NAME);
    }

    /**
     * 重置ID（慎用）
     *
     * @param name
     */
    public void reset(String name) {
        dao.delete(CLASS_NAME, "delete", name);
    }

}