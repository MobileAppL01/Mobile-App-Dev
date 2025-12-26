import React from 'react';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { Ionicons } from '@expo/vector-icons';
import { View, Text } from 'react-native';

// Import màn hình HomeScreen cũ của bạn
import HomeScreen from '../screens/BookingCourt/HomeScreen';

// --- Tạo các màn hình giả lập cho các Tab còn lại ---
const NotificationScreen = () => (
  <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
    <Text>Màn hình Thông Báo</Text>
  </View>
);

const HistoryScreen = () => (
  <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
    <Text>Màn hình Lịch Sử</Text>
  </View>
);

const InfoScreen = () => (
  <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
    <Text>Màn hình Info (Profile)</Text>
  </View>
);

const Tab = createBottomTabNavigator();

const ClientTabs = () => {
  return (
    <Tab.Navigator
      screenOptions={({ route }) => ({
        headerShown: false, // Ẩn header mặc định
        tabBarActiveTintColor: '#3B9AFF', // Màu xanh khi Active (giống ảnh)
        tabBarInactiveTintColor: '#888',  // Màu xám khi Inactive
        tabBarStyle: {
          height: 85, // Chiều cao thanh tab
          paddingBottom: 10, // Khoảng cách dưới
          paddingTop: 5,
          backgroundColor: 'white',
          borderTopWidth: 1,
          borderTopColor: '#eee',
          elevation: 10, // Đổ bóng cho Android
          shadowColor: '#000', // Đổ bóng cho iOS
          shadowOffset: { width: 0, height: -2 },
          shadowOpacity: 0.1,
          shadowRadius: 4,
        },
        tabBarLabelStyle: {
          fontSize: 12, // Kích thước chữ
          fontWeight: '500',
        },
        // Cấu hình Icon cho từng Tab
        tabBarIcon: ({ focused, color, size }) => {
          let iconName: keyof typeof Ionicons.glyphMap;

          if (route.name === 'Trang Chủ') {
            iconName = focused ? 'home' : 'home-outline';
          } else if (route.name === 'Thông Báo') {
            iconName = focused ? 'notifications' : 'notifications-outline';
          } else if (route.name === 'Lịch Sử') {
            iconName = focused ? 'time' : 'time-outline';
          } else if (route.name === 'Info') {
            iconName = focused ? 'ellipsis-horizontal' : 'ellipsis-horizontal-outline';
          } else {
             iconName = 'alert';
          }

          // Trả về Icon
          return <Ionicons name={iconName} size={24} color={color} />;
        },
      })}
    >
      {/* 1. Tab Trang Chủ */}
      <Tab.Screen name="Trang Chủ" component={HomeScreen} />

      {/* 2. Tab Thông Báo */}
      <Tab.Screen name="Thông Báo" component={NotificationScreen} />

      {/* 3. Tab Lịch Sử */}
      <Tab.Screen name="Lịch Sử" component={HistoryScreen} />

      {/* 4. Tab Info */}
      <Tab.Screen name="Info" component={InfoScreen} />
    </Tab.Navigator>
  );
};

export default ClientTabs;