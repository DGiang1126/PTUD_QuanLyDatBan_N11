package entity;

public class CTHoaDon {
    private String maHoaDon;
    private String maMonAn;
    private int soLuong;
    private double donGia;
    private double thanhTien; // Thêm từ sp_GetChiTietHoaDonByMaHD

    public CTHoaDon() {}

    public CTHoaDon(String maHoaDon, String maMonAn, int soLuong, double donGia, double thanhTien) {
        this.maHoaDon = maHoaDon;
        this.maMonAn = maMonAn;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.setThanhTien(thanhTien);
    }

	public String getMaHoaDon() {
		return maHoaDon;
	}
	public void setMaHoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}
	public String getMaMonAn() {
		return maMonAn;
	}
	public void setMaMonAn(String maMonAn) {
		this.maMonAn = maMonAn;
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	public double getDonGia() {
		return donGia;
	}
	public void setDonGia(double donGia) {
		this.donGia = donGia;
	}

	public double getThanhTien() {
		return thanhTien;
	}

	public void setThanhTien(double thanhTien) {
		this.thanhTien = thanhTien;
	}
    
}