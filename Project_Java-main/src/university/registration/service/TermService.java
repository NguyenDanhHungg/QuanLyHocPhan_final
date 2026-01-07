package university.registration.service;

import university.registration.model.Offering;
import university.registration.model.TermSetting;
import university.registration.store.Memory;

import java.util.List;

/**
 * Service xử lý logic nghiệp vụ liên quan đến Học kỳ (Term)
 * 
 * Lớp này chịu trách nhiệm:
 * - Quản lý trạng thái mở/đóng đăng ký của học kỳ
 * - Quản lý Offering (cấu hình mở lớp) cho từng học phần trong học kỳ
 * - Lấy danh sách học kỳ
 */
public class TermService {
    
    /**
     * Kiểm tra học kỳ có đang mở đăng ký hay không
     * 
     * @param term Học kỳ cần kiểm tra (ví dụ: "20252")
     * @return true nếu đang mở đăng ký, false nếu đã đóng
     */
    public boolean isTermOpen(String term) {
        return Memory.isTermOpen(term);
    }
    
    /**
     * Đặt trạng thái mở/đóng đăng ký cho học kỳ
     * 
     * @param term Học kỳ cần cập nhật
     * @param open true để mở đăng ký, false để đóng
     */
    public void setTermOpen(String term, boolean open) {
        Memory.setTermOpen(term, open);
    }
    
    /**
     * Lấy danh sách tất cả học kỳ trong hệ thống
     * 
     * @return Danh sách học kỳ (ví dụ: ["20252", "20251", "20242"])
     */
    public List<String> getAllTerms() {
        return Memory.loadTerms();
    }
    
    /**
     * Lấy Offering (cấu hình mở lớp) của một học phần trong học kỳ
     * 
     * Offering chứa thông tin:
     * - Trạng thái mở/đóng lớp
     * - Chương trình đào tạo được phép đăng ký
     * 
     * @param term Học kỳ
     * @param courseCode Mã học phần
     * @return Đối tượng Offering nếu có, null nếu chưa có cấu hình
     */
    public Offering getOffering(String term, String courseCode) {
        return Memory.getOffering(term, courseCode);
    }
    
    /**
     * Thiết lập Offering (cấu hình mở lớp) cho một học phần trong học kỳ
     * 
     * @param term Học kỳ
     * @param courseCode Mã học phần
     * @param open true nếu mở lớp, false nếu đóng
     * @param allowedProgram Chương trình đào tạo được phép đăng ký (hoặc "Tất cả")
     */
    public void setOffering(String term, String courseCode, boolean open, String allowedProgram) {
        Memory.setOffering(term, courseCode, open, allowedProgram);
    }
}

