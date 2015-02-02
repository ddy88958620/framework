package com.handu.apollo.utils;

import com.google.common.collect.Lists;
import com.handu.apollo.base.TreeVo;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TreeUtilTest {

    @Test
    public void testListToTree() throws Exception {
        TreeVo vo1 = new TestTree();
        vo1.setId(1L);
        vo1.setParentId(0L);
        vo1.setCode("0001");

        TreeVo vo2 = new TestTree();
        vo2.setId(2L);
        vo2.setParentId(1L);
        vo2.setCode("0001-0001");

        TreeVo vo3 = new TestTree();
        vo3.setId(3L);
        vo3.setParentId(2L);
        vo3.setCode("0001-0001-0001");

        TreeVo vo4 = new TestTree();
        vo4.setId(4L);
        vo4.setParentId(0L);
        vo4.setCode("0002");

        TreeVo vo5 = new TestTree();
        vo5.setId(5L);
        vo5.setParentId(4L);
        vo5.setCode("0002-0001");

        TreeVo vo6 = new TestTree();
        vo6.setId(6L);
        vo6.setParentId(4L);
        vo6.setCode("0002-0002");

        TreeVo vo7 = new TestTree();
        vo7.setId(7L);
        vo7.setParentId(0L);
        vo7.setCode("0003");

        List<TreeVo> list = Lists.newArrayList();
        list.add(vo7);
        list.add(vo1);
        list.add(vo2);
        list.add(vo3);
        list.add(vo4);
        list.add(vo5);
        list.add(vo6);

        list = TreeUtil.listToTree(list);
        assertEquals(list.size(), 3);
    }

    class TestTree extends TreeVo {

        @Override
        public void setText(String text) {

        }

        @Override
        public String getText() {
            return null;
        }
    }
}