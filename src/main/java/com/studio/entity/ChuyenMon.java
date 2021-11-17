package com.studio.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "CHUYENMON")
public class ChuyenMon {

	@Id
	@Column(name = "MACM")
	@NotBlank(message = "Không để trống mã chuyên môn")
	private int maCM;
	
	@Column(name = "TENCM")
	private String tenCM;
	
	@Max(value = 4, message = "Hệ số lương phải từ 0 đến 4")
	@Min(value = 0, message = "Hệ số lương phải từ 0 đến 4")
	@Column(name = "HSLUONG")
	private int hSLuong;
	
	@OneToMany(mappedBy = "chuyenMon", fetch = FetchType.EAGER)

	public int getMaCM() {
		return maCM;
	}

	public void setMaCM(int maCM) {
		this.maCM = maCM;
	}

	public String getTenCM() {
		return tenCM;
	}

	public void setTenCM(String tenCM) {
		this.tenCM = tenCM;
	}

	public int gethSLuong() {
		return hSLuong;
	}

	public void sethSLuong(int hSLuong) {
		this.hSLuong = hSLuong;
	}

	public ChuyenMon() {
		super();
	}

	public ChuyenMon(@NotBlank(message = "Không để trống mã chuyên môn") int maCM) {
		super();
		this.maCM = maCM;
	}

	public ChuyenMon(@NotBlank(message = "Không để trống mã chuyên môn") int maCM, String tenCM,
			@Max(value = 4, message = "Hệ số lương phải từ 0 đến 4") @Min(value = 0, message = "Hệ số lương phải từ 0 đến 4") int hSLuong) {
		super();
		this.maCM = maCM;
		this.tenCM = tenCM;
		this.hSLuong = hSLuong;
	}
	
	


}
