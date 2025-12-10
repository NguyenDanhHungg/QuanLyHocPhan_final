package university.registration.store;

import university.registration.model.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class Memory {
    /** Tài khoản PĐT (admin): username -> password */
    public static final Map<String, String> adminPasswords = new HashMap<>();
    /** Danh sách sinh viên tra theo MSSV: studentId -> Student */
    public static final Map<String, Student> studentsById   = new HashMap<>();
    /** Index email: email (lowercase) -> studentId (để kiểm tra email trùng) */
    public static final Map<String, String> emailIndex      = new HashMap<>();
    /** Danh sách học phần master (không phụ thuộc học kỳ): code -> Course */
    public static final Map<String, Course> courses         = new LinkedHashMap<>();
    /** Danh sách học kỳ (ví dụ: 20252, 20251, ...) */
    public static final List<String> terms                  = new ArrayList<>();
    /** Danh sách chương trình đào tạo (CTĐT) */
    public static final List<String> programs               = new ArrayList<>();

    /** Đăng ký học phần:
     *  regs: studentId -> (term -> list RegItem)
     *  Mỗi sinh viên, mỗi kỳ có một danh sách các môn đã đăng ký
     */
    public static final Map<String, Map<String, List<RegItem>>> regs = new HashMap<>();

    /** Cấu hình học kỳ: term -> TermSetting (mở/khóa đăng ký) */
    public static final Map<String, TermSetting> termSettings = new HashMap<>();

    /** Offering theo học kỳ:
     *  term -> (courseCode -> Offering)
     *  Mỗi học kỳ, mỗi học phần có cấu hình mở/đóng, CTĐT được phép
     */
    public static final Map<String, Map<String, Offering>> offerings = new HashMap<>();

    /**
     * Hàm khởi tạo dữ liệu demo ban đầu cho hệ thống.
     * Được gọi một lần khi chương trình khởi động.
     */
    public static void init() {
        // Tài khoản PĐT mặc định: username=pdt, password=pdt123
        adminPasswords.put("pdt", "pdt123");

        // Thêm các chương trình học (CTĐT) demo
        programs.addAll(Arrays.asList(
                "Kỹ thuật Điện tử - Viễn thông 2021",
                "Công nghệ Thông tin 2021",
                "Kỹ thuật Cơ khí 2021"
        ));

        // Danh sách học kỳ demo
        terms.addAll(Arrays.asList("20252","20251","20242"));
        // Với mỗi học kỳ, tạo TermSetting và mặc định là đang mở đăng ký (true)
        for (String t : terms) termSettings.put(t, new TermSetting(true)); // mặc định mở

        // Thêm các học phần (Course) demo vào danh sách courses
        addCourse(new Course("CT101","Lập trình cơ bản",3));
        addCourse(new Course("CT102","Cấu trúc dữ liệu",3));
        addCourse(new Course("EE201","Mạch điện 1",3));
        addCourse(new Course("MA101","Giải tích 1",4));
        addCourse(new Course("PE101","Giáo dục thể chất",2));

        // Mở offering cho HỌC KỲ MỚI NHẤT – cho tất cả chương trình học (CTĐT)
        String latest = terms.get(0); // phần tử đầu trong list terms (20252)
        for (String code : courses.keySet())
            setOffering(latest, code, true, "Tất cả");

        // Tạo một sinh viên demo mặc định
        Student demo = new Student(
                "SV001","Sinh Viên Mặc Định","2004-01-01","Hà Nội",
                "sv001@university.edu","Kỹ thuật Điện tử - Viễn thông 2021"
        );
        // Thêm sinh viên demo vào hệ thống, mật khẩu là "sv123"
        addStudent(demo, "sv123");
    }

    /* ---------- helpers data ---------- */

    /** Thêm một môn học mới vào danh sách courses */
    public static void addCourse(Course c){
        courses.put(c.code,c); // key là mã học phần
    }

    /** Kiểm tra tài khoản PĐT: đúng user và password hay không */
    public static boolean verifyAdmin(String u,String p){
        return p.equals(adminPasswords.get(u));
    }

    /** Kiểm tra đăng nhập sinh viên: đúng MSSV và password hay không */
    public static boolean verifyStudent(String id,String p){
        Student s = studentsById.get(id);            // tìm sinh viên theo MSSV
        return s != null && Objects.equals(s.password,p); // so sánh mật khẩu
    }

    /**
     * Thêm sinh viên mới vào hệ thống.
     * Có kiểm tra trùng MSSV, email trống, CTĐT trống, email đã dùng chưa.
     */
    public static void addStudent(Student s,String pass){
        // Mỗi MSSV chỉ được có 1 tài khoản
        if(studentsById.containsKey(s.studentId))
            throw new RuntimeException("Mỗi MSSV chỉ có 1 tài khoản!");

        // Bắt buộc phải có email
        if(s.email == null || s.email.isBlank())
            throw new RuntimeException("Email không được để trống!");

        // Bắt buộc phải chọn chương trình học
        if(s.program == null || s.program.isBlank())
            throw new RuntimeException("Vui lòng chọn chương trình học!");

        // Dùng email lowercase làm key để tránh phân biệt hoa/thường
        String key = s.email.toLowerCase(Locale.ROOT);

        // Kiểm tra email đã được sử dụng bởi MSSV khác chưa
        if(emailIndex.containsKey(key))
            throw new RuntimeException("Email đã được dùng bởi MSSV: " + emailIndex.get(key));

        // Nếu mọi thứ hợp lệ, gán mật khẩu và lưu vào 2 map
        s.password = pass;
        studentsById.put(s.studentId, s); // lưu student
        emailIndex.put(key, s.studentId); // index email -> MSSV
    }

    /** Trả về bản copy danh sách học kỳ (để UI dùng mà không sửa list gốc) */
    public static List<String> loadTerms(){
        return new ArrayList<>(terms);
    }

    /**
     * Lấy danh sách đăng ký của một sinh viên trong một học kỳ.
     * Nếu chưa có, sẽ tự tạo list trống rồi trả về.
     */
    public static List<RegItem> loadReg(String sid,String term){
        // computeIfAbsent: nếu chưa có key thì tạo mới
        return regs
                .computeIfAbsent(sid, k -> new HashMap<>())      // map term -> list
                .computeIfAbsent(term, k -> new ArrayList<>());  // list RegItem cho term
    }

    /**
     * Thêm một RegItem (môn đăng ký) cho sinh viên trong học kỳ.
     * Trả về true nếu thêm được, false nếu đã tồn tại môn đó (tránh trùng môn).
     */
    public static boolean addReg(String sid,String term,RegItem item){
        var list = loadReg(sid,term); // lấy (hoặc tạo) list đăng ký hiện tại
        // Không cho đăng ký trùng cùng một course code
        if(list.stream().anyMatch(x -> x.course.code.equals(item.course.code)))
            return false;
        list.add(item);
        return true;
    }

    /**
     * Xóa các RegItem theo tập mã học phần (codes)
     * cho 1 sinh viên trong 1 học kỳ.
     */
    public static void deleteByCourseCodes(String sid,String term,Set<String> codes){
        var list = loadReg(sid,term);
        // removeIf: xóa các phần tử có course.code nằm trong tập codes
        list.removeIf(it -> codes.contains(it.course.code));
    }

    /* ---------- term setting ---------- */

    /** Kiểm tra học kỳ có đang mở đăng ký hay không */
    public static boolean isTermOpen(String term){
        // Nếu không tìm thấy TermSetting thì dùng mặc định (đóng = false)
        return termSettings
                .getOrDefault(term, new TermSetting(false))
                .registrationOpen;
    }

    /** Đặt trạng thái mở/đóng cho một học kỳ */
    public static void setTermOpen(String term, boolean open){
        // Nếu term chưa có TermSetting thì tạo mới; sau đó cập nhật registrationOpen
        termSettings
                .computeIfAbsent(term, t -> new TermSetting(open))
                .registrationOpen = open;
    }

    /* ---------- offerings per term ---------- */

    /**
     * Lấy Offering (cấu hình mở lớp) cho một học phần trong một học kỳ.
     * Nếu term chưa có map offerings thì tạo map rỗng.
     */
    public static Offering getOffering(String term, String code){
        return offerings
                .computeIfAbsent(term, t -> new HashMap<>())
                .get(code);
    }

    /**
     * Thiết lập Offering cho một học phần trong một học kỳ.
     * Nếu chưa có map cho term thì tạo mới rồi put vào.
     */
    public static void setOffering(String term, String code, boolean open, String allowedProgram){
        offerings
                .computeIfAbsent(term, t -> new HashMap<>())
                .put(code, new Offering(open, allowedProgram));
    }

    /* ---------- Thống kê & xóa học phần ---------- */

    /**
     * Đếm số lượng đăng ký của một môn (courseCode) trong một học kỳ.
     * Duyệt qua tất cả sinh viên, đếm các RegItem trùng courseCode.
     */
    public static int countRegByCourse(String term, String courseCode){
        int cnt = 0;
        // byTerm: map term -> list RegItem của 1 sinh viên
        for (var byTerm : regs.values()) {
            var list = byTerm.get(term); // lấy list RegItem của học kỳ cần đếm
            if (list == null) continue;  // sinh viên này chưa đăng ký kỳ đó
            // Tăng biến đếm nếu RegItem có course.code khớp courseCode
            for (RegItem it : list)
                if (it.course.code.equals(courseCode)) cnt++;
        }
        return cnt;
    }

    /**
     * Kiểm tra một môn có thể xóa được không.
     * Không cho xóa nếu bất kỳ sinh viên nào đã đăng ký môn đó ở bất kỳ kỳ nào.
     */
    public static boolean canDeleteCourse(String courseCode){
        // byStudent: map term -> list RegItem cho từng sinh viên
        for (var byStudent : regs.values()) {
            for (var list : byStudent.values()) {
                for (RegItem it : list)
                    if (it.course.code.equals(courseCode)) return false; // có người đăng ký rồi
            }
        }
        return true; // không ai đăng ký, có thể xóa
    }

    /**
     * Xóa môn học khỏi hệ thống:
     *  - Xóa khỏi danh sách courses.
     *  - Xóa khỏi offerings của tất cả học kỳ.
     *  (KHÔNG đụng đến regs, nên cần đảm bảo canDeleteCourse trước khi gọi)
     */
    public static void deleteCourse(String courseCode){
        courses.remove(courseCode);         // xóa trong danh sách môn
        for (var m : offerings.values())    // m: map courseCode -> Offering
            m.remove(courseCode);           // xóa offering của môn đó trong từng kỳ
    }

    /** Trả về ngày hiện tại dạng chuỗi "yyyy-MM-dd" (ví dụ: 2025-11-19) */
    public static String today(){
        return new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date());
    }
}
