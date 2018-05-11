package com.miclcx.netgap.service;

import com.miclcx.netgap.entity.SyncJobConfig;

import java.util.List;

public interface SyncDataService {

    /**
     * 获取所有启用的数据同步任务配置
     * @return
     */
    List<SyncJobConfig> getAllSyncJobConfig();

    /**
     * 开始数据同步
     * @param syncJobConfig
     */
    void startSyncJob(final SyncJobConfig syncJobConfig);

    /**
     * 初始化目标库
     * @param syncJobConfig
     */
    void initTargetDatabase(SyncJobConfig syncJobConfig);

}
