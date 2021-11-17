package com.studio.entity;


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
import javax.validation.constraints.Min;

@Entity
@Table(name = "BANGGIA")
public class BangGia {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MAGIA")
	private int maGia;
	
	@ManyToOne
	@JoinColumn(name = "MADV")
	private DichVu dichVu;
	
	@Min(value = 1, message = "Tối thiểu 1 nhân viên thực hiện 1 dịch vụ.")
	@Column(name = "SLNV")
	private int sLNV;
	
	@Min(value = 1, message = "Tối thiểu 1 nhân viên thực hiện 1 dịch vụ.")
	@Column(name = "GIA")
	private int gia;
	
	@OneToMany(mappedBy = "bangGia", fetch = FetchType.EAGER)

	public int getMaGia() {
		return maGia;
	}

	public void setMaGia(int maGia) {
		this.maGia = maGia;
	}

	public DichVu getDichVu() {
		return dichVu;
	}

	public void setDichVu(DichVu dichVu) {
		this.dichVu = dichVu;
	}

	public int getsLNV() {
		return sLNV;
	}

	public void setsLNV(int sLNV) {
		this.sLNV = sLNV;
	}

	public int getGia() {
		return gia;
	}

	public void setGia(int gia) {
		this.gia = gia;
	}

	public BangGia() {
		super();
	}

	public BangGia(int maGia) {
		super();
		this.maGia = maGia;
	}

}
