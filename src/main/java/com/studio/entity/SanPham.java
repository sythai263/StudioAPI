package com.studio.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "SANPHAM")
public class SanPham {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MASP")
	private int maSP;
	
	
	@Column(name = "TENSP")
	@NotBlank(message = "Không để trống tên sản phẩm.")
	private String tenSP;
	
	@ManyToOne
	@JoinColumn(name = "MADV")
	private DichVu dichVu;
	
	@Column(name = "MOTA")
	private String moTa;
	
	@NotBlank(message = "Không để trống link sản phẩm.")
	@Column(name = "LINK")
	private String link;

	public int getMaSP() {
		return maSP;
	}

	public void setMaSP(int maSP) {
		this.maSP = maSP;
	}

	public String getTenSP() {
		return tenSP;
	}

	public void setTenSP(String tenSP) {
		this.tenSP = tenSP;
	}

	public DichVu getDichVu() {
		return dichVu;
	}

	public void setDichVu(DichVu dichVu) {
		this.dichVu = dichVu;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public SanPham() {
		super();
	}

	public SanPham(int maSP) {
		this.maSP = maSP;
	}

	public SanPham(int maSP, String tenSP, String link) {
		this.maSP = maSP;
		this.tenSP = tenSP;
		this.link = link;
	}
}
