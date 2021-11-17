package com.studio.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "CTDH")
public class ChiTietDonHang {
	
	@Id 
	@GeneratedValue
	@Column(name = "MACT")
	private int maCT;
	
	@ManyToOne
	@JoinColumn(name = "MADH")
	private DonHang donHang;
	
	@ManyToOne
	@JoinColumn(name = "MAGIA")
	private BangGia bangGia;
	
	@OneToMany(mappedBy = "chiTiet", fetch = FetchType.EAGER)
	

	public int getMaCT() {
		return maCT;
	}

	public void setMaCT(int maCT) {
		this.maCT = maCT;
	}

	public DonHang getDonHang() {
		return donHang;
	}

	public void setDonHang(DonHang donHang) {
		this.donHang = donHang;
	}

	public BangGia getBangGia() {
		return bangGia;
	}

	public void setBangGia(BangGia bangGia) {
		this.bangGia = bangGia;
	}

	public ChiTietDonHang() {
		super();
	}

	public ChiTietDonHang(int maCT) {
		super();
		this.maCT = maCT;
	}
	


}
