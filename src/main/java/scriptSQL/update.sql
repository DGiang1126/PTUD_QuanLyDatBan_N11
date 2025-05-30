CREATE DATABASE QuanLyDatBan;
USE QuanLyDatBan;

-- Tạo các bảng không có khóa ngoại trước
CREATE TABLE KhachHang (
    MaKH VARCHAR(10) PRIMARY KEY,
    TenKH NVARCHAR(50) NOT NULL,
    SDT VARCHAR(11),
    GioiTinh NVARCHAR(10)
);

CREATE TABLE ThueVAT (
    MaVAT VARCHAR(10) PRIMARY KEY,
    PhanTramThue DECIMAL(5, 2) NOT NULL,
    CONSTRAINT CHK_PhanTramThue CHECK (PhanTramThue >= 0 AND PhanTramThue <= 100)
);

CREATE TABLE PhuongThucThanhToan (
    MaPTTT VARCHAR(10) PRIMARY KEY,
    PhuongThuc NVARCHAR(100) NOT NULL
);

CREATE TABLE KhuyenMai (
    MaKM VARCHAR(10) PRIMARY KEY,
    DieuKien NVARCHAR(255) NOT NULL,
    Ngayketthuc DATETIME,
    Ngaybatdau DATETIME,
    PhanTramGiamGia DECIMAL(5, 2) NOT NULL,
    CONSTRAINT PhanTramGiamGia CHECK (PhanTramGiamGia >= 0 AND PhanTramGiamGia <= 100)
);

CREATE TABLE NhanVien (
    MaNV VARCHAR(10) PRIMARY KEY,
    TenNV NVARCHAR(50) NOT NULL,
    SDT VARCHAR(11),
    GioiTinh NVARCHAR(10),
    ChucVu NVARCHAR(50),
    Tuoi INT,
    Hesoluong FLOAT,
    LuongNV FLOAT,
	LinkIMG VARCHAR(100)
);
ALTER TABLE NhanVien
ADD Email VARCHAR(50);

CREATE TABLE Ban (
    MaBan VARCHAR(10) PRIMARY KEY,
    TrangThai NVARCHAR(50) NOT NULL,
    Tang INT,
    SoCho INT,
    GhiChu NVARCHAR(MAX),
	CONSTRAINT CHK_TrangThai CHECK (TrangThai IN (N'CÒN TRỐNG', N'ĐÃ ĐẶT', N'ĐANG SỬ DỤNG', N'BẢO TRÌ'))
);


ALTER TABLE Ban
ADD TenBan NVARCHAR(20);



CREATE TABLE MonAn (
    MaMonAn VARCHAR(10) PRIMARY KEY,
    TenMonAn NVARCHAR(50) NOT NULL,
    Gia FLOAT,
    GhiChu NVARCHAR(MAX),
    LoaiMonAn NVARCHAR(20),
    DuongDanHinhAnh VARCHAR(255)
);

CREATE TABLE PhieuDatBan (
    MaPDB VARCHAR(10) PRIMARY KEY,
    MaKhachHang VARCHAR(10) NOT NULL,
    MaBan VARCHAR(10) NOT NULL,
    MaNV VARCHAR(10) NOT NULL,
    FOREIGN KEY (MaKhachHang) REFERENCES KhachHang(MaKH),
    FOREIGN KEY (MaBan) REFERENCES Ban(MaBan),
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
);

CREATE TABLE ChiTietPhieuDatBan (
    MaPDB VARCHAR(10) NOT NULL,
    TimeNhanBan DATETIME,
    TimeTraBan DATETIME,
    SoNguoi INT,
    FOREIGN KEY (MaPDB) REFERENCES PhieuDatBan(MaPDB)
);
-- Tạo các bảng có khóa ngoại sau

CREATE TABLE TaiKhoan (
    MaNV VARCHAR(10) PRIMARY KEY,
    TenDangNhap VARCHAR(10) NOT NULL UNIQUE,
    MatKhau VARCHAR(20) NOT NULL,
    TrangThai NVARCHAR(50),
    GioVaoLam DATETIME NULL,
    GioNghi DATETIME NULL,
    SoGioLam FLOAT NOT NULL DEFAULT 0,
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
);


CREATE TABLE HoaDon (
    MaHD VARCHAR(10) PRIMARY KEY,
    MaNV VARCHAR(10) NOT NULL,
    MaKH VARCHAR(10) NOT NULL,
    MaPTTT VARCHAR(10) NOT NULL,
    MaVAT VARCHAR(10) NOT NULL,
    MaKM VARCHAR(10) NOT NULL,
    NgayLap DATETIME,
    NgayXuat DATETIME,
	TrangThai NVARCHAR(50) NOT NULL,
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV),
    FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH),
    FOREIGN KEY (MaPTTT) REFERENCES PhuongThucThanhToan(MaPTTT),
    FOREIGN KEY (MaVAT) REFERENCES ThueVAT(MaVAT),
    FOREIGN KEY (MaKM) REFERENCES KhuyenMai(MaKM)
);

