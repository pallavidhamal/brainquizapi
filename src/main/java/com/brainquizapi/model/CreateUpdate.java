package com.brainquizapi.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter @Setter
public class CreateUpdate {

	@Column(name = "created_date")
    private Date createdate;
	@Column(name = "created_by")
    private String createby;

	@Column(name = "updated_date")
    private Date updatedate;
	@Column(name = "updated_by")
    private String updateby;
	
	@Column(name = "is_deleted")
    private String isdeleted;
	
	@PrePersist
	protected void onCreate() {
	 createdate = new Date();
	}
	@PreUpdate
	protected void onUpdate() {
	  updatedate = new Date();
	}
}