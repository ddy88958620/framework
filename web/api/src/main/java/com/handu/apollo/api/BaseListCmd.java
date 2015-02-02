package com.handu.apollo.api;

import java.util.List;

public abstract class BaseListCmd extends BaseCmd {

    public static Integer PAGESIZE_UNLIMITED = -1;
    public static Integer PAGE_SIZE = 20;

    // ///////////////////////////////////////////////////
    // ///////// BaseList API parameters /////////////////
    // ///////////////////////////////////////////////////

    @Input(description = "页码", type = CommandType.INTEGER)
    private Integer page;

    @Input(description = "每页显示数量，最大100", type = CommandType.INTEGER)
    private Integer pagesize;

    @Input(description = "排序字段，order=username-desc,email-asc", type = CommandType.LIST, collectionType = CommandType.STRING)
    private List<String> order;

    // ///////////////////////////////////////////////////
    // ///////////////// Accessors ///////////////////////
    // ///////////////////////////////////////////////////

    public BaseListCmd() {
    }

    public void configure() {

    }

    public Integer getPage() {
        return page;
    }

    public Integer getPagesize() {
        if (pagesize == null) {
            pagesize = PAGE_SIZE;
        }
        if (pagesize.equals(PAGESIZE_UNLIMITED)) {
            pagesize = Integer.MAX_VALUE;
        }

        return pagesize;
    }

    public int getStartIndex() {
        int startIndex = 0;
        int pageSizeVal = getPagesize();

        if (pageSizeVal == 0) {
            startIndex = 0;
        } else if (page != null) {
            int pageNum = page;
            if (pageNum > 0) {
                startIndex = pageSizeVal * (pageNum - 1);
            }
        }
        return startIndex;
    }

    public List<String> getOrder() {
        return order;
    }
}