CREATE TABLE ChiTietHoaDon (
    MaHD VARCHAR(10) NOT NULL,
    MaMonAn VARCHAR(10) NOT NULL,
    SoLuong INT NOT NULL,
    DonGia FLOAT NOT NULL,
    FOREIGN KEY (MaHD) REFERENCES HoaDon(MaHD),
    FOREIGN KEY (MaMonAn) REFERENCES MonAn(MaMonAn)
);

-- THÊM DỮ LIỆU
INSERT INTO ThueVAT (MaVAT, PhanTramThue) VALUES
('VAT01', 10.00),
('VAT02', 8.00);

INSERT INTO PhuongThucThanhToan (MaPTTT, PhuongThuc) VALUES
('PTTT01', N'Tiền mặt'),
('PTTT03', N'Chuyển khoản');

INSERT INTO KhuyenMai (MaKM, DieuKien, NgayKetThuc, NgayBatDau, PhanTramGiamGia) VALUES
('KM01', N'Giảm giá cho hóa đơn trên 500,000', '2025-12-31 23:59:59', '2025-04-01 00:00:00', 10.00),
('KM02', N'Giảm giá cho khách hàng thành viên', '2025-06-30 23:59:59', '2025-04-01 00:00:00', 15.00);

INSERT INTO KhachHang (MaKH, TenKH, SDT, GioiTinh) VALUES
('KH001', N'Nguyễn Văn An', '0912345678', N'Nam'),
('KH002', N'Trần Thị Bình', '0987654321', N'Nữ'),
('KH003', N'Lê Minh Châu', '0901234567', N'Nữ');

INSERT INTO NhanVien (MaNV, TenNV, SDT, GioiTinh, ChucVu, Tuoi, Hesoluong, LuongNV, LinkIMG) VALUES
('NV001', N'Nguyễn Tân', '0935123456', N'Nam', N'Nhân viên quản lý', 21, 30000, 10000000, null),
('NV002', N'Đỗ Đông Giang', '0978123456', N'Nam', N'Nhân viên quản lý', 21, 30000, 8000000, null),
('NV003', N'Trần Minh Tín', '0967123456', N'Nam', N'Nhân viên quản lý', 21, 30000, 10000000, null);

INSERT INTO TaiKhoan (MaNV, TenDangNhap, MatKhau, GioVaoLam, GioNghi, SoGioLam, TrangThai)
VALUES 
    ('NV001', 'NV001', 'password', '2025-01-01 08:00:00', '2025-01-01 16:00:00', 8, N'Offline'),
    ('NV002', 'NV002', 'password', '2025-01-01 08:00:00', '2025-01-01 16:00:00', 8, N'Offline'),
    ('NV003', 'NV003', 'password', '2025-01-01 08:00:00', '2025-01-01 16:00:00', 8, N'Offline');


INSERT INTO Ban (MaBan, TenBan, TrangThai, Tang, SoCho, GhiChu) VALUES
('B001', N'Bàn 1', N'CÒN TRỐNG', 1, 4, N'Không có'),
('B002', N'Bàn 2', N'CÒN TRỐNG', 1, 4, N'Không có'),
('B003', N'Bàn 3', N'CÒN TRỐNG', 1, 4, N'Không có'),
('B004', N'Bàn 4', N'CÒN TRỐNG', 1, 4, N'Không có'),
('B005', N'Bàn 5', N'CÒN TRỐNG', 1, 4, N'Không có'),
('B006', N'Bàn 6', N'CÒN TRỐNG', 1, 4, N'Không có');


INSERT INTO MonAn (MaMonAn, TenMonAn, Gia, GhiChu, LoaiMonAn, DuongDanHinhAnh)
VALUES 
    ('MA001', N'Ba chỉ bò Mỹ nướng', 150000, N'Ba chỉ bò Mỹ nướng với gia vị đặc biệt, thơm ngon', N'Đồ nướng', '/img/monan1.jpg'),
    ('MA002', N'Sườn non heo nướng', 120000, N'Sườn non heo ướp mật ong, nướng vàng đậm', N'Đồ nướng', '/img/monan2.jpg'),
    ('MA003', N'Bạch tuộc nướng sa tế', 180000, N'Bạch tuộc tươi, nướng sa tế cay nồng', N'Đồ nướng', '/img/monan3.jpg'),
    ('MA004', N'Lưỡi bò nướng', 200000, N'Lưỡi bò nướng mềm, thơm, ăn kèm nước chấm', N'Đồ nướng', '/img/monan4.jpg'),
    ('MA005', N'Tôm sú nướng muối ớt', 170000, N'Tôm sú tươi, nướng muối ớt đậm đà', N'Đồ nướng', '/img/monan5.jpg'),
    ('MA006', N'Gà nướng mật ong', 130000, N'Gà ướp mật ong, nướng vàng, ngọt tự nhiên', N'Đồ nướng', '/img/monan6.jpg'),
    ('MA007', N'Cá kèo nướng', 90000, N'Cá kèo nướng trên than hoa, thơm lừng', N'Đồ nướng', '/img/monan7.jpg'),
    ('MA008', N'Nấm kim châm cuộn ba chỉ bò', 110000, N'Nấm kim châm cuộn ba chỉ bò, nướng giòn', N'Đồ nướng', '/img/monan8.jpg'),
    ('MA009', N'Coca-Cola', 20000, N'Nước ngọt có ga Coca-Cola', N'Đồ uống', '/img/douong1.jpg'),
    ('MA010', N'Pepsi', 20000, N'Nước ngọt có ga Pepsi', N'Đồ uống', '/img/douong2.jpg'),
    ('MA011', N'7 Up', 20000, N'Nước ngọt có ga 7 Up', N'Đồ uống', '/img/douong3.jpg'),
    ('MA012', N'Nước suối', 10000, N'Nước suối tinh khiết', N'Đồ uống', '/img/douong4.jpg'),
    ('MA013', N'Trà đá', 5000, N'Trà đá mát lạnh', N'Đồ uống', '/img/douong5.jpg'),
    ('MA014', N'Đậu que luộc', 30000, N'Đậu que luộc chấm kho quẹt', N'Món ăn kèm', '/img/ankem1.jpg'),
    ('MA015', N'Rau muống xào tỏi', 40000, N'Rau muống xào tỏi thơm ngon', N'Món ăn kèm', '/img/ankem2.jpg'),
    ('MA016', N'Gỏi xoài tôm khô', 50000, N'Gỏi xoài chua ngọt với tôm khô', N'Món ăn kèm', '/img/ankem3.jpg'),
    ('MA017', N'Kim chi', 35000, N'Kim chi cải thảo cay nồng', N'Món ăn kèm', '/img/ankem4.jpg'),
    ('MA018', N'Khoai tây chiên', 45000, N'Khoai tây chiên giòn rụm', N'Món ăn kèm', '/img/ankem5.jpg');


