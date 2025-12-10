package university.registration.model; // Khai báo package chứa lớp Course

// Lớp biểu diễn MÔN HỌC
public class Course {
    // Mã môn và tên môn (final: gán 1 lần trong constructor, không đổi về sau)
    public final String code, name;
    // Số tín chỉ (cũng là hằng, không đổi sau khi tạo đối tượng)
    public final int credits;
    // Constructor: dùng để tạo một Course mới
    // c: mã môn, n: tên môn, cr: số tín chỉ
    public Course(String c, String n, int cr) {
        code = c;       // gán giá trị cho thuộc tính code
        name = n;       // gán giá trị cho thuộc tính name
        credits = cr;   // gán giá trị cho thuộc tính credits
    }
}
