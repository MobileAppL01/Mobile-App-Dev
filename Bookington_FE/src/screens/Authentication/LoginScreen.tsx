import React, { useState } from 'react';
import {
  StyleSheet,
  Text,
  View,
  TextInput,
  TouchableOpacity,
  Image,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  Dimensions,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';
import { StackScreenProps } from '@react-navigation/stack';
import { RootStackParamList } from '../../navigation/RootNavigator';

const { width } = Dimensions.get('window');

// Định nghĩa Type cho Props
type LoginProps = StackScreenProps<RootStackParamList, 'Login'>;

const LoginScreen = ({ navigation }: LoginProps) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = () => {
    // Xử lý logic đăng nhập tại đây (API call, validation...)
    console.log('Login with:', email, password);
    
    // Ví dụ: Nếu thành công thì chuyển vào trang chủ
    // navigation.navigate('Home'); 
    // Hoặc cập nhật State global để RootNavigator tự chuyển đổi
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      <KeyboardAvoidingView
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        style={styles.container}
      >
        <ScrollView
          contentContainerStyle={styles.scrollContent}
          showsVerticalScrollIndicator={false}
        >
          {/* --- 1. Header (Nút Back + Logo) --- */}
          <View style={styles.header}>
            <TouchableOpacity 
              style={styles.backButton} 
              onPress={() => navigation.goBack()}
            >
              <Ionicons name="chevron-back" size={28} color="#3B9AFF" />
            </TouchableOpacity>

            <Image
              source={require('../../assets/Bookington_logo.png')}
              style={styles.logo}
              resizeMode="contain"
            />
            {/* View rỗng để cân bằng layout header */}
            <View style={{ width: 28 }} /> 
          </View>

          {/* --- 2. Tiêu đề --- */}
          {/* Dùng Text lồng nhau để tô màu xanh cho chữ Đăng nhập */}
          <Text style={styles.titleContainer}>
            <Text style={styles.titleNormal}>Chào mừng trở lại, </Text>
            <Text style={styles.titleHighlight}>Đăng nhập</Text>
          </Text>

          {/* --- 3. Form Input --- */}
          <View style={styles.formContainer}>
            
            {/* Email */}
            <View style={styles.inputGroup}>
              <Text style={styles.label}>Tên đăng nhập</Text>
              <TextInput
                style={styles.input}
                placeholder="09xxx/example@gmail.com"
                placeholderTextColor="#A0A0A0"
                value={email}
                onChangeText={setEmail}
                keyboardType="email-address"
                autoCapitalize="none"
              />
            </View>

            {/* Mật khẩu */}
            <View style={styles.inputGroup}>
              <Text style={styles.label}>Mật khẩu</Text>
              <TextInput
                style={styles.input}
                placeholder="abc123"
                placeholderTextColor="#A0A0A0"
                value={password}
                onChangeText={setPassword}
                secureTextEntry
              />
            </View>

            {/* Quên mật khẩu (Căn phải) */}
            <TouchableOpacity style={styles.forgotPasswordContainer}>
              <Text style={styles.linkText}>Quên mật khẩu</Text>
            </TouchableOpacity>

            {/* Button Đăng nhập */}
            <TouchableOpacity style={styles.button} onPress={handleLogin}>
              <Text style={styles.buttonText}>Đăng nhập</Text>
            </TouchableOpacity>

            {/* --- 4. Footer (Đăng ký) --- */}
            <View style={styles.footer}>
              <Text style={styles.footerText}>Chưa có tài khoản? </Text>
              <TouchableOpacity onPress={() => navigation.navigate('PreLogin')}>
                <Text style={styles.linkText}>Đăng ký</Text>
              </TouchableOpacity>
            </View>
          </View>

          {/* --- 5. Hình ảnh trang trí dưới cùng --- */}
          {/* Căn lề trái giống thiết kế */}
          <View style={styles.bottomImageContainer}>
             <Image
              source={require('../../assets/Leechongwei.png')}
              style={styles.bottomImage}
              resizeMode="contain"
            />
          </View>

        </ScrollView>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: 'white',
  },
  container: {
    flex: 1,
  },
  scrollContent: {
    flexGrow: 1,
    paddingHorizontal: 20,
    paddingBottom: 20,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginTop: 10,
    marginBottom: 30,
  },
  backButton: {
    padding: 5,
  },
  logo: {
    width: 140, // Điều chỉnh kích thước logo cho phù hợp
    height: 50, 
  },
  titleContainer: {
    marginBottom: 30,
    textAlign: 'center', // Căn giữa tiêu đề
    alignSelf: 'center',
  },
  titleNormal: {
    fontSize: 20,
    fontWeight: '600',
    color: 'black',
  },
  titleHighlight: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#3B9AFF', // Màu xanh điểm nhấn
  },
  formContainer: {
    marginBottom: 20,
  },
  inputGroup: {
    marginBottom: 20,
  },
  label: {
    fontSize: 16,
    color: '#555',
    marginBottom: 8,
  },
  input: {
    borderWidth: 1,
    borderColor: '#CCCCCC',
    borderRadius: 8,
    paddingHorizontal: 15,
    paddingVertical: 12,
    fontSize: 16,
    color: '#333',
    backgroundColor: '#fff',
  },
  forgotPasswordContainer: {
    alignItems: 'flex-end', // Đẩy text sang phải
    marginBottom: 25,
  },
  button: {
    backgroundColor: '#3B9AFF',
    borderRadius: 8,
    paddingVertical: 15,
    alignItems: 'center',
    marginBottom: 20,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 2,
  },
  buttonText: {
    color: 'white',
    fontSize: 18,
    fontWeight: 'bold',
  },
  footer: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
  },
  footerText: {
    fontSize: 16,
    color: '#555',
    fontWeight: 'bold',
  },
  linkText: {
    fontSize: 16,
    color: '#3B9AFF',
    fontWeight: 'bold',
    textDecorationLine: 'underline',
  },
  
  // Style cho hình ảnh dưới cùng
  bottomImageContainer: {
    marginTop: 'auto', // Đẩy xuống đáy màn hình
    alignItems: 'flex-start', // Căn hình sang trái
    width: '100%',
    height: 150,
    opacity: 0.4, // Làm mờ nhẹ
  },
  bottomImage: {
    width: '50%', // Chiếm khoảng nửa màn hình bề ngang
    height: '100%',
  }
});

export default LoginScreen;