-- Stored Procedure SQL
CREATE PROCEDURE ThemHoaDon
    @MaNV VARCHAR(10),
    @MaKH VARCHAR(10),
    @MaPTTT VARCHAR(10),
    @MaVAT VARCHAR(10),
    @MaKM VARCHAR(10),
    @NgayLap DATETIME,
    @NgayXuat DATETIME,
    @TrangThai NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @NewMaHD NVARCHAR(10);
    DECLARE @MaxMaHD NVARCHAR(10);
    DECLARE @NextNumber INT;

    -- Lấy mã hóa đơn lớn nhất hiện tại
    SELECT @MaxMaHD = MAX(MaHD) FROM HoaDon;

    IF @MaxMaHD IS NULL
    BEGIN
        -- Nếu chưa có hóa đơn nào, bắt đầu với HD000001
        SET @NewMaHD = 'HD000001';
    END
    ELSE
    BEGIN
        -- Trích xuất phần số, tăng thêm 1 và tạo mã hóa đơn mới
        SET @NextNumber = CAST(SUBSTRING(@MaxMaHD, 3, LEN(@MaxMaHD) - 2) AS INT) + 1;
        SET @NewMaHD = 'HD' + RIGHT('000000' + CAST(@NextNumber AS NVARCHAR(6)), 6);
    END

    -- Thêm dữ liệu mới vào bảng HoaDon
    INSERT INTO HoaDon (
        MaHD, MaNV, MaKH, MaPTTT, MaVAT, MaKM, NgayLap, NgayXuat, TrangThai
    )
    VALUES (
        @NewMaHD, @MaNV, @MaKH, @MaPTTT, @MaVAT, @MaKM, @NgayLap, @NgayXuat, @TrangThai
    );
END;


--KHuyến Mãi
CREATE PROCEDURE sp_AddPromotion
    @PromotionId VARCHAR(10),
    @Condition NVARCHAR(255),
    @StartDate DATETIME,
    @EndDate DATETIME,
    @DiscountPercentage Decimal(5,2)
AS
BEGIN
    INSERT INTO KhuyenMai (MaKM, DieuKien, NgayBatDau, NgayKetThuc, PhanTramGiamGia)
    VALUES (@PromotionId, @Condition, @StartDate, @EndDate, @DiscountPercentage);
END;

-- 2. Update promotion
CREATE PROCEDURE sp_UpdatePromotion
    @PromotionId VARCHAR(10),
    @Condition NVARCHAR(255),
    @StartDate DATETIME,
    @EndDate DATETIME,
    @DiscountPercentage Decimal(5,2)
AS
BEGIN
    UPDATE KhuyenMai
    SET DieuKien = @Condition,
        NgayBatDau = @StartDate,
        NgayKetThuc = @EndDate,
        PhanTramGiamGia = @DiscountPercentage
    WHERE MaKM = @PromotionId;
END;

-- 3. Delete promotion
CREATE PROCEDURE sp_DeletePromotion
    @PromotionId VARCHAR(10)
AS
BEGIN
    DELETE FROM KhuyenMai
    WHERE MaKM = @PromotionId;
END;

-- 4. Get list of promotions
CREATE PROCEDURE sp_GetAllPromotions
AS
BEGIN
    SELECT * FROM KhuyenMai;
END;

-- 5. Get promotion by ID

CREATE PROCEDURE sp_GetPromotionById
    @PromotionId VARCHAR(10)
AS
BEGIN
    SELECT MaKM ,DieuKien ,Ngayketthuc ,Ngaybatdau ,PhanTramGiamGia
	FROM KhuyenMai
    WHERE MaKM LIKE  @PromotionId;
