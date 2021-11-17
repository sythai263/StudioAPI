package com.studio.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "VIPHAM")
public class ViPham {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MAVP")
	private int maVP;
	
	@ManyToOne
	@JoinColumn(name = "MANV")
	private NhanVien nhanVien;
	
	@ManyToOne
	@JoinColumn(name = "MANQ")
	private NoiQuy noiQuy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TGIAN")
	private Date thoiGian;

	public int getMaVP() {
		return maVP;
	}

	public void setMaVP(int maVP) {
		this.maVP = maVP;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	public NoiQuy getNoiQuy() {
		return noiQuy;
	}

	public void setNoiQuy(NoiQuy noiQuy) {
		this.noiQuy = noiQuy;
	}

	public Date getThoiGian() {
		return thoiGian;
	}

	public void setThoiGian(Date thoiGian) {
		this.thoiGian = thoiGian;
	}

	public ViPham() {
		super();
	}

}
