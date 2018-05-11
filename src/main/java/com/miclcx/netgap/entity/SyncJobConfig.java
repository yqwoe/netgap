package com.miclcx.netgap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.beans.Transient;

public class SyncJobConfig {

    private long id;
    /**
     * 源数据库名称
     */
    private String sourceDbName;
    /**
     * 源数据库地址
     */
    private String sourceDbHost;
    /**
     * 源数据库端口
     */
    private int sourceDbPort;
    /**
     * 源数据库用户名
     */
    private String sourceDbUsername;
    /**
     * 源数据库用户密码
     */
    private String sourceDbPassword;
    /**
     * 源表名称
     */
    private String sourceTableName;
    /**
     * 目标数据库名称
     */
    private String targetDbName;
    /**
     * 目标数据库地址
     */
    private String targetDbHost;
    /**
     * 目标数据库端口
     */
    private int targetDbPort;
    /**
     * 目标数据库用户名
     */
    private String targetDbUsername;
    /**
     * 目标数据库用户密码
     */
    private String targetDbPassword;
    /**
     * 目标表名称
     */
    private String targetTableName;

    /**
     * 是否启用同步
     */
    private boolean enable;

    /**
     * 源库数据源的key
     */
    @JsonIgnore
    private String sourceDataSourceKey;

    /**
     * 目标库数据源的key
     */
    @JsonIgnore
    private String targetDataSourceKey;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSourceDbName() {
        return sourceDbName;
    }

    public void setSourceDbName(String sourceDbName) {
        this.sourceDbName = sourceDbName;
    }

    public String getSourceDbHost() {
        return sourceDbHost;
    }

    public void setSourceDbHost(String sourceDbHost) {
        this.sourceDbHost = sourceDbHost;
    }

    public int getSourceDbPort() {
        return sourceDbPort;
    }

    public void setSourceDbPort(int sourceDbPort) {
        this.sourceDbPort = sourceDbPort;
    }

    public String getSourceDbUsername() {
        return sourceDbUsername;
    }

    public void setSourceDbUsername(String sourceDbUsername) {
        this.sourceDbUsername = sourceDbUsername;
    }

    public String getSourceDbPassword() {
        return sourceDbPassword;
    }

    public void setSourceDbPassword(String sourceDbPassword) {
        this.sourceDbPassword = sourceDbPassword;
    }

    public String getSourceTableName() {
        return sourceTableName;
    }

    public void setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName;
    }

    public String getTargetDbName() {
        return targetDbName;
    }

    public void setTargetDbName(String targetDbName) {
        this.targetDbName = targetDbName;
    }

    public String getTargetDbHost() {
        return targetDbHost;
    }

    public void setTargetDbHost(String targetDbHost) {
        this.targetDbHost = targetDbHost;
    }

    public int getTargetDbPort() {
        return targetDbPort;
    }

    public void setTargetDbPort(int targetDbPort) {
        this.targetDbPort = targetDbPort;
    }

    public String getTargetDbUsername() {
        return targetDbUsername;
    }

    public void setTargetDbUsername(String targetDbUsername) {
        this.targetDbUsername = targetDbUsername;
    }

    public String getTargetDbPassword() {
        return targetDbPassword;
    }

    public void setTargetDbPassword(String targetDbPassword) {
        this.targetDbPassword = targetDbPassword;
    }

    public String getTargetTableName() {
        return targetTableName;
    }

    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getSourceDataSourceKey() {
        this.sourceDataSourceKey = "SourceDB-"+getSourceDbName()+"-"+getSourceDbHost()+":"+getSourceDbPort();
        return this.sourceDataSourceKey;
    }

    public String getTargetDataSourceKey() {
        this.targetDataSourceKey = "TargetDB-"+getTargetDbName()+"-"+getTargetDbHost()+":"+getTargetDbPort();
        return this.targetDataSourceKey;
    }


    @Override
    public String toString() {
        return "SyncJobConfig{" +
                "id=" + id +
                ", sourceDbName='" + sourceDbName + '\'' +
                ", sourceDbHost='" + sourceDbHost + '\'' +
                ", sourceDbPort='" + sourceDbPort + '\'' +
                ", sourceDbUsername='" + sourceDbUsername + '\'' +
                ", sourceDbPassword='" + sourceDbPassword + '\'' +
                ", sourceTableName='" + sourceTableName + '\'' +
                ", targetDbName='" + targetDbName + '\'' +
                ", targetDbHost='" + targetDbHost + '\'' +
                ", targetDbPort='" + targetDbPort + '\'' +
                ", targetDbUsername='" + targetDbUsername + '\'' +
                ", targetDbPassword='" + targetDbPassword + '\'' +
                ", targetTableName='" + targetTableName + '\'' +
                ", enable=" + enable +
                '}';
    }
}
