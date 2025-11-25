import React from 'react';
import { View, Text, Button, StyleSheet, Image, Dimensions } from 'react-native';

type Props = {
  onFinish: () => void;
};

const { width } = Dimensions.get('window');

export default function OnboardingScreen({ onFinish }: Props) {
  return (
    <View style={styles.container}>
      {/* Header Image */}
      <Image
        source={require('../../assets/onboarding.png')}
        style={styles.headerImage}
        resizeMode="cover"
      />

      {/* Text content */}
      <View style={styles.textContainer}>
        <Text style={styles.title}>Life is short and the world is wide</Text>

        <Text style={styles.subtitle}>
          At Friends tours and travel, we customize reliable and trustworthy educational tours to destinations all over the world
        </Text>

        <Button title="Get Started" onPress={onFinish} />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
  headerImage: {
    width: width,
  },
  textContainer: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: 15,
    color: '#333',
  },
  subtitle: {
    fontSize: 16,
    textAlign: 'center',
    marginBottom: 30,
    color: '#555',
  },
});
