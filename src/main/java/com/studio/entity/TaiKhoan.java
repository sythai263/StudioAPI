package com.studio.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TAIKHOAN")
public class TaiKhoan {
	
	@Id
	@Column(name = "SDT")
	@NotNull(message = "Không để trống số điện thoại.")
	private String sdt;
	
	
	@Column(name ="MATKHAU")
	@NotNull(message = "Không để trống mật khẩu.")
	private String matKhau;
	
	@Column(name ="BIKHOA")
	private boolean biKhoa;
	
	@Column(name = "ROLE")
	private String role;
	
	@OneToOne(mappedBy = "taiKhoan",fetch = FetchType.LAZY)

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

	public boolean isBiKhoa() {
		return biKhoa;
	}

	public void setBiKhoa(boolean biKhoa) {
		this.biKhoa = biKhoa;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public TaiKhoan(String sdt, String matKhau) {
		super();
		this.sdt = sdt;
		this.matKhau = matKhau;
	}
	public TaiKhoan(String sdt) {
		super();
		this.sdt = sdt;
	}

	public TaiKhoan() {
		super();
	}


}