END;

--Nhân Viên

go

CREATE PROCEDURE GetAllNhanVien

AS
BEGIN	

    SELECT MaNV, TenNV, SDT, GioiTinh, ChucVu, Tuoi, Hesoluong , LuongNV , LinkIMG , Email FROM NhanVien;
END;
CREATE PROCEDURE InsertNhanVien (
    @MaNV NVARCHAR(10),
    @TenNV NVARCHAR(50),
    @SDT NVARCHAR(15),
    @GioiTinh NVARCHAR(10),
    @ChucVu NVARCHAR(50),
    @Tuoi INT,	
    @Hesoluong FLOAT,
    @LuongNV FLOAT,
    @LinkIMG NVARCHAR(255),
    @Email NVARCHAR(50)
)
AS
BEGIN
    INSERT INTO NhanVien(MaNV, TenNV, SDT, GioiTinh, ChucVu, Tuoi, Hesoluong, LuongNV, LinkIMG, Email)
    VALUES(@MaNV, @TenNV, @SDT, @GioiTinh, @ChucVu, @Tuoi, @Hesoluong, @LuongNV, @LinkIMG, @Email);
END;
GO

CREATE PROCEDURE UpdateNhanVien (
    @TenNV NVARCHAR(50),
    @SDT NVARCHAR(15),
    @GioiTinh NVARCHAR(10),
    @ChucVu NVARCHAR(50),
    @Tuoi INT,
    @Hesoluong FLOAT,
    @LuongNV FLOAT,
    @LinkIMG NVARCHAR(255),
    @Email NVARCHAR(50),
    @MaNV NVARCHAR(10)
)
AS
BEGIN
    UPDATE NhanVien
    SET TenNV = @TenNV,
        SDT = @SDT,
        GioiTinh = @GioiTinh,
        ChucVu = @ChucVu,
        Tuoi = @Tuoi,
        Hesoluong = @Hesoluong,
        LuongNV = @LuongNV,
        LinkIMG = @LinkIMG,
        Email = @Email
    WHERE MaNV = @MaNV;
END;
GO

GO

-- Stored procedure để xóa một nhân viên theo mã NV
CREATE PROCEDURE DeleteNhanVien (
    @MaNV NVARCHAR(10)
)
AS
BEGIN
    DELETE FROM NhanVien WHERE MaNV = @MaNV;
END;
GO


CREATE PROCEDURE SearchNhanVien
    @Keyword NVARCHAR(50)
AS
BEGIN
    SELECT MaNV, TenNV, SDT, GioiTinh, ChucVu, Tuoi, Hesoluong, LuongNV, LinkIMG, Email
    FROM NhanVien
    WHERE MaNV LIKE  @Keyword 
       OR TenNV LIKE  @Keyword 
       OR SDT LIKE  @Keyword 
       OR Email LIKE  @Keyword ;
END;
GO

---HÓA ĐƠN TÍN

CREATE PROCEDURE sp_GetAllHoaDon
AS
BEGIN
    SELECT 
        hd.MaHD, 
        hd.MaNV, 
        hd.MaKH, 
        hd.MaPTTT, 
        hd.MaVAT, 
        hd.MaKM, 
        hd.NgayLap, 
        hd.NgayXuat, 
        hd.TrangThai,
        b.MaBan,
        kh.SDT,
        COALESCE(SUM(ct.SoLuong * ct.DonGia), 0) AS TongTien
    FROM HoaDon hd
    LEFT JOIN KhachHang kh ON hd.MaKH = kh.MaKH
    LEFT JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD
    LEFT JOIN ChiTietBan cb ON hd.MaHD = cb.MaHD
    LEFT JOIN Ban b ON cb.MaBan = b.MaBan
    GROUP BY 
        hd.MaHD, hd.MaNV, hd.MaKH, hd.MaPTTT, hd.MaVAT, hd.MaKM, 
        hd.NgayLap, hd.NgayXuat, hd.TrangThai, b.MaBan, kh.SDT
    ORDER BY 
        hd.NgayLap DESC;
END;

CREATE PROCEDURE sp_SearchHoaDon
    @MaHD VARCHAR(10) = NULL,
    @MaKH VARCHAR(10) = NULL,
    @MaBan VARCHAR(10) = NULL,
    @SDT VARCHAR(15) = NULL,
    @TrangThai NVARCHAR(50) = NULL,
    @StartDate DATETIME = NULL,
    @EndDate DATETIME = NULL,
    @SortByGia VARCHAR(10) = NULL
