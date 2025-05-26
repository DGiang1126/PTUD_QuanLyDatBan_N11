package entity;

import java.time.LocalDateTime;

public class HoaDon {
    private String maHD;
    private NhanVien nhanVien;
    private KhachHang khachHang;
    private PhuongThucThanhToan PTTT;
    private ThueVAT thueVAT;
    private KhuyenMai khuyenMai;
    private LocalDateTime ngayLap;
    private LocalDateTime ngayXuat;
    private String trangThai;
    private PhieuDatBan ban; // Thêm từ PhieuDatBan
	private double tongTien;


    public HoaDon() {
    	
    }
    public HoaDon(String maHD, NhanVien nhanVien, KhachHang khachHang, PhuongThucThanhToan PTTT, ThueVAT thueVAT, KhuyenMai khuyenMai, LocalDateTime ngayLap, LocalDateTime ngayXuat, String trangThai, PhieuDatBan ban) {
    	this.maHD = maHD;
    	this.nhanVien = nhanVien;
    	this.khachHang = khachHang;
    	this.PTTT = PTTT;
    	this.thueVAT = thueVAT;
    	this.khuyenMai = khuyenMai;
    	this.ngayLap = ngayLap;
    	this.ngayXuat = ngayXuat;
    	this.trangThai = trangThai;
    	this.ban=ban;
    }
    
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	public String getMaHD() {
		return maHD;
	}
	public void setMaHD(String maHD) {
		this.maHD = maHD;
	}
	public NhanVien getNhanVien() {
		return nhanVien;
	}
	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}
	public KhachHang getKhachHang() {
		return khachHang;
	}
	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}
	public PhuongThucThanhToan getPTTT() {
		return PTTT;
	}
	public void setPTTT(PhuongThucThanhToan pTTT) {
		PTTT = pTTT;
	}
	public ThueVAT getThueVAT() {
		return thueVAT;
	}
	public void setThueVAT(ThueVAT thueVAT) {
		this.thueVAT = thueVAT;
	}
	public KhuyenMai getKhuyenMai() {
		return khuyenMai;
	}
	public void setKhuyenMai(KhuyenMai khuyenMai) {
		this.khuyenMai = khuyenMai;
	}
	public LocalDateTime getNgayLap() {
		return ngayLap;
	}
	public void setNgayLap(LocalDateTime ngayLap) {
		this.ngayLap = ngayLap;
	}
	public LocalDateTime getNgayXuat() {
		return ngayXuat;
	}
	public void setNgayXuat(LocalDateTime ngayXuat) {
		this.ngayXuat = ngayXuat;
	}

	public double getTongTien() {
		return tongTien;
	}
	public void setTongTien(double tongTien) {
		this.tongTien = tongTien;
	}
	public PhieuDatBan getBan() {
		return ban;
	}
	public void setBan(PhieuDatBan phieuDatBan) {
		this.ban = phieuDatBan;
	}




}