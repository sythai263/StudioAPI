package com.studio.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "DICHVU")
public class DichVu {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MADV")
	private int maDV;

	
	@Column(name = "TENDV")
	@NotBlank(message = "Không để trống tên dịch vụ")
	private String tenDV;
	
	@Column(name = "MOTA")
	private String moTa;
	
	@OneToMany(mappedBy = "dichVu", fetch = FetchType.EAGER)

	public int getMaDV() {
		return maDV;
	}

	public void setMaDV(int maDV) {
		this.maDV = maDV;
	}

	public String getTenDV() {
		return tenDV;
	}

	public void setTenDV(String tenDV) {
		this.tenDV = tenDV;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public DichVu() {
		super();
	}

	public DichVu(int maDV) {
		this.maDV = maDV;
	}

	public DichVu(int maDV, String tenDV, String moTa) {
		this.maDV = maDV;
		this.tenDV = tenDV;
		this.moTa = moTa;
	}
}
