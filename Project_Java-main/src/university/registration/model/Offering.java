package university.registration.model; // Khai báo package chứa lớp Offering

// Lớp biểu diễn "đợt mở môn" hoặc cấu hình mở cho một môn học
public class Offering {
    // Cho biết môn này hiện có mở đăng ký hay không (true: mở, false: đóng)
    public boolean open;
    // Chương trình đào tạo được phép đăng ký (ví dụ: "SE", "CS", "All"...)
    public String allowedProgram;

    // Constructor: tạo một Offering với trạng thái mở/đóng và chương trình cho phép
    // o: trạng thái mở đăng ký, ap: chương trình được phép đăng ký
    public Offering(boolean o, String ap) {
        open = o;                // gán trạng thái mở/đóng
        allowedProgram = ap;     // gán chương trình được phép đăng ký
    }
}
