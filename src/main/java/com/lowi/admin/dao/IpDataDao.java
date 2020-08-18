package com.lowi.admin.dao;

import com.lowi.admin.entity.IpData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Lowi
 * @since 2020-05-22
 */
@Repository
public interface IpDataDao extends BaseMapper<IpData> {

    void batchInert(List<IpData> ipDataList);

    List<IpData> getIpAddr(Long ip);

    List<Map> getTables();

    List<Map> getTableData(String tableName);
}
