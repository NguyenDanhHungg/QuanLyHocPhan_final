package university.registration.model; // Khai báo package chứa lớp TermSetting

// Lớp cấu hình HỌC KỲ (term), chủ yếu là trạng thái mở đăng ký
public class TermSetting {
    // Cho biết trong học kỳ này hệ thống đăng ký đang mở hay đóng
    public boolean registrationOpen;

    // Constructor: tạo mới TermSetting với trạng thái mở/đóng
    // open: true → đang mở đăng ký, false → đã đóng
    public TermSetting(boolean open) {
        registrationOpen = open;  // gán trạng thái mở/đóng đăng ký
    }
}
