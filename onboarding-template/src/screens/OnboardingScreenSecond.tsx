import React from "react";
import {
  StyleSheet,
  Text,
  View,
  Image,
  TouchableOpacity,
  SafeAreaView,
  StatusBar,
  Dimensions,
} from "react-native";
import { StackScreenProps } from '@react-navigation/stack';
import { RootStackParamList } from '../navigation/RootNavigator';
const { width, height } = Dimensions.get("window");

// ❗ KHAI BÁO TYPE CHUẨN CHO PROPS
type OnboardingSecondProps = StackScreenProps<
  RootStackParamList,
  'OnboardingSecond'
> & {
  onFinish: () => Promise<void>;
};

const OnboardingScreenSecond = ({ navigation, onFinish }: OnboardingSecondProps) => {
  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="light-content" />

      {/* 1. Phần Logo */}
      <View style={styles.headerContainer}>
        {/* Bạn thay source={require('./path/to/logo.png')} vào đây */}
        <Image
          source={require("../../assets/Bookington_logo.png")} // Logo demo giả định
          style={styles.logo}
        />
      </View>

      {/* 2. Phần Hình Ảnh Chính */}
      <View style={styles.imageContainer}>
        {/* Bạn thay hình vận động viên vào đây */}
        <Image
          source={require("../../assets/Leechongwei.png")}
          style={styles.mainImage}
          resizeMode="contain"
        />
      </View>

      {/* 3. Phần Nội Dung & Nút Bấm */}
      <View style={styles.bottomContainer}>
        <Text style={styles.title}>Thanh toán nhanh chóng</Text>

        <Text style={styles.description}>
          Trải nghiệm thanh toán tiện lợi qua MoMo, VNPay và ứng dụng ngân hàng
          an toàn, dễ dàng
        </Text>

        {/* Pagination Dots */}
        <View style={styles.paginationContainer}>
          <View style={[styles.dot, styles.activeDot]} />
          <View style={[styles.dot, styles.activeDot]} />
          <View style={styles.dot} />
        </View>

        {/* Button Tiếp Tục */}
        {/* 2. Thêm sự kiện onPress vào nút bấm */}
        <TouchableOpacity
          style={styles.button}
          activeOpacity={0.8}
          // Dòng quan trọng: Chuyển sang màn hình thứ 2
          onPress={() => navigation.navigate("OnboardingThird")}
        >
          <Text style={styles.buttonText}>Tiếp tục</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#3B9AFF", // Màu xanh chủ đạo giống hình
    alignItems: "center",
    justifyContent: "space-between",
  },
  headerContainer: {
    marginTop: 20,
    alignItems: "center",
    height: height * 0.15, // Chiếm 15% màn hình
  },
  logo: {
    width: width * 0.8,
  },
  logoText: {
    color: "white",
    fontWeight: "bold",
    fontSize: 18,
    marginTop: 5,
    letterSpacing: 1,
  },
  imageContainer: {
    height: height * 0.45, // Chiếm 45% màn hình cho ảnh to
    justifyContent: "center",
    alignItems: "center",
    width: width,
  },
  mainImage: {
    width: "90%",
    height: "90%",
  },
  bottomContainer: {
    height: height * 0.4, // Chiếm 40% màn hình còn lại
    width: "100%",
    alignItems: "center",
    paddingHorizontal: 20,
    justifyContent: "flex-start",
  },
  title: {
    fontSize: 28,
    fontWeight: "bold",
    color: "white",
    marginBottom: 10,
  },
  description: {
    fontSize: 16,
    color: "#E0F0FF", // Màu trắng hơi nhạt cho text phụ
    textAlign: "center",
    lineHeight: 24,
    marginBottom: 20,
    paddingHorizontal: 10,
  },
  paginationContainer: {
    flexDirection: "row",
    marginBottom: 30,
    alignItems: "center",
  },
  dot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    backgroundColor: "rgba(255, 255, 255, 0.5)", // Dot mờ
    marginHorizontal: 5,
  },
  activeDot: {
    width: 10,
    height: 10,
    backgroundColor: "transparent",
    borderWidth: 2,
    borderColor: "white", // Dot rỗng active
    borderRadius: 6,
  },
  button: {
    backgroundColor: "white",
    width: "80%",
    paddingVertical: 15,
    borderRadius: 30,
    alignItems: "center",
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
  buttonText: {
    color: "#3B9AFF", // Màu chữ cùng màu nền
    fontSize: 18,
    fontWeight: "600",
  },
});

export default OnboardingScreenSecond;
