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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "NHANVIEN")
public class NhanVien {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MANV")
	private int maNV;
	
	
	@Column(name = "HOTEN")
	@NotBlank(message = "Không để trống họ tên")
	private String hoTen;
	
	@OneToOne
	@JoinColumn(name = "SDT")
	private TaiKhoan taiKhoan;
	
	@ManyToOne
	@JoinColumn(name = "MACM")
	private ChuyenMon chuyenMon;
	
	@Column(name = "FACEBOOK")
	private String facebook;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "NGAYSINH")
	private Date ngaySinh;
	
	
	@Column(name = "AVATAR")
	private String avatar;

	@Column(name="KINHDO")
	private double kinhDo;
	
	@Column(name="VIDO")
	private double viDo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CAPNHAT")
	private Date capNhat;

	public double getKinhDo() {
		return kinhDo;
	}

	public void setKinhDo(double kinhDo) {
		this.kinhDo = kinhDo;
	}

	public double getViDo() {
		return viDo;
	}

	public void setViDo(double viDo) {
		this.viDo = viDo;
	}

	public Date getCapNhat() {
		return capNhat;
	}

	public void setCapNhat(Date capNhat) {
		this.capNhat = capNhat;
	}

	public int getMaNV() {
		return maNV;
	}

	public void setMaNV(int maNV) {
		this.maNV = maNV;
	}

	public String getHoTen() {
		return hoTen;
	}

	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}

	public TaiKhoan getTaiKhoan() {
		return taiKhoan;
	}

	public void setTaiKhoan(TaiKhoan taiKhoan) {
		this.taiKhoan = taiKhoan;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}


	public Date getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(Date ngaySinh) {
		this.ngaySinh = ngaySinh;
	}


	public ChuyenMon getChuyenMon() {
		return chuyenMon;
	}

	public void setChuyenMon(ChuyenMon chuyenMon) {
		this.chuyenMon = chuyenMon;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public NhanVien(int maNV) {
		super();
		this.maNV = maNV;
	}

	public NhanVien() {
		super();
	}

}
