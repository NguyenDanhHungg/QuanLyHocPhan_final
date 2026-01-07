package university.registration.service;

import university.registration.model.Student;
import university.registration.store.Memory;

/**
 * Service xử lý logic nghiệp vụ liên quan đến Sinh viên
 * 
 * Lớp này chịu trách nhiệm:
 * - Thêm mới sinh viên vào hệ thống
 * - Kiểm tra tính hợp lệ của thông tin sinh viên (MSSV, Email)
 * - Xác thực email đã được sử dụng chưa
 */
public class StudentService {
    
    /**
     * Thêm sinh viên mới vào hệ thống
     * 
     * Service này sẽ kiểm tra các điều kiện:
     * - MSSV chưa tồn tại trong hệ thống
     * - Email chưa được sử dụng bởi sinh viên khác
     * - Email và CTĐT không được để trống
     * 
     * @param student Đối tượng Student cần thêm vào hệ thống
     * @param password Mật khẩu cho tài khoản sinh viên
     * @throws RuntimeException nếu vi phạm các điều kiện trên
     */
    public void addStudent(Student student, String password) {
        // Gọi Memory để thực hiện lưu trữ (Memory sẽ kiểm tra các điều kiện)
        Memory.addStudent(student, password);
    }
    
    /**
     * Kiểm tra MSSV đã tồn tại trong hệ thống chưa
     * 
     * @param studentId Mã số sinh viên cần kiểm tra
     * @return true nếu MSSV đã tồn tại, false nếu chưa có
     */
    public boolean isStudentIdExists(String studentId) {
        return Memory.studentsById.containsKey(studentId);
    }
    
    /**
     * Kiểm tra Email đã được sử dụng bởi sinh viên khác chưa
     * 
     * @param email Email cần kiểm tra (sẽ được chuyển về lowercase)
     * @return true nếu email đã được sử dụng, false nếu chưa có
     */
    public boolean isEmailExists(String email) {
        return Memory.emailIndex.containsKey(email.toLowerCase());
    }
    
    /**
     * Lấy thông tin sinh viên theo MSSV
     * 
     * @param studentId Mã số sinh viên
     * @return Đối tượng Student nếu tìm thấy, null nếu không tồn tại
     */
    public Student getStudentById(String studentId) {
        return Memory.studentsById.get(studentId);
    }
}