AS
BEGIN
    SELECT 
        hd.MaHD, 
        hd.MaNV, 
        hd.MaKH, 
        hd.MaPTTT, 
        hd.MaVAT, 
        hd.MaKM, 
        hd.NgayLap, 
        hd.NgayXuat, 
        hd.TrangThai,
        b.MaBan,
        kh.SDT,
        SUM(ct.SoLuong * ct.DonGia) AS TongTien
    FROM HoaDon hd
    LEFT JOIN KhachHang kh ON hd.MaKH = kh.MaKH
    LEFT JOIN ChiTietHoaDon ct ON hd.MaHD = ct.MaHD
    LEFT JOIN ChiTietBan cb ON hd.MaHD = cb.MaHD
    LEFT JOIN Ban b ON cb.MaBan = b.MaBan
    WHERE 
        (@MaHD IS NULL OR hd.MaHD LIKE '%' + @MaHD + '%')
        AND (@MaKH IS NULL OR hd.MaKH LIKE '%' + @MaKH + '%')
        AND (@MaBan IS NULL OR b.MaBan LIKE '%' + @MaBan + '%')
        AND (@SDT IS NULL OR kh.SDT LIKE '%' + @SDT + '%')
        AND (@TrangThai IS NULL OR @TrangThai = 'Tất cả' OR hd.TrangThai = @TrangThai)
        AND (@StartDate IS NULL OR hd.NgayLap >= @StartDate)
        AND (@EndDate IS NULL OR hd.NgayLap <= @EndDate)
    GROUP BY 
        hd.MaHD, hd.MaNV, hd.MaKH, hd.MaPTTT, hd.MaVAT, hd.MaKM, 
        hd.NgayLap, hd.NgayXuat, hd.TrangThai, b.MaBan, kh.SDT
    ORDER BY 
        CASE 
            WHEN @SortByGia = 'DESC' THEN SUM(ct.SoLuong * ct.DonGia)
        END DESC,
        CASE 
            WHEN @SortByGia = 'ASC' THEN SUM(ct.SoLuong * ct.DonGia)
        END ASC,
        hd.NgayLap DESC; -- Default sort by NgayLap if SortByGia is not specified
END;

---Mon An
CREATE PROCEDURE GetAllMonAn
AS
BEGIN
    SELECT MaMonAn, TenMonAn, Gia, GhiChu, LoaiMonAn, DuongDanHinhAnh
    FROM MonAn;
END;

CREATE PROCEDURE GetMonAnByMa
    @MaMonAn VARCHAR(10)
AS
BEGIN
    SELECT MaMonAn, TenMonAn, Gia, GhiChu, LoaiMonAn, DuongDanHinhAnh
    FROM MonAn
    WHERE MaMonAn = @MaMonAn;
END;

CREATE PROCEDURE AddMonAn
    @MaMonAn VARCHAR(10),
    @TenMonAn NVARCHAR(50),
    @Gia FLOAT,
    @GhiChu NVARCHAR(MAX),
    @LoaiMonAn NVARCHAR(20),
    @DuongDanHinhAnh VARCHAR(255)
AS
BEGIN
    INSERT INTO MonAn (MaMonAn, TenMonAn, Gia, GhiChu, LoaiMonAn, DuongDanHinhAnh)
    VALUES (@MaMonAn, @TenMonAn, @Gia, @GhiChu, @LoaiMonAn, @DuongDanHinhAnh);
END;

CREATE PROCEDURE UpdateMonAn
    @MaMonAn VARCHAR(10),
    @TenMonAn NVARCHAR(50),
    @Gia FLOAT,
    @GhiChu NVARCHAR(MAX),
    @LoaiMonAn NVARCHAR(20),
    @DuongDanHinhAnh VARCHAR(255)
AS
BEGIN
    UPDATE MonAn
    SET TenMonAn = @TenMonAn,
        Gia = @Gia,
        GhiChu = @GhiChu,
        LoaiMonAn = @LoaiMonAn,
        DuongDanHinhAnh = @DuongDanHinhAnh
    WHERE MaMonAn = @MaMonAn;
END;

CREATE PROCEDURE DeleteMonAn
    @MaMonAn VARCHAR(10)
AS
BEGIN
    DELETE FROM MonAn
    WHERE MaMonAn = @MaMonAn;
END;

CREATE PROCEDURE SearchMonAn
    @SearchValue NVARCHAR(50),
    @SearchBy NVARCHAR(20) -- 'MaMonAn' ho?c 'TenMonAn'
AS
BEGIN
    IF @SearchBy = 'MaMonAn'
    BEGIN
        SELECT MaMonAn, TenMonAn, Gia, GhiChu, LoaiMonAn, DuongDanHinhAnh
        FROM MonAn
        WHERE MaMonAn LIKE '%' + @SearchValue + '%';
    END
    ELSE IF @SearchBy = 'TenMonAn'
    BEGIN
        SELECT MaMonAn, TenMonAn, Gia, GhiChu, LoaiMonAn, DuongDanHinhAnh
        FROM MonAn
        WHERE TenMonAn LIKE '%' + @SearchValue + '%';
    END
    ELSE
    BEGIN
        SELECT MaMonAn, TenMonAn, Gia, GhiChu, LoaiMonAn, DuongDanHinhAnh
        FROM MonAn;
    END
END;

CREATE PROCEDURE FilterMonAnByLoai
    @LoaiMonAn NVARCHAR(20)
AS
BEGIN
    IF @LoaiMonAn = 'Tất cả'
    BEGIN
        SELECT MaMonAn, TenMonAn, Gia, GhiChu, LoaiMonAn, DuongDanHinhAnh
        FROM MonAn;
    END
    ELSE
    BEGIN
        SELECT MaMonAn, TenMonAn, Gia, GhiChu, LoaiMonAn, DuongDanHinhAnh
        FROM MonAn
        WHERE LoaiMonAn = @LoaiMonAn;
    END
