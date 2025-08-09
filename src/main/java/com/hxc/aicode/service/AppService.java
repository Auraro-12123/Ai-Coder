package com.hxc.aicode.service;

import com.hxc.aicode.model.dto.app.AppQueryRequest;
import com.hxc.aicode.model.vo.AppVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.hxc.aicode.model.entity.App;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author hxc
 */
public interface AppService extends IService<App> {

    AppVO getAppVO(App app);

    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    List<AppVO> getAppVOList(List<App> appList);
}
