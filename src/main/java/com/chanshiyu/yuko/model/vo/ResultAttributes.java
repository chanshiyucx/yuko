package com.chanshiyu.yuko.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SHIYU
 * @date 2020/3/31 16:53
 * @description 分页描述信息
 */
@Data
@NoArgsConstructor
public class ResultAttributes {

    /**
     * 当前页数
     */
    private Long pageNum;

    /**
     * 页大小
     */
    private Long pageSize;

    /**
     * 总数
     */
    private Long total;

}
