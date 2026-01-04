import { create } from 'zustand';

type NotificationType = 'success' | 'error' | 'info';

interface NotificationState {
    visible: boolean;
    message: string;
    type: NotificationType;
    showNotification: (message: string, type?: NotificationType) => void;
    hideNotification: () => void;
}

export const useNotificationStore = create<NotificationState>((set) => ({
    visible: false,
    message: '',
    type: 'info',
    showNotification: (message, type = 'info') => {
        set({ visible: true, message, type });
        // Auto hide after 3 seconds
        setTimeout(() => {
            set({ visible: false });
        }, 3000);
    },
    hideNotification: () => set({ visible: false }),
}));
