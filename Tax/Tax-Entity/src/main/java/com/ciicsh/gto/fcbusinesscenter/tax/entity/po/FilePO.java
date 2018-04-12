package com.ciicsh.gto.fcbusinesscenter.tax.entity.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import java.time.LocalDateTime;

/**
 * <p>
 * 文件表
 * </p>
 *
 * @author yuantongqing
 * @since 2018-01-05
 */
@TableName("tax_fc_file")
public class FilePO{

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 业务ID（申报子任务ID；缴纳子任务ID；完税凭证子任务ID）
     */
	@TableField("business_id")
	private Long businessId;
    /**
     * 业务类型（01.申报回执单；02.缴纳凭证；03.完税凭证回执）
     */
	@TableField("business_type")
	private String businessType;
    /**
     * 文件类型
     */
	@TableField("file_type")
	private String fileType;
    /**
     * 源文件名
     */
	@TableField("filename_source")
	private String filenameSource;
    /**
     * 存储文件名
     */
	@TableField("filename_save")
	private String filenameSave;
    /**
     * 路径
     */
	@TableField("file_path")
	private String filePath;
    /**
     * 是否可用
     */
	@TableField("is_active")
	private Boolean isActive;
    /**
     * 创建时间
     */
    @TableField(value="created_time",fill = FieldFill.INSERT)
	private LocalDateTime createdTime;
    /**
     * 修改时间
     */
    @TableField(value="modified_time",fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime modifiedTime;
    /**
     * 创建人
     */
    @TableField(value="created_by",fill = FieldFill.INSERT)
	private String createdBy;
    /**
     * 修改人
     */
    @TableField(value="modified_by",fill = FieldFill.INSERT_UPDATE)
	private String modifiedBy;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFilenameSource() {
		return filenameSource;
	}

	public void setFilenameSource(String filenameSource) {
		this.filenameSource = filenameSource;
	}

	public String getFilenameSave() {
		return filenameSave;
	}

	public void setFilenameSave(String filenameSave) {
		this.filenameSave = filenameSave;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDateTime getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(LocalDateTime modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Override
	public String toString() {
		return "File{" +
			"id=" + id +
			", businessId=" + businessId +
			", businessType=" + businessType +
			", fileType=" + fileType +
			", filenameSource=" + filenameSource +
			", filenameSave=" + filenameSave +
			", filePath=" + filePath +
			", isActive=" + isActive +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
	}
}
