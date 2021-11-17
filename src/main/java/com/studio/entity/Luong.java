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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "LUONG")
public class Luong {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MALUONG")
	private int maLuong;
	
	@ManyToOne
	@JoinColumn(name = "MANV")
	private NhanVien nhanVien;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "NGAYTK")
	private Date ngayTK;
	
	@Column(name = "MOTA")
	private String moTa;
	
	@Min(value = 0, message = "Lương cơ bản phải là số dương.")
	@NotBlank(message = "Lương cơ bản không để trống")
	@Column(name = "LUONGCUNG")
	private int luongCung;
	
	@Min(value = 0, message = "Tiền hoa hồng phải là số dương.")
	@Column(name = "HOAHONG")
	private int hoaHong;
	
	@Min(value = 0, message = "Tiền vi phạm phải là số dương.")
	@Column(name = "VIPHAM")
	private int viPham;
	
	@Min(value = 0, message = "Lương thực nhận phải là số dương.")
	@NotBlank(message = "Lương thực nhận không để trống")
	@Column(name = "THUCNHAN")
	private int thucNhan;

	@Column(name = "THANHTOAN")
	private boolean thanhToan;

	public int getMaLuong() {
		return maLuong;
	}

	public void setMaLuong(int maLuong) {
		this.maLuong = maLuong;
	}

	public boolean isThanhToan() {
		return thanhToan;
	}

	public void setThanhToan(boolean thanhToan) {
		this.thanhToan = thanhToan;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	public Date getNgayTK() {
		return ngayTK;
	}

	public void setNgayTK(Date ngayTK) {
		this.ngayTK = ngayTK;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public int getLuongCung() {
		return luongCung;
	}

	public void setLuongCung(int luongCung) {
		this.luongCung = luongCung;
	}

	public int getHoaHong() {
		return hoaHong;
	}

	public void setHoaHong(int hoaHong) {
		this.hoaHong = hoaHong;
	}

	public int getViPham() {
		return viPham;
	}

	public void setViPham(int viPham) {
		this.viPham = viPham;
	}

	public int getThucNhan() {
		return thucNhan;
	}

	public void setThucNhan(int thucNhan) {
		this.thucNhan = thucNhan;
	}

	public Luong() {
		super();
	}

	public Luong(int maLuong, NhanVien nhanVien, Date ngayTK, String moTa, int luongCung, int hoaHong, int viPham, int thucNhan) {
		this.maLuong = maLuong;
		this.nhanVien = nhanVien;
		this.ngayTK = ngayTK;
		this.moTa = moTa;
		this.luongCung = luongCung;
		this.hoaHong = hoaHong;
		this.viPham = viPham;
		this.thucNhan = thucNhan;
	}

	public Luong(int maLuong) {
		this.maLuong = maLuong;
	}
}
