// import React from "react";
// import {
//   StyleSheet,
//   Text,
//   View,
//   Image,
//   TouchableOpacity,
//   StatusBar,
//   Dimensions,
// } from "react-native";
// import { StackScreenProps } from '@react-navigation/stack';
// import { RootStackParamList } from "../../navigation/RootNavigator";
// import { SafeAreaView } from "react-native-safe-area-context";
// const { width, height } = Dimensions.get("window");

// // ❗ KHAI BÁO TYPE CHUẨN CHO PROPS
// type OnboardingThirdProps = StackScreenProps<
//   RootStackParamList,
//   'OnboardingThird'
// > & {
//   onFinish: () => Promise<void>;
// };

// const OnboardingScreenThird = ({ navigation, onFinish } : OnboardingThirdProps ) => {
//   return (
//     <SafeAreaView style={styles.container}>
//       <StatusBar barStyle="light-content" />

//       {/* 1. Phần Logo */}
//       <View style={styles.headerContainer}>
//         <Image
//           source={require("../../assets/Bookington_logo.png")} // Logo demo giả định
//           style={styles.logo}
//         />
//       </View>

//       {/* 2. Phần Hình Ảnh Chính */}
//       <View style={styles.imageContainer}>
//         <Image
//           source={require("../../assets/Leechongwei.png")}
//           style={styles.mainImage}
//           resizeMode="contain"
//         />
//       </View>

//       {/* 3. Phần Nội Dung & Nút Bấm */}
//       <View style={styles.bottomContainer}>
//         <Text style={styles.title}>Đảm bảo đúng hẹn</Text>

//         <Text style={styles.description}>
//           Nhận lời nhắc thông minh và cập nhật hàng tuần về lịch đặt sân của bạn
//           - luôn đúng giờ, sẵn sàng chơi
//         </Text>

//         {/* Pagination Dots */}
//         <View style={styles.paginationContainer}>
//           <View style={[styles.dot, styles.activeDot]} />
//           <View style={[styles.dot, styles.activeDot]} />
//           <View style={[styles.dot, styles.activeDot]} />
//         </View>

//         {/* Button Tiếp Tục */}
//         {/* 2. Thêm sự kiện onPress vào nút bấm */}
//         <TouchableOpacity
//           style={styles.button}
//           activeOpacity={0.8}
//           // Dòng quan trọng: Chuyển sang màn hình thứ 2
//           onPress={() => navigation.navigate("PreLogin")}
//         >
//           <Text style={styles.buttonText}>Tiếp tục</Text>
//         </TouchableOpacity>
//       </View>
//     </SafeAreaView>
//   );
// };

// const styles = StyleSheet.create({
//   container: {
//     flex: 1,
//     backgroundColor: "#3B9AFF", // Màu xanh chủ đạo giống hình
//     alignItems: "center",
//     justifyContent: "space-between",
//   },
//   headerContainer: {
//     marginTop: 20,
//     alignItems: "center",
//     height: height * 0.15, // Chiếm 15% màn hình
//   },
//   logo: {
//     width: width * 0.8,
//   },
//   logoText: {
//     color: "white",
//     fontWeight: "bold",
//     fontSize: 18,
//     marginTop: 5,
//     letterSpacing: 1,
//   },
//   imageContainer: {
//     height: height * 0.45, // Chiếm 45% màn hình cho ảnh to
//     justifyContent: "center",
//     alignItems: "center",
//     width: width,
//   },
//   mainImage: {
//     width: "90%",
//     height: "90%",
//   },
//   bottomContainer: {
//     height: height * 0.4, // Chiếm 40% màn hình còn lại
//     width: "100%",
//     alignItems: "center",
//     paddingHorizontal: 20,
//     justifyContent: "flex-start",
//   },
//   title: {
//     fontSize: 28,
//     fontWeight: "bold",
//     color: "white",
//     marginBottom: 10,
//   },
//   description: {
//     fontSize: 16,
//     color: "#E0F0FF", // Màu trắng hơi nhạt cho text phụ
//     textAlign: "center",
//     lineHeight: 24,
//     marginBottom: 20,
//     paddingHorizontal: 10,
//   },
//   paginationContainer: {
//     flexDirection: "row",
//     marginBottom: 30,
//     alignItems: "center",
//   },
//   dot: {
//     width: 8,
//     height: 8,
//     borderRadius: 4,
//     backgroundColor: "rgba(255, 255, 255, 0.5)", // Dot mờ
//     marginHorizontal: 5,
//   },
//   activeDot: {
//     width: 10,
//     height: 10,
//     backgroundColor: "transparent",
//     borderWidth: 2,
//     borderColor: "white", // Dot rỗng active
//     borderRadius: 6,
//   },
//   button: {
//     backgroundColor: "white",
//     width: "80%",
//     paddingVertical: 15,
//     borderRadius: 30,
//     alignItems: "center",
//     shadowColor: "#000",
//     shadowOffset: {
//       width: 0,
//       height: 2,
//     },
//     shadowOpacity: 0.25,
//     shadowRadius: 3.84,
//     elevation: 5,
//   },
//   buttonText: {
//     color: "#3B9AFF", // Màu chữ cùng màu nền
//     fontSize: 18,
//     fontWeight: "600",
//   },
// });

