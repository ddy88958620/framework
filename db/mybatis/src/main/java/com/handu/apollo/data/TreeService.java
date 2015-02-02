package com.handu.apollo.data;

import com.handu.apollo.data.utils.Param;
import com.handu.apollo.utils.CharPool;
import com.handu.apollo.utils.CodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by markerking on 14/9/4.
 */
@Service
@Transactional
public class TreeService {
    public static final String CLASSNAME = TreeService.class.getName() + CharPool.PERIOD;

    @Autowired
    private Dao _dao;

    public void updateLeaf(String tableName) {
        Map<String, Object> params = new Param.Builder()
                .put("tableName", tableName)
                .build();
        _dao.update(CLASSNAME, "updateLeaf", params);
        _dao.update(CLASSNAME, "updateNotLeaf", params);
    }

    public String getLastCodeByParent(String tableName, Serializable parentId) {
        Map<String, Object> params = new Param.Builder()
                .put("tableName", tableName)
                .put("parentId", parentId)
                .build();
        return _dao.get(CLASSNAME, "getLastCodeByParent", params);
    }

    public void updateCode(String tableName, Serializable id, String code) {
        Map<String, Object> params = new Param.Builder()
                .put("tableName", tableName)
                .put("id", id)
                .put("code", code)
                .build();
        _dao.update(CLASSNAME, "updateCode", params);
    }

    public String generateCode(String tableName, Serializable parentId) {
        String nextCode;
        String lastCode = getLastCodeByParent(tableName, parentId);
        if (StringUtils.isNotBlank(lastCode)) {
            nextCode = CodeUtil.getNextCode(lastCode);
        } else {
            String parentCode = getCodeById(tableName, parentId);
            if (parentCode != null) {
                nextCode = CodeUtil.getFirstCode(parentCode);
            } else {
                nextCode = CodeUtil.getFirstCode(null);
            }
        }
        return nextCode;
    }

    public String getCodeById(String tableName, Serializable id) {
        Map<String, Object> params = new Param.Builder()
                .put("tableName", tableName)
                .put("id", id)
                .build();
        return _dao.get(CLASSNAME, "getCodeById", params);
    }

    public List<Map<String, Serializable>> likeByCode(String tableName, String code) {
        Map<String, Object> params = new Param.Builder()
                .put("tableName", tableName)
                .putLikeR("code", code)
                .build();
        return _dao.getList(CLASSNAME, "likeByCode", params);
    }

    public void updateParentIdCode(String tableName, Serializable id, Serializable parentId, String code) {
        Map<String, Object> params = new Param.Builder()
                .put("tableName", tableName)
                .put("id", id)
                .put("parentId", parentId)
                .put("code", code)
                .build();
        _dao.update(CLASSNAME, "updateParentIdCode", params);
    }

    public void resetCode(String tableName, Serializable id, Serializable oldParentId, Serializable newParentId) {
        if (!oldParentId.equals(newParentId)) {
            // 查询旧code
            String oldCode = getCodeById(tableName, id);
            // 得到新code
            String code = generateCode(tableName, newParentId);
            // 更新parentId和新生成的code
            updateParentIdCode(tableName, id, newParentId, code);

            // 根据旧code查询出所有下级
            List<Map<String, Serializable>> childs = likeByCode(tableName, oldCode);
            for (Map<String, Serializable> child : childs) {
                // 将下级的旧code变更为新code
                String childNewCode = ((String) child.get("code")).replace(oldCode, code);
                updateCode(tableName, child.get("id"), childNewCode);
            }
        }
    }
}

