package com.handu.apollo.data;

import com.handu.apollo.data.vo.Counter;
import config.TestMybatisConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by markerking on 14-4-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(classes = {TestMybatisConfig.class})
@Transactional
public class DaoTest {
    @Autowired Dao _dao;

    @Test
    public void testInsert() throws Exception {
        Counter c = new Counter();
        c.setName("test");
        c.setCurrentId(1);
        _dao.insert("com.handu.apollo.data.CounterDao.", "insert", c);
    }

    @Test
    public void testUpdate() throws Exception {
        Counter c = new Counter();
        c.setName("DEFAULT");
        c.setCurrentId(5);
        _dao.update("com.handu.apollo.data.CounterDao.", "update", c);
    }

    @Test
    public void testDelete() throws Exception {
        Counter c = new Counter();
        c.setName("DEFAULT");
        _dao.delete("com.handu.apollo.data.CounterDao.", "delete", c);
    }

    @Test
    public void testDelete1() throws Exception {
        _dao.delete("com.handu.apollo.data.CounterDao.", "delete", "DEFAULT");
    }

    @Test
    public void testGet() throws Exception {
        Counter counter = _dao.get("com.handu.apollo.data.CounterDao.", "getCounterByName", "DEFAULT");
        System.out.println(counter);
    }

    @Test
    public void testGetList() throws Exception {
        System.out.println(_dao.getList("com.handu.apollo.data.CounterDao.", "listAllCounters"));
    }

    @Test
    public void testGetList1() throws Exception {
        System.out.println(_dao.getList("com.handu.apollo.data.CounterDao.", "listAllCounters", "DEFAULT"));
    }

    @Test
    public void testCount() throws Exception {

    }

    @Test
    public void testPage() throws Exception {

    }
}
