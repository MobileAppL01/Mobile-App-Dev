import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Image } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useAuthStore } from '../../store/useAuthStore';
import { Ionicons } from '@expo/vector-icons';

export default function UserProfileScreen() {
    const { user, logout } = useAuthStore();

    // Fake data if user is missing properties
    const displayUser = {
        name: user?.name || "Nguyễn Văn A",
        email: user?.email || "nguyenvana@gmail.com",
        avatar: "https://i.pravatar.cc/300" // Placeholder avatar
    };

    return (
        <SafeAreaView style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.headerTitle}>Thông tin cá nhân</Text>
            </View>

            <View style={styles.content}>
                <View style={styles.profileSection}>
                    <Image source={{ uri: displayUser.avatar }} style={styles.avatar} />
                    <Text style={styles.userName}>{displayUser.name}</Text>
                    <Text style={styles.userEmail}>{displayUser.email}</Text>
                </View>

                <View style={styles.menuSection}>
                    <TouchableOpacity style={styles.menuItem}>
                        <Ionicons name="person-outline" size={24} color="#333" />
                        <Text style={styles.menuText}>Chỉnh sửa thông tin</Text>
                        <Ionicons name="chevron-forward" size={20} color="#ccc" />
                    </TouchableOpacity>

                    <TouchableOpacity style={styles.menuItem}>
                        <Ionicons name="card-outline" size={24} color="#333" />
                        <Text style={styles.menuText}>Phương thức thanh toán</Text>
                        <Ionicons name="chevron-forward" size={20} color="#ccc" />
                    </TouchableOpacity>

                    <TouchableOpacity style={styles.menuItem}>
                        <Ionicons name="settings-outline" size={24} color="#333" />
                        <Text style={styles.menuText}>Cài đặt</Text>
                        <Ionicons name="chevron-forward" size={20} color="#ccc" />
                    </TouchableOpacity>
                </View>

                <TouchableOpacity style={styles.logoutButton} onPress={logout}>
                    <Ionicons name="log-out-outline" size={20} color="#fff" />
                    <Text style={styles.logoutText}>Đăng xuất</Text>
                </TouchableOpacity>
            </View>
        </SafeAreaView>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
    },
    header: {
        padding: 16,
        borderBottomWidth: 1,
        borderBottomColor: '#f0f0f0',
        alignItems: 'center',
    },
    headerTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#333',
    },
    content: {
        flex: 1,
        padding: 20,
        alignItems: 'center',
    },
    profileSection: {
        alignItems: 'center',
        marginBottom: 40,
    },
    avatar: {
        width: 100,
        height: 100,
        borderRadius: 50,
        marginBottom: 16,
        borderWidth: 2,
        borderColor: '#3B9AFF',
    },
    userName: {
        fontSize: 22,
        fontWeight: 'bold',
        color: '#333',
        marginBottom: 4,
    },
    userEmail: {
        fontSize: 14,
        color: '#666',
    },
    menuSection: {
        width: '100%',
        marginBottom: 40,
    },
    menuItem: {
        flexDirection: 'row',
        alignItems: 'center',
        paddingVertical: 16,
        borderBottomWidth: 1,
        borderBottomColor: '#f5f5f5',
    },
    menuText: {
        flex: 1,
        fontSize: 16,
        color: '#333',
        marginLeft: 16,
    },
    logoutButton: {
        flexDirection: 'row',
        backgroundColor: '#FF4D4D',
        paddingVertical: 12,
        paddingHorizontal: 30,
        borderRadius: 25,
        alignItems: 'center',
        justifyContent: 'center',
        width: '80%',
    },
    logoutText: {
        color: '#fff',
        fontSize: 16,
        fontWeight: '600',
        marginLeft: 8,
    },
});
