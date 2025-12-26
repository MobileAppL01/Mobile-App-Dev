import React, { useEffect, useState } from "react";
import { NavigationContainer } from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";

// Import Storage
import {
  getHasSeenOnboarding,
  setHasSeenOnboarding,
  clearOnboarding,
} from "../storage/onboardingStorage";

// Import Screens
import OnboardingScreenFirst from "../screens/Onboarding/OnboardingScreenFirst";
import OnboardingScreenSecond from "../screens/Onboarding/OnboardingScreenSecond";
import OnboardingScreenThird from "../screens/Onboarding/OnboardingScreenThird";
import HomeScreen from "../screens/Onboarding/HomeScreen";
import PreLogin from "../screens/Onboarding/PreLogin";
import SignUpScreen from "../screens/Authentication/SignUpScreen";
import LoginScreen from "../screens/Authentication/LoginScreen";

// Define Types for all screens
export type RootStackParamList = {
  OnboardingFirst: undefined;
  OnboardingSecond: undefined; // <--- ADDED THIS
  OnboardingThird: undefined;
  OnboardingFourth: undefined;
  PreLogin: undefined;
  Home: undefined;
  SignUp: { method: "email" | "phone" };
  Login: undefined;
};

const Stack = createStackNavigator<RootStackParamList>();

export default function RootNavigator() {
  const [loading, setLoading] = useState(true);
  const [hasSeenOnboarding, setHasSeen] = useState(false);

  useEffect(() => {
    (async () => {
      const seen = await getHasSeenOnboarding();
      console.log("User has seen onboarding:", seen);

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
        {!hasSeenOnboarding ? (
          <>
            <Stack.Screen
              name="OnboardingFirst"
              component={OnboardingScreenFirst}
            />

            <Stack.Screen name="OnboardingSecond">
              {(props) => (
                <OnboardingScreenSecond
                  {...props}
                  onFinish={handleOnboardingFinish}
                />
              )}
            </Stack.Screen>

            <Stack.Screen name="OnboardingThird">
              {(props) => (
                <OnboardingScreenThird
                  {...props}
                  onFinish={handleOnboardingFinish}
                />
              )}
            </Stack.Screen>

            <Stack.Screen name="PreLogin">
              {(props) => (
                <PreLogin {...props} onFinish={handleOnboardingFinish} />
              )}
            </Stack.Screen>

            <Stack.Screen name="SignUp" component={SignUpScreen} />

            <Stack.Screen name="Login" component={LoginScreen} />
          </>
        ) : (
          // === MAIN APP FLOW ===
          <>
            <Stack.Screen name="Home" component={HomeScreen} />
          </>
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
}
