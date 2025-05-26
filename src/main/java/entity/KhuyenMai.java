package entity;

import java.time.LocalDateTime;


public class KhuyenMai {
    private String maKM;
    private String dieuKien;
    private LocalDateTime ngayKetThuc;
    private LocalDateTime ngayBatDau;
    private double phanTramGiamGia;
    public KhuyenMai() {
    	
    }
    public KhuyenMai(String maKH, String dieuKien, LocalDateTime ngayKetThuc, LocalDateTime ngayBatDau, double phanTramGiamGia) {
    	this.maKM = maKH;
    	this.dieuKien = dieuKien;
    	this.ngayKetThuc = ngayKetThuc;
    	this.ngayBatDau = ngayBatDau;
    	this.phanTramGiamGia = phanTramGiamGia;
    }
	public String getMaKM() {
		return maKM;
	}
	public void setMaKM(String maKM) {
		this.maKM = maKM;
	}
	public String getDieuKien() {
		return dieuKien;
	}
	public void setDieuKien(String dieuKien) {
		this.dieuKien = dieuKien;
	}
	public LocalDateTime getngayKetThuc() {
		return ngayKetThuc;
	}
	public void setngayKetThuc(LocalDateTime ngayKetThuc) {
		this.ngayKetThuc = ngayKetThuc;
	}
	public LocalDateTime getNgayBatDau() {
		return ngayBatDau;
	}
	public void setNgayBatDau(LocalDateTime ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}
	public double getPhanTramGiamGia() {
		return phanTramGiamGia;
	}
	public void setPhanTramGiamGia(double phanTramGiamGia) {
		this.phanTramGiamGia = phanTramGiamGia;
	}


}