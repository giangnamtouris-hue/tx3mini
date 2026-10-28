# TX3 Mini Apple Lite Launcher

Launcher dành cho Android 6+ (API 23+) với hình nền xanh chuyển sắc tĩnh, đồng hồ và ngày tháng lấy từ hệ thống, **chỉ hai ô lớn YouTube và Trình duyệt**. Dùng phím mũi tên / OK trên remote; phím MENU hoặc dòng nhỏ phía dưới để vào Cài đặt Android. Dùng Java Android gốc, không thư viện UI ngoài, không yêu cầu Internet trong chính launcher.

## Quan trọng
- Đây là **mã nguồn Android Studio**, không phải APK đã biên dịch. Chưa được chạy thử trên TX3 Mini thực tế.
- Launcher không cài sẵn YouTube hoặc browser. Nếu nút YouTube không tìm thấy app sẽ mở URL qua browser; nếu chưa có trình duyệt hãy cài một browser hỗ trợ thiết bị trước.
- TX3 Mini có nhiều biến thể firmware. Không can thiệp hệ thống trước khi kiểm tra launcher hoạt động.

## Build APK trên máy tính
1. Cài Android Studio phiên bản tương thích với Android Gradle Plugin 8.6.1, JDK 17, Android SDK Platform 35.
2. Giải nén ZIP, trong Android Studio chọn Open > thư mục `TX3Mini_AppleLite`.
3. Project đã khai báo plugin Gradle; nếu Android Studio hỏi về Gradle distribution, chọn Gradle 8.7 (project không kèm Gradle wrapper).
4. Đồng bộ Gradle. Chọn Build > Build Bundle(s) / APK(s) > Build APK(s).
5. Lấy `app/build/outputs/apk/debug/app-debug.apk`.

## Build trên GitHub Actions (không cần cài Android Studio)
1. Tạo repository GitHub riêng, upload **toàn bộ nội dung** thư mục dự án (bao gồm thư mục `.github`).
2. Vào tab Actions > Build Android APK > Run workflow. Nếu workflow tự chạy do push thì mở lượt chạy đó.
3. Khi có dấu tích xanh, tải artifact `TX3Mini-AppleLite-APK`, giải nén để lấy `app-debug.apk`.
4. Build online cần tài khoản GitHub và kết nối Internet; workflow là mã nguồn để tham khảo, chưa được chạy trong môi trường này.

## Cài vào TX3 Mini bằng USB
1. Copy `app-debug.apk` sang USB FAT32.
2. Cắm vào box, vào File Manager chọn APK; bật Unknown sources / Install unknown apps cho trình quản lý file khi được hỏi.
3. Mở TX3 Mini Apple Lite Launcher; dùng Left / Right / OK thử YouTube và browser.
4. Nhấn HOME, chọn app mới > Always nếu xuất hiện; hoặc Settings > Apps > Default apps > Home app (tùy firmware).
5. Nếu Home vẫn quay về launcher cũ, firmware có thể khóa Home. Không xóa launcher gốc hay chạy lệnh ADB vô hiệu hóa Home khi chưa có cách cứu hộ.

## Tối ưu box an toàn và hoàn tác được
1. Trước hết **giữ launcher gốc**, chụp ảnh màn hình danh sách Apps và ghi lại các ứng dụng muốn tắt.
2. Settings > Apps: gỡ app tự cài không dùng; với app rác có nút Disable, tắt từng app rồi khởi động lại kiểm tra Wi-Fi, remote, YouTube, browser.
3. Không tắt Android System, Google Play services, Settings, System UI, Package Installer, Input/remote service, launcher gốc, hay bất cứ app lạ chưa rõ chức năng.
4. Không dùng "one-click debloat" hoặc `pm uninstall --user 0` hàng loạt. Nếu bắt buộc dùng ADB, lấy tên package chính xác của từng app, có đường khôi phục trước, và dùng `pm disable-user --user 0 <package>` riêng lẻ. Phục hồi với `pm enable <package>`.
5. Tắt hình nền động, giảm các thang animation trong Developer options xuống 0.5x. Giữ trống dung lượng lưu trữ. Launcher không thể tự tăng CPU/RAM hoặc tắt các dịch vụ hệ thống.

## Khôi phục launcher gốc
- Settings > Apps > Default apps > Home app, chọn launcher gốc (nếu menu có).
- Hoặc Settings > Apps > TX3 Mini Apple Lite Launcher > Clear defaults/Uninstall. Dùng phím MENU trong launcher để mở Settings nếu Home bị lỗi.

## Tùy biến
- Hình nền JPG: `app/src/main/res/drawable/wallpaper.jpg` (1280x720).
- Màu sắc, bố cục: `MainActivity.java`.
- Thời gian sai: chỉnh Date & time, múi giờ GMT+07:00 và bật đồng bộ giờ qua mạng nếu có.