// export default OnboardingScreenThird;

import React from "react";
import {
  StyleSheet,
  Text,
  View,
  Image,
  TouchableOpacity,
  StatusBar,
  Dimensions,
} from "react-native";
import { StackScreenProps } from "@react-navigation/stack";
import { SafeAreaView } from "react-native-safe-area-context";

// Import your specific paths
import { RootStackParamList } from "../../navigation/RootNavigator";
import { useAuthStore } from "../../store/useAuthStore";

const { width, height } = Dimensions.get("window");

// Define the Props type
type Props = StackScreenProps<RootStackParamList, "OnboardingThird">;

const OnboardingScreenThird = ({ navigation }: Props) => {
  // Hooks must be called INSIDE the component
  const setHasSeenOnboarding = useAuthStore(
    (state) => state.setHasSeenOnboarding
  );

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="light-content" />

      {/* 1. Phần Logo */}
      <View style={styles.headerContainer}>
        <Image
          source={require("../../assets/Bookington_logo.png")}
          style={styles.logo}
          resizeMode="contain"
        />
      </View>

      {/* 2. Phần Hình Ảnh Chính */}
      <View style={styles.imageContainer}>
        <Image
          source={require("../../assets/Leechongwei.png")}
          style={styles.mainImage}
          resizeMode="contain"
        />
      </View>

      {/* 3. Phần Nội Dung & Nút Bấm */}
      <View style={styles.bottomContainer}>
        <Text style={styles.title}>Đảm bảo đúng hẹn</Text>

        <Text style={styles.description}>
          Nhận lời nhắc thông minh và cập nhật hàng tuần về lịch đặt sân của bạn
          - luôn đúng giờ, sẵn sàng chơi
        </Text>

        {/* Pagination Dots */}
        <View style={styles.paginationContainer}>
          <View style={[styles.dot, styles.activeDot]} />
          <View style={[styles.dot, styles.activeDot]} />
          <View style={[styles.dot, styles.activeDot]} />
        </View>
        {/* Button Tiếp Tục */}
        <TouchableOpacity
          style={styles.button}
          activeOpacity={0.8}
          // Chuyển sang màn hình thứ 3
          onPress={() => navigation.navigate("PreLogin")}
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
    backgroundColor: "#3B9AFF", // Màu xanh chủ đạo
    alignItems: "center",
    justifyContent: "space-between",
  },
  headerContainer: {
    marginTop: 20,
    alignItems: "center",
    height: height * 0.15,
  },
  logo: {
    width: width * 0.6, // Adjusted width slightly for better fit
    height: "100%",
  },
  imageContainer: {
    height: height * 0.45,
    justifyContent: "center",
    alignItems: "center",
    width: width,
  },
  mainImage: {
    width: "90%",
    height: "90%",
  },
  bottomContainer: {
    height: height * 0.4,
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
    textAlign: "center",
  },
  description: {
    fontSize: 16,
    color: "#E0F0FF",
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
    backgroundColor: "rgba(255, 255, 255, 0.5)",
    marginHorizontal: 5,
  },
  activeDot: {
    width: 10,
    height: 10,
    backgroundColor: "transparent",
    borderWidth: 2,
    borderColor: "white",
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
    color: "#3B9AFF",
    fontSize: 18,
    fontWeight: "600",
  },
});

export default OnboardingScreenThird;
