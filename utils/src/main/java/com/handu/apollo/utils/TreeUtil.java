package com.handu.apollo.utils;

import com.google.common.collect.Lists;
import com.handu.apollo.base.TreeVo;
import com.handu.apollo.base.Treeable;

import java.util.List;

/**
 * Created by markerking on 14/9/3.
 */
public final class TreeUtil {

    private TreeUtil() {}

    public static List listToTree(List list) {
        TreeVo root = new TreeVo() {
            @Override
            public void setText(String text) {

            }

            @Override
            public String getText() {
                return null;
            }
        };
        root.setId(0L);
        return findChild(root, list);
    }

    public static List listToTree(Treeable root,List list) {
        return findChild(root, list);
    }

    private static List<Treeable> findChild(Treeable root, List<Treeable> allDatas) {
        List<Treeable> children = Lists.newArrayList();
        List<Treeable> notChildren = Lists.newArrayList();
        for (Treeable vo : allDatas) {
            if (vo.getParentId().equals(root.getId())) {
                children.add(vo);
            }else{
                notChildren.add(vo);
            }
        }

        for (Treeable child : children) {
            child.setChildren(findChild(child, notChildren));
            if (child.getChildren() == null || child.getChildren().size() < 1) {
                child.setLeaf(true);
            } else {
                child.setLeaf(false);
            }
        }

        return children;
    }
}