END;

CREATE PROCEDURE GetAllLoaiMonAn
AS
BEGIN
    SELECT DISTINCT LoaiMonAn
    FROM MonAn
    WHERE LoaiMonAn IS NOT NULL AND LoaiMonAn != ''
    ORDER BY LoaiMonAn;
END;

--Khach Hang
CREATE PROCEDURE sp_GetAllKhachHang
AS
BEGIN
    SELECT * FROM KhachHang;
END;
GO

-- 2. Stored Procedure để thêm khách hàng
CREATE PROCEDURE sp_AddKhachHang
    @MaKH VARCHAR(10),
    @TenKH NVARCHAR(50),
    @SDT VARCHAR(11),
    @GioiTinh NVARCHAR(10)
AS
BEGIN
    INSERT INTO KhachHang (MaKH, TenKH, SDT, GioiTinh)
    VALUES (@MaKH, @TenKH, @SDT, @GioiTinh);
END;
GO

-- 3. Stored Procedure để cập nhật khách hàng
CREATE PROCEDURE sp_UpdateKhachHang
    @MaKH VARCHAR(10),
    @TenKH NVARCHAR(50),
    @SDT VARCHAR(11),
    @GioiTinh NVARCHAR(10)
AS
BEGIN
    UPDATE KhachHang
    SET TenKH = @TenKH, SDT = @SDT, GioiTinh = @GioiTinh
    WHERE MaKH = @MaKH;
END;
GO

-- 4. Stored Procedure để xóa khách hàng
CREATE PROCEDURE sp_DeleteKhachHang
    @MaKH VARCHAR(10)
AS
BEGIN
    DELETE FROM KhachHang
    WHERE MaKH = @MaKH;
END;
GO

-- 5. Stored Procedure để lấy khách hàng theo mã
CREATE PROCEDURE sp_GetKhachHangByMa
    @MaKH VARCHAR(10)
AS
BEGIN
    SELECT * FROM KhachHang
    WHERE MaKH = @MaKH;
END;
GO

CREATE PROCEDURE sp_SearchKhachHang
    @Query NVARCHAR(50),
    @Criteria NVARCHAR(20)
AS
BEGIN
    IF @Criteria = 'Mã khách hàng'
        SELECT * FROM KhachHang WHERE MaKH = @Query;
    ELSE IF @Criteria = 'Tên khách hàng'
        SELECT * FROM KhachHang WHERE TenKH LIKE '%' + @Query + '%';
    ELSE IF @Criteria = 'Số điện thoại'
        SELECT * FROM KhachHang WHERE SDT LIKE '%' + @Query + '%';
END;
GO

drop procedure sp_SearchKhachHang

CREATE PROCEDURE sp_GetAllTaiKhoan
AS
BEGIN
    SELECT tk.MaNV, tk.TenDangNhap, tk.MatKhau, tk.TrangThai, tk.GioVaoLam, tk.GioNghi, tk.SoGioLam,
           nv.TenNV, nv.SDT, nv.GioiTinh, nv.ChucVu, nv.Tuoi, nv.Hesoluong, nv.LuongNV, nv.LinkIMG, nv.Email
    FROM TaiKhoan tk
    JOIN NhanVien nv ON tk.MaNV = nv.MaNV;
END;

-- Thêm tài khoản
CREATE OR ALTER PROCEDURE sp_InsertTaiKhoan
    @MaNV VARCHAR(10),
    @TenDangNhap VARCHAR(10),
    @MatKhau VARCHAR(20),
    @TrangThai NVARCHAR(50)
AS
BEGIN
    INSERT INTO TaiKhoan (MaNV, TenDangNhap, MatKhau, TrangThai, SoGioLam)
    VALUES (@MaNV, @TenDangNhap, @MatKhau, @TrangThai, 0);
END;

-- Xóa tài khoản
CREATE OR ALTER PROCEDURE sp_DeleteTaiKhoan
    @MaNV VARCHAR(10)
AS
BEGIN
    DELETE FROM TaiKhoan WHERE MaNV = @MaNV;
END;

-- Sửa tài khoản
CREATE OR ALTER PROCEDURE sp_UpdateTaiKhoan
    @MaNV VARCHAR(10),
    @TenDangNhap VARCHAR(10),
    @MatKhau VARCHAR(20),
    @TrangThai NVARCHAR(50)
AS
BEGIN
    UPDATE TaiKhoan
    SET TenDangNhap = @TenDangNhap, MatKhau = @MatKhau, TrangThai = @TrangThai
    WHERE MaNV = @MaNV;
END;

-- Tìm kiếm tài khoản
CREATE OR ALTER PROCEDURE sp_SearchTaiKhoan
    @Keyword VARCHAR(50)
AS
BEGIN
    SELECT tk.MaNV, tk.TenDangNhap, tk.MatKhau, tk.TrangThai, tk.GioVaoLam, tk.GioNghi, tk.SoGioLam,
           nv.TenNV, nv.SDT, nv.GioiTinh, nv.ChucVu, nv.Tuoi, nv.Hesoluong, nv.LuongNV, nv.LinkIMG, nv.Email
    FROM TaiKhoan tk
    JOIN NhanVien nv ON tk.MaNV = nv.MaNV
    WHERE tk.MaNV LIKE '%' + @Keyword + '%' OR tk.TenDangNhap LIKE '%' + @Keyword + '%';
