import React, { useEffect, useState } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';

// Import Storage
import { getHasSeenOnboarding, setHasSeenOnboarding, clearOnboarding } from '../storage/onboardingStorage';

// Import Screens
import OnboardingScreenFirst from '../screens/OnboardingScreenFirst';
import OnboardingScreenSecond from '../screens/OnboardingScreenSecond'; // <--- ADDED THIS
import HomeScreen from '../screens/HomeScreen';
import OnboardingScreenThird from '../screens/OnboardingScreenThird';
import PreLogin from '../screens/PreLogin';

// Define Types for all screens
export type RootStackParamList = {
  OnboardingFirst: undefined;
  OnboardingSecond: undefined; // <--- ADDED THIS
  OnboardingThird: undefined;
  OnboardingFourth: undefined;
  PreLogin: undefined;
  Home: undefined;
};

const Stack = createStackNavigator<RootStackParamList>();

export default function RootNavigator() {
  const [loading, setLoading] = useState(true);
  const [hasSeenOnboarding, setHasSeen] = useState(false);

  useEffect(() => {
    (async () => {
      // --- DEBUGGING ONLY ---
      // Uncomment the line below if you want to force reset the onboarding to test again
      // await clearOnboarding(); 
      // ----------------------

      const seen = await getHasSeenOnboarding();
      console.log('User has seen onboarding:', seen);
      
      setHasSeen(seen);
      setLoading(false);
    })();
  }, []);

  if (loading) {
    return null; 
  }

  // Define the function to handle finishing the onboarding flow
  const handleOnboardingFinish = async () => {
    await setHasSeenOnboarding(); // Save to storage
    setHasSeen(true); // Update state to switch to Home
  };

  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={{ headerShown: false }}>
        
        { !hasSeenOnboarding ? (
          // === ONBOARDING FLOW ===
          // We use React.Fragment (<>...</>) to group multiple screens
          <>
            {/* SCREEN 1: User clicks "Next" -> Navigates to OnboardingSecond */}
            <Stack.Screen name="OnboardingFirst" component={OnboardingScreenFirst} />

            {/* SCREEN 2: User clicks "Get Started" -> Calls onFinish -> Goes to Home */}
            <Stack.Screen name="OnboardingSecond">
              {props => <OnboardingScreenSecond {...props} onFinish={handleOnboardingFinish} />}
            </Stack.Screen>

               {/* SCREEN 3: User clicks "Get Started" -> Calls onFinish -> Goes to Home */}
            <Stack.Screen name="OnboardingThird">
              {props => <OnboardingScreenThird {...props} onFinish={handleOnboardingFinish} />}
            </Stack.Screen>
{/* 
              <Stack.Screen name="OnboardingFourth">
              {props => <OnboardingScreenFourth {...props} onFinish={handleOnboardingFinish} />}
            </Stack.Screen> */}

               <Stack.Screen name="PreLogin">
              {props => <PreLogin {...props} onFinish={handleOnboardingFinish} />}
            </Stack.Screen>
          </>
        ) : (
          // === MAIN APP FLOW ===
          <Stack.Screen name="Home" component={HomeScreen} />
        )}

      </Stack.Navigator>
    </NavigationContainer>
  );
}