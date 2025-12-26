import React from 'react';
import { 
  StyleSheet, 
  Text, 
  View, 
  Image, 
  TouchableOpacity, 
  StatusBar, 
  Dimensions 
} from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons'; // Thư viện icon của Expo
import { SafeAreaView } from "react-native-safe-area-context";
import { StackScreenProps } from '@react-navigation/stack';
import { RootStackParamList } from '../../navigation/RootNavigator';
const { width, height } = Dimensions.get("window");

// ❗ KHAI BÁO TYPE CHUẨN CHO PROPS
type PreLoginProps = StackScreenProps<
  RootStackParamList,
  'PreLogin'
> & {
  onFinish: () => Promise<void>;
};

const PreLogin = ({ navigation, onFinish }: PreLoginProps) => {
  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="light-content" />
      
      {/* 1. Phần Logo (Top) */}
      <View style={styles.topSection}>
        {/* Thay logo Bookinton vào đây */}
        <Image
          source={require('../../assets/Bookington_logo.png')} // Đường dẫn ảnh logo của bạn
          style={styles.logo}
          resizeMode="contain"
        />
      </View>

      {/* 2. Phần Nút Bấm (Middle) */}
      <View style={styles.middleSection}>
        
        {/* Nút Đăng ký bằng Email */}
        <TouchableOpacity style={styles.button} activeOpacity={0.7} onPress={() => navigation.navigate('SignUp', {method:'email'})}>
          <MaterialCommunityIcons name="email-outline" size={24} color="white" style={styles.icon} />
          <Text style={styles.buttonText}>Đăng ký bằng Email</Text>
        </TouchableOpacity>

        {/* Nút Đăng ký bằng điện thoại */}
        <TouchableOpacity style={styles.button} activeOpacity={0.7} onPress={() => navigation.navigate('SignUp', {method:'phone'})}>
          <MaterialCommunityIcons name="phone" size={24} color="white" style={styles.icon} />
          <Text style={styles.buttonText}>Đăng ký bằng điện thoại</Text>
        </TouchableOpacity>

        {/* Divider "Hoặc" */}
        <View style={styles.dividerContainer}>
          <View style={styles.line} />
          <Text style={styles.orText}>hoặc</Text>
          <View style={styles.line} />
        </View>

        {/* Text Đã có tài khoản */}
        <View style={styles.loginContainer}>
          <Text style={styles.textNormal}>Đã có tài khoản?  </Text>
          <TouchableOpacity onPress={() => navigation.navigate('Login')}>
            <Text style={styles.textBold}>Đăng nhập</Text>
          </TouchableOpacity>
        </View>

      </View>

      {/* 3. Phần Ảnh Chân trang (Bottom) */}
      {/* Thay hình ảnh người chơi cầu lông vào đây */}
      <Image
        source={{ uri: 'https://i.imgur.com/your-footer-image.png' }} // Placeholder hoặc require local asset
        style={styles.footerImage}
        resizeMode="contain"
      />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#3B9AFF', // Màu xanh chủ đạo
    alignItems: 'center',
  },
  
  // --- Style Phần Top ---
  topSection: {
    flex: 0.4, // Chiếm 40% màn hình phía trên
    justifyContent: 'center',
    alignItems: 'center',
    width: '100%',
    paddingTop: 50,
  },
  logo: {
    width: width * 1,// Logo rộng 60% màn hình
  },

  // --- Style Phần Giữa (Nút bấm) ---
  middleSection: {
    flex: 0.4,
    width: '100%',
    alignItems: 'center',
    paddingHorizontal: 30,
    justifyContent: 'flex-start',
  },
  button: {
    flexDirection: 'row', // Xếp icon và chữ ngang hàng
    width: '100%',
    height: 50,
    borderWidth: 1.5,
    borderColor: 'white',
    borderRadius: 8,
    alignItems: 'center',
    justifyContent: 'center', // Canh giữa nội dung trong nút
    marginBottom: 15,
    backgroundColor: 'transparent',
  },
  icon: {
    position: 'absolute', // Để icon nằm cố định bên trái
    left: 20,
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: '600',
  },
  
  // Divider "hoặc"
  dividerContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    width: '100%',
    marginVertical: 25,
  },
  line: {
    flex: 1,
    height: 1,
    backgroundColor: 'rgba(255, 255, 255, 0.6)', // Line mờ
  },
  orText: {
    color: 'white',
    marginHorizontal: 10,
    fontSize: 14,
  },

  // Login Text
  loginContainer: {
    flexDirection: 'row',
    marginTop: 10,
  },
  textNormal: {
    color: 'white',
    fontSize: 15,
  },
  textBold: {
    color: 'white',
    fontSize: 15,
    fontWeight: 'bold',
    textDecorationLine: 'underline', // Gạch chân chữ Đăng nhập
  },

  // --- Style Phần Chân trang ---
  footerImage: {
    position: 'absolute', // Đặt tuyệt đối ở đáy
    bottom: 0,
    width: width,
    height: height * 0.2, // Cao khoảng 20% màn hình
    opacity: 0.8,
  },
});

export default PreLogin;