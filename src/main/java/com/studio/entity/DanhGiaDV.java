package com.studio.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "DANHGIADV")
public class DanhGiaDV {

	@Id
	@GeneratedValue
	@Column(name = "MADG")
	private int maDG;
	
	@Column(name = "BINHLUAN")
	private String binhLuan;
	
	@Column(name = "SOSAO")
	@NotBlank(message = "Vui lòng chọn mức đánh giá từ 0 đến 5 sao.")
	@Size(min = 0, max = 5, message = "Số sao phải trong khoảng từ 0 đến 5")
	private int soSao;
	
	@ManyToOne
	@JoinColumn(name = "MACT")
	private ChiTietDonHang chiTiet;

	public int getMaDG() {
		return maDG;
	}

	public void setMaDG(int maDG) {
		this.maDG = maDG;
	}

	public String getBinhLuan() {
		return binhLuan;
	}

	public void setBinhLuan(String binhLuan) {
		this.binhLuan = binhLuan;
	}

	public int getSoSao() {
		return soSao;
	}

	public void setSoSao(int soSao) {
		this.soSao = soSao;
	}

	public ChiTietDonHang getChiTiet() {
		return chiTiet;
	}

	public void setChiTiet(ChiTietDonHang chiTiet) {
		this.chiTiet = chiTiet;
	}

	public DanhGiaDV() {
		super();
	}


}