END;



SELECT ROUTINE_NAME, ROUTINE_TYPE, CREATED, LAST_ALTERED
FROM information_schema.ROUTINES
DROP PROCEDURE IF EXISTS themKH;
SELECT * FROM NhanVien
select * from TaiKhoan
select * from Ban	
select * from ThueVAT
select * from KhuyenMai
select * from KhachHang
select * from HoaDon
select * from PhuongThucThanhToan
select * from ThueVAT
select * from KhuyenMai
select * from MonAn
select * from ChiTietHoaDon
select * from PhieuDatBan
select * from ChiTietPhieuDatBan
SELECT * FROM Ban WHERE TenBan = 'BÀN 1'
SELECT * FROM PhieuDatBan
SELECT MaPDB FROM PhieuDatBan ORDER BY MaPDB DESC



SELECT TOP 1 P.MaPDB 
FROM PhieuDatBan P
JOIN ChiTietPhieuDatBan C ON P.MaPDB = C.MaPDB
WHERE MaBan = 'B001'
ORDER BY C.TimeNhanBan DESC;

SELECT * FROM PhieuDatBan where MaBan = 'B001'
SELECT TOP 1 * FROM PhieuDatBan WHERE MaBan = 'B001' ORDER BY MaPDB DESC
SELECT MaMonAn, SoLuong, DonGia FROM ChiTietHoaDon WHERE MaHD = 'HD000005'
ALTER TABLE ChiTietPhieuDatBan
ADD CONSTRAINT FK_PhieuDatBan
FOREIGN KEY (MaPDB) REFERENCES PhieuDatBan(MaPDB)
ON DELETE CASCADE;

SELECT *
FROM sys.database_permissions
WHERE class_desc = 'OBJECT_OR_COLUMN' AND major_id = OBJECT_ID('HoaDon');

DELETE FROM ChiTietHoaDon WHERE MaHD = 'HD000001';
DELETE FROM HoaDon WHERE MaHD = 'HD000001';

delete from KhuyenMai where MaKM = 'KM01'

SELECT * FROM MonAn WHERE MaMonAn = 'MA001'
SELECT TOP 1 * FROM PhieuDatBan WHERE MaBan = 'B001' ORDER BY MaPDB DESC
SELECT MaHD FROM HoaDon WHERE MaKH = 'KH003' AND TrangThai = N'Chưa thanh toán'
SELECT * FROM ChiTietHoaDon WHERE MaHD = 'HD000001'
SELECT MaKM FROM KhuyenMai WHERE MaKM = 'KM01'
select * from ChiTietHoaDon
select * from MonAn where TenMonAn = 'Pepsi'
SELECT * FROM ChiTietHoaDon WHERE MaHD = 'HD000001'
SELECT 
    MaHD,
    MaMonAn, 
    SUM(SoLuong) AS TongSoLuong,
    SUM(DonGia) AS TongDonGia
FROM 
    ChiTietHoaDon 
WHERE 
    MaHD = 'HD000001' 
GROUP BY 
    MaHD, MaMonAn;


select * from dbo.Ban
select * from dbo.ChiTietHoaDon
select * from dbo.ChiTietPhieuDatBan
select * from dbo.HoaDon


INSERT INTO HoaDon (MaHD, MaNV, MaKH, MaPTTT, MaVAT, MaKM, NgayLap, NgayXuat, TrangThai)
VALUES
    ('HD000005', 'NV001', 'KH002', 'PTTT01', 'VAT01', 'KM01', '2025-04-30 09:00:00.000', '2025-04-30 09:15:00.000', 'Đã thanh toán'),
    ('HD000006', 'NV001', 'KH002', 'PTTT01', 'VAT01', 'KM01', '2025-05-01 11:00:00.000', '2025-05-01 11:20:00.000', 'Đã thanh toán');

INSERT INTO ChiTietHoaDon (MaHD, MaMonAn, SoLuong, DonGia)
VALUES
    ('HD000005', 'MA001', 1, 150000),
    ('HD000005', 'MA002', 1, 300000),
    ('HD000006', 'MA005', 1, 250000),
    ('HD000006', 'MA006', 1, 470000);

INSERT INTO PhieuDatBan (MaPDB, MaKhachHang, MaBan, MaNV)
VALUES
    ('PDB00002', 'KH002', 'B005', 'NV001'),
    ('PDB00003', 'KH002', 'B006', 'NV001');

INSERT INTO ChiTietPhieuDatBan (MaPDB, TimeNhanBan, TimeTraBan, SoNguoi)
VALUES
    ('PDB00002', '2025-04-30 08:50:00.000', '2025-04-30 09:30:00.000', 4),
    ('PDB00003', '2025-05-01 10:50:00.000', '2025-05-01 11:30:00.000', 4);



ALTER TABLE ChiTietPhieuDatBan
ADD CONSTRAINT FK__ChiTietPh__MaPDB__4BAC3F29
FOREIGN KEY (MaPDB) REFERENCES PhieuDatBan(MaPDB)

