import React from "react";
import { View, Text, StyleSheet, TouchableOpacity, Alert } from "react-native";
import { useAuthStore } from "../../store/useAuthStore";
import { Ionicons } from "@expo/vector-icons"; // Import Icon

export default function HomeScreen() {
  // Lấy hàm logout từ store
  const logout = useAuthStore((state) => state.logout);

  const handleLogout = () => {
    // Thêm xác nhận trước khi đăng xuất để tăng trải nghiệm UX
    Alert.alert(
      "Đăng xuất",
      "Bạn có chắc chắn muốn đăng xuất không?",
      [
        {
          text: "Hủy",
          style: "cancel",
        },
        {
          text: "Đăng xuất",
          onPress: () => logout(),
          style: "destructive", // Style màu đỏ trên iOS
        },
      ]
    );
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Trang Chủ Bookington</Text>
      <Text style={styles.subtitle}>
        Xin chào User, đây là màn hình dành cho Client.
      </Text>

      {/* Khoảng cách */}
      <View style={{ height: 40 }} />

      {/* --- NÚT ĐĂNG XUẤT ĐÃ ĐƯỢC DESIGN --- */}
      <TouchableOpacity
        style={styles.logoutButton}
        onPress={handleLogout}
        activeOpacity={0.8}
      >
        <Ionicons name="log-out-outline" size={24} color="white" />
        <Text style={styles.logoutText}>Đăng xuất</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
    padding: 20,
    backgroundColor: "#F5F7FA", // Màu nền xám nhẹ cho dịu mắt
  },
  title: {
    fontSize: 26,
    fontWeight: "bold",
    marginBottom: 10,
    color: "#333",
  },
  subtitle: {
    fontSize: 16,
    color: "#666",
    textAlign: "center",
  },
  
  // --- Style cho Nút Đăng xuất ---
  logoutButton: {
    flexDirection: "row", // Xếp icon và chữ nằm ngang
    backgroundColor: "#FF4757", // Màu đỏ cà chua (Tomato red) đẹp mắt
    paddingVertical: 14,
    paddingHorizontal: 30,
    borderRadius: 30, // Bo tròn dạng viên thuốc
    alignItems: "center",
    
    // Đổ bóng (Shadow) cho nút nổi lên
    shadowColor: "#FF4757",
    shadowOffset: {
      width: 0,
      height: 4,
    },
    shadowOpacity: 0.3,
    shadowRadius: 4.65,
    elevation: 8, // Đổ bóng cho Android
  },
  logoutText: {
    color: "white",
    fontSize: 16,
    fontWeight: "bold",
    marginLeft: 8, // Khoảng cách giữa Icon và Chữ
  },
});