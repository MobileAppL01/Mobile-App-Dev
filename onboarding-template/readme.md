# My Expo App

Ứng dụng mẫu sử dụng **React Native + Expo** với các tính năng cơ bản:

- Trang **Onboarding** chỉ chạy lần đầu mở app.
- Trang **Home** sau khi hoàn tất Onboarding.
- Lưu trạng thái đã xem Onboarding bằng **AsyncStorage**.

---

## Yêu cầu

- Node.js >= 16
- npm hoặc yarn
- Expo CLI: `npm install -g expo-cli`
- Điện thoại với **Expo Go** (Android / iOS)
- Cả máy tính và điện thoại đều **cùng mạng Wi-Fi** nếu dùng LAN

---

## Cài đặt

1. Clone repository:

```bash
git clone https://github.com/username/my-expo-app.git
cd my-expo-app
```

2. Cài dependencies:

```bash
npm install
```
hoặc nếu dùng yarn:

```bash
yarn install
```

---

## Chạy ứng dụng
1. Khởi chạy dev server:

``` bash
npx expo start
```

2. Mở Expo Go trên điện thoại.

3. Quét QR code hiển thị trên terminal.

4. App sẽ load trên điện thoại:

- Nếu là lần đầu mở → sẽ hiển thị Onboarding Screen.

- Lần tiếp theo → sẽ trực tiếp vào Home Screen.

---

## Tùy chọn nâng cao & Gỡ rối

Nếu bạn không thể kết nối cùng mạng Wi-Fi hoặc muốn chạy qua cáp, hãy tham khảo các tùy chọn dưới đây.

1. Chạy qua Tunnel (Nếu không cùng Wi-Fi)
Nếu bạn ở khác mạng Wi-Fi (ví dụ: dùng 4G), bạn có thể dùng "tunnel" để public dev server của mình.

Thêm cờ ```--tunnel``` vào lệnh khởi động:

```Bash
npx expo start --tunnel
```

**Gặp lỗi khi chạy tunnel?** 
Nếu bạn nhận được lỗi CommandError: Install @expo/ngrok@^4.1.0 and try again, hãy chạy lệnh sau để cài đặt dependency bị thiếu:

```Bash
npm install @expo/ngrok --save-dev
```

2. Chạy qua cáp USB (Dành cho Android)

Để có kết nối ổn định và nhanh hơn, bạn có thể chạy qua cáp USB (yêu cầu đã cài đặt Android ADB).

1. Cắm điện thoại Android của bạn vào máy tính qua cáp USB và bật chế độ "USB Debugging".

2. Chạy lệnh sau trên terminal (mở một terminal mới) để map cổng:

```Bash
adb reverse tcp:8081 tcp:8081
```

3. Quay lại terminal đang chạy npm run start (từ bước 1 chính).

4. Nhấn tổ hợp phím Shift + A.

5. Một danh sách các thiết bị Android đã kết nối sẽ hiện ra, chọn thiết bị của bạn để chạy ứng dụng.

--

## Triển khai với EAS (Expo Application Services)

Để tạo bản build (file ```.apk``` hoặc ```.ipa```) và gửi lên các cửa hàng ứng dụng, bạn có thể sử dụng Expo Application Services (EAS).

1. Cài đặt EAS CLI (nếu chưa có):

```Bash
npm install -g eas-cli
```

2. Đăng nhập vào tài khoản Expo:

```Bash
eas login
```

3. Cấu hình dự án cho EAS (chỉ chạy lần đầu):

```Bash
eas build:configure
```

4. Thao tác này sẽ tạo ra file eas.json trong dự án của bạn.

Bắt đầu quá trình build:

```Bash
eas build
```

5. Bạn cũng có thể chỉ định nền tảng cụ thể:

```Bash
eas build --platform android
# hoặc
eas build --platform ios
```

**Ghi chú quan trọng**: Trong lần đầu chạy eas build hoặc khi cấu hình credentials, EAS sẽ hỏi bạn rất nhiều câu hỏi xác nhận (ví dụ: tạo keystore mới, quản lý provisioning profile...). Như bạn đã đề cập, nếu bạn muốn chấp nhận các lựa chọn mặc định do EAS đề xuất, bạn có thể sẽ cần chọn Y (Yes) liên tục cho đến khi quá trình build bắt đầu.

Sau khi build thành công, bạn sẽ nhận được một liên kết để tải về file cài đặt.

---

## Thư mục chính
```bash
MyApp/
├── App.tsx               # Entry point
├── src/
│   ├── screens/          # Các màn hình Onboarding, Home
│   ├── navigation/       # RootNavigator
│   └── storage/          # AsyncStorage helper
├── package.json
└── README.md
```