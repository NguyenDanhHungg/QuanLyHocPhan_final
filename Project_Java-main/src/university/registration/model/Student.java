package university.registration.model; // Khai báo package chứa lớp Student

// Lớp biểu diễn SINH VIÊN
public class Student {
    // Các thông tin cơ bản của sinh viên (final: không đổi sau khi tạo)
    public final String studentId; // Mã số sinh viên
    public final String fullName;  // Họ tên đầy đủ
    public final String dob;       // Ngày sinh
    public final String address;   // Địa chỉ
    public final String email;     // Email
    public final String program;   // Chương trình đào tạo (SE, CS,...)

    // Mật khẩu (có thể thay đổi → không dùng final)
    public String password;

    // Constructor: tạo một Student mới (chưa có mật khẩu)
    // id: MSSV, name: Họ tên, dob: ngày sinh,
    // addr: địa chỉ, email: email, program: chương trình đào tạo
    public Student(String id, String name, String dob, String addr, String email, String program) {
        this.studentId = id;      // gán MSSV
        this.fullName = name;     // gán họ tên
        this.dob = dob;           // gán ngày sinh
        this.address = addr;      // gán địa chỉ
        this.email = email;       // gán email
        this.program = program;   // gán chương trình đào tạo
        // password chưa được gán ở đây, thường sẽ được set sau:
        // student.password = "matkhau";
    }
}

