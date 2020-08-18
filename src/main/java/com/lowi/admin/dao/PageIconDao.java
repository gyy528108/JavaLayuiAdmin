package com.lowi.admin.dao;

import com.lowi.admin.entity.PageIcon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Lowi
 * @since 2020-08-17
 */
@Repository
public interface PageIconDao extends BaseMapper<PageIcon> {

    /**
     * 批量插入数据
     * @param list 数据
     * @date 2020-08-17 16:38
     * */
    void batchInsert(List<PageIcon> list);
}
