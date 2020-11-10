package org.fms.cfs.common.webapp.domain;

import java.util.Date;

import com.riozenc.titanTool.annotation.TablePrimaryKey;
import com.riozenc.titanTool.mybatis.MybatisEntity;

/**
 * 计量点与互感器资产中间表
 */
public class MeterInductorAssetsRelDomain extends ManagerParamEntity  implements MybatisEntity {
    //id
    @TablePrimaryKey
    private Long id;
    //计量点id
    private Long meterId;
    //资产id
    private Long inductorAssetsId;
    //互感器序号
    private Byte phaseSeq;
    //互感器序号
    private Byte inductorOrder;
    //互感器标识
    private Byte inductorType;
    //创建时间
    private Date createDate;
    //状态
    private Byte status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public Long getInductorAssetsId() {
        return inductorAssetsId;
    }

    public void setInductorAssetsId(Long inductorAssetsId) {
        this.inductorAssetsId = inductorAssetsId;
    }

    public Byte getPhaseSeq() {
        return phaseSeq;
    }

    public void setPhaseSeq(Byte phaseSeq) {
        this.phaseSeq = phaseSeq;
    }

    public Byte getInductorOrder() {
        return inductorOrder;
    }

    public void setInductorOrder(Byte inductorOrder) {
        this.inductorOrder = inductorOrder;
    }

    public Byte getInductorType() {
        return inductorType;
    }

    public void setInductorType(Byte inductorType) {
        this.inductorType = inductorType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
