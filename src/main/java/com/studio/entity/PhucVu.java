package com.studio.entity;

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
@Table(name = "PHUCVU")
public class PhucVu {

	@Id
	@Column(name = "MAPV")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int maPV;

	@ManyToOne
	@JoinColumn(name = "MACT")
	private ChiTietDonHang chiTiet;

	@ManyToOne
	@JoinColumn(name = "MANV")
	private NhanVien nhanVien;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TGCHECKIN")
	private Date tGCheckIn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TGCHECKOUT")
	private Date tGCheckOut;

	@Column(name = "ANHCHECKIN")
	private String anhCheckIn;

	@Column(name = "ANHCHECKOUT")
	private String anhCheckOut;
	
	@Column(name = "HOAHONG")
	private int hoaHong;
	

	@OneToMany(mappedBy = "phucVu", fetch = FetchType.EAGER)

	public int getMaPV() {
		return maPV;
	}

	public void setMaPV(int maPV) {
		this.maPV = maPV;
	}


	public ChiTietDonHang getChiTiet() {
		return chiTiet;
	}

	public void setChiTiet(ChiTietDonHang chiTiet) {
		this.chiTiet = chiTiet;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	public Date gettGCheckIn() {
		return tGCheckIn;
	}

	public void settGCheckIn(Date tGCheckIn) {
		this.tGCheckIn = tGCheckIn;
	}

	public Date gettGCheckOut() {
		return tGCheckOut;
	}

	public void settGCheckOut(Date tGCheckOut) {
		this.tGCheckOut = tGCheckOut;
	}

	public String getAnhCheckIn() {
		return anhCheckIn;
	}

	public void setAnhCheckIn(String anhCheckIn) {
		this.anhCheckIn = anhCheckIn;
	}

	public String getAnhCheckOut() {
		return anhCheckOut;
	}

	public void setAnhCheckOut(String anhCheckOut) {
		this.anhCheckOut = anhCheckOut;
	}

	
	public int getHoaHong() {
		return hoaHong;
	}

	public void setHoaHong(int hoaHong) {
		this.hoaHong = hoaHong;
	}

	public PhucVu() {
		super();
	}

	public PhucVu(int maPV) {
		this.maPV = maPV;
	}
}


