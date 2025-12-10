package university.registration.model; // Khai báo package chứa lớp RegItem

// Lớp biểu diễn một "mục đăng ký" (một dòng đăng ký 1 môn của sinh viên)
public class RegItem {
    // Môn học mà sinh viên đăng ký (final: không đổi, luôn gắn với 1 Course)
    public final Course course;
    // Ngày đăng ký (dạng chuỗi), trạng thái đăng ký (Registered/Cancelled/...)
    public String date, status;

    // Constructor: tạo một RegItem mới
    // c: môn học, d: ngày đăng ký, s: trạng thái đăng ký
    public RegItem(Course c, String d, String s) {
        course = c;     // gán môn học cho mục đăng ký
        date = d;       // gán ngày đăng ký
        status = s;     // gán trạng thái đăng ký
    }
}

