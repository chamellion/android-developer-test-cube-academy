plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("dagger.hilt.android.plugin")
	id("kotlin-kapt")
	id("kotlin-parcelize")
}

android {
	namespace = "com.cube.cubeacademy"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.cube.cubeacademy"
		minSdk = 30
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "com.cube.cubeacademy.di.CustomTestRunner"

		buildConfigField("String", "API_URL", "\"https://cube-academy-api.cubeapis.com/\"")
		buildConfigField("String", "AUTH_TOKEN", "\"${project.property("authToken")}\"")
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

	kotlinOptions {
		jvmTarget = "17"
	}

	buildFeatures {
		viewBinding = true
		buildConfig = true
		dataBinding = true
	}
}

dependencies {
	implementation("androidx.core:core-ktx:1.12.0")
	implementation("androidx.appcompat:appcompat:1.6.1")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
	implementation("com.google.android.material:material:1.10.0")
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.google.code.gson:gson:2.10.1")
	implementation("com.squareup.retrofit2:converter-gson:2.9.0")
	implementation("androidx.constraintlayout:constraintlayout:2.1.4")
	implementation ("com.google.android.material:material:1.10.0")

	//Needed extension for viewModel delegation and activity ktx extension functions
	implementation ("androidx.activity:activity-ktx:1.8.0")

	//Simple alert dialog for displaying different messages to end users
	implementation("com.github.f0ris.sweetalert:library:1.6.2")

	// Hilt
	implementation("com.google.dagger:hilt-android:2.48")
	kapt("com.google.dagger:hilt-compiler:2.48")
	testImplementation("com.google.dagger:hilt-android-testing:2.48")
	androidTestImplementation("com.google.dagger:hilt-android-testing:2.48")
	kaptTest("com.google.dagger:hilt-android-compiler:2.48")
	kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.48")

	//Timber for logging
	implementation("com.jakewharton.timber:timber:5.0.1")

	//Logging interceptor to print network logs
	implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

	//Custom Radio Button for rating bar
	implementation("io.github.ihermandev:advanced-radio-group:1.0.0")

	// ViewModel
	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

	testImplementation("junit:junit:4.13.2")
	//Truth assertion library for writing good and readable test
	testImplementation ("com.google.truth:truth:1.1.5")
	androidTestImplementation ("com.google.truth:truth:1.1.5")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}