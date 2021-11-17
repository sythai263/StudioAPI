package com.studio.entity;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "KHACHHANG")
public class KhachHang {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MAKH")
	private int maKH;
	
	@Column(name = "HOTEN")
	private String hoTen;
	
	@OneToOne
	@JoinColumn(name = "SDT", nullable = false)
	private TaiKhoan taiKhoan;
	
	@Column(name = "FACEBOOK")
	private String facebook;
	
	@Column(name = "DIACHI")
	private String diaChi;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "NGAYSINH")
	private Date ngaySinh;
	
	@Column(name = "AVATAR")
	private String avatar;

	@OneToMany(mappedBy = "khachHang", fetch = FetchType.EAGER)
	
	
	public int getMaKH() {
		return maKH;
	}

	public void setMaKH(int maKH) {
		this.maKH = maKH;
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

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public Date getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(Date ngaySinh) {
		this.ngaySinh = ngaySinh;
	}
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	


	public KhachHang(int maKH) {
		super();
		this.maKH = maKH;
	}

	public KhachHang() {
		super();
	}

}
