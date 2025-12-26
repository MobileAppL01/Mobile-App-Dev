// src/navigation/RootNavigator.tsx
import React from "react";
import { NavigationContainer } from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";
import ClientTabs from "./ClientTabs";

// 1. Import Store
import { useAuthStore } from "../store/useAuthStore";

// Import Screens
import OnboardingScreenFirst from "../screens/Onboarding/OnboardingScreenFirst";
import OnboardingScreenSecond from "../screens/Onboarding/OnboardingScreenSecond";
import OnboardingScreenThird from "../screens/Onboarding/OnboardingScreenThird";
import HomeScreen from "../screens/BookingCourt/HomeScreen";
import PreLogin from "../screens/Onboarding/PreLogin";
import SignUpScreen from "../screens/Authentication/SignUpScreen";
import LoginScreen from "../screens/Authentication/LoginScreen";
import ManagerHomeScreen from "../screens/ManagingCourt/ManagerHomeScreen";
import RevenueScreen from "../screens/ManagingCourt/RevenueScreen";

export type RootStackParamList = {
  OnboardingFirst: undefined;
  OnboardingSecond: undefined;
  OnboardingThird: undefined;
  PreLogin: undefined;
  Home: undefined;
  SignUp: { method: "email" | "phone" };
  Login: undefined;
  ManagerHome: undefined;
  ManagerRevenue: undefined;
  ClientTabs: undefined;
};

const Stack = createStackNavigator<RootStackParamList>();

export default function RootNavigator() {
  // 2. Lấy state từ Zustand (không cần useEffect hay useState nữa)
  // isAuthenticated: Đã đăng nhập hay chưa?
  // hasSeenOnboarding: Đã xem intro chưa?
  const { isAuthenticated, hasSeenOnboarding, user } = useAuthStore();
  console.log("user", user);
  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={{ headerShown: false }}>
        {/* LOGIC ĐIỀU HƯỚNG CHÍNH */}
        {isAuthenticated ? (
          // === NHÓM 1: ĐÃ ĐĂNG NHẬP ===

          // Kiểm tra ROLE ở đây
          user?.role === "MANAGER" ? (
            // --- A. Nếu là MANAGER ---
            <>
              <Stack.Screen name="ManagerHome" component={ManagerHomeScreen} />
              <Stack.Screen name="ManagerRevenue" component={RevenueScreen} />
            </>
          ) : (
            // --- B. Nếu là CLIENT (Khách) ---
            <>
              {/* <Stack.Screen name="Home" component={HomeScreen} /> */}
              <Stack.Screen name="ClientTabs" component={ClientTabs} />
              {/* Thêm các màn hình khác của khách vào đây (Booking, Profile...) */}
            </>
          )
        ) : (
          // === NHÓM 2: CHƯA ĐĂNG NHẬP (Auth Flow) ===
          <>
            {!hasSeenOnboarding && (
              <>
                <Stack.Screen
                  name="OnboardingFirst"
                  component={OnboardingScreenFirst}
                />
                <Stack.Screen
                  name="OnboardingSecond"
                  component={OnboardingScreenSecond}
                />
                <Stack.Screen
                  name="OnboardingThird"
                  component={OnboardingScreenThird}
                />
              </>
            )}

            <Stack.Screen name="PreLogin" component={PreLogin} />
            <Stack.Screen name="Login" component={LoginScreen} />
            <Stack.Screen name="SignUp" component={SignUpScreen} />
          </>
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
}
