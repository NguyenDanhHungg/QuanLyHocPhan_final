package university.registration.service;

import university.registration.model.Course;
import university.registration.store.Memory;

import java.util.List;
import java.util.Map;

/**
 * Service xử lý logic nghiệp vụ liên quan đến Học phần (Course)
 * 
 * Lớp này chịu trách nhiệm:
 * - Thêm, xóa, cập nhật học phần
 * - Kiểm tra điều kiện có thể xóa học phần (không có sinh viên nào đăng ký)
 * - Đếm số lượng sinh viên đã đăng ký một học phần
 * - Lấy danh sách học phần
 */
public class CourseService {
    
    /**
     * Thêm học phần mới vào hệ thống
     * 
     * @param course Đối tượng Course cần thêm
     */
    public void addCourse(Course course) {
        Memory.addCourse(course);
    }
    
    /**
     * Kiểm tra học phần có thể xóa được không
     * 
     * Học phần chỉ có thể xóa khi:
     * - Không có sinh viên nào đã đăng ký học phần này ở bất kỳ học kỳ nào
     * 
     * @param courseCode Mã học phần cần kiểm tra
     * @return true nếu có thể xóa, false nếu đã có sinh viên đăng ký
     */
    public boolean canDeleteCourse(String courseCode) {
        return Memory.canDeleteCourse(courseCode);
    }
    
    /**
     * Xóa học phần khỏi hệ thống
     * 
     * Lưu ý: Nên gọi canDeleteCourse() trước khi xóa để đảm bảo an toàn
     * 
     * @param courseCode Mã học phần cần xóa
     */
    public void deleteCourse(String courseCode) {
        Memory.deleteCourse(courseCode);
    }
    
    /**
     * Lấy học phần theo mã
     * 
     * @param courseCode Mã học phần
     * @return Đối tượng Course nếu tìm thấy, null nếu không tồn tại
     */
    public Course getCourseByCode(String courseCode) {
        return Memory.courses.get(courseCode);
    }
    
    /**
     * Lấy danh sách tất cả học phần trong hệ thống
     * 
     * @return Map chứa tất cả học phần (key: mã HP, value: Course)
     */
    public Map<String, Course> getAllCourses() {
        return Memory.courses;
    }
    
    /**
     * Đếm số lượng sinh viên đã đăng ký một học phần trong một học kỳ
     * 
     * @param term Học kỳ cần đếm
     * @param courseCode Mã học phần
     * @return Số lượng sinh viên đã đăng ký
     */
    public int countRegistrationsByCourse(String term, String courseCode) {
        return Memory.countRegByCourse(term, courseCode);
    }
}