create PROCEDURE sp_XoaPhieuDatTheoBan
    @MaBan NVARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @LatestMaPDB NVARCHAR(10);

    -- Lấy mã phiếu đặt mới nhất theo MaPDB
    SELECT TOP 1 @LatestMaPDB = MaPDB
    FROM PhieuDatBan
    WHERE MaBan = @MaBan
    ORDER BY MaPDB DESC;

    -- Nếu tìm thấy mã phiếu đặt mới nhất, thì tiến hành xóa
    IF @LatestMaPDB IS NOT NULL
    BEGIN
        DELETE FROM ChiTietPhieuDatBan WHERE MaPDB = @LatestMaPDB;
        DELETE FROM PhieuDatBan WHERE MaPDB = @LatestMaPDB;
    END
END;


select * from dbo.Ban
select * from dbo.ChiTietPhieuDatBan
select * from dbo.PhieuDatBan
select * from dbo.HoaDon
select * from dbo.ChiTietHoaDon
select * from dbo.GopBan

SELECT COUNT(*) FROM GopBan WHERE MaBanChinh = 'B001'

delete from dbo.ChiTietPhieuDatBan
delete from dbo.PhieuDatBan
delete from dbo.GopBan
delete from dbo.Ban
delete from dbo.ChiTietHoaDon
delete from dbo.HoaDon


INSERT INTO Ban (MaBan, TenBan, TrangThai, Tang, SoCho, GhiChu) VALUES
('B001', N'Bàn 1', N'CÒN TRỐNG', 1, 4, N'Không có'),
('B002', N'Bàn 2', N'CÒN TRỐNG', 1, 4, N'Không có'),
('B003', N'Bàn 3', N'CÒN TRỐNG', 1, 4, N'Không có'),
('B004', N'Bàn 4', N'CÒN TRỐNG', 1, 4, N'Không có'),
('B005', N'Bàn 5', N'CÒN TRỐNG', 1, 4, N'Không có'),
('B006', N'Bàn 6', N'CÒN TRỐNG', 1, 4, N'Không có'),
('B007', N'Bàn 7', N'CÒN TRỐNG', 2, 8, N'Không có'),
('B008', N'Bàn 8', N'CÒN TRỐNG', 2, 8, N'Không có'),
('B009', N'Bàn 9', N'CÒN TRỐNG', 2, 8, N'Không có'),
('B010', N'Bàn 10', N'CÒN TRỐNG', 2, 8, N'Không có'),
('B011', N'Bàn 11', N'CÒN TRỐNG', 3, 12, N'Không có'),
('B012', N'Bàn 12', N'CÒN TRỐNG', 3, 12, N'Không có');

INSERT INTO Ban (MaBan, TenBan, TrangThai, Tang, SoCho, GhiChu) VALUES
('B001', N'Bàn 1', N'CÒN TRỐNG', 1, 4, N'Không có'),
('B002', N'Bàn 2', N'CÒN TRỐNG', 1, 4, N'Không có'),
('B003', N'Bàn 3', N'CÒN TRỐNG', 1, 4, N'Không có'),
('B004', N'Bàn 4', N'CÒN TRỐNG', 1, 4, N'Không có'),
('B005', N'Bàn 5', N'CÒN TRỐNG', 1, 4, N'Không có'),
('B006', N'Bàn 6', N'CÒN TRỐNG', 1, 4, N'Không có');
CREATE TABLE GopBan (
    MaBanChinh VARCHAR(10),
    MaBanGop VARCHAR(10),
    ThoiGianGop DATETIME,
    PRIMARY KEY (MaBanChinh, MaBanGop),
    FOREIGN KEY (MaBanChinh) REFERENCES Ban(MaBan),
    FOREIGN KEY (MaBanGop) REFERENCES Ban(MaBan)
);


EXEC sp_SearchKhachHang '0901234567', 'Số điện thoại';

-- In danh sách tất cả các bảng trong cơ sở dữ liệu
SELECT 
    TABLE_NAME
FROM 
    INFORMATION_SCHEMA.TABLES
WHERE 
    TABLE_TYPE = 'BASE TABLE'
ORDER BY 
    TABLE_NAME;

-- In danh sách tất cả các bảng cùng với các cột và thuộc tính của chúng
SELECT 
    t.TABLE_NAME AS 'Tên Bảng',
    c.COLUMN_NAME AS 'Tên Cột',
    c.DATA_TYPE AS 'Kiểu Dữ Liệu',
    c.CHARACTER_MAXIMUM_LENGTH AS 'Kích Thước Tối Đa',
    c.IS_NULLABLE AS 'Có Thể NULL',
    c.COLUMN_DEFAULT AS 'Giá Trị Mặc Định'
FROM 
    INFORMATION_SCHEMA.TABLES t
LEFT JOIN 
    INFORMATION_SCHEMA.COLUMNS c 
ON 
    t.TABLE_NAME = c.TABLE_NAME
WHERE 
    t.TABLE_TYPE = 'BASE TABLE'
ORDER BY 
    t.TABLE_NAME, c.ORDINAL_POSITION;
