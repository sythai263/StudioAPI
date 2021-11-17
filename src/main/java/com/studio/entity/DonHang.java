package com.studio.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "DONHANG")
public class DonHang {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MADH")
	private int maDH;
	
	@ManyToOne
	@JoinColumn(name = "MAKH")
	private KhachHang khachHang;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TGDAT")
	private Date tGDat;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TGBATDAU")
	private Date tGBatDau;
	
	@Column(name = "DIADIEM")
	private String diaDiem;
	
	@Column(name = "TONGTIEN")
	private int tongTien;
	
	@OneToMany(mappedBy = "donHang", fetch = FetchType.EAGER)
	
	public int getMaDH() {
		return maDH;
	}

	public void setMaDH(int maDH) {
		this.maDH = maDH;
	}

	public KhachHang getKhachHang() {
		return khachHang;
	}

	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}
	

	public Date gettGDat() {
		return tGDat;
	}

	public void settGDat(Date tGDat) {
		this.tGDat = tGDat;
	}

	public Date gettGBatDau() {
		return tGBatDau;
	}

	public void settGBatDau(Date tGBatDau) {
		this.tGBatDau = tGBatDau;
	}

	public String getDiaDiem() {
		return diaDiem;
	}

	public void setDiaDiem(String diaDiem) {
		this.diaDiem = diaDiem;
	}

	public int getTongTien() {
		return tongTien;
	}

	public void setTongTien(int tongTien) {
		this.tongTien = tongTien;
	}


	public DonHang() {
		super();
	}

	public DonHang(int maDH) {
		this.maDH = maDH;
	}

	@Override
	public String toString() {
		return "{" +
				"'maDH':" + maDH +
				", 'khachHang':" + khachHang.toString() +
				", 'tGDat':" + tGDat +
				", 'tGBatDau':" + tGBatDau +
				", 'diaDiem':'" + diaDiem + '\'' +
				", 'tongTien':" + tongTien +
				'}';
	}
}
