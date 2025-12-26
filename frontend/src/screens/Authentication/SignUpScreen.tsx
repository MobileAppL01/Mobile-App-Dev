import React, { useState } from "react";
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
  Alert, // Import Alert for validation
} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { Ionicons } from "@expo/vector-icons";
import { StackScreenProps } from "@react-navigation/stack";
import { RootStackParamList } from "../../navigation/RootNavigator";

// Define Props
type SignUpProps = StackScreenProps<RootStackParamList, "SignUp">;

const SignUpScreen = ({ navigation, route }: SignUpProps) => {
  const { method } = route.params;

  // --- State ---
  const [inputValue, setInputValue] = useState(""); // Use one state for Email OR Phone
  const [fullName, setFullName] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  // State for toggling password visibility
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  // --- Logic Helpers ---
  const isEmail = method === "email";

  // Validation & Submit Handler
  const handleRegister = () => {
    // 1. Check Empty
    if (!inputValue || !fullName || !password || !confirmPassword) {
      Alert.alert("Lỗi", "Vui lòng điền đầy đủ thông tin");
      return;
    }

    // 2. Check Password Match
    if (password !== confirmPassword) {
      Alert.alert("Lỗi", "Mật khẩu nhập lại không khớp");
      return;
    }

    // 3. Success logic (API Call here)
    console.log("Registering with:", { inputValue, fullName, password });
    Alert.alert("Thành công", "Mã OTP đã được gửi!");
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      <KeyboardAvoidingView
        behavior={Platform.OS === "ios" ? "padding" : "height"}
        style={styles.container}
      >
        <ScrollView
          contentContainerStyle={styles.scrollContent}
          showsVerticalScrollIndicator={false}
        >
          {/* --- Header --- */}
          <View style={styles.header}>
            <TouchableOpacity
              style={styles.backButton}
              onPress={() => navigation.goBack()}
            >
              <Ionicons name="chevron-back" size={28} color="#3B9AFF" />
            </TouchableOpacity>

            <Image
              source={require("../../assets/Bookington_logo.png")}
              style={styles.logo}
              resizeMode="contain"
            />
            <View style={{ width: 28 }} />
          </View>

          <Text style={styles.title}>Đăng Ký</Text>

          <View style={styles.formContainer}>

            {/* Email Input */}
            <View style={styles.inputGroup}>
              <Text style={styles.label}>Địa chỉ email</Text>
              <TextInput
                style={styles.input}
                placeholder="example@gmail.com"
                placeholderTextColor="#A0A0A0"
                value={inputValue}
                onChangeText={setInputValue}
                keyboardType="email-address"
                autoCapitalize="none"
              />
            </View>

            {/* Full Name */}
            <View style={styles.inputGroup}>
              <Text style={styles.label}>Họ và tên</Text>
              <TextInput
                style={styles.input}
                placeholder="Nguyễn Văn A"
                placeholderTextColor="#A0A0A0"
                value={fullName}
                onChangeText={setFullName}
                autoCapitalize="words"
              />
            </View>

            {/* Password */}
            <View style={styles.inputGroup}>
              <Text style={styles.label}>Mật khẩu</Text>
              <View style={styles.passwordContainer}>
                <TextInput
                  style={[styles.input, styles.passwordInput]} // Combine styles
                  placeholder="abc123"
                  placeholderTextColor="#A0A0A0"
                  value={password}
                  onChangeText={setPassword}
                  secureTextEntry={!showPassword} // Toggle logic
                />
                <TouchableOpacity
                  style={styles.eyeIcon}
                  onPress={() => setShowPassword(!showPassword)}
                >
                  <Ionicons
                    name={showPassword ? "eye" : "eye-off"}
                    size={24}
                    color="#A0A0A0"
                  />
                </TouchableOpacity>
              </View>
            </View>

            {/* Confirm Password */}
            <View style={styles.inputGroup}>
              <Text style={styles.label}>Nhập lại mật khẩu</Text>
              <View style={styles.passwordContainer}>
                <TextInput
                  style={[styles.input, styles.passwordInput]}
                  placeholder="abc123"
                  placeholderTextColor="#A0A0A0"
                  value={confirmPassword}
                  onChangeText={setConfirmPassword}
                  secureTextEntry={!showConfirmPassword}
                />
                <TouchableOpacity
                  style={styles.eyeIcon}
                  onPress={() => setShowConfirmPassword(!showConfirmPassword)}
                >
                  <Ionicons
                    name={showConfirmPassword ? "eye" : "eye-off"}
                    size={24}
                    color="#A0A0A0"
                  />
                </TouchableOpacity>
              </View>
            </View>

            {/* Button */}
            <TouchableOpacity style={styles.button} onPress={handleRegister}>
              <Text style={styles.buttonText}>Gửi mã OTP</Text>
            </TouchableOpacity>

            {/* Login Link - FIXED */}
            <View style={styles.footer}>
              <Text style={styles.footerText}>Đã có tài khoản? </Text>
              <TouchableOpacity onPress={() => navigation.navigate("Login")}>
                <Text style={styles.linkText}>Đăng nhập</Text>
              </TouchableOpacity>
            </View>
          </View>

          {/* Footer Image */}
          <View style={styles.bottomImageContainer}>
            <Image
              source={require("../../assets/Leechongwei.png")}
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
    backgroundColor: "white",
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
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    marginTop: 10,
    marginBottom: 20,
  },
  backButton: {
    padding: 5,
  },
  logo: {
    width: 150,
    height: 60,
  },
  title: {
    fontSize: 24,
    fontWeight: "bold",
    color: "black",
    textAlign: "center",
    marginBottom: 30,
  },
  formContainer: {
    marginBottom: 20,
  },
  inputGroup: {
    marginBottom: 20,
  },
  label: {
    fontSize: 16,
    color: "#555",
    marginBottom: 8,
  },
  input: {
    borderWidth: 1,
    borderColor: "#CCCCCC",
    borderRadius: 8,
    paddingHorizontal: 15,
    paddingVertical: 12,
    fontSize: 16,
    color: "#333",
    backgroundColor: "#fff",
  },
  // New Styles for Password Eye Icon
  passwordContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: "#CCCCCC",
    borderRadius: 8,
    backgroundColor: "#fff",
  },
  passwordInput: {
    flex: 1,
    borderWidth: 0, // Remove border because container has it
  },
  eyeIcon: {
    padding: 10,
  },
  // End New Styles
  button: {
    backgroundColor: "#3B9AFF",
    borderRadius: 8,
    paddingVertical: 15,
    alignItems: "center",
    marginTop: 10,
    marginBottom: 20,
  },
  buttonText: {
    color: "white",
    fontSize: 18,
    fontWeight: "bold",
  },
  footer: {
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "center",
  },
  footerText: {
    fontSize: 16,
    color: "#555",
    fontWeight: "bold",
  },
  linkText: {
    fontSize: 16,
    color: "#3B9AFF",
    fontWeight: "bold",
    textDecorationLine: "underline",
  },
  bottomImageContainer: {
    marginTop: "auto",
    alignItems: "flex-start",
    width: "100%",
    height: 150,
    opacity: 0.5,
  },
  bottomImage: {
    width: "60%",
    height: "100%",
  },
});

export default SignUpScreen;