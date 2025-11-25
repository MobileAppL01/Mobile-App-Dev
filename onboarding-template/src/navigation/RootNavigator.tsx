import React, { useEffect, useState } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';

import { getHasSeenOnboarding, setHasSeenOnboarding } from '../storage/onboardingStorage';
import OnboardingScreen from '../screens/OnboardingScreen';
import HomeScreen from '../screens/HomeScreen';

type RootStackParamList = {
  Onboarding: undefined;
  Home: undefined;
};

const Stack = createStackNavigator<RootStackParamList>();

export default function RootNavigator() {
  const [loading, setLoading] = useState(true);
  const [hasSeenOnboarding, setHasSeen] = useState(false);

  useEffect(() => {
    (async () => {
      const seen = await getHasSeenOnboarding();
      setHasSeen(seen);
      setLoading(false);
    })();
  }, []);

  if (loading) {
    return null;
  }

  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={{ headerShown: false }}>
        { !hasSeenOnboarding ? (
          <Stack.Screen name="Onboarding">
            {props => <OnboardingScreen {...props} onFinish={async () => {
              await setHasSeenOnboarding();
              setHasSeen(true);
            }} />}
          </Stack.Screen>
        ) : (
          <Stack.Screen name="Home" component={HomeScreen} />
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
}

